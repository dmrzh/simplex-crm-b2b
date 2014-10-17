ALTER TABLE juridicalperson ADD COLUMN fax character varying(255);

update juridicalperson set fax=(
	select array_to_string(array_agg(contact.value), ';') from contact where contact.client_id=juridicalperson.id AND contact.contactType='FAX'
	);

ALTER TABLE juridicalperson ADD COLUMN phones character varying(255);

update juridicalperson set phones=(
	select array_to_string(array_agg(contact.value), ';') from contact where contact.client_id=juridicalperson.id AND contact.contactType='PHONE'
	);