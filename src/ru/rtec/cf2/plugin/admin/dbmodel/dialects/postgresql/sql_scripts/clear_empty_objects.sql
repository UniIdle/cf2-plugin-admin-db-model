/* Скрипт для снятия полномочий пользователя по редактированию объекта */

DELETE FROM %s 
WHERE access_object_id NOT IN (SELECT id FROM object);
