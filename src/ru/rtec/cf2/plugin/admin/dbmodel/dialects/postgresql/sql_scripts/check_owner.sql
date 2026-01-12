--Скрипт выполняет проверку, является ли текущий пользователь "Владельцем"

SELECT pg_get_userbyid(nspowner)
FROM pg_namespace 
WHERE nspname = current_schema() AND 
		pg_get_userbyid(nspowner) = session_user;