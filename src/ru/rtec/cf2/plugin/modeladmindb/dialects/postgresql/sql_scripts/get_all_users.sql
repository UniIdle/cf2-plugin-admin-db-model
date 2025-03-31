/* Скрипт получения списка пользователей конфигуратора */

WITH RECURSIVE rec AS ( 
	SELECT roleid, member 
	FROM pg_auth_members 
	WHERE roleid IN ( 
		SELECT oid 
		FROM pg_roles 
		WHERE rolname = 'cf2_base_user_role' 
	)

	UNION 
	SELECT m.roleid, m.member 
	FROM pg_auth_members AS m 
	JOIN rec ON rec.member = m.roleid 
) SELECT u.usename 
	FROM pg_roles r 
	JOIN rec AS m ON r.oid = m.roleid 
	JOIN pg_user u ON u.usesysid = m.member 
	WHERE u.usename NOT IN (
		SELECT schema_owner 
		FROM information_schema.schemata 
		WHERE schema_name = current_schema
	)
	ORDER BY u.usename;