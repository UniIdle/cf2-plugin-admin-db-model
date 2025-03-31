package ru.rtec.cf2.plugin.modeladmindb;

import ru.rtec.cf2.plugin.model.objects.IConnectionStateListener;

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

	/**
	 * Устанавливает слушатель подключения/отключения к БД
	 * @param connectionDBListener слушатель
	 */
	public void setConnectionDBListener(IConnectionStateListener connectionDBListener);
}
