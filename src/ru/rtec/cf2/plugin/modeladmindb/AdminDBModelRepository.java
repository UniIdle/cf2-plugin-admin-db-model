package ru.rtec.cf2.plugin.modeladmindb;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.io.IOException;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.g4.utils.resources.ResourceBundleHandlerWrapper;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;
import ru.rtec.cf2.plugin.modeladmindb.util.SQLScriptReader;


public class AdminDBModelRepository implements IAdminDBModelRepository {
	private final List<String> necessuryDBObjects = Arrays.asList(
			"cf2_object_access_controller_role", 
			"cf2_reference_editor_role", 
			"cf2_function_access_controller_role", 
			"users_access_map", 
			"cf2_object_editor_role",
			"cf2_base_user",
			"check_access",
			"define_root_id"
		);

	/**
	 * Логер
	 */
	protected Logger log = Logger.getLogger(getClass().getName());

	/**
	 * Обертка для ResourceBundle
	 */
	private IResourceBundleWrapper resourceBundleWrapper = new ResourceBundleHandlerWrapper(
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

	@Override
	public boolean isValidSchema() {
		Boolean result = true;
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript("check_db.sql");
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next()) {
				if (!necessuryDBObjects.contains(rs.getString(1))) {
					result = false;
					log.info(resourceBundleWrapper.getString("AdminDBModelRepository_UncorrectAdministrationDB"));
					break;
				};
			};

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

		return result;
	}

	@Override
	public boolean isAccessController() {
		Boolean result = false;
		ResultSet rs;
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript("check_access_controller.sql");
			rs = stmt.executeQuery(query);

			rs.next();
			result = rs.getBoolean(1);
			log.info(resourceBundleWrapper.getString("AdminDBModelRepository_AdministratorUser") + result.toString());
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

		return result;
	}

	@Override
	public List<String> requestAllUsers() {
		List<String> result = new ArrayList<>();
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript("get_all_users.sql");
			ResultSet rs = stmt.executeQuery(query);

			while(rs.next()) {
				String userName = rs.getString(1);
				result.add(userName);
			}
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

		return result;
	}

	@Override
	public void deleteUserByName(String userName) throws SQLException {
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript("delete_user.sql");
			stmt.executeUpdate(String.format(query, userName));
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
	public void changeUserPassword(String userName, String newPassword) throws SQLException {
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript("change_pass.sql");
			stmt.executeUpdate(String.format(query, userName, newPassword));
		} catch (IOException err) {
			log.warning(err.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException err) {
				log.warning(err.getMessage());
			}
		}
	}

	@Override
	public void createUser(String userName, String password) throws SQLException {
		Session session = dbModel.getSession();
		Connection connection = session.connection();

		try {
			Statement stmt = connection.createStatement();
			String query = reader.performScript("create_user.sql");
			stmt.executeUpdate(String.format(query, userName, password));
		} catch (IOException err) {
			log.warning(err.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException err) {
				log.warning(err.getMessage());
			}
		}
	}
	
}
