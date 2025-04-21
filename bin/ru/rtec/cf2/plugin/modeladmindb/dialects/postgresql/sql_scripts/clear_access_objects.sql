/* Скрипт удаляющий пользователей без доступа к редактированию объектов */

DELETE FROM users_access_map_table 
WHERE user_name = '%s';