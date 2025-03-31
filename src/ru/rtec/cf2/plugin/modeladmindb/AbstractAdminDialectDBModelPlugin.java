package ru.rtec.cf2.plugin.modeladmindb;

import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.g4.utils.resources.ResourceBundleHandlerWrapper;
import ru.rtec.cf2.IApplicationContext;
import ru.rtec.cf2.pi.ICompose;
import ru.rtec.cf2.pi.IPlugin;
import ru.rtec.cf2.pi.PluginVersion;
import ru.rtec.cf2.plugin.model.objects.IConnectionStateListener;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;


public abstract class AbstractAdminDialectDBModelPlugin implements IPlugin, ICompose, IAdminDBModel {
	/**
	 * Обертка для ResourceBundle
	 */
	private IResourceBundleWrapper resourceBundleWrapper = new ResourceBundleHandlerWrapper(
			IAdminDBModel.class.getPackage().getName() + ".resources.resource");

	/**
	 * Контекст приложения
	 */
	private IApplicationContext context;

	/**
	 * Модуль реализующий интерфес по запросам к БД
	 */
	public IAdminDBModelRepository dbRepository;

	/**
	* Слушатель события подключения/отключения к базе данных
	*/
	private IConnectionStateListener connectionDBListener;


	public AbstractAdminDialectDBModelPlugin() {
		dbRepository = new AdminDBModelRepository();
	}


	@Override
	public String getPackageNameAsPath() {
		return "/" + getClass().getPackage().getName().replace(".", "/");
	}

	@Override
	public IAdminDBModelRepository getDBRepository() {
		return this.dbRepository;
	}

	@Override
	public void setConnectionDBListener(IConnectionStateListener connectionDBListener) {
		this.connectionDBListener = connectionDBListener;
	}

	/**
	 * Получить описание плагина
	 *
	 * @return описание плагина
	 */
	@Override
	public String getDescription() {
		return resourceBundleWrapper.getString("AdminDialectDBModelPlugin_Description");
	}

	/**
	 * Установить контекст приложения(ссылка на объект ядра конфигуратора)
	 *
	 * @param context контекст приложения
	 */
	@Override
	public void setApplicationContext(IApplicationContext context) {
		this.context = context;
	}

	/**
	 * Получить версию плагина
	 *
	 * @return версия плагина
	 */
	@Override
	public PluginVersion getVersion() {
		return new PluginVersion(1, 0);
	}

	/**
	 * Связывание с другими плагинами
	 */
	@Override
	public void doCompose() {
		IDBObjects dbModel = (IDBObjects) context.findPlugin(IDBObjects.class);
		dbRepository.setDBModel(dbModel);

		if (connectionDBListener != null) {
			dbModel.addConnectionStateListener(connectionDBListener);
		}
	}

}
