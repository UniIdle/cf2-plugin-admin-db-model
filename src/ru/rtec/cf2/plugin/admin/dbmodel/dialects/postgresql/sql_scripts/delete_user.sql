/* Скрипт по удалению пользователя конфигуратора */

DROP USER %1$s;

DELETE FROM users_access_map_table 
WHERE user_name = '%1$s';
