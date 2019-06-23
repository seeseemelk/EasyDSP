package be.seeseemelk.easydsp.modules;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioInputStream;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public class VolumeChangerModule extends Module
{
	private AudioInputStream input;
	private AudioOutputStream output;
	private double volumeModifier = 0.5;
	
	public VolumeChangerModule()
	{
		super("Volume Control", true);
		
		setColor(200, 100, 100);
		
		input = createAudioInputStream("IN");
		output = createAudioOutputStream("OUT");
		
		JSlider volumeSlider = new JSlider(0, 100);
		volumeSlider.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				volumeModifier = ((double) volumeSlider.getValue()) / 100.0;
			}
		});
		addOption("Volume", volumeSlider);
	}
	
	public static void register(EasyDSP dsp)
	{
		dsp.registerModule(VolumeChangerModule.class, "Volume Control");
	}
	
	@Override
	public void run(double time) throws InterruptedException
	{
		for (;;)
		{
			double value = input.read();
			value *= volumeModifier;
			output.write(value);
		}
	}
}






















