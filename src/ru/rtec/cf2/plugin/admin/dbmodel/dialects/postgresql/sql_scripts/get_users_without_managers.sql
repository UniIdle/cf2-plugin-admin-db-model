/* Скрипт получения списка пользователей конфигуратора (кроме членов роли "Администратор пользователей" */

WITH RECURSIVE rec AS ( 
	SELECT roleid, member 
	FROM pg_auth_members 
	WHERE roleid IN ( 
		SELECT oid 
		FROM pg_roles 
		WHERE rolname = '%1$s'
	) AND member NOT IN (
		SELECT member 
		FROM pg_auth_members 
		WHERE roleid in (
			SELECT oid 
			FROM pg_roles 
			WHERE rolname = '%2$s'
		)
	)
	UNION 
	SELECT m.roleid, m.member 
	FROM pg_auth_members AS m 
	JOIN rec ON rec.member = m.roleid 
	where m.member not in (
		SELECT member 
		FROM pg_auth_members 
		WHERE roleid in (
			SELECT oid 
			FROM pg_roles 
			WHERE rolname = '%2$s'
		)
	)
) SELECT DISTINCT(u.usename) 
	FROM pg_roles r 
	JOIN rec AS m ON r.oid = m.roleid 
	JOIN pg_user u ON u.usesysid = m.member 
	WHERE u.usename NOT IN (
		SELECT schema_owner 
		FROM information_schema.schemata 
		WHERE schema_name = current_schema
	) AND
	u.usename != current_user
	ORDER BY u.usename;