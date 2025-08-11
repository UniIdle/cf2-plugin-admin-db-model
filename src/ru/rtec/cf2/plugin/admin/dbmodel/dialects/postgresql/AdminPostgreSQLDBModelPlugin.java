package ru.rtec.cf2.plugin.admin.dbmodel.dialects.postgresql;

import ru.rtec.cf2.plugin.admin.dbmodel.AbstractAdminDialectDBModelPlugin;


/**
 * Основной плагин, реализующий интерфейс {@link ru.rtec.cf2.plugin.admin.dbmodel.IAdminDBModel}, для БД PostgreSQL
 */
public class AdminPostgreSQLDBModelPlugin extends AbstractAdminDialectDBModelPlugin {

	public AdminPostgreSQLDBModelPlugin() {
		//Устанавливаем путь к SQL-скриптам относительно файла текущего класса
		dbRepository.setSQLScriptsPath(getPackageNameAsPath() + "/sql_scripts");
	}
}
