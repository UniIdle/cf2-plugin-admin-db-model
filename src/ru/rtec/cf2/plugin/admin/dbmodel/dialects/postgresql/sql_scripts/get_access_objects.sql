/* Скрипт для получения имен объектов доступных пользователю*/

SELECT id 
FROM object 
WHERE check_access_function(id);
