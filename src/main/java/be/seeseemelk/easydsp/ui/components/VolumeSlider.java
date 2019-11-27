package be.seeseemelk.easydsp.ui.components;

import java.util.function.Consumer;

public class VolumeSlider extends Slider<Float>
{

	public VolumeSlider(Consumer<Float> callback, float defaultValue)
	{
		super(0, 10_000, (int) (defaultValue * 10_000), "%.2f%%",
				i -> i / 10_000f,
				i -> i / 100f,
				callback);
	}

	public VolumeSlider(Consumer<Float> callback)
	{
		this(callback, 1f);
	}
}
