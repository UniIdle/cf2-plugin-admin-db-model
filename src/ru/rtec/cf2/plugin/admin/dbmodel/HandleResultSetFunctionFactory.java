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


/**
 * Фабрика функций для обработки ResultSet
 */
public class HandleResultSetFunctionFactory {
	/**
	 * Необходимые объекты в БД
	 */
	private static final List<Object> NECESSARY_DB_OBJECTS = new ArrayList<Object>() {{
		addAll(Arrays.asList(AdminDBTables.values()));
		addAll(Arrays.asList(AdminDBFunctions.values()));
		addAll(Arrays.asList(AdminDBRoles.values()));
	}};


	/**
	 * Логгер
	 */
	protected static Logger log = 
			LoggerFactory.getLogger(HandleResultSetFunctionFactory.class);


	/**
	 * Конструктор
	 */
	private HandleResultSetFunctionFactory() {

	}


	public static Function<ResultSet, List<String>> schemaValidationFunction() {
		return (rs) -> {
			List<String> necessaryDBObjects = NECESSARY_DB_OBJECTS.stream().map(e -> e.toString())
					.collect(Collectors.toList());

			try {
				while(rs.next()) {
					necessaryDBObjects.remove(rs.getString(1));
				};

				return necessaryDBObjects;
			} catch (SQLException e) {
				log.warn(e.getMessage());
				log.error(LoggingUtils.dumpThrowable(e));

				return necessaryDBObjects;
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

	public static Function<ResultSet, Map<Long, Long>> getLongLongMapResultFunction() {
		return (rs) -> {
			Map<Long, Long> result = new HashMap<>();

			try {
				while(rs.next()) {
					result.put(rs.getLong(1), rs.getLong(2));
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
