package ru.dev_server.client.dao;

import net.sf.autodao.AutoDAO;
import net.sf.autodao.Dao;
import net.sf.autodao.Finder;
import net.sf.autodao.Limit;
import net.sf.autodao.Named;
import net.sf.autodao.Offset;
import ru.dev_server.client.model.Company;
import ru.dev_server.client.model.Employee;
import ru.dev_server.client.model.ItGroup;
import ru.dev_server.client.model.JuridicalPerson;

import java.util.List;

/**
 * .
 */

@AutoDAO
public interface JuridicalPersonDAO extends Dao<JuridicalPerson, Long> {

    @Finder(query="select count (*) from JuridicalPerson where company=:company AND deleted=false ")
    public Long getClientCount(@Named("company") Company company);

    @Finder(query="from JuridicalPerson  where company=:company AND deleted=false order by name")
    public List<JuridicalPerson> findAll(@Named("company") Company company,@Offset int offset, @Limit int limit);


    @Finder(query="select distinct jp from JuridicalPerson jp  " +
            " left outer join  jp.publicGroups as pub  " +
            " left outer join  jp.privateGroups as priv  "+
            " where jp.company=:company AND (pub=:itGroup OR priv=:itGroup)" +
            " ")
    public  List<JuridicalPerson>  findByGroup(@Named("company") Company company, @Named("itGroup")ItGroup itGroup);



    @Finder(query="select count (distinct jp) from JuridicalPerson jp" +



            " where " +
            "(:name ='' OR (UPPER(jp.name)=UPPER(:name) OR UPPER(jp.name) like UPPER(:name ))) AND "+
            "(:region ='' OR :region =jp.region OR UPPER(jp.region) like UPPER(:region)) AND "+
            "(:city ='' OR :city =jp.city OR UPPER(jp.city) like UPPER(:city)) AND "+
            "(:address ='' OR :address =jp.address OR UPPER(jp.address) like UPPER(:address))AND " +
            "(:note ='' OR :note=jp.note OR UPPER(jp.note) like UPPER(:note))AND " +

            "(:phone ='' OR (jp.phones like :phone OR jp.phones=:phone)) AND "+
            "(:fax ='' OR (jp.fax like :fax OR jp.fax=:fax)) AND "+

            " not exists (select ig from ItGroup as ig where ig.company=:company AND ig not member of jp.publicGroups AND ig not in (:noPubGroups) ) AND "+
            " not exists (select ig from ItGroup as ig where ig.owner=:employee AND ig not member of jp.privateGroups AND ig not in (:noPrivGroups) ) AND "+


            " jp.company=:company AND jp.deleted=false ")
    public Long getJuridicalPersonCountByFilter(@Named("name") String name, @Named("region") String region,
                                                @Named("city") String city, @Named("address") String address,
                                                @Named("phone") String phone, @Named("fax") String fax,
                                                @Named("note") String note,
                                                @Named("noPubGroups") List<ItGroup> noPubGroups,
                                                @Named("noPrivGroups") List<ItGroup> noPrivGroups,
                                                @Named("employee") Employee employee,
                                                @Named("company") Company company);





    @Finder(query="select jp from JuridicalPerson jp " +

            " where " +
            "(:name ='' OR (UPPER(jp.name)=UPPER(:name) OR UPPER(jp.name) like UPPER(:name ))) AND "+
            "(:region ='' OR :region =jp.region OR UPPER(jp.region) like UPPER(:region)) AND "+
            "(:city ='' OR :city =jp.city OR UPPER(jp.city) like UPPER(:city)) AND "+
            "(:address ='' OR :address =jp.address OR UPPER(jp.address) like UPPER(:address))AND " +
            "(:note ='' OR :note=jp.note OR UPPER(jp.note) like UPPER(:note))AND " +

            "(:phone ='' OR (jp.phones like :phone OR jp.phones=:phone)) AND "+
            "(:fax ='' OR (jp.fax like :fax OR jp.fax=:fax)) AND "+



            " not exists (select ig from ItGroup as ig where ig.company=:company AND ig not member of jp.publicGroups AND ig not in (:noPubGroups) ) AND "+
            " not exists (select ig from ItGroup as ig where ig.owner=:employee AND ig not member of jp.privateGroups AND ig not in (:noPrivGroups) ) AND "+


           " jp.company=:company AND jp.deleted=false")

    public List<JuridicalPerson> findByFilter(@Named("name") String name, @Named("region") String region,
                                              @Named("city") String city,@Named("address") String address,
                                              @Named("phone") String phone,@Named("fax") String fax,
                                              @Named("note") String note,
                                              @Named("noPubGroups") List<ItGroup> noPubGroups,
                                              @Named("noPrivGroups") List<ItGroup> noPrivGroups,
                                              @Named("employee") Employee employee,
                                              @Named("company") Company company,
                                              @Offset int offset, @Limit int limit);



}