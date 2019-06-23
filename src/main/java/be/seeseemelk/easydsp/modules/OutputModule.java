package be.seeseemelk.easydsp.modules;

import be.seeseemelk.easydsp.streams.InputPipe;

import javax.sound.sampled.*;

@DSPModule("Output Module")
public class OutputModule extends RunnableModule
{
	private float[] buffer;
	private byte[] lineBuffer;
	private InputPipe input;
	private SourceDataLine line;

	@Override
	public void init() throws LineUnavailableException
	{
		setColor(255, 255, 0);
		setDescription("Play backs its inputs to the default audio device.");

		input = createInput("Input");

		buffer = new float[(int) getEngine().getSamplesPerExecution()];

		var format = new AudioFormat(44100f, 24, 1, true, false);

		var info = new DataLine.Info(SourceDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
			System.err.println("Line not supported");

		try
		{
			line = (SourceDataLine) AudioSystem.getLine(info);
			lineBuffer = new byte[buffer.length * 3];
			line.open(format, lineBuffer.length);
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
	public void run()
	{
		input.read(buffer);

		int i = 0;
		for (float value : buffer)
		//for (int y = 0; y < buffer.length; y++)
		{
			int intValue = (int) (value * 100000);
			lineBuffer[i++] = (byte) (intValue);
			lineBuffer[i++] = (byte) (intValue >> 8);
			lineBuffer[i++] = (byte) (intValue >> 16);
		}

		line.write(lineBuffer, 0, lineBuffer.length);
	}
}
