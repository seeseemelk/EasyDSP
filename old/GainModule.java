package be.seeseemelk.easydsp.modules;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioInputStream;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public class GainModule extends Module
{
	private AudioInputStream input;
	private AudioOutputStream output;
	private double gain = 1.0;
	
	public GainModule()
	{
		super("Gain", true);
		
		input = createAudioInputStream("IN");
		output = createAudioOutputStream("OUT");
		
		JSlider gainSlider = new JSlider(0, 10000, 100);
		gainSlider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				gain = ((double) gainSlider.getValue()) / 100.0;
			}
		});
		addOption("Gain", gainSlider);
	}
	
	public static void register(EasyDSP dsp)
	{
		dsp.registerModule(GainModule.class, "Gain");
	}
	
	@Override
	public void run(double time) throws InterruptedException
	{
		for (;;)
		{
			double value = input.read();
			value = Math.max(Math.min(value*gain, 1.0), -1.0);
			output.write(value);
		}
	}
}
