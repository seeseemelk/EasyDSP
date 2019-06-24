package be.seeseemelk.easydsp.modules;

import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;

import javax.swing.*;

@DSPModule(value = "Volume", group = ModuleGroup.EFFECT)
public class VolumeModule extends Module implements OutputPort
{
	private InputPipe input;
	private float volume = 1.0f;

	@Override
	public void init() throws Exception
	{
		input = createInput("Input");
		createOutput("Output", this);

		int maxValue = 10_000;
		JSlider slider = new JSlider(0, maxValue, maxValue);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(1000);
		slider.addChangeListener(e -> {
			volume = ((float) slider.getValue()) / maxValue;
		});
		addOption("Volume", slider);
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
