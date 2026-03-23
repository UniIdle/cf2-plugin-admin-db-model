/* Скрипт для выдачи полномочий пользователю по редактированию объекта */

--Вставляем id объекта в таблицу доступа к объектам запись с доступом на редактирование (если запись уже существует для этого объекта обновляется режим доступа с чтения на редактирование)
INSERT INTO %3$s 
VALUES ('%1$s', %2$s, true, true) 
ON CONFLICT (user_name, accessible_object_id)
DO UPDATE SET
	is_writeable = EXCLUDED.is_writeable,
	is_readable = EXCLUDED.is_readable;

----Проверяем есть ли в таблице доступа к объектам элементы у которых родители уже записаны в данную таблицу с правом доступа на редактирование, если есть удалям их
DELETE FROM object_access_table WHERE check_writeable_object_for_user_function(accessible_object_id, '%1$s');