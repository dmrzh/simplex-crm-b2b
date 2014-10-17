CREATE FUNCTION  sort_order(ct_id numeric) RETURNS integer as $$
DECLARE
    cl_id numeric;
    sortOrd integer;
BEGIN
    select client_id into cl_id from  contact where id=ct_id;
    select A.SORT_ORDER INTO sortOrd from (
    select ct1.id as id , Row_Number() OVER (ORDER BY id)+9 as SORT_ORDER from contact ct1 where ct1.client_id=cl_id
    ) AS A
    WHERE A.id=ct_id;
    RETURN sortOrd;
END;
$$ LANGUAGE plpgsql;
update contact set index=sort_order(id) ;
DROP FUNCTION sort_order(numeric);
