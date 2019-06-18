package be.seeseemelk.easydsp.streams;

import java.util.ArrayList;
import java.util.List;

import be.seeseemelk.easydsp.modules.Module;

public class AudioOutputStream
{
	private Module module;
	private List<AudioStream> streams = new ArrayList<>();
	private List<AudioStream> modStreams = new ArrayList<>();
	
	public AudioOutputStream(Module module)
	{
		this.module = module;
	}
	
	public Module getModule()
	{
		return module;
	}
	
	public void addStream(AudioStream stream)
	{
		modStreams = new ArrayList<>(streams);
		modStreams.add(stream);
		stream.setAudioOutputStream(this);
	}
	
	public void removeStream(AudioStream stream)
	{
		modStreams = new ArrayList<>(streams);
		modStreams.remove(stream);
		stream.setAudioOutputStream(null);
	}
	
	public void clearStreams()
	{
		modStreams = new ArrayList<>();
	}
	
	public List<AudioStream> getStreams()
	{
		return streams;
	}
	
	public boolean isConnected()
	{
		boolean connected = (streams.size() > 0);
		if (!connected && modStreams.size() > 0)
		{
			streams = modStreams;
			connected = true;
		}
		return connected;
	}
	
	public void write(double value) throws InterruptedException
	{
		for (AudioStream stream : streams)
		{
			if (stream != null)
				stream.write(value);
		}
		streams = modStreams;
	}
	
	public void write(double[] values) throws InterruptedException
	{
		for (AudioStream stream : streams)
			stream.write(values);
		streams = modStreams;
	}
}












