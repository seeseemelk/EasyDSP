package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.Util;
import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

import java.util.Arrays;

@DSPModule(value = "Volume Gate", group = ModuleGroup.FILTER)
public class VolumeGateModule extends Module implements OutputPort
{
	private InputPipe input;
	private float volumeThreshold;

	@Override
	public void init() throws Exception
	{
		input = createInput("Input");
		createOutput("Output", this);

		addOption("Volume Threshold", new VolumeSlider(volume -> volumeThreshold = volume * 2f));
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!input.read(buffer))
			return false;

		float sumSquared = 0;
		for (float f : buffer)
			sumSquared += f * f;

		float rms = (float) Math.sqrt(1f / buffer.length * sumSquared);
		if (rms < volumeThreshold)
			Arrays.fill(buffer, 0);
		return true;
	}
}
