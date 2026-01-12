/* Скрипт по удалению пользователя конфигуратора */

DO $$ 
BEGIN
	IF (SELECT EXISTS (
		SELECT pg_get_userbyid(nspowner)
		FROM pg_namespace 
		WHERE nspname = current_schema() AND 
				pg_get_userbyid(nspowner) = session_user
	)) THEN
		EXECUTE 'DROP USER %1$s;';
		EXECUTE 'DELETE FROM %3$s WHERE user_name = ''%1$s'';';
		EXECUTE 'DELETE FROM %4$s WHERE user_name = ''%1$s'';';
	ELSE
		EXECUTE 'SET ROLE %2$s;';
		EXECUTE 'DROP USER %1$s;';
		EXECUTE 'DELETE FROM %3$s WHERE user_name = ''%1$s'';';
		EXECUTE 'DELETE FROM %4$s WHERE user_name = ''%1$s'';';
		EXECUTE 'RESET ROLE;';
	END IF;
END $$;
