/* Скрипт для назначения членства пользователя в групповой роли */

DO $$ 
BEGIN
	IF '%1$s' = '%3$s' THEN
		EXECUTE 'REVOKE %2$s FROM %3$s;';
		EXECUTE 'GRANT %1$s TO %2$s;';
	ELSE
		EXECUTE 'GRANT %1$s TO %2$s;';
	END IF;
END $$;
