/* Скрипт для получения имен доступных корневых объектов*/

SELECT obj.id, obj.name
FROM users_access_map_table uamt 
JOIN object obj
ON obj.id = uamt.access_object_id
WHERE user_name = '%s';
