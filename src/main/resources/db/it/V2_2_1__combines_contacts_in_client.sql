ALTER TABLE client ADD COLUMN phones character varying(255);
ALTER TABLE client ADD COLUMN email character varying(255);

update client set phones=(
	select array_to_string(array_agg(contact.value), ';') from contact where contact.client_id=client.id AND contact.contactType='PHONE'
	);


update client set email=(
    select array_to_string(array_agg(contact.value), ';') from contact where contact.client_id=client.id AND contact.contactType='EMAIL'
    );
