/* Скрипт проверяющий целостность БД для организации ролевого доступа */

SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME IN ('%s', '%s', '%s') 
UNION 
SELECT proname 
FROM pg_proc p 
JOIN pg_namespace n ON p.pronamespace = n.oid 
WHERE n.nspname = current_schema AND 
	p.proname IN ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s') 
UNION 
SELECT rolname 
FROM pg_roles 
WHERE rolname IN (
	'%s', '%s', '%s', '%s', '%s', '%s'
) 
ORDER BY proname;
