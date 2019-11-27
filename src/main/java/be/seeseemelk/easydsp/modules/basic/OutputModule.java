package be.seeseemelk.easydsp.modules.basic;

import java.awt.Color;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.modules.RunnableModule;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

@DSPModule(value = "Output", group = ModuleGroup.BASIC)
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
		var format = new AudioFormat(44100f, 16, 1, true, false);

		var info = new DataLine.Info(SourceDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
			System.err.println("Line not supported");

		line = (SourceDataLine) AudioSystem.getLine(info);
		lineBuffer = new byte[buffer.length * 2];
		line.open(format, lineBuffer.length);
		line.start();

		// Add slider
		addOption("Volume", new VolumeSlider(volume -> this.volume = volume, 0.1f));
	}

	@Override
	public void run()
	{
		if (!input.read(buffer))
			return;

		int i = 0;
		for (float value : buffer)
		{
			/*int intValue = (int) ((value * volume) * 0x007F_FFFF);
			lineBuffer[i++] = (byte) (intValue);
			lineBuffer[i++] = (byte) (intValue >> 8);
			lineBuffer[i++] = (byte) (intValue >> 16);*/
			int intValue = (int) ((value * volume) * 0x7FFF);
			lineBuffer[i++] = (byte) (intValue);
			lineBuffer[i++] = (byte) (intValue >> 8);
		}

		line.write(lineBuffer, 0, lineBuffer.length);
	}
}
