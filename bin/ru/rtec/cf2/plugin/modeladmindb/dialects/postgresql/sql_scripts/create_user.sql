/* Скрипт для создания пользователя конфигуратора */

CREATE USER %1$s WITH PASSWORD '%2$s';
GRANT cf2_base_user_role TO %1$s;
ALTER ROLE %1$s SET search_path TO current_schema;
