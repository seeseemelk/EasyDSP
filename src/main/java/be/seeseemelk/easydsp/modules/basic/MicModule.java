package be.seeseemelk.easydsp.modules.basic;

import be.seeseemelk.easydsp.modules.DSPModule;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleGroup;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.ui.components.VolumeSlider;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.lang.annotation.Target;

@DSPModule(value = "Microphone", group = ModuleGroup.BASIC)
public class MicModule extends Module implements OutputPort
{
	private float[] buffer;
	private byte[] lineBuffer;
	private TargetDataLine line;
	private float volume = 1.0f;

	@Override
	public void init() throws LineUnavailableException
	{
		setColor(new Color(255, 242, 11));
		setDescription("Allows access to the system's microphone.");

		createOutput("Output", this);

		buffer = new float[getEngine().getSamplesPerExecution()];

		// Open microphone input
		//var format = new AudioFormat(44100f, 24, 1, true, false);
		var format = new AudioFormat(44100f, 16, 1, true, false);

		var info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
			System.err.println("Line not supported");

		try
		{
			line = (TargetDataLine) AudioSystem.getLine(info);
			lineBuffer = new byte[buffer.length * 2];
			line.open(format, lineBuffer.length);
			line.start();
		}
		catch (LineUnavailableException ex)
		{
			ex.printStackTrace();
		}

		// Add volume slider
		addOption("Volume", new VolumeSlider(volume -> this.volume = volume));
	}

	@Override
	public void onStart()
	{
		line.flush();
	}

	@Override
	public void onCycle()
	{
		line.read(lineBuffer, 0, lineBuffer.length);

		for (int i = 0, y = 0; i < lineBuffer.length; i += 2, y++)
		{
			/*int a = Byte.toUnsignedInt(lineBuffer[i + 0]);
			int b = Byte.toUnsignedInt(lineBuffer[i + 1]);
			int c = lineBuffer[i + 2];
			int intValue = (a | (b << 8) | (c << 16));
			buffer[y] = ((float) intValue) / 0x007F_FFFF;*/

			int a = Byte.toUnsignedInt(lineBuffer[i]);
			int b = lineBuffer[i + 1];
			int intValue = (a | (b << 8));
			buffer[y] = (((float) intValue) / 0x7FFF) * volume;
		}
	}

	@Override
	public boolean read(float[] buffer)
	{
		System.arraycopy(this.buffer, 0, buffer, 0, buffer.length);
		return true;
	}
}
