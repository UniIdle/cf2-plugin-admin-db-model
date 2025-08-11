--Скрипт выполняет проверку, является ли текущий пользователь "Владельцем"

SELECT pg_get_userbyid(nspowner)
FROM pg_namespace 
WHERE nspname = current_schema() and pg_get_userbyid(nspowner) = current_user;