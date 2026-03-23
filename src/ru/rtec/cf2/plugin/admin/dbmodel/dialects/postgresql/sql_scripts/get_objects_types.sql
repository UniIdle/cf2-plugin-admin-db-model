/* Скрипт возвращает отображение объектов и их типов */

SELECT obj.id, cls.id
FROM object AS obj 
JOIN class_hierarchy AS ch 
	ON obj.class_hierarchy_id = ch.id 
JOIN classes AS cls
	ON ch.class_id = cls.id;
