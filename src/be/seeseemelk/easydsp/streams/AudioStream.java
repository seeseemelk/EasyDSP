package be.seeseemelk.easydsp.streams;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AudioStream
{
	private BlockingQueue<Double> queue;
	private AudioInputStream inputStream;
	private AudioOutputStream outputStream;
	
	public AudioStream()
	{
		queue = new ArrayBlockingQueue<>(16);
	}
	
	public void setAudioInputStream(AudioInputStream stream)
	{
		inputStream = stream;
	}
	
	public void setAudioOutputStream(AudioOutputStream stream)
	{
		outputStream = stream;
	}
	
	public AudioInputStream getInputStream()
	{
		return inputStream;
	}
	
	public AudioOutputStream getOutputStream()
	{
		return outputStream;
	}
	
	public void write(double value) throws InterruptedException
	{
		queue.put(value);
	}
	
	public void write(double[] values) throws InterruptedException
	{
		for (double value : values)
			queue.put(value);
	}
	
	public double read()
	{
		try
		{
			return queue.take();
		}
		catch (InterruptedException e)
		{
			//e.printStackTrace();
			return 0;
		}
	}
}
