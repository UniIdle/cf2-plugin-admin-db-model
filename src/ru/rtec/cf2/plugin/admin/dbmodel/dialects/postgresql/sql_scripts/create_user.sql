/* Скрипт для создания пользователя конфигуратора */

CREATE USER %1$s WITH PASSWORD '%2$s';
GRANT %3$s TO %1$s;
GRANT %1$s TO %4$s WITH ADMIN OPTION;

DO $$
DECLARE schema_name name;
BEGIN
	schema_name := current_schema;
	EXECUTE 'ALTER ROLE %1$s SET search_path TO ' || schema_name || ';';
END$$;
