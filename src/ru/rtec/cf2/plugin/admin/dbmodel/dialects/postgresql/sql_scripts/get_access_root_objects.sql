/* Скрипт для получения имен доступных корневых объектов*/

SELECT obj.id, obj.name 
FROM users_access_map_table uamt 
JOIN object obj
ON uamt.access_object_id = obj.id
WHERE uamt.user_name = '%s';
