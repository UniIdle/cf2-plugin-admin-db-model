/* Скрипт для получения привелегий пользователя */

SELECT r.rolname
FROM pg_roles r
JOIN pg_auth_members m ON r.oid = m.roleid
JOIN pg_roles r2 ON r2.oid = m.member
WHERE r2.rolname = '%s' AND
	r.rolname != 'cf2_base_user_role';