package be.seeseemelk.easydsp.streams;

import be.seeseemelk.easydsp.modules.Module;

import java.util.HashSet;
import java.util.Set;

/**
 * An output port allows to send back audio samples to a destination.
 */
public class OutputPipe
{
	private final Module module;
	private final String name;
	private final OutputPort handler;
	private final Set<InputPipe> inputs = new HashSet<>();

	public OutputPipe(Module module, String name, OutputPort handler)
	{
		this.module = module;
		this.name = name;
		this.handler = handler;
	}

	void connect(InputPipe input)
	{
		inputs.add(input);
	}

	public void disconnectAll()
	{
		for (var input : new HashSet<>(inputs))
			disconnect(input);
	}

	public void disconnect(InputPipe input)
	{
		input.doDisconnect();
		doDisconnect(input);
	}

	public void doDisconnect(InputPipe input)
	{
		inputs.remove(input);
	}

	public Module getModule()
	{
		return module;
	}

	public String getName()
	{
		return name;
	}

	public boolean read(float[] buffer)
	{
		return handler.read(buffer);
	}

	@Override
	public String toString()
	{
		return name;
	}
}
