package be.seeseemelk.easydsp.streams;

import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitGenerator;
import com.jsyn.unitgen.UnitSource;

public class JSynWriter extends UnitGenerator implements UnitSource
{
	public UnitOutputPort output;
	
	private AudioInputStream inputStream;
	
	public JSynWriter(AudioInputStream stream)
	{
		addPort(output = new UnitOutputPort("Output"));
		inputStream = stream;
	}

	@Override
	public void generate(int start, int limit)
	{
		double[] values = output.getValues();
		for (int i = start; i < limit; i++)
		{
			values[i] = inputStream.read();
		}
	}

	@Override
	public UnitOutputPort getOutput()
	{
		return output;
	}

}
