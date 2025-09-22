/* Скрипт по удалению пользователя конфигуратора */

SET ROLE %2$s;

REVOKE %1$s FROM %2$s;

DROP USER %1$s;
DELETE FROM %3$s 
WHERE user_name = '%1$s';

RESET ROLE;
