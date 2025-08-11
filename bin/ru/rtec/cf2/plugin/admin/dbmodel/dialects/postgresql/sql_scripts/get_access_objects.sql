/* Скрипт для получения имен доступных объектов*/

SELECT id, name 
FROM users_access_map_table uamt 
JOIN object obj
ON uamt.access_object_id = obj.id
WHERE uamt.user_name = '%s';
