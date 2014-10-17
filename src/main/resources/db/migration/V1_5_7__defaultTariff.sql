ALTER TABLE tariff ADD COLUMN defaulttariff character(1);
UPDATE tariff SET defaulttariff='Y';
ALTER TABLE tariff ALTER COLUMN defaulttariff SET NOT NULL;