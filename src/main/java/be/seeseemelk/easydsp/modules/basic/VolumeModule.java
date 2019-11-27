package be.seeseemelk.easydsp.modules.basic;

import java.awt.Color;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

@DSPModule(value = "Volume", group = ModuleGroup.EFFECT)
public class VolumeModule extends Module implements OutputPort
{
	private InputPipe input;
	private float volume = 1.0f;

	@Override
	public void init() throws Exception
	{
		setDescription("Allows the volume of a stream to be modified. Some more text. Some more text. Some more text.");
		setColor(new Color(84, 95, 227));

		input = createInput("Input");
		createOutput("Output", this);

		int maxValue = 10_000;
		addOption("Volume", new VolumeSlider(vol -> volume = vol));
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!input.read(buffer))
			return false;

		for (int i = 0; i < buffer.length; i++)
			buffer[i] *= volume;

		return true;
	}
}
