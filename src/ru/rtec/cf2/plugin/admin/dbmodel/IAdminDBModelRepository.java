package ru.rtec.cf2.plugin.admin.dbmodel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ru.rtec.cf2.plugin.model.objects.IDBObjects;


/**
 * Интерфейс для работы с ролевым доступом к объектам конфигуратора
 */
public interface IAdminDBModelRepository {
	/**
	 * Устанавливает объектную модель текущей БД
	 * 
	 * @param dbModel ссылка на модель БД
	 */
	public void setDBModel(IDBObjects dbModel);

	/**
	 * Возвращает объектную модель текущей БД
	 * 
	 * @param dbModel ссылка на модель БД
	 */
	public IDBObjects getDBModel();

	/**
	 * Основной плагин, реализующий интерфейс {@link ru.rtec.cf2.plugin.admin.dbmodel.IAdminDBModel}, устанавливает путь к директории с SQL-скриптами
	 * 
	 * @param path путь до директории скриптов относительно classpath
	 */
	public void setSQLScriptsPath(String path);

	/**
	 * Возвращает имя пользователя, подключившегося к БД
	 * 
	 * @return имя текущего пользователя
	 */
	public String getCurrentUserName() throws SQLException;

	/**
	 * Проверяет целостность БД
	 * 
	 * @return список объектов недостающих в БД для ролевого доступа
	 */
	public List<String> schemaValidation() throws SQLException;

	/**
	 * Удаляет устаревшие строки из таблицы users_access_map_table
	 */
	public void preprocessingDBObjects() throws SQLException;

	/**
	 * Проверяет, является ли текущий пользователь "Владельцем"
	 * 
	 * @return булево значение, является ли пользователь "Владельцем"
	 */
	public boolean isOwner() throws SQLException;

	/**
	 * Возвращает список администраторов пользователей
	 * 
	 * @return список пользователей
	 */
	public List<String> requestUserManagers() throws SQLException;

	/**
	 * Возвращает список пользователей по членству в роли
	 * 
	 * @return список пользователей
	 */
	public List<String> requestUsersByRole(AdminDBRoles role) throws ADBMError, SQLException;

	/**
	 * Удаляет пользователя конфигуратора оп его имени
	 * 
	 * @param userName имя пользователя
	 */
	public void deleteUser(String userName) throws ADBMError, SQLException;

	/**
	 * Меняет пароль для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param newPassword новый пароль
	 */
	public void changeUserPassword(String userName, String newPassword) throws ADBMError, SQLException;

	/**
	 * Меняет имя пользователя
	 * 
	 * @param userName имя пользователя
	 * @param newUserName новое имя пользователя
	 */
	public void changeUserName(String userName, String newUserName) throws ADBMError, SQLException;

	/**
	 * Создает нового пользователя
	 * 
	 * @param userName имя пользователя
	 * @param password пароль
	 */
	public void createUser(String userName, String password) throws ADBMError, SQLException;

	/**
	 * Получает все роли к которым принадлежит пользователь
	 * 
	 * @return список административных привилегий пользователя
	 */
	public List<String> getCurrentUserRoles() throws SQLException;

	/**
	 * Получает все роли к которым принадлежит пользователь
	 * 
	 * @param userName имя пользователя
	 */
	public List<String> getUserRoles(String userName) throws SQLException;

	/**
	 * Устанавливает для пользователя членство в указанной роли
	 * 
	 * @param privilege имя групповой роли
	 * @param userName имя пользователя
	 */
	public void grantPrivilege(String privilege, String userName) throws SQLException;

	/**
	 * Исключает пользователя из групповой роли
	 * 
	 * @param privilege имя групповой роли
	 * @param userName имя пользователя
	 */
	public void revokePrivilege(String privilege, String userName) throws SQLException;

	/**
	 * Получение списка типов доступных для конкретного пользователя
	 * 
	 * @param userName имя пользователя
	 * @return список доступных типов
	 */
	public List<Long> getAccessTypesForUser(String userName) throws SQLException;

	/**
	 * Получение списка типов к которым выдан доступ
	 * 
	 * @param userName имя пользователя
	 * @return список назначенных типов
	 */
	public List<Long> getPermittedTypesForUser(String userName) throws SQLException;

	/**
	 * Выдача доступа к типу для определенного пользователя
	 * 
	 * @param userName имя пользователя
	 * @param typeId id шаблона
	 */
	public void grantPermissionToType(String userName, Long typeId) throws SQLException;

	/**
	 * Отзыв доступа к объекту для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param typeId id шаблона
	 */
	public void revokePermissionFromType(String userName, Long typeId) throws SQLException;

	/**
	 * Получение списка объектов доступных для редактирования конкретному пользователю
	 * 
	 * @param userName имя пользователя
	 * @return список доступных объектов
	 */
	public List<Long> getAccessObjectsForUser(String userName) throws SQLException;

	/**
	 * Получение списка объектов к которым выдан доступ
	 * 
	 * @param userName имя пользователя
	 * @return список назначенных объектов
	 */
	public List<Long> getPermittedObjectsForUser(String userName) throws SQLException;

	/**
	 * Получение списка объектов доступных для чтения конкретному пользователю
	 * 
	 * @param userName имя пользователя
	 * @return список объектов для чтения
	 */
	public List<Long> getReadableObjectsForUser(String userName) throws SQLException;

	/**
	 * Получение списка объектов назначенных для чтения конкретному пользователю
	 * 
	 * @param userName имя пользователя
	 * @return список объектов для чтения
	 */
	public List<Long> getPermittedReadableObjectsForUser(String userName) throws SQLException;

	/**
	 * Выдача доступа к объекту для определенного пользователя
	 * 
	 * @param userName имя пользователя
	 * @param objectId id объекта
	 */
	public void grantPermissionToObject(String userName, Long objectId) throws SQLException;

	/**
	 * Выдача доступа чтения к объекту для определенного пользователя
	 * 
	 * @param userName имя пользователя
	 * @param objectId id объекта
	 */
	public void grantPermissionForReadingToObject(String userName, Long objectId) throws SQLException;

	/**
	 * Отзыв доступа к объекту для пользователя
	 * 
	 * @param userName имя пользователя
	 * @param objectId id объекта
	 */
	public void revokePermissionFromObject(String userName, Long objectId) throws SQLException;

	/**
	 * Возвращает список заблокированных групп свойств
	 * 
	 * @return список заблокированных групп свойств
	 */
	public List<String> requestLockedPropertyGroups() throws SQLException;

	/**
	 * Блокирует группу свойств
	 * 
	 * @param propertyGroupName имя блокируемой группы свойств
	 */
	public void lockPropertyGroup(String propertyGroupName) throws SQLException;

	/**
	 * Снимает блокировку с группы свойств
	 * 
	 * @param propertyGroupName имя блокируемой группы свойств
	 */
	public void unlockPropertyGroup(String propertyGroupName) throws SQLException;

	/**
	 * Возвращает отображение объектов и их типов
	 */
	public Map<Long, Long> requestObjectsTypes() throws SQLException;

}
