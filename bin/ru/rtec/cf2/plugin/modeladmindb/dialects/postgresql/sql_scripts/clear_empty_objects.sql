/* Скрипт для снятия полномочий пользователя по редактированию объекта */

DELETE FROM users_access_map_table 
WHERE access_object_id NOT IN (SELECT id FROM object);
