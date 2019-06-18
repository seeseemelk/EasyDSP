package be.seeseemelk.easydsp.streams;

import be.seeseemelk.easydsp.modules.Module;

public class AudioInputStream
{
	private Module module;
	private AudioStream stream;
	
	public AudioInputStream(Module module)
	{
		this.module = module;
	}
	
	public void setAudioStream(AudioStream stream)
	{
		this.stream = stream;
		
		if (stream == null)
			return;
		
		stream.setAudioInputStream(this);
	}
	
	public AudioStream getAudioStream()
	{
		return stream;
	}
	
	public Module getModule()
	{
		return module;
	}
	
	public double read()
	{
		if (stream != null)
			return stream.read();
		else
			return 0;
	}
}
