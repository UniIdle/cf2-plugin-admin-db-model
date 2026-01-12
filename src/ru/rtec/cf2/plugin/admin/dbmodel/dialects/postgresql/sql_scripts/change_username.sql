/* Скрипт для смены имени пользователя */

DO $$ 
BEGIN
	IF (SELECT EXISTS (
		SELECT pg_get_userbyid(nspowner)
		FROM pg_namespace 
		WHERE nspname = current_schema() AND 
				pg_get_userbyid(nspowner) = session_user
	)) THEN
		EXECUTE 'ALTER USER %1$s RENAME TO %2$s';
	ELSE
		EXECUTE 'SET ROLE %3$s;';
		EXECUTE 'ALTER USER %1$s RENAME TO %2$s;';
		EXECUTE 'RESET ROLE;';
	END IF;
END $$;
