package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;

@DSPModule(value = "Multiplier", group = ModuleGroup.EFFECT)
public class MultiplierModule extends Module implements OutputPort
{
	private InputPipe inputA;
	private InputPipe inputB;
	private float[] bufferB;

	@Override
	public void init() throws Exception
	{
		setDescription("Multplies two signals together.");

		inputA = createInput("Input A");
		inputB = createInput("Input B");

		createOutput("Output", this);

		bufferB = new float[getEngine().getSamplesPerExecution()];
	}

	@Override
	public boolean read(float[] buffer)
	{
		if (!inputA.read(buffer) || !inputB.read(bufferB))
			return false;

		for (int i = 0; i < buffer.length; i++)
			buffer[i] *= bufferB[i];

		return true;
	}
}
