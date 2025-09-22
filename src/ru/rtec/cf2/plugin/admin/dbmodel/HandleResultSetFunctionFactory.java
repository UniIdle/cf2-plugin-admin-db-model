package ru.rtec.cf2.plugin.admin.dbmodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.g4.utils.log.LoggingUtils;
import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.rtec.cf2.ResourcesStorage;
import ru.rtec.cf2.plugin.admin.dbmodel.dialects.postgresql.AdminPostgreSQLDBModelPlugin;


/**
 * Фабрика функций для обработки ResultSet
 */
public class HandleResultSetFunctionFactory {
	/**
	 * Необходимые объекты в БД
	 */
	private static final List<DBObjects> NECESSARY_DB_OBJECTS = 
			Arrays.asList(DBObjects.values());

	/**
	 * Логгер
	 */
	protected static Logger log = 
			LoggerFactory.getLogger(HandleResultSetFunctionFactory.class);

	/**
	 * Обертка для ResourceBundle
	 */
	private static IResourceBundleWrapper resourceBundle = 
			ResourcesStorage.getBundle(AdminPostgreSQLDBModelPlugin.class);

	/**
	 * Конструктор
	 */
	private HandleResultSetFunctionFactory() {

	}


	public static Function<ResultSet, Boolean> checkValidSchemaFunction() {
		return (rs) -> {
			try {
				List<String> necessuryDBObjects = NECESSARY_DB_OBJECTS.stream().map(Enum::name)
						.collect(Collectors.toList());

				while(rs.next()) {
					necessuryDBObjects.remove(rs.getString(1));
				};

				if (necessuryDBObjects.size() == 0) {
					return true;
				} else {
					log.warn(resourceBundle.getString(
							"AdminDBModelRepository_UncorrectAdministrationDB"));

					log.warn("В схеме отсутствуют объекты: {}", necessuryDBObjects);

					return false;
				}
			} catch (SQLException e) {
				log.warn(e.getMessage());
				log.error(LoggingUtils.dumpThrowable(e));

				return false;
			}
		};
	}

	public static Function<ResultSet, Boolean> checkOwnerFunction() {
		return (rs) -> {
			try {
				if (rs.next()) {
					return true;
				} else {
					return false;
				}
			} catch (SQLException e) {
				log.warn(e.getMessage());
				log.error(LoggingUtils.dumpThrowable(e));

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
				log.warn(e.getMessage());
				log.error(LoggingUtils.dumpThrowable(e));

				return result;
			}
		};
	}

	public static Function<ResultSet, List<Long>> getLongListResultFunction() {
		return (rs) -> {
			List<Long> result = new ArrayList<>();

			try {
				while(rs.next()) {
					result.add(rs.getLong(1));
				}

				return result;
			} catch (SQLException e) {
				log.warn(e.getMessage());
				log.error(LoggingUtils.dumpThrowable(e));

				return result;
			}
		};
	}

	public static Function<ResultSet, Map<Long, String>> getLongStringMapResultFunction() {
		return (rs) -> {
			Map<Long, String> result = new HashMap<>();

			try {
				while(rs.next()) {
					result.put(rs.getLong(1), rs.getString(2));
				}

				return result;
			} catch (SQLException e) {
				log.info(e.getMessage());
				log.error(LoggingUtils.dumpThrowable(e));

				return result;
			}
		};
	}

}
