package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.Util;
import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

import java.awt.*;

@DSPModule(value = "Mixer", group = ModuleGroup.EFFECT)
public class MixerModule extends Module implements OutputPort
{
	private InputPipe inputA;
	private InputPipe inputB;
	private float[] bufferB;
	private float volumeA;
	private float volumeB;

	@Override
	public void init() throws Exception
	{
		setDescription("The Mixer mixes two different sound channels into one.");
		setColor(new Color(176, 74, 191));

		inputA = createInput("Input A");
		inputB = createInput("Input B");

		createOutput("Output", this);

		bufferB = new float[getEngine().getSamplesPerExecution()];

		addOption("Volume A", new VolumeSlider(volume -> volumeA = volume, 0.5f));
		addOption("Volume B", new VolumeSlider(volume -> volumeB = volume, 0.5f));
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!inputA.read(buffer))
		{
			var success = inputB.read(buffer);
			if (success)
				Util.fmul(buffer, volumeB);
			return success;
		}

		Util.fmul(buffer, volumeA);
		if (!inputB.read(bufferB))
			return true;

		Util.fmul(bufferB, volumeB);
		for (int i = 0; i < buffer.length; i++)
			buffer[i] += bufferB[i];
		return true;
	}
}
