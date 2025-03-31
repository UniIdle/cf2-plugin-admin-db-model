/* Скрипт проверяющий целостность БД для организации ролевого доступа */

SELECT proname
FROM pg_proc p 
JOIN pg_namespace n ON p.pronamespace = n.oid 
WHERE n.nspname = 'cf2_schema' AND
	p.proname IN ('define_root_id', 'check_access')
UNION
SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='users_access_map'
UNION
SELECT rolname
FROM pg_roles
WHERE rolname in (
	'cf2_base_user',
	'cf2_function_access_controller_role', 
	'cf2_object_editor_role',
	'cf2_reference_editor_role', 
	'cf2_object_access_controller_role'
);