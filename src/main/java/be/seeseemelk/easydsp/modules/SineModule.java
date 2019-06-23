package be.seeseemelk.easydsp.modules;

import javax.swing.JSlider;

import be.seeseemelk.easydsp.streams.OutputPort;

@DSPModule("Sine Generator")
public class SineModule extends Module implements OutputPort
{
	private double frequency = 1.0;
	
	public void init()
	{
		setColor(255, 145, 243);
		setDescription("The sine generator will generate a sine wave from -1 to +1. The frequency can be modified.");
		
		createOutput("Sine", this);
		
		JSlider freqSlider = new JSlider(30, 1000, 200);
		freqSlider.setPaintTicks(true);
		freqSlider.setMinorTickSpacing(50);
		freqSlider.addChangeListener(e -> {
			frequency = ((double) freqSlider.getValue()) / 100.0;
		});
		addOption("Frequency", freqSlider);
	}

	@Override
	public void read(float[] buffer)
	{
		double time = getEngine().getRuntime();
		for (int i = 0; i < buffer.length; i++)
		{
			buffer[i] = (float) Math.sin(time * frequency + i);
		}
	}
}
