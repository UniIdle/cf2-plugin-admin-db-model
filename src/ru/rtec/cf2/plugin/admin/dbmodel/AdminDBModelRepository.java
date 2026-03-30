package ru.rtec.cf2.plugin.admin.dbmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ru.g4.utils.log.LoggingUtils;
import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;
import ru.rtec.cf2.plugin.admin.dbmodel.util.SQLScriptReader;


/**
 * Реализация интерфейса {@link IAdminDBModelRepository}
 */
public class AdminDBModelRepository implements IAdminDBModelRepository {
	/**
	 * Логгер
	 */
	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Обертка для ResourceBundle
	 */
	private IResourceBundleWrapper resourceBundle;

	/**
	 * Ссылка на модуль модели объектов. Ему в случае удачного подключения
	 * отдается ссылка на фабрику сессий
	 */
	private IDBObjects dbModel;

	/**
	 * Модуль читающий SQL-скрипты из файлов
	 */
	private SQLScriptReader reader;


	/**
	 * Конструктор
	 */
	public AdminDBModelRepository() {

	}


	public void setResourceBundle(IResourceBundleWrapper resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	@Override
	public void setSQLScriptsPath(String path) {
		this.reader = new SQLScriptReader(String.format("%s/", path));
	}

	@Override
	public void setDBModel(IDBObjects dbModel) {
		this.dbModel = dbModel;
	}

	@Override
	public IDBObjects getDBModel() {
		return this.dbModel;
	}

	@Override
	public String getCurrentUserName() {
		return dbModel.getConnectionParameters().getUserName();
	}

	private <T> T queryShell(String script, Function<ResultSet, T> handleResultSet, 
			Object... parameters) throws SQLException {

		try (Session session = dbModel.getSession()) {
			String query = reader.performScript(script);

			return session.doReturningWork(connection -> {
				PreparedStatement ps = 
						connection.prepareStatement(String.format(query, parameters));
				
				boolean hasResult = ps.execute();
				return hasResult ? handleResultSet.apply(ps.getResultSet()) : null;
			});
		} catch (HibernateException e) {
			log.warn(e.getMessage());
			log.error(LoggingUtils.dumpThrowable(e));

			throw new SQLException(e.getMessage());
		} catch (Throwable ex) {
			log.warn(ex.getMessage());
			log.error(LoggingUtils.dumpThrowable(ex));

			return null;
		}
	}

	@Override
	public List<String> schemaValidation() throws SQLException {
		List<Object> necessaryObjects = new ArrayList<>();
		necessaryObjects.addAll(Arrays.asList(AdminDBTables.values()));
		necessaryObjects.addAll(Arrays.asList(AdminDBFunctions.values()));
		necessaryObjects.addAll(Arrays.asList(AdminDBRoles.values()));

		return (List<String>) queryShell("check_db.sql", 
				HandleResultSetFunctionFactory.schemaValidationFunction(), 
				necessaryObjects.toArray());
	}

	@Override 
	public void preprocessingDBObjects() throws SQLException {
		queryShell("clear_empty_objects_and_types.sql", null, 
				AdminDBTables.classes_access_table, AdminDBTables.object_access_table);
	}

	@Override
	public boolean isOwner() throws SQLException {
		return (boolean) queryShell("check_owner.sql", 
				HandleResultSetFunctionFactory.checkOwnerFunction());
	}

	@Override
	public List<String> requestUserManagers() throws SQLException {
		return (List<String>) queryShell("get_user_managers.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				AdminDBRoles.cf2_user_manager_role);
	}

	@Override
	public List<String> requestUsersByRole(AdminDBRoles role) throws ADBMError, SQLException {
		if (role.equals(AdminDBRoles.cf2_user_manager_role)) {
			throw new ADBMError(resourceBundle.getString("InaccessibleUserManagersRequest"));
		}

		return (List<String>) queryShell("get_users_by_role.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				role, AdminDBRoles.cf2_user_manager_role);
	}

	@Override
	public void deleteUser(String userName) throws ADBMError, SQLException {
		queryShell("delete_user.sql", null, 
				userName, AdminDBRoles.cf2_user_manager_role, AdminDBTables.classes_access_table, 
				AdminDBTables.object_access_table);

		log.info(resourceBundle.getStringFormat("SuccessDeleteUser_Message", userName));
	}

	@Override
	public void changeUserPassword(String userName, String newPassword) throws ADBMError, SQLException {
		queryShell("change_pass.sql",null, 
				userName, newPassword, AdminDBRoles.cf2_user_manager_role);

		log.info(resourceBundle.getString("SuccessChangePassowrd_Message"));
	}

	@Override
	public void changeUserName(String userName, String newUserName) throws ADBMError, SQLException {
		queryShell("change_username.sql",null, 
				userName, newUserName, AdminDBRoles.cf2_user_manager_role);

		log.info(resourceBundle.getString("SuccessChangeUsername_Message"));
	}

	@Override
	public void createUser(String userName, String password) throws ADBMError, SQLException {
		queryShell("create_user.sql", null, 
				userName, password, AdminDBRoles.cf2_base_user_role, AdminDBRoles.cf2_user_manager_role);

		log.info(resourceBundle.getStringFormat("SuccessCreateUser_Message", userName));
	}

	@Override
	public List<String> getCurrentUserRoles() throws SQLException {
		return (List<String>) queryShell("get_user_roles.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction());
	}

	@Override
	public List<String> getUserRoles(String userName) throws SQLException {
		return (List<String>) queryShell("get_roles_by_username.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				userName, AdminDBRoles.cf2_base_user_role);
	}

	@Override
	public void grantPrivilege(String privilege, String userName) throws SQLException {
		queryShell("grant_privilege.sql", null, 
				privilege, userName, AdminDBRoles.cf2_user_manager_role);
	}

	@Override
	public void revokePrivilege(String privilege, String userName) throws SQLException {
		queryShell("revoke_privilege.sql", null, 
				privilege, userName, AdminDBRoles.cf2_user_manager_role);
	}

	@Override
	public List<Long> getAccessTypesForUser(String userName) throws SQLException {
		return (List<Long>) queryShell("get_access_types.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				userName, AdminDBFunctions.check_accessible_class_for_user_function);
	}

	@Override
	public List<Long> getPermittedTypesForUser(String userName) throws SQLException {
		return (List<Long>) queryShell("get_permitted_types.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				userName, AdminDBTables.classes_access_table);
	}

	@Override
	public void grantPermissionToType(String userName, Long typeId) throws SQLException {
		queryShell("grant_permission_to_type.sql", null, 
				userName, typeId, AdminDBTables.classes_access_table,
			AdminDBFunctions.check_accessible_class_for_user_function);
	}

	@Override
	public void revokePermissionFromType(String userName, Long typeId) throws SQLException {
		queryShell("revoke_permission_from_type.sql", null,
				userName, typeId, AdminDBTables.classes_access_table);
	}

	@Override
	public List<Long> getAccessObjectsForUser(String userName) throws SQLException {
		return (List<Long>) queryShell("get_access_objects.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				userName, AdminDBFunctions.check_writeable_object_for_user_function);
	}

	@Override
	public List<Long> getPermittedObjectsForUser(String userName) throws SQLException {
		return (List<Long>) queryShell("get_permitted_objects.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				userName, AdminDBTables.object_access_table);
	}

	@Override
	public List<Long> getReadableObjectsForUser(String userName) throws SQLException {
		return (List<Long>) queryShell("get_readable_objects.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				userName, AdminDBFunctions.check_readable_object_for_user_function);
	}

	@Override
	public List<Long> getPermittedReadableObjectsForUser(String userName) throws SQLException {
		return (List<Long>) queryShell("get_permitted_readable_objects.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				userName, AdminDBTables.object_access_table);
	}

	@Override
	public void grantPermissionToObject(String userName, Long objectId) throws SQLException {
		queryShell("grant_permission_to_object.sql", null, 
				userName, objectId, AdminDBTables.object_access_table);
	}

	@Override
	public void grantPermissionForReadingToObject(String userName, Long objectId) throws SQLException {
		queryShell("grant_readable_permission_to_object.sql", null, 
				userName, objectId, AdminDBTables.object_access_table);
	}

	@Override
	public void revokePermissionFromObject(String userName, Long objectId) throws SQLException {
		queryShell("revoke_permission_from_object.sql", null,
				userName, objectId, AdminDBTables.object_access_table);
	}

	/**
	 * Возвращает список заблокированных групп свойств
	 * 
	 * @return список заблокированных групп свойств
	 */
	public List<String> requestLockedPropertyGroups() throws SQLException {
		return (List<String>) queryShell("get_locked_pg.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				AdminDBTables.property_groups_access_table);
	}

	/**
	 * Блокирует группу свойств
	 * 
	 * @param propertyGroupName имя блокируемой группы свойств
	 */
	public void lockPropertyGroup(String propertyGroupName) throws SQLException {
		queryShell("lock_pg.sql", null,
				propertyGroupName, AdminDBTables.property_groups_access_table);
	}

	/**
	 * Снимает блокировку с группы свойств
	 * 
	 * @param propertyGroupName имя блокируемой группы свойств
	 */
	public void unlockPropertyGroup(String propertyGroupName) throws SQLException {
		queryShell("unlock_pg.sql", null,
				propertyGroupName, AdminDBTables.property_groups_access_table);
	}

	/**
	 * Возвращает отображение объектов и их типов
	 */
	public Map<Long, Long> requestObjectsTypes() throws SQLException {
		return (Map<Long, Long>) queryShell("get_objects_types.sql", 
				HandleResultSetFunctionFactory.getLongLongMapResultFunction());
	}

}
