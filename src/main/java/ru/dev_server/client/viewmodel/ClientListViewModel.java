package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.ExcelUtils;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.dao.MeetingDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.ContactType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ClientListViewModel {
    private static final Logger LOG=LoggerFactory.getLogger(ClientListViewModel.class) ;


    private String clientFilter;
    @WireVariable("clientDAO")
    private ClientDAO clientDAO;
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable("categoryDAO")
    private CategoryDAO categoryDAO;

    @WireVariable
    private MeetingDAO meetingDAO;

    @WireVariable
    private DynamicColumnDAO dynamicColumnDAO;

    @WireVariable
    private ExcelUtils excelUtils;

    @WireVariable
    private DynamicaColumnUtils dynamicaColumnUtils;


    private HashSet<Client> selectedItems;

    private Converter typeConverter=new I18nConverter(ContactType.class);

    private ClientListModel clientList;

    @Init
    public void initFoo(){
        clientList=new ClientListModel(clientDAO,authService);
    }


    @GlobalCommand  @NotifyChange("clientList")
    public  void refreshClients() {
        clientList.clearData();

    }


    @Command("newClient")
    public void newClient(){
        HashMap params = new HashMap();
        params.put("client",dynamicaColumnUtils.createClient());
        Executions. createComponents("/client.zul", null, params);
    }



    @GlobalCommand("editClient")
    public void editClient(@BindingParam("client")Client client){
        if(client==null){
            if(selectedItems.isEmpty()){
                return;
            }  else if (selectedItems.size()>=2){
                Messagebox.show("Нельзя редактировать сразу несколько клиентов.",
                        "Внимание",
                        Messagebox.OK,
                        Messagebox.ERROR);
                return;
            }
            client =selectedItems.iterator().next();
        }
        HashMap params = new HashMap();
        params.put("client",client);
        Executions. createComponents("/client.zul", null, params);
    }
    @Command
    public void  clickClient(@BindingParam("client")Client client){
        HashMap params = new HashMap();
        params.put("client",client);
        Executions. createComponents("/client.zul", null, params);
    }




    @Command("filterClients") @NotifyChange({"clientList"})
    public void filterClients(){
        try{
            String query = new ClientUtils().checkFilterAndReturnQuery(clientFilter);
            clientList.setFilter(query);
        }catch (IllegalArgumentException e){
            LOG.debug("@Command(\"filterClients\")  rejected"+clientFilter);
            Messagebox.show("только буквы и цифры");
        }
    }

    @Command("deleteClient") @NotifyChange({"visible", "clientList","selectedItems"})
    public void deleteClient(){
        for(Client del:selectedItems){
            del=clientDAO.get(del.getId());
            del.setDeleted(true);
            clientDAO.saveOrUpdate(del);
        }
        selectedItems.clear();
        refreshClients();
    }



    @Command("importExcel")  @NotifyChange({"clientList","categories"})
    public void importExcel(@BindingParam("upEvent") UploadEvent event){
        org.zkoss.util.media.Media media = event.getMedia();


        try {
            excelUtils.importExcel(media.getStreamData());
            refreshClients();
        } catch (Exception e) {
            Messagebox.show("Ошибка импорта. Некорректный excel файл." + e.getMessage(), "Ошибка", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        LOG.debug("importExcel");
    }

    @Command("exportExcel")
    public void exportExcel() throws Exception{
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            excelUtils.export(outputStream);
            outputStream.close();
            ByteArrayInputStream inputStream= new ByteArrayInputStream(outputStream.toByteArray());
            Filedownload.save(inputStream, "application/vnd.ms-excel", "clients.xls");
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(),e);
            Messagebox.show("Ошибка: " + e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }

    }

    public ClientListModel getClientList() {
        return clientList;
    }


    public Converter getTypeConverter() {
        return typeConverter;
    }


    public HashSet<Client> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(HashSet<Client> selectedItems) {
        this.selectedItems = selectedItems;
    }



    public String getClientFilter() {
        return clientFilter;
    }

    public void setClientFilter(String clientFilter) {
        this.clientFilter = clientFilter;
    }

}
