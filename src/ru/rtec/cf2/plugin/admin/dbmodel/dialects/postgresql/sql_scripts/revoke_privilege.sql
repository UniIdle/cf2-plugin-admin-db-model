/* Скрипт для исключения пользователя из групповой роли */

SET ROLE %3$s;
REVOKE %1$s FROM %2$s;
RESET ROLE;
