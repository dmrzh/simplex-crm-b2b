package ru.dev_server.client.dao;

import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;

import javax.annotation.Resource;
import java.util.List;

/**
 * .
 */
public class JuridicalPersonFilterDAO {

    @Resource
    private SessionFactory sessionFactory;


    public Long getJuridicalPersonCountByFilter(@Named("name") String name, @Named("region") String region,
                                                @Named("city") String city, @Named("address") String address,
                                                @Named("phone") String phone, @Named("fax") String fax,
                                                @Named("note") String note,
                                                @Named("noPubGroups") List<ItGroup> noPubGroups,
                                                @Named("noPrivGroups") List<ItGroup> noPrivGroups,
                                                @Named("company") Company company){

        Session sess= sessionFactory.getCurrentSession();

        Criteria criteria = sess.createCriteria(JuridicalPerson.class ,"jp");


        criteria.setProjection(Projections.countDistinct("id"));
//        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

        addrestrictions(name, region, city, address, phone, fax, note, noPubGroups, noPrivGroups, company, criteria);


        Object o = criteria.uniqueResult();
        return (Long) o;

    }

    private void addrestrictions(String name, String region, String city, String address, String phone, String fax, String note, List<ItGroup> noPubGroups, List<ItGroup> noPrivGroups, Company company, Criteria criteria) {
        criteria.createAlias("jp.publicGroups", "pub");
        criteria.createAlias("jp.privateGroups", "priv");


        checkAndAdd(criteria, "name", name);
        checkAndAdd(criteria, "region", region);
        checkAndAdd(criteria, "city", city);
        checkAndAdd(criteria, "address", address);
        checkAndAdd(criteria, "note", note);


        checkAndAdd(criteria, "phones", phone);
        checkAndAdd(criteria, "fax", fax);


        criteria.add(Restrictions.eq("company", company));
        criteria.add(Restrictions.eq("deleted", false));

        for(ItGroup pub:noPubGroups){
            criteria.add(Restrictions.eq("pub.id", pub.getId()));

        }

        for(ItGroup priv:noPrivGroups){
            criteria.add(Restrictions.eq("priv.id", priv.getId()));

        }
    }


    public List<Long> findByFilter(@Named("name") String name, @Named("region") String region,
                                              @Named("city") String city,@Named("address") String address,
                                              @Named("phone") String phone,@Named("fax") String fax,
                                              @Named("note") String note,
                                              @Named("noPubGroups") List<ItGroup> noPubGroups,
                                              @Named("noPrivGroups") List<ItGroup> noPrivGroups,
                                              @Named("company") Company company,
                                              @Offset int offset, @Limit int limit){

        Session sess= sessionFactory.getCurrentSession();

        Criteria criteria = sess.createCriteria(JuridicalPerson.class,"jp");

        criteria.setProjection(Projections.distinct(Projections.id()));


        addrestrictions(name, region, city, address, phone, fax, note, noPubGroups, noPrivGroups, company, criteria);


        criteria.setFirstResult(offset);
        criteria.setMaxResults(limit);

        List<Long> results= criteria.list();
        return results;

    }


    public void checkAndAdd(Criteria criteria, String col, String val){
        if(val!=null && val.trim().length()>0){
            criteria.add(Restrictions.ilike(col, val));
        }

    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
