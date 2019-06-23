package be.seeseemelk.easydsp.modules;

import be.seeseemelk.easydsp.EasyDSP;
import be.seeseemelk.easydsp.streams.AudioInputStream;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public class MultiplierModule extends Module
{
	private AudioInputStream input;
	private AudioInputStream multiplier;
	private AudioOutputStream output;
	
	public MultiplierModule()
	{
		super("Multiplier", true);
		
		setColor(0x91, 0xED, 0xFF);
		
		input = createAudioInputStream("IN");
		multiplier = createAudioInputStream("MUL");
		output = createAudioOutputStream("OUT");
	}
	
	public static void register(EasyDSP dsp)
	{
		dsp.registerModule(MultiplierModule.class, "Multiplier");
	}
	
	@Override
	public void run(double time) throws InterruptedException
	{
		for (;;)
		{
			double inputValue = input.read();
			double multiplierValue = multiplier.read();
			double value = inputValue * multiplierValue;
			output.write(value);
		}
	}
}
















