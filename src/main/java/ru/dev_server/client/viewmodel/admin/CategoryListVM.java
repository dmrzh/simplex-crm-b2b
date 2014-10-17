package ru.dev_server.client.viewmodel.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import ru.dev_server.client.AuthService;
import ru.dev_server.client.dao.CategoryDAO;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.model.Category;
import ru.dev_server.client.model.Client;

import java.util.HashMap;
import java.util.List;

/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class CategoryListVM {
    private static final Logger LOG= LoggerFactory.getLogger(CategoryListVM.class);
    @WireVariable("authService")
    private AuthService authService;
    @WireVariable
    private CategoryDAO categoryDAO;
    private List<Category> categoryList;
    private Category currentCategory;
    @WireVariable("clientDAO")
    private ClientDAO clientDAO;


    @Init
    public void init(){
       refreshCategoryList();
    }


    @Command
    @NotifyChange("categoryList")
    public void create(){
        Category category=new Category();
        category.setCompany(authService.getCompany());
        HashMap<String, Category> arg = new HashMap<String, Category>();
        arg.put("category", category);
        Executions. createComponents("/admin/category.zul", null, arg);
        init();
    }

    @Command
    public void edit(){
        if(currentCategory==null) return;
        HashMap<String, Category> arg = new HashMap<String, Category>();
        arg.put("category", currentCategory);
        Executions. createComponents("/admin/category.zul", null, arg);
    }
    @Command  @NotifyChange("categoryList")
    public void delete(){
        if(currentCategory== null){return;}
        List<Client> clientFoNullCategory = clientDAO.findByCategory(authService.getCompany(), currentCategory);
        for(Client c:clientFoNullCategory){
            c.setCategory(null);
        }
        categoryDAO.delete(categoryDAO.merge(currentCategory));
        refreshCategoryList();
    }

    @GlobalCommand
    @NotifyChange("categoryList")
    public void refreshCategoryList(){
        categoryList =categoryDAO.findAll(authService.getCompany());
    }


    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
