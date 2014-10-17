package ru.dev_server.client.utils;

import ru.dev_server.client.model.Client;
import ru.dev_server.client.model.Contact;
import ru.dev_server.client.model.ContactType;

/**.*/
public class ClientUtils {

    static  public boolean hasMobile(Client client) {
        for(Contact ct:client.getContacts()){
            if(ContactType.MOBILE==ct.getContactType()){
                return true;
            }
        }
        return false;
    }

    static  public boolean hasEmail(Client client) {
        for(Contact ct:client.getContacts()){
            if(ContactType.EMAIL==ct.getContactType()){
                return true;
            }
        }
        return false;
    }

    static  public Contact getFirstMobile(Client client) {
        for(Contact ct:client.getContacts()){
            if(ContactType.MOBILE==ct.getContactType()){
                return ct;
            }
        }
        throw new IllegalStateException("cant find mobile");
    }

    static  public Contact getFirstEmail(Client client) {
        for(Contact ct:client.getContacts()){
            if(ContactType.EMAIL==ct.getContactType()){
                return ct;
            }
        }
        throw new IllegalStateException("cant find email");
    }
}
