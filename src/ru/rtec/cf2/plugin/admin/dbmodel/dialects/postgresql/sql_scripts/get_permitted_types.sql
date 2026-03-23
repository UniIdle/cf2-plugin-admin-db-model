/* Скрипт для получения ТИПОВ(ШАБЛОНОВ) к которым выдавался доступ конкретному пользователю */

SELECT accessible_class_id 
FROM %2$s 
WHERE user_name = '%1$s';
