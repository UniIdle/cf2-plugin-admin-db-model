/* Скрипт для получения ОБЪЕКТОВ к которым назначался доступ для чтения конкретному пользователю */

SELECT accessible_object_id 
FROM %2$s 
WHERE user_name = '%1$s' 
	AND NOT is_writeable 
	AND is_readable;