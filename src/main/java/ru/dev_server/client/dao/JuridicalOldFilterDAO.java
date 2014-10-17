package ru.dev_server.client.dao;

import net.sf.autodao.Named;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;
import ru.dev_server.client.model.JuridicalPersonFilter;

import javax.annotation.Resource;
import java.util.List;

/**
 * .
 */
public class JuridicalOldFilterDAO {
    @Resource
    private SessionFactory sessionFactory;



    public Long getJuridicalPersonCountByFilter(JuridicalPersonFilter filter,

                                                List<ItGroup> noPubGroups, boolean hasPub,
                                                List<ItGroup> noPrivGroups,boolean hasPriv,
                                                Employee employee,
                                                Company company){


        String header = "select count (distinct jp) from JuridicalPerson jp";


        boolean hasNoPub = !noPubGroups.isEmpty();
        boolean hasNoPriv = !noPrivGroups.isEmpty();
        String query = createQuery(header, hasNoPub, hasNoPriv, hasPub, hasPriv);
        Session session = sessionFactory.getCurrentSession();
        Query hQquery = session.createQuery(query);
        hQquery.setString("name", JuridicalPersonFilter.wrapFullSearch(filter.getName()));
        hQquery.setString("region", JuridicalPersonFilter.wrapFullSearch(filter.getRegion()));
        hQquery.setString("city", JuridicalPersonFilter.wrapFullSearch(filter.getCity()));
        hQquery.setString("address", JuridicalPersonFilter.wrapFullSearch(filter.getAddress()));
        hQquery.setString("contact", JuridicalPersonFilter.wrapFullSearch(filter.getContact()));
        hQquery.setString("fax", JuridicalPersonFilter.wrapFullSearch(filter.getFax()));
        hQquery.setString("site", JuridicalPersonFilter.wrapFullSearch(filter.getSite()));
        if(hasPub&&hasNoPub){
            hQquery.setParameterList("noPubGroups", noPubGroups);
        }
        if(hasPriv){
            hQquery.setParameter("employee", employee);
            if(hasNoPriv){
                hQquery.setParameterList("noPrivGroups", noPrivGroups);
            }
        }
        hQquery.setParameter("company",company);
        Object o = hQquery.uniqueResult();
        return (Long)o;


    }



    public List<JuridicalPerson> findByFilter(JuridicalPersonFilter filter,
                                              @Named("noPubGroups") List<ItGroup> noPubGroups, boolean hasPub,
                                              @Named("noPrivGroups") List<ItGroup> noPrivGroups,boolean hasPriv,
                                              Employee employee,
                                              Company company,
                                              int offset, int limit){

        String header = "select distinct jp from JuridicalPerson jp";
        boolean hasNoPub = !noPubGroups.isEmpty();
        boolean hasNoPriv = !noPrivGroups.isEmpty();

        String query = createQuery(header, hasNoPub,hasNoPriv, hasPub, hasPriv);
        query=query+" order by jp.name";
        Session session = sessionFactory.getCurrentSession();
        Query hQquery = session.createQuery(query);
        hQquery.setString("name", JuridicalPersonFilter.wrapFullSearch(filter.getName()));
        hQquery.setString("region", JuridicalPersonFilter.wrapFullSearch(filter.getRegion()));
        hQquery.setString("city", JuridicalPersonFilter.wrapFullSearch(filter.getCity()));
        hQquery.setString("address", JuridicalPersonFilter.wrapFullSearch(filter.getAddress()));
        hQquery.setString("contact", JuridicalPersonFilter.wrapFullSearch(filter.getContact()));
        hQquery.setString("fax", JuridicalPersonFilter.wrapFullSearch(filter.getFax()));
        hQquery.setString("site", JuridicalPersonFilter.wrapFullSearch(filter.getSite()));
        if(hasPub&&hasNoPub){
            hQquery.setParameterList("noPubGroups", noPubGroups);
        }
        if(hasPriv){
            hQquery.setParameter("employee", employee);
            if(hasNoPriv){
                hQquery.setParameterList("noPrivGroups", noPrivGroups);
            }
        }



        hQquery.setParameter("company",company);

        hQquery.setFirstResult(offset);
        hQquery.setMaxResults(limit);
        List list = hQquery.list();
        return list;

    }

    private String  createQuery(String header, boolean hasNoPub, boolean hasNoPriv, boolean hasPub, boolean hasPriv) {
        String  query= header +
                " left outer join  jp.contactPerson as contactPerson "+
                " where " +
                "(:name ='' OR (UPPER(jp.name)=UPPER(:name) OR UPPER(jp.name) like UPPER(:name ))) AND "+
                "(:region ='' OR UPPER(jp.region)=UPPER(:region) OR UPPER(jp.region) like UPPER(:region)) AND "+
                "(:city ='' OR (UPPER(jp.city)=UPPER(:city)  OR UPPER(jp.city) like UPPER(:city))) AND "+
                "(:address ='' OR UPPER(jp.address)=UPPER(:address) OR UPPER(jp.address) like UPPER(:address))AND " +
                "(:site ='' OR UPPER(jp.site)=UPPER(:site) OR UPPER(jp.site) like UPPER(:site))AND " +

                "(:contact ='' OR " +
                    " UPPER (jp.phones) like UPPER (:contact) OR UPPER (jp.phones)=UPPER (:contact) " +
                    " OR UPPER (contactPerson.name) like UPPER (:contact) OR UPPER (contactPerson.name) = UPPER (:contact) " +
                    " OR UPPER (contactPerson.phones) like UPPER (:contact)  OR UPPER (contactPerson.phones) =UPPER (:contact) " +
                    " OR UPPER (contactPerson.email) like UPPER (:contact) OR UPPER (contactPerson.email) = UPPER (:contact) " +
                ") AND "+
                "(:fax ='' OR (jp.fax like :fax OR jp.fax=:fax)) AND ";
        if(hasPub) {

            query = query + " not exists (" +
                    "select ig from ItGroup as ig where ig.company=:company AND ig not member of jp.publicGroups ";
            if (hasNoPub) {
                query = query + " AND ig not in (:noPubGroups) ";
            }
            query = query + ") AND ";
        }
         if(hasPriv) {
             query = query + " not exists (select ig from ItGroup as ig where ig.owner=:employee AND ig not member of jp.privateGroups ";
             if (hasNoPriv) {
                 query = query + "AND ig not in (:noPrivGroups) ";
             }
             query = query + ") AND ";

         }

        query = query +" jp.company=:company AND jp.deleted=false";
        return query;
     }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}

