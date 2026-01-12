/* Скрипт для снятия полномочий пользователя по редактированию объекта */

DELETE FROM %s 
WHERE accessible_class_id NOT IN (SELECT id FROM classes);

DELETE FROM %s 
WHERE accessible_object_id NOT IN (SELECT id FROM object);
