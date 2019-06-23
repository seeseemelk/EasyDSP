package be.seeseemelk.easydsp.modules;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioInputStream;

public class OutputModule extends Module
{
	private AudioInputStream input;
	private SourceDataLine line;
	
	public OutputModule()
	{
		super("Audio Output", true);
		
		setColor(100, 155, 100);
		
		input = createAudioInputStream("IN");
		
		AudioFormat format = new AudioFormat(44100f, 24, 1, true, false);
		
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
		{
			System.err.println("Line not supported");
		}
		
		try
		{
			line = (SourceDataLine) AudioSystem.getLine(info);
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
		dsp.registerModule(OutputModule.class, "Audio Output");
	}
	
	private void write(double value)
	{
		int intValue = (int) (value * 0x7FFFFF);
		byte[] bytes = new byte[3];
		bytes[0] = (byte) (intValue & 0xFF);
		bytes[1] = (byte) ((intValue >> 8) & 0xFF);
		bytes[2] = (byte) ((intValue >> 16) & 0xFF);
		line.write(bytes, 0, 3);
	}
	
	@Override
	public void run(double time)
	{
		for (;;)
		{
			double value = input.read();
			write(value);
		}
	}
}














