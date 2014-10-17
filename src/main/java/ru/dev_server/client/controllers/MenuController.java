package ru.dev_server.client.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.FullComposer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import ru.dev_server.client.ExportImport;

import java.util.HashMap;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class MenuController extends SelectorComposer implements FullComposer{
    private static final Logger LOG= LoggerFactory.getLogger(MenuController.class);
    @WireVariable
    private ExportImport exportImport;


    private boolean hasRole(String s){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            for(GrantedAuthority g:authentication.getAuthorities()){
                if(g.getAuthority().equals(s)){
                    return true;
                }
            }
            return false;
    }


    @Override
    public void doAfterCompose(Component comp) throws Exception {
        if(comp instanceof Menuitem){

            Menuitem menuitem = (Menuitem) comp;
            String value= menuitem.getValue();
            if(value.length()>0)
                comp.setVisible(hasRole(value));
        }
        super.doAfterCompose(comp);
    }



    @Listen("onClick = menuitem#aboutItm")
    public void onAbout(){
        HashMap params = new HashMap();
        Executions. createComponents("/about.zul", null, params);
    }
    @Listen("onClick = menuitem#employeeItm")
    public void onEmployee(){
        HashMap params = new HashMap();
        Executions. createComponents("/admin/employeeList.zul", null, params);
    }

    @Listen("onClick = menuitem#constructorItm")
    public void onConstructor(){
        HashMap params = new HashMap();
        Executions. createComponents("/admin/constructor.zul", null, params);
    }
    @Listen("onClick = menuitem#companySettings")
    public void onCompanySettings(){
        HashMap params = new HashMap();
        Executions. createComponents("/admin/companySettings.zul", null, params);
    }
    @Listen("onClick = menuitem#categoryItm")
    public void onCategory(){
        HashMap params = new HashMap();
        Executions. createComponents("/admin/categoryList.zul", null, params);
    }

    @Listen("onClick = menuitem#docItm")
    public void onDoc(){
        HashMap params = new HashMap();
        Executions. createComponents("/help.zul", null, params);
    }
    @Listen("onClick = menuitem#exportColumnsItm")
    public void onExportColumn(){
        exportImport.exportColumns();
    }

    @Listen("onUpload = menuitem#importColumnsItm")
    public void onImportColumn(UploadEvent event){
        try {
            exportImport.importColumns(event);
        } catch (Exception e) {
            LOG.debug(e.getMessage(),e);
            Messagebox.show("Ошибка импорта");
        }
    }

    @Listen("onClick = menuitem#increaseBalance")
    public void onIncreaseBalance(){
        HashMap params = new HashMap();
        Executions. createComponents("/WEB-INF/zul/admin/RKpay.zul", null, params);
    }
    @Listen("onClick = menuitem#paymentHistory")
    public void onPaymentHistory(){
        HashMap params = new HashMap();
        Executions. createComponents("/WEB-INF/zul/admin/paymentHistory.zul", null, params);
    }



    @Listen("onClick = menuitem#massSms")
    public void onMmssSms(){
        HashMap hashMap = new HashMap();
        hashMap.put("zul","/WEB-INF/zul/massSmsList.zul");
        hashMap.put("title","Все массовые рассылки СМС");
        BindUtils.postGlobalCommand(null, null, "newTab", hashMap);
//        HashMap params = new HashMap();
//        Executions. createComponents("/WEB-INF/zul/massSmsList.zul", null, params);
    }





}
