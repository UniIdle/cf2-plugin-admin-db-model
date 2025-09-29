/* Скрипт по удалению пользователя конфигуратора */

DROP USER %1$s;

DELETE FROM %3$s 
WHERE user_name = '%1$s';
