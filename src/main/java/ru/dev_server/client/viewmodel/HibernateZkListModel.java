package ru.dev_server.client.viewmodel;

import net.sf.autodao.Dao;
import net.sf.autodao.PersistentEntity;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zul.AbstractListModel;
import org.zkoss.zul.event.ListDataEvent;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.List;

/**
 * .
 */
@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public abstract class HibernateZkListModel extends AbstractListModel<PersistentEntity> implements org.zkoss.zul.ext.Sortable {
    private Long[] ids;

     private Dao dao;


    protected HibernateZkListModel(Dao dao) {
        this.dao = dao;
    }

    private int PAGE_SIZE = 50;

    public HibernateZkListModel() {
        clearData();
    }


    public void clearData(){
        clearSelection();
        long clientCount=getObjectCount();

        int oldClientCount= ids.length;
        ids =new Long[((int)clientCount)];
        if(oldClientCount>0){
            fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
        }
    }

    abstract protected  long getObjectCount();

    @Override
    public PersistentEntity getElementAt(int index) {
        if(ids[index]==null){
            int i=0;
            for(JuridicalPerson c:getPageByIndex(index)){
                ids[index+(i++)]=c.getId();
            }
        }
        PersistentEntity client = dao.get(ids[index]);
        return client;
    }

    abstract List<JuridicalPerson> getPageByIndex(int index);

    @Override
    public int getSize() {
        return ids.length;
    }
}
