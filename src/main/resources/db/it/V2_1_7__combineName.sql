ALTER TABLE Client  ADD COLUMN name character varying(255);
update  Client set name=
                        case WHEN firstName is null  then '' else firstName || ' ' END||
                        case WHEN middleName is null  then '' else middleName || ' ' END||
                        case WHEN lastName is null  then '' else lastName END  ;

CREATE INDEX nameidx  ON client  USING btree  (name);

--todo remove firstName middleName  lastName


