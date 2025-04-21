/* Скрипт проверяет полномочия администратора доступа для текущего пользователя */

SELECT r.rolname
FROM pg_roles r
JOIN pg_auth_members m ON r.oid = m.roleid
JOIN pg_user u ON u.usesysid = m.member
WHERE u.usename = current_user and (
	r.rolname = 'cf2_object_access_controller_role' OR 
	r.rolname = 'cf2_function_access_controller_role'
);