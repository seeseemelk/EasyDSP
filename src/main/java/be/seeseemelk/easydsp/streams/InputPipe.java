package be.seeseemelk.easydsp.streams;

public class InputPipe
{
	private OutputPipe output;
	private Module module;
	private String name;

	public InputPipe(String name, Module module, OutputPipe output)
	{
		this(name);
		this.module = module;
		this.output = output;
	}
	
	public InputPipe(String name)
	{
		this.name = name;
	}

	public OutputPipe getOutput()
	{
		return output;
	}
	
	public Module getModule()
	{
		return module;
	}

	public boolean isConnected()
	{
		return output != null;
	}

	public void connect(OutputPipe port)
	{
		this.output = port;
	}

	public void disconnect()
	{
		output = null;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		if (output == null)
			return getName();
		else
			return String.format("%s (connected to %s)", name, output.getModule().toString());
	}

	public void read(float[] buffer)
	{
		if (output != null)
			output.read(buffer);
	}
}
