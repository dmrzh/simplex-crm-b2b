-- 'Y' and 'N' instead boolean (for hql query execution)

ALTER TABLE itevent  ADD COLUMN remind2 character(1);
ALTER TABLE itevent  ADD COLUMN completed2  character(1);

update itevent set remind2='Y' where remind=true;
update itevent set remind2='N' where remind=false;

update itevent set completed2='Y' where completed=true;
update itevent set completed2='N' where completed=false;


ALTER TABLE itevent DROP COLUMN remind;
ALTER TABLE itevent DROP COLUMN completed;

ALTER TABLE itevent RENAME remind2  TO remind;
ALTER TABLE itevent ALTER COLUMN remind SET NOT NULL;

ALTER TABLE itevent RENAME completed2  TO completed;
ALTER TABLE itevent ALTER COLUMN completed SET NOT NULL;
