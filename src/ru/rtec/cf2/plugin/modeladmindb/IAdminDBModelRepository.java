package ru.rtec.cf2.plugin.modeladmindb;

import java.util.List;
import java.util.Map;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;


/**
 * Интерфейс для работы с ролевым доступом к объектам конфигуратора
 * 
 */
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
	 * Удаляет устаревшние строки из таблицы users_access_map_table
	 */
	public void preprocDbObjects();

	/**
	 * Проверяет полномочия текущего пользователя на управление ролевым доступом
	 * 
	 * @return булево значение является ли текущий пользователь админом
	 */
	public List<String> currentUserAdminPrivileges();

	/**
	 * Возвращает список всех пользователей конфигуратора
	 * 
	 * @return список пользователей
	 */
	public List<String> requestAllUsers();

	/**
	 * Возвращает список пользователей редакторов объектов
	 * 
	 * @return редаторы объектов
	 */
	public List<String> requestObjectEditors();

	/**
	 * Удаляет пользователя конфигуратора оп его имени
	 * 
	 * @param userName имя пользователя
	 */
	public void deleteUserByName(String userName) throws ADBMError;

	/**
	 * Меняeт пароль для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param newPassword новый пароль
	 */
	public void changeUserPassword(String userName, String newPassword) throws ADBMError;

	/**
	 * Меняeт имя пользователя
	 * 
	 * @param userName имя пользователя
	 * @param newUserName новое имя пользователя
	 */
	public void changeUserName(String userName, String newUserName) throws ADBMError;

	/**
	 * Создает нового пользователя
	 * 
	 * @param userName имя пользователя
	 * @param password пароль
	 */
	public void createUser(String userName, String password) throws ADBMError;

	/**
	 * Получает все роли к которым принадлежит пользователь
	 * 
	 * @param userName имя пользователя
	 */
	public List<String> getUserRoles(String userName);

	/**
	 * Устанавливает для пользователя членство в указанной роли
	 * 
	 * @param privilege имя групповой роли
	 * @param userName имя пользователя
	 */
	public void grantPrivilege(String privilege, String userName);

	/**
	 * Исключает пользователя из групповой роли
	 * 
	 * @param privilege имя групповой роли
	 * @param userName имя пользователя
	 */
	public void revokePrivilege(String privilege, String userName);

	/**
	 * Получение имен всех объектов
	 * 
	 * @return список имен объектов
	 */
	public Map<Integer, String> getAllObjects();

	/**
	 * Получение имен доступных объектов
	 * 
	 * @param userName имя пользователя
	 * @return список имен объектов
	 */
	public Map<Integer, String> getAccessObjects(String userName);

	/**
	 * Назначение привилегий на объект для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param objectId ID объекта
	 */
	public void grantAccessToObject(String userName, int objectId);

	/**
	 * Удаление привилегии на объект для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param objectId ID объекта
	 */
	public void revokeAccessFromObject(String userName, int objectId);

	/**
	 * Удаляет объекты из таблицы users_access_map_table для указанного пользователя
	 * 
	 * @param userName имя пользователя
	 */
	public void clearUserAccessObjects(String userName);

}
