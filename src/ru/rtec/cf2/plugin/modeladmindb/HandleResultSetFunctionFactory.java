package ru.rtec.cf2.plugin.modeladmindb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.g4.utils.resources.ResourceBundleHandlerWrapper;


/**
 * Фабрика функций для обработки ResultSet
 * 
 */
public class HandleResultSetFunctionFactory {
	private static final List<String> necessuryDBObjects = Arrays.asList(
		"cf2_object_access_controller_role", 
		"cf2_reference_editor_role", 
		"cf2_function_access_controller_role", 
		"cf2_object_editor_role", 
		"cf2_base_user_role", 
		"users_access_map_table", 
		"check_access_function", 
		"define_root_id_function" 
	);

	/**
	 * Логер
	 */
	protected static Logger log = Logger.getLogger(HandleResultSetFunctionFactory.class.getName());

	/**
	 * Обертка для ResourceBundle
	 */
	private static IResourceBundleWrapper resourceBundleWrapper = new ResourceBundleHandlerWrapper(
			IAdminDBModel.class.getPackage().getName() + ".resources.resource");


	private HandleResultSetFunctionFactory() {

	}


	public static Function<ResultSet, Boolean> getValidSchemaFunction() {
		return (rs) -> {
			Boolean result = true;
			try {
				while(rs.next()) {
					if (!necessuryDBObjects.contains(rs.getString(1))) {
						result = false;
						log.warning(resourceBundleWrapper.getString("AdminDBModelRepository_UncorrectAdministrationDB"));
						log.warning("В схеме отсутствует объект - " + rs.getString(1));
						break;
					};
				};
				return result;
			} catch (SQLException e) {
				log.info(e.getMessage());
				return false;
			}
		};
	}

	public static Function<ResultSet, List<String>> getStringListResultFunction() {
		return (rs) -> {
			List<String> result = new ArrayList<>();
			try {
				while(rs.next()) {
					result.add(rs.getString(1));
				}
				return result;
			} catch (SQLException e) {
				log.info(e.getMessage());
				return result;
			}
		};
	}

	public static Function<ResultSet, Map<Integer, String>> getIntegerStringMapResultFunction() {
		return (rs) -> {
			Map<Integer, String> result = new HashMap<>();
			try {
				while(rs.next()) {
					result.put(rs.getInt(1), rs.getString(2));
				}
				return result;
			} catch (SQLException e) {
				log.info(e.getMessage());
				return result;
			}
		};
	}

}
