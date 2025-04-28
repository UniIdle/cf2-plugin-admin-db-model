/* Скрипт получения списка пользователей с возможностью редактирования объектов */

SELECT distinct(u.usename)
FROM pg_roles r
JOIN pg_auth_members m ON r.oid = m.roleid
JOIN pg_user u ON u.usesysid = m.member
WHERE r.rolname = 'cf2_object_editor_role'
ORDER BY u.usename;
