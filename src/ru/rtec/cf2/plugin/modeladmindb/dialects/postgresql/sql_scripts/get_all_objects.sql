/* Скрипт для получения имен всех объектов*/

SELECT id, name 
FROM object 
WHERE parent_id IS NULL;
