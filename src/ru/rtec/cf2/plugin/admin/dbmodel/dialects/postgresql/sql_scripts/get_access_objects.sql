/* Скрипт для получения имен объектов доступных пользователю*/

SELECT id 
FROM object 
WHERE %s(id);
