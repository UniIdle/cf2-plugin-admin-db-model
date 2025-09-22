/* Скрипт для получения имен доступных корневых объектов*/

SELECT obj.id, obj.name
FROM %2$s uamt 
JOIN object obj
ON obj.id = uamt.access_object_id
WHERE user_name = '%1$s';
