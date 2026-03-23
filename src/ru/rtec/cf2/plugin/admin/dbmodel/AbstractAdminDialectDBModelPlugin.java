package ru.rtec.cf2.plugin.admin.dbmodel;

import ru.g4.utils.resources.IResourceBundleWrapper;
import ru.rtec.cf2.IApplicationContext;
import ru.rtec.cf2.pi.ICompose;
import ru.rtec.cf2.pi.IPlugin;
import ru.rtec.cf2.pi.PluginVersion;
import ru.rtec.cf2.plugin.model.objects.IDBObjects;


/**
 * Абстрактный класс плагина для работы с ролевым доступом к объектам конфигуратора
 */
public abstract class AbstractAdminDialectDBModelPlugin implements IPlugin, ICompose, IAdminDBModel {
	/**
	 * Обертка для ResourceBundle
	 */
	protected IResourceBundleWrapper resourceBundle;

	/**
	 * Контекст приложения
	 */
	private IApplicationContext context;

	/**
	 * Модуль реализующий интерфейс по запросам к БД
	 */
	protected IAdminDBModelRepository dbRepository;


	/**
	 * Конструктор
	 */
	public AbstractAdminDialectDBModelPlugin() {
		dbRepository = new AdminDBModelRepository();
	}


	protected void setResourceBundle(IResourceBundleWrapper resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	@Override
	public String getPackageNameAsPath() {
		return "/" + getClass().getPackage().getName().replace(".", "/");
	}

	@Override
	public IAdminDBModelRepository getDBRepository() {
		return this.dbRepository;
	}

	/**
	 * Получить описание плагина
	 *
	 * @return описание плагина
	 */
	@Override
	public String getDescription() {
		return resourceBundle.getString("AdminDialectDBModelPlugin_Description");
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
	}

}
