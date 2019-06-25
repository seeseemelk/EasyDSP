package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

import java.util.Random;

@DSPModule(value = "Noise Generator", group = ModuleGroup.GENERATOR)
public class NoiseModule extends Module implements OutputPort
{
	private final Random random = new Random();
	private float volume;

	@Override
	public void init() throws Exception
	{
		setDescription("Generates white noise.");
		createOutput("Output", this);

		addOption("Volume", new VolumeSlider(volume -> this.volume = volume));
	}

	@Override
	public boolean read(float[] buffer)
	{
		for (int i = 0; i < buffer.length; i++)
			buffer[i] = (random.nextFloat() * 2f - 1f) * volume;
		return true;
	}
}
