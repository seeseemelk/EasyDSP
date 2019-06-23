package be.seeseemelk.easydsp;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleFactory;
import be.seeseemelk.easydsp.ui.MainWindow;
import org.reflections.Reflections;

public class EasyDSP
{
	private Logger logger = Logger.getLogger("EasyDSP");
	private MainWindow window;
	private final Engine engine;

	public EasyDSP()
	{
		engine = new Engine();
	}
	
	/**
	 * Open the EasyDSP main window
	 */
	public void open()
	{
		SwingUtilities.invokeLater(() -> {
			window = new MainWindow(engine);
			loadAnnotatedModules();
			window.setVisible(true);
		});
	}
	
	/**
	 * Registers a new module to the application.
	 * @param factory The module to add.
	 */
	public void registerModule(ModuleFactory factory)
	{
		window.registerModule(factory);
	}
	
	/**
	 * Registers a new module to the application.
	 * @param module The module to register.
	 */
	public void registerModule(Class<? extends Module> module)
	{
		registerModule(new ModuleFactory(module));
	}
	
	private void loadAnnotatedModules()
	{
		int count = 0;
		Reflections reflections = new Reflections("be.seeseemelk.easydsp.modules");
		for (Class<?> annotated : reflections.getTypesAnnotatedWith(DSPModule.class))
		{
			Class<? extends Module> module = annotated.asSubclass(Module.class);
			registerModule(module);
			count++;
		}
		logger.info(String.format("Loaded %d modules", count));
	}
	
	public static void main(String[] args) throws SecurityException, IOException
	{
		LogManager.getLogManager().readConfiguration(EasyDSP.class.getResourceAsStream("/logger.properties"));
		
		EasyDSP dsp = new EasyDSP();
		dsp.open();
	}
}















