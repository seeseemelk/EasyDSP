package be.seeseemelk.easydsp.modules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.*;

import javax.swing.JComponent;

import be.seeseemelk.easydsp.Engine;
import be.seeseemelk.easydsp.streams.InputPipe;
import be.seeseemelk.easydsp.streams.OutputPort;
import be.seeseemelk.easydsp.streams.OutputPipe;

public abstract class Module
{
	private List<InputPipe> inputs = new ArrayList<>();
	private List<OutputPipe> outputs = new ArrayList<>();

	private List<JComponent> options = new ArrayList<>();
	private Map<JComponent, String> optionNames = new HashMap<>();
	
	private Point position = new Point();
	private Dimension size = new Dimension();
	private Rectangle rectangle = new Rectangle();
	private Color color = new Color(100, 100, 255);
	private String name;
	private String description = "";
	private Engine engine;
	private int id = 0;
	
	protected Module()
	{
		setSize(180, 200);
	}

	public void init() throws Exception
	{

	}

	public Engine getEngine()
	{
		if (engine == null)
			throw new IllegalStateException("Module is not fully instantiated yet");
		return engine;
	}
	
	public void setEngine(Engine engine)
	{
		this.engine = engine;
	}
	
	protected void addOption(String name, JComponent component)
	{
		options.add(component);
		optionNames.put(component, name);
	}
	
	public List<JComponent> getOptions()
	{
		return options;
	}
	
	public String getOptionName(JComponent component)
	{
		return optionNames.get(component);
	}

	public void delete()
	{
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setColor(int r, int g, int b)
	{
		color = new Color(r, g, b);
	}
	
	public InputPipe createInput(String name)
	{
		InputPipe pipe = new InputPipe(name);
		inputs.add(pipe);
		return pipe;
	}
	
	public OutputPipe createOutput(String name, OutputPort handler)
	{
		var output = new OutputPipe(this, name, handler);
		outputs.add(output);
		return output;
	}
	
	public List<InputPipe> getInputs()
	{
		return inputs;
	}
	
	public List<OutputPipe> getOutputs()
	{
		return outputs;
	}
	
	public void setSize(int width, int height)
	{
		size.setSize(width, height);
		rectangle.setSize(width, height);
	}
	
	public void setLocation(int x, int y)
	{
		position.setLocation(x, y);
		rectangle.setLocation(x, y);
	}
	
	public int getWidth()
	{
		return (int) size.getWidth();
	}
	
	public int getHeight()
	{
		return (int) size.getHeight();
	}
	
	public int getX()
	{
		return (int) position.getX();
	}
	
	public int getY()
	{
		return (int) position.getY();
	}
	
	public Rectangle getRectangle()
	{
		return rectangle;
	}
	
	public void paint(Graphics g)
	{
		g.setColor(color);
		g.fillRect(getX()+3, getY()+3, getWidth()-6, getHeight()-6);
		
		g.setColor(new Color(50, 50, 50));
		g.setFont(new Font("TimesRoman", Font.BOLD, 14));
		
		String name = toString();
		int textWidth = g.getFontMetrics().stringWidth(name);
		
		g.drawString(name, getX()+(getWidth()-textWidth)/2, getY()+20);
	}

	@Override
	public String toString()
	{
		return String.format("%s [%d]", getName(), getId());
	}
}












