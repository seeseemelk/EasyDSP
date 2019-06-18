package be.seeseemelk.easydsp;

import javax.swing.SwingUtilities;

import be.seeseemelk.easydsp.modules.GainModule;
import be.seeseemelk.easydsp.modules.InputModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.MultiplierModule;
import be.seeseemelk.easydsp.modules.NoiseModule;
import be.seeseemelk.easydsp.modules.OutputModule;
import be.seeseemelk.easydsp.modules.SineModule;
import be.seeseemelk.easydsp.modules.VolumeChangerModule;
import be.seeseemelk.easydsp.ui.MainWindow;

public class EasyDSP
{	
	private MainWindow window;
	
	/**
	 * Open the EasyDSP main window
	 */
	public void open()
	{
		EasyDSP dsp = this;
		
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				window = new MainWindow();
				
				OutputModule.register(dsp);
				InputModule.register(dsp);
				VolumeChangerModule.register(dsp);
				SineModule.register(dsp);
				NoiseModule.register(dsp);
				MultiplierModule.register(dsp);
				GainModule.register(dsp);
				
				window.setVisible(true);
			}
		});
	}
	
	public void registerModule(Class<? extends Module> module, String name)
	{
		if (window != null)
		{
			window.registerModule(module, name);
		}
	}
	
	public static void main(String[] args)
	{
		EasyDSP dsp = new EasyDSP();
		dsp.open();
	}
}















