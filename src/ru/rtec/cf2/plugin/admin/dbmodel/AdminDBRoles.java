package ru.rtec.cf2.plugin.admin.dbmodel;


/**
 * Перечисление групповых ролей в БД для администрирования в CF2
 */
public enum AdminDBRoles {
	cf2_base_user_role,
	cf2_template_editor_role,
	cf2_object_editor_role,
	cf2_sys_admin_role,
	cf2_user_manager_role,
	cf2_security_admin_role;


	/**
	 * Название роли для пользовательского интерфейса
	 */
	private String roleName;


	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

}
