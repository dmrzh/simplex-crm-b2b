package ru.dev_server.client;

import org.apache.commons.lang.text.StrTokenizer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.ClientDynamicValue;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;
import ru.dev_server.client.model.DynamicColumn;
import ru.dev_server.client.model.ExstendedTables;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**. */
public class ExcelUtils {
    private static final Logger LOG= LoggerFactory.getLogger(ExcelUtils.class);

    @Resource
    private ClientDAO clientDAO;

    @Resource
    private CategoryDAO categoryDAO;

    @Resource
    private AuthService authService;

    @Resource
    DynamicColumnDAO dynamicColumnDAO;

    public void export(OutputStream outputStream) throws IOException {
        List<Client>  clientList=clientDAO.findAll(authService.getCompany());

        Workbook wb = new HSSFWorkbook();
        Sheet sheet1 = wb.createSheet("Clients");
        Row row = sheet1.createRow((short)0);

        Cell cell = row.createCell(0);
        cell.setCellValue("Фамилия");
        cell = row.createCell(1);
        cell.setCellValue("Имя");
        cell = row.createCell(2);
        cell.setCellValue("Отчество");
        cell = row.createCell(3);
        cell.setCellValue("Категория");
        cell = row.createCell(4);
        cell.setCellValue("Контакты");
        cell = row.createCell(5);
        cell.setCellValue("Адрес");
        cell = row.createCell(6);
        cell.setCellValue("Заметка");

        for(int i=0;i<clientList.size();i++){
            row = sheet1.createRow((short)i+1);

            cell = row.createCell(0);
            cell.setCellValue(clientList.get(i).getName());
            cell = row.createCell(1);
            cell.setCellValue("");
            cell = row.createCell(2);
            cell.setCellValue("");
            cell = row.createCell(3);
            Category category = clientList.get(i).getCategory();
            if(category!=null){
                cell.setCellValue(category.getName());
            }
            cell = row.createCell(4);
            StringBuffer sb=new StringBuffer();
            for(Contact contact:clientList.get(i).getContacts()){
                sb.append(contact.getContactType().name());
                sb.append("\t");
                sb.append(contact.getValue()==null?" ":contact.getValue());
                sb.append("\t");
                sb.append(contact.getNote()==null?" ":contact.getNote());
                sb.append("\n");
            }
            if(sb.length()>0)sb.deleteCharAt(sb.length()-1);
            cell.setCellValue(sb.toString());

            cell = row.createCell(5);
            cell.setCellValue(clientList.get(i).getAddress());
            cell = row.createCell(6);
            cell.setCellValue(clientList.get(i).getNote());

        }

        wb.write(outputStream);

    }


    public void importExcel(InputStream streamData) throws Exception{
        LOG.debug("importExcel");
        Workbook wb;
        wb = new HSSFWorkbook(streamData);
        Company company = authService.getCompany();
        Iterator<Row> iterator = wb.getSheetAt(0).iterator();
        iterator.next();//headers
        while (iterator.hasNext()){
            Row row = iterator.next();
            String lastName =getStringValue(row,0);
            String firstName=getStringValue(row, 1);
            String middleName = getStringValue(row,2);
            String category = getStringValue(row,3);

            String contacts=getStringValue(row,4);
            String address =getStringValue(row,5);
            String note =getStringValue(row, 6);

            Client client = new Client();
            client.setName(lastName);            ;
            client.setCategory(getCreateOrGetCategory(category));
            StrTokenizer tokenizer = new StrTokenizer(contacts,"\n");
            while (tokenizer.hasNext()){
                String lineToken = tokenizer.nextToken();
                StrTokenizer smallTokenizer = new StrTokenizer(lineToken ,"\t");


                while (smallTokenizer .hasNext()){
                    Contact contact = new Contact();
                    String contactType = smallTokenizer.nextToken();
                    String contactValue = smallTokenizer.nextToken();
                    String contactNote = smallTokenizer.nextToken();

                    ContactType.valueOf(contactType);
                    contact.setValue(contactValue);

                    contact.setNote(contactNote);
                    client.getContacts().add(contact);
                    contact.setClient(client);
                }
            }
            client.setAddress(address);
            client.setNote(note);
            client.setCompany(company);
            for(DynamicColumn dc:dynamicColumnDAO.findAll(authService.getCompany(), ExstendedTables.CLIENT)){
                ClientDynamicValue value = new ClientDynamicValue();
                value.setClient(client);
                value.setDynamicColumn(dc);
                client.getDynamicValueList().put(dc,value);
            }
            clientDAO.saveOrUpdate(client);
        }



    }
    private String getStringValue(Row row,int index){
        if(row.getCell(index)==null){
            return null;
        }else {
            return row.getCell(index).getStringCellValue();
        }
    }
    private Date getDateValue(Row row,int index){
        if(row.getCell(index)==null){
            return null;
        }else {
            return row.getCell(index).getDateCellValue();
        }
    }
    private Category getCreateOrGetCategory(String categoryName) {
        if(categoryName==null || categoryName.length()==0){
            return null;
        }
        Company company = authService.getCompany();
        Category nc = categoryDAO.findByName(company,categoryName);
        if(nc==null){
            nc = new Category();
            nc.setName(categoryName);
            nc.setCompany(company);
            categoryDAO.saveOrUpdate(nc);
        }
        return nc;    }


}
