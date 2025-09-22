/* Скрипт проверяющий целостность БД для организации ролевого доступа */

SELECT proname
FROM pg_proc p 
JOIN pg_namespace n ON p.pronamespace = n.oid 
WHERE n.nspname = current_schema AND
	p.proname IN ('%s', '%s')
UNION
SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='%s'
UNION
SELECT rolname
FROM pg_roles
WHERE rolname in (
	'%s', '%s', '%s', '%s', '%s', '%s'
)
ORDER BY proname;