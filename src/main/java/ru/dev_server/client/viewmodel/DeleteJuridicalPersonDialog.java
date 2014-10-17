package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.sys.BinderCtrl;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.dao.JuridicalPersonDAO;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.Random;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class DeleteJuridicalPersonDialog {
    private static final Logger LOG= LoggerFactory.getLogger(DeleteJuridicalPersonDialog.class);
    private static final Random RANDOM = new Random();

    @WireVariable
    private JuridicalPersonDAO juridicalPersonDAO;

    private JuridicalPerson juridicalPerson;

    private  String checkCode;
    private  String checkAnswer;
    private Window window;

    public DeleteJuridicalPersonDialog() {
        this.checkCode = ""+ RANDOM.nextInt(99999) ;
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("juridicalPerson") JuridicalPerson juridicalPerson){
        this.window=view;
        this.juridicalPerson =juridicalPerson;
    }
    @Command
    public void delete(){
        this.window.detach();
        if(checkCode.equals(checkAnswer)) {
            juridicalPerson = juridicalPersonDAO.get(juridicalPerson.getId());
            juridicalPerson.setDeleted(true);
            juridicalPersonDAO.saveOrUpdate(juridicalPerson);
            juridicalPerson = null;
            BindUtils.postGlobalCommand(BinderCtrl.DEFAULT_QUEUE_NAME, null, "refreshJuridicalPersons", null);
        }else{
            Messagebox.show("Код не подтверждён","Ошибка!",Messagebox.OK,Messagebox.ERROR);
        }

    }

    public String getCheckAnswer() {
        return checkAnswer;
    }

    public void setCheckAnswer(String checkAnswer) {
        this.checkAnswer = checkAnswer;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
