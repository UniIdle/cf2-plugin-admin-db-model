/* Скрипт для смены пароля у пользователя конфигуратора */

SET ROLE %3$s;
ALTER ROLE %1$s WITH PASSWORD '%2$s';
RESET ROLE;