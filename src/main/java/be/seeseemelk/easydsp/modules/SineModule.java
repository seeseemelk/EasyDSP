package be.seeseemelk.easydsp.modules;

import javax.swing.JSlider;

import be.seeseemelk.easydsp.streams.OutputPort;

@DSPModule("Sine Generator")
public class SineModule extends Module implements OutputPort
{
	private float frequency = 200.0f;

	@Override
	public void init()
	{
		setColor(255, 145, 243);
		setDescription("The sine generator will generate a sine wave from -1 to +1. The frequency can be modified.");
		
		createOutput("Sine", this);
		
		JSlider freqSlider = new JSlider(20, 15000, 200);
		freqSlider.setPaintTicks(true);
		freqSlider.setMinorTickSpacing(1000);
		freqSlider.addChangeListener(e -> {
			frequency = (float) freqSlider.getValue();
		});
		addOption("Frequency", freqSlider);
	}

	@Override
	public boolean read(float[] buffer)
	{
		float sampleRate = getEngine().getSampleRate();
		long samples = getEngine().getSamplesPlayed();

		for (int i = 0; i < buffer.length; i++)
			buffer[i] = (float) Math.sin((samples + i) / sampleRate * frequency * Math.PI);

		return true;
	}
}
