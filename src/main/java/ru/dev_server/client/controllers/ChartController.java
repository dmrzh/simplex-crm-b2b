package ru.dev_server.client.controllers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Area;
import org.zkoss.zul.Chart;
import org.zkoss.zul.PieModel;
import org.zkoss.zul.SimplePieModel;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Company;

import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ChartController  extends SelectorComposer {
    private static final int SHOW_MAX_CATIGORIES = 10;
    private Chart chart;
    @WireVariable
    CategoryDAO categoryDAO;
    @WireVariable
    ClientDAO clientDAO;

    @WireVariable("authService")
    private AuthService authService;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        chart=(Chart )comp;
        chart.addEventListener("onClick",new EventListener<MouseEvent>(){
            @Override
            public void onEvent(MouseEvent event) throws Exception {
                Area area = (Area)event.getAreaComponent();
                if (area != null)
                    alert(area.getTooltiptext());
            }
        });
        doRefresh();
        super.doAfterCompose(comp);
    }

    @Listen("onRefresh=#mychart")
    public void onRefresh(ForwardEvent e){
            doRefresh();
    }
    private void doRefresh() {
        Company company = authService.getCompany();
        List<Category> cats = categoryDAO.findAll(company);
        int maxSize=Math.min(SHOW_MAX_CATIGORIES, cats.size());
        PieModel model = new SimplePieModel();
        for(int i=0;i<maxSize;i++){
            model.setValue(cats.get(i).getName(), clientDAO.getClientCount(company,cats.get(i) ));
        }
        chart.setModel(model);
    }


}
