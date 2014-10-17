package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.PropConverter;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.MassSmsDAO;
import ru.dev_server.client.dao.SmsNotificationDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.MassSms;
import ru.dev_server.client.model.NotificationStatus;
import ru.dev_server.client.model.SmsNotification;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MassSmsVM {
    private static final Logger LOG= LoggerFactory.getLogger(MassSmsVM.class);
    private List<Category> categoryList;
    private Category selectedCategory;
    private List<Client> filteredClients;
    private Client clientToAdd;
    private Map.Entry<Client,SmsNotification>  selectedSmsNotification;

    @WireVariable
    private MassSmsDAO massSmsDAO;
    @WireVariable
    SmsNotificationDAO smsNotificationDAO;
    @WireVariable
    private CategoryDAO categoryDAO;
    @WireVariable
    private ClientDAO clientDAO;
    @WireVariable
    private AuthService authService;
    @WireVariable
    private DynamicaColumnUtils dynamicaColumnUtils;
    private String filter;
    private Window smsSpamWin;
    private MassSms currentMassSms;
    private Date tempTime;
    PropConverter statusConverter=new PropConverter(NotificationStatus.class.getCanonicalName()+".properties");

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("massSms") MassSms massSms){
        smsSpamWin=view;
        currentMassSms=massSms;
        categoryList=categoryDAO.findAll(authService.getCompany());
        filteredClients=clientDAO.findAll(authService.getCompany());
        tempTime=(Date)currentMassSms.getStartDate().clone();
    }

    @Command  @NotifyChange({"currentMassSms"})
    public void refresh(){
        filteredClients=clientDAO.findAll(authService.getCompany());
    }

    @NotifyChange("filteredClients")  @GlobalCommand("refreshClients")   @Command("refreshClients")
    public void refreshClients(){
        Company company = authService.getCompany();
        if(selectedCategory==null&&(filter==null || filter.length()==0)){
            int limit = clientDAO.getClientCount(company).intValue();
            filteredClients=clientDAO.findAll(company ,0, limit);
        }else if(selectedCategory==null){
            String q = new ClientUtils().checkFilterAndReturnQuery(filter);
            int limit = clientDAO.getClientCountByFilter(q, company).intValue();
            filteredClients=clientDAO.findByFilter(q, company,0, limit);
        }else if(filter==null || filter.length()==0){
            int limit = clientDAO.getClientCount(company, selectedCategory).intValue();
            filteredClients=clientDAO.findByCategory(authService.getCompany(),selectedCategory);
        }else{
            String q = new ClientUtils().checkFilterAndReturnQuery(filter);
            int limit = clientDAO.getClientCountByFilterAndCategory(q,selectedCategory,company).intValue();
            filteredClients=clientDAO.findByFilterAndCategory(q, selectedCategory, company,0, limit);
        }
    }
    @Command
    public void chengeStartTime(){
        currentMassSms.getStartDate().setHours(tempTime.getHours());
        currentMassSms.getStartDate().setMinutes(tempTime.getMinutes());
    }
    @Command
    public void save(){
        massSmsDAO.saveOrUpdate(currentMassSms);
       smsSpamWin.detach();
    }

    @Command   @NotifyChange("currentMassSms")
    public void delete(){
        currentMassSms=massSmsDAO.get(currentMassSms.getId());
        if(selectedSmsNotification.getValue().getNotificationStatus()!=NotificationStatus.WAITING){
            Messagebox.show("Клиентов, которым отослано смс, удалить нельзя.","Удалить нельзя",Messagebox.OK,Messagebox.ON_ABORT);
        }
        currentMassSms.getSmsNotifications().remove(selectedSmsNotification.getKey());
        refresh();
    }
    @Command
    public void newClient(){
        Client client = dynamicaColumnUtils.createClient();
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("client",client);
        BindUtils.postGlobalCommand(null, null, "editClient", args);
    }
    @Command @NotifyChange({"filter","selectedCategory","filteredClients"})
    public void clearFilters(){
        filter=null;
        selectedCategory=null;
        refreshClients();
    }

    @Command   @NotifyChange("currentMassSms")
    public void deleteAll(){
        currentMassSms=massSmsDAO.get(currentMassSms.getId());
        currentMassSms.getSmsNotifications().clear();
        refresh();
    }
    @Command @NotifyChange("currentMassSms")
    public void addClient(){
        if(clientToAdd==null){
            Messagebox.show("Выбирете клиента для добавления в список рассылки");
            return;
        }
        if(currentMassSms.getId()!=null){
            currentMassSms=massSmsDAO.get(currentMassSms.getId());
        }
        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setMassSms(currentMassSms);
        smsNotification.setClient(clientToAdd);
        currentMassSms.getSmsNotifications().put(clientToAdd, smsNotification);
        massSmsDAO.saveOrUpdate(currentMassSms);
        smsNotificationDAO.saveOrUpdate(smsNotification);
        refresh();
    }
    @Command @NotifyChange("currentMassSms")
    public void addAllClient(){
        if(currentMassSms.getId()!=null){
            currentMassSms=massSmsDAO.get(currentMassSms.getId());
        }
        for (Client c:filteredClients){
            SmsNotification smsNotification = new SmsNotification();
            smsNotification.setClient(c);
            smsNotification.setMassSms(currentMassSms);
            smsNotificationDAO.saveOrUpdate(smsNotification);
            currentMassSms.getSmsNotifications().put(c,smsNotification);
        }
        massSmsDAO.saveOrUpdate(currentMassSms);
        refresh();
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public List<Client> getFilteredClients() {
        return filteredClients;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public Client getClientToAdd() {
        return clientToAdd;
    }

    public void setClientToAdd(Client clientToAdd) {
        this.clientToAdd = clientToAdd;
    }


    public Map.Entry getSelectedSmsNotification() {
        return selectedSmsNotification;
    }

    public void setSelectedSmsNotification(Map.Entry selectedSmsNotification) {
        this.selectedSmsNotification = selectedSmsNotification;
    }

    public MassSms getCurrentMassSms() {
        return currentMassSms;
    }

    public Date getTempTime() {
        return tempTime;
    }

    public void setTempTime(Date tempTime) {
        this.tempTime = tempTime;
    }

    public PropConverter getStatusConverter() {
        return statusConverter;
    }
}
