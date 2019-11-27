package be.seeseemelk.easydsp.ui.components;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class Slider<T> extends JPanel
{
	private static final int maxValue = 1000;
	private final Function<Integer, T> converter;
	private final Function<Integer, T> displayConverter;
	private final Consumer<T> callback;
	private final String format;
	private final JSlider slider;
	private final JLabel label;

	public Slider(int minValue, int maxValue, int defaultValue, String format,
				  Function<Integer, T> converter,
				  Function<Integer, T> displayConverter,
				  Consumer<T> callback)
	{
		super(new BorderLayout());

		this.format = format;

		this.converter = converter;
		this.displayConverter = displayConverter;
		this.callback = callback;

		slider = new JSlider(minValue, maxValue, defaultValue);
		slider.setPaintTicks(true);
		slider.setMinorTickSpacing(1000);
		slider.addChangeListener(e -> {
			fireChange(slider.getValue());
		});

		add(slider);

		label = new JLabel();
		add(label, BorderLayout.EAST);
		fireChange(maxValue);
	}

	private void updateLabel(T value)
	{
		label.setText(String.format(format, value));
	}

	private void fireChange(float value)
	{
		updateLabel(displayConverter.apply(slider.getValue()));
		callback.accept(converter.apply(slider.getValue()));
	}
}
