/* Скрипт для создания пользователя конфигуратора */

CREATE USER %1$s WITH PASSWORD '%2$s';
GRANT cf2_base_user_role TO %1$s;

DO $$
DECLARE schema_name name;
BEGIN
	schema_name := current_schema;
	EXECUTE 'ALTER ROLE %1$s SET search_path TO ' || schema_name;
END$$;
