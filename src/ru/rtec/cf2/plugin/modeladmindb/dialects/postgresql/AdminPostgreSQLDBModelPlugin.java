package ru.rtec.cf2.plugin.modeladmindb.dialects.postgresql;

import ru.rtec.cf2.plugin.modeladmindb.AbstractAdminDialectDBModelPlugin;


/**
 * Основной плагин, реализующий интерфейс {@link ru.rtec.cf2.plugin.modeladmindb.IAdminDBModel}, для БД PostgreSQL
 * 
 */
public class AdminPostgreSQLDBModelPlugin extends AbstractAdminDialectDBModelPlugin {

	public AdminPostgreSQLDBModelPlugin() {
		//Устанавливаем путь к SQL-скриптам относительно файла текущего класса
		dbRepository.setSQLScriptsPath(getPackageNameAsPath() + "/sql_scripts");
	}
}
