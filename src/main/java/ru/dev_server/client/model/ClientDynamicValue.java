package ru.dev_server.client.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**.*/
@Entity
public class ClientDynamicValue extends DynamicValue {

    @ManyToOne
    private Client client;

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
