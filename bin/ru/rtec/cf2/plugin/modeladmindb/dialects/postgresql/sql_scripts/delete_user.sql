/* Скрипт по удалению пользователя конфигуратора */

DROP USER %s;

DELETE FROM users_access_map_table 
WHERE user_name = '%s';
