/* Скрипт для исключения пользователя из групповой роли */

DO $$
BEGIN
	IF '%1$s' = 'cf2_function_access_controller_role' THEN
		EXECUTE 'ALTER ROLE %2$s NOCREATEROLE;';
	END IF;

	EXECUTE 'REVOKE %1$s FROM %2$s;';
END$$;