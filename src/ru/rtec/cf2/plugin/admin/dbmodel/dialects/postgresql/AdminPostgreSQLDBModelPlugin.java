package ru.rtec.cf2.plugin.admin.dbmodel.dialects.postgresql;

import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.plugin.admin.dbmodel.AbstractAdminDialectDBModelPlugin;
import ru.rtec.cf2.plugin.admin.dbmodel.AdminDBModelRepository;
import ru.rtec.cf2.plugin.admin.dbmodel.AdminDBRoles;


/**
 * Основной плагин, реализующий интерфейс {@link ru.rtec.cf2.plugin.admin.dbmodel.IAdminDBModel}, для БД PostgreSQL
 */
public class AdminPostgreSQLDBModelPlugin extends AbstractAdminDialectDBModelPlugin {

	public AdminPostgreSQLDBModelPlugin() {
		//Устанавливаем путь к SQL-скриптам относительно файла текущего класса
		dbRepository.setSQLScriptsPath(getPackageNameAsPath() + "/sql_scripts");

		//Получим обертку для ресурсов
		resourceBundle = ResourcesStorage.getBundle(getClass());

		//Передадим обертку для ресурсов в репозиторий
		((AdminDBModelRepository) dbRepository).setResourceBundle(resourceBundle);

		//Инициализируем enum AdminDBRoles именами ролей из ресурсов (для различных языков интерфейса)
		initUserRoles();
	}

	private void initUserRoles() {
		for (AdminDBRoles role : AdminDBRoles.values()) {
			role.setRoleName(resourceBundle.getString(role.toString()));
		}
	}

}
