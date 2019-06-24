package be.seeseemelk.easydsp.modules;

import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;

@DSPModule(value = "Mixer", group = ModuleGroup.EFFECT)
public class MixerModule extends Module implements OutputPort
{
	private InputPipe inputA;
	private InputPipe inputB;
	private float[] bufferB;

	@Override
	public void init() throws Exception
	{
		inputA = createInput("Input A");
		inputB = createInput("Input B");

		createOutput("Output", this);

		bufferB = new float[getEngine().getSamplesPerExecution()];
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!inputA.read(buffer))
		{
			return inputB.read(buffer);
		}
		else if (inputB.read(bufferB))
		{
			for (int i = 0; i < buffer.length; i++)
				buffer[i] += bufferB[i];
			return true;
		}
		else
			return false;
	}
}
