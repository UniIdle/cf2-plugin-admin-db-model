/* Скрипт для получения ОБЪЕКТОВ к которым выдавался доступ конкретному пользователю */

SELECT accessible_object_id 
FROM %2$s 
WHERE user_name = '%1$s' AND is_writeable;