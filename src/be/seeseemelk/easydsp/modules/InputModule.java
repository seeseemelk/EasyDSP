package be.seeseemelk.easydsp.modules;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public class InputModule extends Module
{
	private AudioOutputStream output;
	private TargetDataLine line;
	
	public InputModule()
	{
		super("Audio Input", true);
		
		setColor(100, 100, 255);
		
		output = createAudioOutputStream("OUT");
		
		AudioFormat format = new AudioFormat(44100f, 24, 1, true, false);
		
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
		{
			System.err.println("Line not supported");
		}
		
		try
		{
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format, 16);
			line.start();
			System.out.println("Line opened");
			System.out.println("Buffer size: " + line.getBufferSize());
			System.out.println("Volume: " + line.getLevel());
			if (line.getLevel() == AudioSystem.NOT_SPECIFIED)
				System.out.println("Not specified!");
		}
		catch (LineUnavailableException ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void delete()
	{
		line.stop();
		line.close();
		super.delete();
	}

	public static void register(EasyDSP dsp)
	{
		dsp.registerModule(InputModule.class, "Audio Input");
	}
	
	private double read()
	{
		byte[] b = new byte[3];
		System.out.println(line.available());
		line.read(b, 0, 3);
		int value = (int)b[0] + ((int)b[1]<<8) + ((int)b[2]<<16);
		return (double) value / (double) 0x7FFFFF;
	}
	
	@Override
	public void run(double time) throws InterruptedException
	{
		for (;;)
		{
			if (output.isConnected())
			{
				System.out.println("Connected");
				double value = read();
				output.write(value);
			}
			else
			{
				System.out.println("Not connected");
				line.drain();
			}
		}
	}
}











