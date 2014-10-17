CREATE TABLE jurid_person_group_priv
(
  juridicalperson_id numeric NOT NULL,
  group_id numeric NOT NULL
);
insert into jurid_person_group_priv select distinct  gpg.* from jurid_person_group gpg, itgroup g where gpg.group_id=g.id AND  g.company_id is null;
delete   from jurid_person_group where group_id in (select id from itgroup where company_id is null);