package ru.dev_server.client.viewmodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Converter;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Window;
import ru.dev_server.client.I18nConverter;
import ru.dev_server.client.dao.ClientDAO;
import ru.dev_server.client.dao.ContactDAO;
import ru.dev_server.client.model.AbstractClient;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;

import java.util.regex.Matcher;


/**.*/
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ContactViewModel {
    private static final Logger LOG= LoggerFactory.getLogger(ContactViewModel.class);


    @WireVariable("clientDAO")
    private ClientDAO clientDAO;

    @WireVariable("contactDAO")
    private ContactDAO contactDAO;
    private Window contactWin;


    private Contact currentContact;


    private ContactType[] contactTypes= ContactType.values();

    private Converter typeConverter=new I18nConverter(ContactType.class);


    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Window view,@ExecutionArgParam("contact") Contact contact){
        contactWin=view;
        currentContact=contact;
    }

    public Validator getFormValidator(){
        return new AbstractValidator() {
            @Override
            public void validate(ValidationContext ctx) {
               Contact base = (Contact)ctx.getProperty().getBase();
               String  contactVal =(String) ctx.getProperties("value")[0].getValue();
                    Matcher matcher = base.getContactType().getPattern().matcher(contactVal);
                    if(!matcher.matches()){
                        addInvalidMessage(ctx, "Неверный формат. Используйте формат "+base.getContactType().getFormatMsg());
                    }
            }
        };
    }

    @Command
    public void save(){
        AbstractClient client = currentContact.getClient();
        if(!client.getContacts().contains(currentContact)){
            client.getContacts().add(currentContact);
        }
        contactWin.detach();
    }
    @Command
    public void cancel(){
        contactWin.detach();
    }


    public Contact getCurrentContact() {
        return currentContact;
    }

    public void setCurrentContact(Contact currentContact) {
        this.currentContact = currentContact;
    }

    public ContactType[] getContactTypes() {
        return contactTypes;
    }

    public Converter getTypeConverter() {
        return typeConverter;
    }

    public void setTypeConverter(Converter typeConverter) {
        this.typeConverter = typeConverter;
    }
}
