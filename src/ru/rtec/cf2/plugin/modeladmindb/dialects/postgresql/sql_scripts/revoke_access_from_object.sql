/* Скрипт для снятия полномочий пользователя по редактированию объекта */

DELETE FROM users_access_map_table 
WHERE user_name = '%1$s' AND access_object_id = %2$s;
