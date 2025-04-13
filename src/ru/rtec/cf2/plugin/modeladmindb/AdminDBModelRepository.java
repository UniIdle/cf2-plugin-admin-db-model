package ru.rtec.cf2.plugin.modeladmindb;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.io.IOException;
import org.hibernate.Session;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.g4.utils.resources.ResourceBundleHandlerWrapper;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;
import ru.rtec.cf2.plugin.modeladmindb.util.SQLScriptReader;


/**
 * Реализация интерфейса {@link IAdminDBModelRepository}
 * 
 */
public class AdminDBModelRepository implements IAdminDBModelRepository {
	/**
	 * Логер
	 */
	protected Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Обертка для ResourceBundle
	 */
	private static IResourceBundleWrapper resourceBundleWrapper = new ResourceBundleHandlerWrapper(
			IAdminDBModel.class.getPackage().getName() + ".resources.resource");

	/**
	 * Сссылка на модуль модели объектов. Ему в случае удачного подключения
	 * отдается ссылка на фабрику сессий
	 */
	IDBObjects dbModel;

	/**
	 * Модуль читающий SQL-скрипты из файлов
	 */
	SQLScriptReader reader;


	@Override
	public void setSQLScriptsPath(String path) {
		this.reader = new SQLScriptReader(path + "/");
	}

	@Override
	public void setDBModel(IDBObjects dbModel) {
		this.dbModel = dbModel;
	}

	private <T> Object queryShell(String script, Function<ResultSet, T> handleResultSet, String... paramenters) {
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript(script);
			boolean hasResult = stmt.execute(paramenters.length == 0 ? query : String.format(query, paramenters));
			
			return hasResult ? handleResultSet.apply(stmt.getResultSet()) : null;
		} catch (IOException e) {
			log.warning(e.getMessage());
		} catch (SQLException e) {
			log.warning(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warning(e.getMessage());
			}
		}

		return null;
	}

	@Override
	public boolean isValidSchema() {
		return (boolean) queryShell("check_db.sql", HandleResultSetFunctionFactory.getValidSchemaFunction());
	}

	@Override
	public boolean isAccessController() {
		return (boolean) queryShell("check_access_controller.sql", HandleResultSetFunctionFactory.getIsAccessControllerFunction());
	}

	@Override
	public List<String> requestAllUsers() {
		return (List<String>) queryShell("get_all_users.sql", HandleResultSetFunctionFactory.getStringListResultFunction());
	}

	@Override
	public void deleteUserByName(String userName) throws SQLException {
		queryShell("delete_user.sql", null, userName);
		log.info(String.format(resourceBundleWrapper.getString("SuccessDeleteUser_Message"), userName));
	}

	@Override
	public void changeUserPassword(String userName, String newPassword) throws SQLException {
		queryShell("change_pass.sql",null, userName, newPassword);
		log.info(String.format(resourceBundleWrapper.getString("SuccessEditUser_Message"), userName, newPassword));
	}

	@Override
	public void createUser(String userName, String password) throws SQLException {
		queryShell("create_user.sql", null, userName, password);
		log.info(String.format(resourceBundleWrapper.getString("SuccessCreateUser_Message"), userName, password));
	}

	@Override
	public List<String> getUserRoles(String userName) {
		return (List<String>) queryShell("get_user_roles.sql", HandleResultSetFunctionFactory.getStringListResultFunction(), userName);
	}

	@Override
	public void grantPrivilege(String privilege, String userName) {
		queryShell("grant_privilege.sql", null, privilege, userName);
	}

	@Override
	public void revokePrivilege(String privilege, String userName) {
		queryShell("revoke_privilege.sql", null, privilege, userName);
	}

	@Override
	public Map<Integer, String> getAllObjects() {
		return (Map<Integer, String>) queryShell("get_all_objects.sql", HandleResultSetFunctionFactory.getIntegerStringMapResultFunction());
	}

	@Override
	public Map<Integer, String> getAccessObjects(String userName) {
		return (Map<Integer, String>) queryShell("get_access_objects.sql", HandleResultSetFunctionFactory.getIntegerStringMapResultFunction(), userName);
	}

	@Override
	public void grantAccessToObject(String userName, int objectId) {
		queryShell("grant_access_to_object.sql", null, userName, String.valueOf(objectId));
	}
	
	@Override
	public void revokeAccessFromObject(String userName, int objectId) {
		queryShell("revoke_access_from_object.sql", null, userName, String.valueOf(objectId));
	}

}
