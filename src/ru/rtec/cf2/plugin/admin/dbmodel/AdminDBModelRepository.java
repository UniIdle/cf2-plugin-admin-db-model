package ru.rtec.cf2.plugin.admin.dbmodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ru.g4.utils.log.LoggingUtils;
import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;
import ru.rtec.cf2.plugin.admin.dbmodel.dialects.postgresql.AdminPostgreSQLDBModelPlugin;
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
	private IResourceBundleWrapper resourceBundle = 
			ResourcesStorage.getBundle(AdminPostgreSQLDBModelPlugin.class);

	/**
	 * Сссылка на модуль модели объектов. Ему в случае удачного подключения
	 * отдается ссылка на фабрику сессий
	 */
	IDBObjects dbModel;

	/**
	 * Модуль читающий SQL-скрипты из файлов
	 */
	SQLScriptReader reader;

	/**
	 * Конструктор
	 */
	public AdminDBModelRepository() {

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
	public String getCurrentUserName() {
		return dbModel.getConnectionParameters().getUserName();
	}

	private <T> T queryShell(String script, Function<ResultSet, T> handleResultSet, 
			Object... parameters) {

		try (Session session = dbModel.getSession()) {
			String query = reader.performScript(script);

			return session.doReturningWork(connection -> {
				PreparedStatement ps = 
						connection.prepareStatement(String.format(query, parameters));
				
				boolean hasResult = ps.execute();
				return hasResult ? handleResultSet.apply(ps.getResultSet()) : null;
			});
		} catch (IOException e) {
			log.warn(e.getMessage());
			log.error(LoggingUtils.dumpThrowable(e));
		} catch (HibernateException e) {
			log.warn(e.getMessage());
			log.error(LoggingUtils.dumpThrowable(e));
			throw new ADBMError(e.getMessage());
		} catch (Exception ex) {
			log.warn(ex.getMessage());
			log.error(LoggingUtils.dumpThrowable(ex));
		}

		return null;
	}

	@Override
	public boolean isValidSchema() {
		return (boolean) queryShell("check_db.sql", 
				HandleResultSetFunctionFactory.checkValidSchemaFunction(), 
				DBObjects.define_root_id_function, DBObjects.check_access_function, 
				DBObjects.users_access_map_table, DBObjects.cf2_base_user_role, 
				DBObjects.cf2_dictionary_editor_role, DBObjects.cf2_object_access_controller_role,
				DBObjects.cf2_user_manager_role, DBObjects.cf2_security_admin_role, 
				DBObjects.cf2_object_editor_role);
	}

	@Override 
	public void preprocDBObjects() {
		queryShell("clear_empty_objects.sql", null, 
				DBObjects.users_access_map_table);
	}

	@Override
	public boolean isOwner() {
		return (boolean) queryShell("check_owner.sql", 
				HandleResultSetFunctionFactory.checkOwnerFunction());
	}

	@Override
	public List<String> requestUserManagers() {
		return (List<String>) queryShell("get_user_managers.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				DBObjects.cf2_user_manager_role);
	}

	@Override
	public List<String> requestUsersWithoutManagers() {
		return (List<String>) queryShell("get_users_without_managers.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				DBObjects.cf2_base_user_role, DBObjects.cf2_user_manager_role);
	}

	@Override
	public void deleteUser(String userName) throws ADBMError {
		queryShell("delete_user.sql", null, 
				userName, DBObjects.cf2_user_manager_role, DBObjects.users_access_map_table);

		log.info(resourceBundle.getStringFormat("SuccessDeleteUser_Message", userName));
	}

	@Override
	public void changeUserPassword(String userName, String newPassword) throws ADBMError {
		queryShell("change_pass.sql",null, 
				userName, newPassword, DBObjects.cf2_user_manager_role);

		log.info(resourceBundle.getString("SuccessChangePassowrd_Message"));
	}

	@Override
	public void changeUserName(String userName, String newUserName) throws ADBMError {
		queryShell("change_username.sql",null, 
				userName, newUserName, DBObjects.cf2_user_manager_role);

		log.info(resourceBundle.getString("SuccessChangeUsername_Message"));
	}

	@Override
	public void createUser(String userName, String password) throws ADBMError {
		queryShell("create_user.sql", null, 
				userName, password, DBObjects.cf2_base_user_role, DBObjects.cf2_user_manager_role);

		log.info(resourceBundle.getStringFormat("SuccessCreateUser_Message", userName));
	}

	@Override
	public List<String> getCurrentUserRoles() {
		return (List<String>) queryShell("get_user_roles.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction());
	}

	@Override
	public List<String> getUserRoles(String userName) {
		return (List<String>) queryShell("get_roles_by_username.sql", 
				HandleResultSetFunctionFactory.getStringListResultFunction(), 
				userName, DBObjects.cf2_base_user_role);
	}

	@Override
	public void grantPrivilege(String privilege, String userName) {
		queryShell("grant_privilege.sql", null, 
				privilege, userName, DBObjects.cf2_user_manager_role);
	}

	@Override
	public void revokePrivilege(String privilege, String userName) {
		queryShell("revoke_privilege.sql", null, 
				privilege, userName, DBObjects.cf2_user_manager_role);
	}

	@Override
	public Map<Long, String> getAllObjects() {
		return (Map<Long, String>) queryShell("get_all_objects.sql", 
				HandleResultSetFunctionFactory.getLongStringMapResultFunction());
	}

	@Override
	public List<Long> getAccessObjects() {
		return (List<Long>) queryShell("get_access_objects.sql", 
				HandleResultSetFunctionFactory.getLongListResultFunction(), 
				DBObjects.check_access_function);
	}

	@Override
	public Map<Long, String> getAccessRootObjects(String userName) {
		return (Map<Long, String>) queryShell("get_access_root_objects.sql", 
				HandleResultSetFunctionFactory.getLongStringMapResultFunction(), 
				userName, DBObjects.users_access_map_table);
	}

	@Override
	public void grantAccessToObject(String userName, Long objectId) {
		queryShell("grant_access_to_object.sql", null, 
				userName, String.valueOf(objectId), DBObjects.users_access_map_table);
	}
	
	@Override
	public void revokeAccessFromObject(String userName, Long objectId) {
		queryShell("revoke_access_from_object.sql", null, 
				userName, String.valueOf(objectId), DBObjects.users_access_map_table);
	}

	@Override
	public void clearUserAccessObjects(String userName) {
		queryShell("clear_access_objects.sql", null, 
				userName, DBObjects.users_access_map_table);
	}

}
