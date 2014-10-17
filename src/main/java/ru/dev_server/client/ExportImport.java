package ru.dev_server.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Filedownload;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.ClientDynamicValueDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;
import ru.dev_server.client.viewmodel.admin.DynamicColumnVM;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**.*/
public class ExportImport {
    @Resource
    private AuthService authService;

    @Resource
    private CategoryDAO categoryDAO;

    @Resource
    private DynamicColumnDAO dynamicColumnDAO;


    @Resource
    private ClientDynamicValueDAO clientDynamicValueDAO;

    @Resource
    private ClientDAO clientDAO;

    @Resource
    private DynamicaColumnUtils dynamicaColumnUtils;


    public void exportColumns(){
        try{
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Company company = authService.getCompany();
            List<DynamicColumn> all = dynamicColumnDAO.findAll(company);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element simplexCrmNode = document.createElement("simplex-crm");
            document.appendChild(simplexCrmNode);

            Element dynamicColumnListNode = document.createElement("DynamicColumnList");
            simplexCrmNode.appendChild(dynamicColumnListNode);

            for(DynamicColumn col:all){
                Element dynamicColumnNode = document.createElement("DynamicColumn");
                dynamicColumnNode.setAttribute("name", col.getName());
                dynamicColumnNode.setAttribute("type", col.getType().getSimpleName());
                dynamicColumnNode.setAttribute("id", col.getId().toString());
                dynamicColumnNode.setAttribute("object", col.getExstendedTables().name());
                dynamicColumnListNode.appendChild(dynamicColumnNode);
            }

            Element categoryesNode = document.createElement("Categories");
            simplexCrmNode.appendChild(categoryesNode);
            for(Category cat: categoryDAO.findAll(company)){
                Element categoryEl = document.createElement("category");
                categoryEl.appendChild(document.createTextNode(cat.getName()));
                categoryEl.setAttribute("id", "" + cat.getId());
                categoryesNode.appendChild(categoryEl);
            }

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);

            ByteArrayInputStream inputStream= new ByteArrayInputStream(outputStream.toByteArray());
            Filedownload.save(inputStream, "text/xml", "AdditionalFields.xml");
        }catch (Exception ex){
            Messagebox.show("Ошибка при экспорте");
        }
    }

    public void importColumns(UploadEvent event)throws Exception {
        org.zkoss.util.media.Media media = event.getMedia();

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setValidating(false);
        DocumentBuilder builder = f.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(media.getReaderData()));
        Node root = doc.getFirstChild();
        if(! "simplex-crm".equals(root.getNodeName())){
            throw new IllegalArgumentException();
        }


        NodeList childNodes = findChildElement(root,"DynamicColumnList").getChildNodes();
        for(int i=0;i<childNodes.getLength();i++){
            Node dynamicColumnNode= childNodes.item(i);
            if(!(dynamicColumnNode.getNodeType()==Node.ELEMENT_NODE)){
                continue;
            }
            if(! "DynamicColumn".equals(dynamicColumnNode.getNodeName())){
                continue;
            }
            NamedNodeMap attributes = dynamicColumnNode.getAttributes();
            String nameAttr = attributes.getNamedItem("name").getNodeValue();
            String typeAttr = attributes.getNamedItem("type").getNodeValue();
            String idAttr = attributes.getNamedItem("id").getNodeValue();
            String objAttr= attributes.getNamedItem("object").getNodeValue();
            addIfNeed(nameAttr, getTypeBySimpleName(typeAttr),Long.parseLong(idAttr),objAttr);
        }

        childNodes = findChildElement(root,"Categories").getChildNodes();
        for(int i=0;i<childNodes.getLength();i++){
            Node categoryNode= childNodes.item(i);
            if(!(categoryNode.getNodeType()==Node.ELEMENT_NODE)){
                continue;
            }
            if(! "category".equals(categoryNode.getNodeName())){
                continue;
            }

            NamedNodeMap attributes = categoryNode.getAttributes();
            String idAttr= attributes.getNamedItem("id").getNodeValue();
            String categoryName = categoryNode.getTextContent();

            Category category=null;
            if(idAttr!=null && idAttr.length()>0){
                category = categoryDAO.get(Long.parseLong(idAttr));
            }
            if(category==null){
                category=new Category();
            }
            category.setCompany(authService.getCompany());
            category.setName(categoryName);
            categoryDAO.saveOrUpdate(category);
        }

    }

    private Node findChildElement(Node node, String s){
        NodeList childNodes = node.getChildNodes();
        for(int i=0;i<childNodes.getLength();i++){
            Node n = childNodes.item(i);
            if(!(n.getNodeType()==Node.ELEMENT_NODE)){
                continue;
            }
            if(s.equals(n.getNodeName())){
                return n;
            }
        }
        return null;
    }
    private Class getTypeBySimpleName(String typeAttr) {
        for(Class cl: DynamicColumnVM.CLASSES){
            if(cl.getSimpleName().equals(typeAttr)){
                return cl;
            }
        }
        return null;
    }
    private void addIfNeed(String nameAttr, Class typeAttr,Long idAttr,String objAttr ) throws IllegalArgumentException{
        DynamicColumn dynamicColumn;
        ExstendedTables exstendedTables = ExstendedTables.valueOf(objAttr);

        if(dynamicColumnDAO.get(idAttr)!=null&&dynamicColumnDAO.get(idAttr).getCompany().equals(authService.getCompany())){
            dynamicColumn = dynamicColumnDAO.get(idAttr);
            dynamicColumn.setType(typeAttr);
            dynamicColumn.setName(nameAttr);
            dynamicColumnDAO.saveOrUpdate(dynamicColumn);
        }else{
            dynamicColumn = new DynamicColumn();
            dynamicColumn.setCompany(authService.getCompany());
            dynamicColumn.setExstendedTables(exstendedTables);
            dynamicColumn.setType(typeAttr);
            dynamicColumn.setName(nameAttr);
            dynamicColumnDAO.saveOrUpdate(dynamicColumn);
            dynamicaColumnUtils.updateDependentObjects(dynamicColumn);
        }
    }
}
