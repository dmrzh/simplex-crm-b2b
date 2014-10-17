ALTER TABLE payment ADD COLUMN paymentstate character varying(255);
UPDATE payment  SET paymentstate ='PAYING' WHERE payed=false;
UPDATE payment  SET paymentstate ='PAYED' WHERE payed=true;
ALTER TABLE payment ALTER COLUMN paymentstate SET NOT NULL;

ALTER TABLE payment DROP COLUMN payed;