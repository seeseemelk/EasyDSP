package be.seeseemelk.easydsp.modules;

import java.util.Random;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public class NoiseModule extends Module
{
	private AudioOutputStream stream;
	
	public NoiseModule()
	{
		super("Noise Generator", true);
		
		stream = createAudioOutputStream("OUT");
		
		setColor(0xFF, 0xF4, 0x91);
	}
	
	public static void register(EasyDSP dsp)
	{
		dsp.registerModule(NoiseModule.class, "Noise Generator");
	}

	@Override
	public void run(double time) throws InterruptedException
	{
		Random random = new Random();
		for (;;)
		{
			stream.write(random.nextDouble());
		}
	}

}
