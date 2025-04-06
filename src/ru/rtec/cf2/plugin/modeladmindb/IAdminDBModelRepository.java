package ru.rtec.cf2.plugin.modeladmindb;

import java.sql.SQLException;
import java.util.List;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;

public interface IAdminDBModelRepository {
	/**
	 * Устанавливает объектную модель текущей БД
	 * 
	 * @param dbModel ссылка на модель БД
	 */
	public void setDBModel(IDBObjects dbModel);

	/**
	 * Основной плагин, реализующий интерфейс {@link ru.rtec.cf2.plugin.modeladmindb.IAdminDBModel}, устанавливает путь к директории с SQL-скриптами
	 * 
	 * @param path путь до директории скриптов отностительно classpath
	 */
	public void setSQLScriptsPath(String path);

	/**
	 * Проверяет целостность БД
	 * 
	 * @return булево значение в наличии ли все необходимы объекты БД для организации ролевого доступа
	 */
	public boolean isValidSchema();

	/**
	 * Проверяет полномочия текущего пользователя на управление ролевым доступом
	 * 
	 * @return булево значение является ли текущий пользователь админом
	 */
	public boolean isAccessController();

	/**
	 * Возвращает список всех пользователей конфигуратора
	 * 
	 * @return список пользователей
	 */
	public List<String> requestAllUsers();

	/**
	 * Удаляет пользователя конфигуратора оп его имени
	 * 
	 * @param userName имя пользователя
	 */
	public void deleteUserByName(String userName) throws SQLException;

	/**
	 * Меняeт пароль для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param newPassword новый пароль
	 */
	public void changeUserPassword(String userName, String newPassword) throws SQLException;

	/**
	 * Создает нового пользователя
	 * 
	 * @param userName имя пользователя
	 * @param password пароль
	 */
	public void createUser(String userName, String password) throws SQLException;

	/**
	 * Получает все роли к которым принадлежит пользователь
	 * 
	 * @param userName имя пользователя
	 */
	public List<String> getUserRoles(String userName);
}
