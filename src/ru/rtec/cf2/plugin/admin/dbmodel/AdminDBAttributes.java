package ru.rtec.cf2.plugin.admin.dbmodel;


/**
 * Перечисление объектов БД для администрирования в CF2
 */
public enum AdminDBAttributes {
	//=====Таблицы=====
	classes_access_table,
	property_groups_access_table,
	object_access_table,

	//====Функции=====
	check_accessible_class_function,
	check_accessible_parent_class_function,
	check_accessible_class_parameters_function,
	check_accessible_class_property_groups_function,
	check_accessible_class_parameter_property_groups_function,
	check_accessible_property_group_function,
	check_writeable_object_function,
	check_writeable_parent_object_function,
	check_writeable_object_property_groups_function,
	check_writeable_object_parameter_property_groups_function,
	check_readable_object_function,
	check_readable_object_property_groups_function,

	//=====Роли=====
	cf2_base_user_role,
	cf2_template_editor_role,
	cf2_object_editor_role,
	cf2_sys_admin_role,
	cf2_user_manager_role,
	cf2_security_admin_role;
}
