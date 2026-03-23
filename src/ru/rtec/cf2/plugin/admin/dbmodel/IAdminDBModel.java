package ru.rtec.cf2.plugin.admin.dbmodel;


/**
 * Интерфейс для работы с ролевым доступом к объектам конфигуратора
 */
public interface IAdminDBModel {
	/**
	 * Получает объект репозитория который будет выполнять запросы к БД
	 * @return ссылка на репозиторий
	 */
	public IAdminDBModelRepository getDBRepository();

	/**
	 * Преобразует имя пакета плагина в путь
	 * @return ссылка на репозиторий
	 */
	public String getPackageNameAsPath();

}
