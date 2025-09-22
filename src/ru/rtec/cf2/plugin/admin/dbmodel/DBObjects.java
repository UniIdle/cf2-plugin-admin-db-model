package ru.rtec.cf2.plugin.admin.dbmodel;


/**
 * Перечисление объектов БД для администрирования в CF2
 */
public enum DBObjects {
	define_root_id_function,
	check_access_function,

	users_access_map_table,

	cf2_base_user_role,
	cf2_user_manager_role,
	cf2_object_access_controller_role,
	cf2_object_editor_role,
	cf2_dictionary_editor_role,
	cf2_security_admin_role;
}
