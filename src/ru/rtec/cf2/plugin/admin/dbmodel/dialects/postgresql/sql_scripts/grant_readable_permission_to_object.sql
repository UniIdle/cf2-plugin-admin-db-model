/* Скрипт для выдачи полномочий пользователю по редактированию объекта */

INSERT INTO %3$s 
VALUES ('%1$s', %2$s, false, true) 
ON CONFLICT (user_name, accessible_object_id)
DO UPDATE SET
	is_writeable = EXCLUDED.is_writeable,
	is_readable = EXCLUDED.is_readable;

----Проверяем есть ли в таблице доступа к объектам элементы у которых родители уже записаны в данную таблицу с правом доступа на чтение, если есть удалям их
DELETE FROM object_access_table 
WHERE accessible_object_id = (
	SELECT oat.accessible_object_id FROM object_access_table AS oat
	JOIN object AS o
	ON oat.accessible_object_id = o.id
	WHERE NOT oat.is_writeable AND check_readable_object_for_user_function(o.parent_id, 'cf2_object_editor')
);
