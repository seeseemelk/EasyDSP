package be.seeseemelk.easydsp.modules;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public class SineModule extends Module
{
	private AudioOutputStream stream;
	private double frequency = 2.0;
	
	public SineModule()
	{
		super("Sine Generator", true);
		
		setColor(255, 145, 243);
		
		stream = createAudioOutputStream("SINE");
		
		JSlider freqSlider = new JSlider(30, 1000, 200);
		freqSlider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				frequency = ((double) freqSlider.getValue()) / 100.0;
			}
		});
		addOption("Frequency", freqSlider);
	}
	
	public static void register(EasyDSP dsp)
	{
		dsp.registerModule(SineModule.class, "Sine Generator");
	}
	
	@Override
	public void run(double time) throws InterruptedException
	{
		for (;;)
		{
			double value = Math.sin(time*frequency);
			stream.write(value);
			time += (1.0 / 44100.0)*1000.0;
		}
	}
}
