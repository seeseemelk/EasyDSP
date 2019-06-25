package be.seeseemelk.easydsp.modules.basic;

import javax.swing.JSlider;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

@DSPModule(value = "Sine Generator", group = ModuleGroup.GENERATOR)
public class SineModule extends Module implements OutputPort
{
	private float frequency = 200.0f;
	private float volume;

	@Override
	public void init()
	{
		setColor(255, 145, 243);
		setDescription("The sine generator will generate a sine wave from -1 to +1. The frequency can be modified.");
		
		createOutput("Sine", this);
		
		JSlider freqSlider = new JSlider(0, 10000, 200);
		freqSlider.setPaintTicks(true);
		freqSlider.setMinorTickSpacing(1000);
		freqSlider.addChangeListener(e -> {
			frequency = (float) freqSlider.getValue();
		});
		addOption("Frequency", freqSlider);
		addOption("Volume", new VolumeSlider(volume -> this.volume = volume, 0.5f));
	}

	@Override
	public boolean read(float[] buffer)
	{
		float sampleRate = getEngine().getSampleRate();
		long samples = getEngine().getSamplesPlayed();

		for (int i = 0; i < buffer.length; i++)
			buffer[i] = (float) Math.sin((samples + i) / sampleRate * frequency * Math.PI) * volume;

		return true;
	}
}
