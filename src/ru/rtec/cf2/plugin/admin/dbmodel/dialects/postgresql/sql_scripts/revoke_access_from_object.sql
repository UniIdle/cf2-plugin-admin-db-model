/* Скрипт для снятия полномочий пользователя по редактированию объекта */

DELETE FROM %3$s 
WHERE user_name = '%1$s' AND access_object_id = %2$s;
