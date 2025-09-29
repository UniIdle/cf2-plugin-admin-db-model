/* Скрипт для исключения пользователя из групповой роли */

DO $$ 
BEGIN
	IF '%1$s' = '%3$s' THEN
		EXECUTE 'REVOKE %1$s FROM %2$s;';
		EXECUTE 'GRANT %2$s TO %3$s WITH ADMIN OPTION;';
	ELSE
		EXECUTE 'REVOKE %1$s FROM %2$s;';
	END IF;
END $$;
