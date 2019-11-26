package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;

import java.util.Arrays;

@DSPModule(value = "Delay", group = ModuleGroup.EFFECT)
public class DelayModule extends Module implements OutputPort
{
	private static final int delay = 8;
	private InputPipe input;
	private float[][] delayBuffer;
	private boolean[] returnValues;
	private int index;
	private boolean alreadyExecuted = false;

	@Override
	public void init() throws Exception
	{
		setDescription("This module will delay audio coming in.");

		input = createInput("Input");
		createOutput("Output", this);

		delayBuffer = new float[delay][getEngine().getSamplesPerExecution()];
		returnValues = new boolean[delay];
	}

	@Override
	public void onStart()
	{
		for (var buffer : delayBuffer)
			Arrays.fill(buffer, 0f);
		index = -1;
	}

	@Override
	public void onCycle()
	{
		alreadyExecuted = false;
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!alreadyExecuted)
		{
			index = (index + 1) % delay;
			System.arraycopy(delayBuffer[index], 0, buffer, 0, buffer.length);
			input.read(delayBuffer[index]);
		}
		var returnValue = returnValues[index];
		//returnValues[index] = ;
		return returnValue;
	}
}
