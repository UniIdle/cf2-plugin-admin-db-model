package ru.rtec.cf2.plugin.modeladmindb;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.io.IOException;
import org.hibernate.Session;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.g4.utils.resources.ResourceBundleHandlerWrapper;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;
import ru.rtec.cf2.plugin.modeladmindb.util.SQLScriptReader;


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

	private <T> Object executeQueryShell(String script, Function<ResultSet, T> handleResultSet) {
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript(script);
			ResultSet rs = stmt.executeQuery(query);
			return handleResultSet.apply(rs);
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

	private void executeUpdateShell(String script, UnaryOperator<String> handleResultSet) throws SQLException{
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript(script);
			query = handleResultSet.apply(query);
			stmt.executeUpdate(query);
		} catch (IOException e) {
			log.warning(e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				log.warning(e.getMessage());
			}
		}
	}

	@Override
	public boolean isValidSchema() {
		return (boolean) executeQueryShell("check_db.sql", HandleResultSetFunctionFactory.getValidSchemaFunction());
	}


	@Override
	public boolean isAccessController() {
		return (boolean) executeQueryShell("check_access_controller.sql", HandleResultSetFunctionFactory.getIsAccessControllerFunction());
	}

	@Override
	public List<String> requestAllUsers() {
		return (List<String>) executeQueryShell("get_all_users.sql", HandleResultSetFunctionFactory.getRequestAllUsersFunction());
	}

	@Override
	public void deleteUserByName(String userName) throws SQLException {
		executeUpdateShell("delete_user.sql", HandleResultSetFunctionFactory.getDeleteUserFunction(userName));
		log.info(String.format(resourceBundleWrapper.getString("SuccessDeleteUser_Message"), userName));
	}

	@Override
	public void changeUserPassword(String userName, String newPassword) throws SQLException {
		executeUpdateShell("change_pass.sql", HandleResultSetFunctionFactory.getChangeUserPasswordFunction(userName, newPassword));
		log.info(String.format(resourceBundleWrapper.getString("SuccessEditUser_Message"), userName));
	}

	@Override
	public void createUser(String userName, String password) throws SQLException {
		executeUpdateShell("create_user.sql", HandleResultSetFunctionFactory.getCreateUserFunction(userName, password));
		log.info(String.format(resourceBundleWrapper.getString("SuccessCreateUser_Message"), userName));
	}

	@Override
	public List<String> getUserRoles(String userName) {
		return (List<String>) executeQueryShell("get_user_roles.sql", HandleResultSetFunctionFactory.getUserRolesFunction());
	}
	
}
