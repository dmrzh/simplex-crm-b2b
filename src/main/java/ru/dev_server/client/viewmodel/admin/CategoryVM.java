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
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.model.Category;

/**. */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CategoryVM {
    private static final Logger LOG= LoggerFactory.getLogger(CategoryVM.class) ;
    @WireVariable
    private CategoryDAO categoryDAO;



    private  Window categoryWin;
    private Category currentCategory;

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("category")Category category){
        categoryWin =view;
        currentCategory=category;
    }

    @Command
    public void save(){
        categoryDAO.saveOrUpdate(currentCategory);
        categoryWin.detach();
    }



    @Command
    public void cancel(){
        categoryWin.detach();
    }


    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }
}
