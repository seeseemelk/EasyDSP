package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;

import javax.swing.*;
import java.awt.*;

@DSPModule("Distortion")
public class DistortionModule extends Module implements OutputPort
{
	private InputPipe input;
	private float gain = 3.0f;

	@Override
	public void init() throws Exception
	{
		setDescription("Applies a hard clipping distorion filter to the audio stream.");
		setColor(new Color(73, 181, 238));

		input = createInput("Input");
		createOutput("Output", this);

		int minValue =      10_000;
		int maxValue = 100_000_000;
		JSlider slider = new JSlider(minValue, maxValue, maxValue);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(5_000_000);
		slider.addChangeListener(e -> {
			gain = ((float) slider.getValue()) / minValue;
		});
		addOption("Gain", slider);
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!input.read(buffer))
			return false;

		for (int i = 0; i < buffer.length; i++)
		{
			buffer[i] = Math.min(Math.max(buffer[i] * gain, -1f), 1f);
		}

		return true;
	}
}
