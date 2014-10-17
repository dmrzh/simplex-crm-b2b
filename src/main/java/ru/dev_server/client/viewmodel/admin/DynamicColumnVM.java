package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.DynamicaColumnUtils;
import ru.dev_server.client.dao.DynamicColumnDAO;
import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.DynamicColumn;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DynamicColumnVM {

    private static final Logger LOG= LoggerFactory.getLogger(DynamicColumnVM.class);

    public static final ArrayList<Class<?>> CLASSES = new ArrayList<Class<?>>();
    static{ CLASSES.add(String.class); CLASSES.add(Integer.class);CLASSES.add(Double.class);
    CLASSES.add(Date.class); CLASSES.add(Client.class); CLASSES.add(URL.class);CLASSES.add(Boolean.class);CLASSES.add(Color.class);}


    @WireVariable
    DynamicColumnDAO dynamicColumnDAO;

    @WireVariable
    private  DynamicaColumnUtils dynamicaColumnUtils;

    private  Window dynamicColumnWin;

    private DynamicColumn dynamicColumn;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("dynamicColumn") DynamicColumn dynamicColumn){
        dynamicColumnWin=view;
        this.dynamicColumn=dynamicColumn;
    }

    @Command
    public void save(){
        boolean newColumn = dynamicColumn.getId()==null;
        dynamicColumnDAO.saveOrUpdate(dynamicColumn);
        if(newColumn){
            dynamicaColumnUtils.updateDependentObjects(dynamicColumn);
        }
        dynamicColumnWin.detach();
    }



    public DynamicColumn getDynamicColumn() {
        return dynamicColumn;
    }

    public void setDynamicColumn(DynamicColumn dynamicColumn) {
        this.dynamicColumn = dynamicColumn;
    }
    public List<Class<?>> getColumnTypes(){
        return CLASSES;
    }
}
