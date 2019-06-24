package be.seeseemelk.easydsp.modules;

import be.seeseemelk.easydsp.streams.InputPipe;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;

@DSPModule("Output")
public class OutputModule extends RunnableModule
{
	private float[] buffer;
	private byte[] lineBuffer;
	private InputPipe input;
	private SourceDataLine line;
	private float volume = 1.0f;

	@Override
	public void init() throws LineUnavailableException
	{
		setColor(new Color(144, 255, 33));
		setDescription("Play backs its inputs to the default audio device.");

		input = createInput("Input");

		buffer = new float[(int) getEngine().getSamplesPerExecution()];

		// Open speaker
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
		}
		catch (LineUnavailableException ex)
		{
			ex.printStackTrace();
		}

		// Add slider
		int maxValue = 10_000;
		JSlider slider = new JSlider(0, maxValue, maxValue);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(1000);
		slider.addChangeListener(e -> {
			volume = ((float) slider.getValue()) / maxValue;
		});
		addOption("Volume", slider);
	}

	@Override
	public void run()
	{
		if (!input.read(buffer))
			return;

		int i = 0;
		for (float value : buffer)
		{
			int intValue = (int) ((value * volume) * 0x007F_FFFF);
			lineBuffer[i++] = (byte) (intValue);
			lineBuffer[i++] = (byte) (intValue >> 8);
			lineBuffer[i++] = (byte) (intValue >> 16);
		}

		line.write(lineBuffer, 0, lineBuffer.length);
	}
}
