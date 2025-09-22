/* Скрипт для смены имени пользователя */

SET ROLE %3$s;
ALTER USER %1$s RENAME TO %2$s;
RESET ROLE;
