package ru.dev_server.client.utils;

import net.sf.autodao.PersistentEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Phase;
import org.zkoss.bind.PhaseListener;
import org.zkoss.zkplus.spring.SpringUtil;

import java.lang.reflect.Field;

/**
 * .
 */
public class HibernateAttachPhaseListener implements PhaseListener{
    private static final Logger LOG = LoggerFactory.getLogger(HibernateAttachPhaseListener.class);


    @Override
    public void prePhase(Phase phase, BindContext ctx) {
        LOG.debug("prePhase");

        if(phase!=Phase.COMMAND && phase!=Phase.GLOBAL_COMMAND){
            return;
        }
        final Object viewModel = ctx.getBinder().getViewModel();
        final Field[] fields = viewModel.getClass().getDeclaredFields();

        for(Field f:fields){

            final DetachableModel annotation = f.getAnnotation(DetachableModel.class);
            if(annotation==null){
                continue;
            }
            try {
                f.setAccessible(true);
                final Object o = f.get(viewModel);
                if(! (o instanceof PersistentEntity)){
                    continue;
                }

                PersistentEntity pe=(PersistentEntity)o;

                if(pe.getPrimaryKey()==null){
                    continue;
                }

                final Session currentSession = getCurrentSession();
                final Object newPe = currentSession.get(o.getClass(), pe.getPrimaryKey());
                f.set(viewModel,newPe);



            } catch (Exception e) {
                LOG.error(e.getMessage(),e);
            }

        }

    }

    private Session getCurrentSession(){
        SessionFactory sf=(SessionFactory)SpringUtil.getBean("sessionFactory");
        return sf.getCurrentSession();

    }

    @Override
    public void postPhase(Phase phase, BindContext ctx) {
        LOG.debug("postPhase");
    }
}
