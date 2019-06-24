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
		port.connect(this);
		this.output = port;
	}

	public void disconnect()
	{
		if (output != null)
			output.doDisconnect(this);
		doDisconnect();
	}

	void doDisconnect()
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

	public boolean read(float[] buffer)
	{
		if (output != null)
			return output.read(buffer);
		else
			return false;
	}
}
