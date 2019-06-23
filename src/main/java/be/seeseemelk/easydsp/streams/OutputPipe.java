package be.seeseemelk.easydsp.streams;

import be.seeseemelk.easydsp.modules.Module;

/**
 * An output port allows to send back audio samples to a destination.
 */
public class OutputPipe
{
	private final Module module;
	private final String name;
	private final OutputPort handler;

	public OutputPipe(Module module, String name, OutputPort handler)
	{
		this.module = module;
		this.name = name;
		this.handler = handler;
	}

	public Module getModule()
	{
		return module;
	}

	public String getName()
	{
		return name;
	}

	public void read(float[] buffer)
	{
		handler.read(buffer);
	}

	@Override
	public String toString()
	{
		return name;
	}
}
