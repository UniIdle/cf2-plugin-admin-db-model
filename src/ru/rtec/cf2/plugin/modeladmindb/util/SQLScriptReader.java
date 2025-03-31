package ru.rtec.cf2.plugin.modeladmindb.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SQLScriptReader {
	/**
	 * Путь к SQL-скриптам. Устанавливаеся плагином с определенным типом БД (PostgreSQL/Oracle/MySQL)
	 */
	private String SQL_SCRIPTS_PATH;


	public SQLScriptReader(String path) {
		this.SQL_SCRIPTS_PATH = path;
	}


	/**
	 * Метод для чтения SQL запроса из файла
	 * @param filepath путь к SQL скрипту
	 * @return SQL запрос из файла в формате строки
	 * @throws IOException
	 */
	public String performScript(String fileName) throws IOException {
		StringBuilder query = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						getClass().getResourceAsStream(SQL_SCRIPTS_PATH + fileName)))) {
							
			while (reader.ready()) {
				String line = reader.readLine();
				String trimLine;

				int comment = line.indexOf("--");
				if (comment >= 0) {
					trimLine = line.substring(0, comment).trim();
				} else {
					trimLine = line.trim();
				}

				if (trimLine.isEmpty()) {
					continue;
				}

				query.append(trimLine).append(' ');
			}
		}

		return query.toString();
	}
}
