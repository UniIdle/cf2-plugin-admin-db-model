/* Скрипт для смены пароля у пользователя конфигуратора */

DO $$ 
BEGIN
	IF (SELECT EXISTS (
		SELECT pg_get_userbyid(nspowner)
		FROM pg_namespace 
		WHERE nspname = current_schema() AND 
				pg_get_userbyid(nspowner) = session_user
	)) THEN
		EXECUTE 'ALTER ROLE %1$s WITH PASSWORD ''%2$s'';';
	ELSE
		EXECUTE 'SET ROLE %3$s;';
		EXECUTE 'ALTER ROLE %1$s WITH PASSWORD ''%2$s'';';
		EXECUTE 'RESET ROLE;';
	END IF;
END $$;
