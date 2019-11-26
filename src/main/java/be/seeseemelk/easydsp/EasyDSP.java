package be.seeseemelk.easydsp;

import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.reflections.Reflections;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.ui.MainWindow;

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
			loadLookAndFeel();
			loadAnnotatedModules();
			window = new MainWindow(engine);
			window.setVisible(true);
		});
	}
	
	private void loadLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e)
		{
			logger.severe("Failed to set native look and feel");
			e.printStackTrace();
		}
	}
	
	private void loadAnnotatedModules()
	{
		int count = 0;
		Reflections reflections = new Reflections("be.seeseemelk.easydsp.modules");
		for (Class<?> annotated : reflections.getTypesAnnotatedWith(DSPModule.class))
		{
			Class<? extends Module> module = annotated.asSubclass(Module.class);
			engine.registerModule(module);
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















