/* Скрипт для назначения членства пользователя в групповой роли */

DO $$
BEGIN
	IF '%1$s' = 'cf2_security_controller_role' THEN
		EXECUTE 'ALTER ROLE %2$s CREATEROLE;';
		EXECUTE 'GRANT cf2_security_controller_role TO %2$s WITH ADMIN OPTION;';
	ELSE
		EXECUTE 'GRANT %1$s TO %2$s;';
	END IF;
END$$;
