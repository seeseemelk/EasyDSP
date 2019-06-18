package be.seeseemelk.easydsp.modules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

import be.seeseemelk.easydsp.streams.AudioInputStream;
import be.seeseemelk.easydsp.streams.AudioOutputStream;

public abstract class Module
{
	private HashMap<String, AudioInputStream> inputs = new HashMap<>();
	private HashMap<String, AudioOutputStream> outputs = new HashMap<>();
	private List<JComponent> options = new ArrayList<>();
	private Map<JComponent, String> optionNames = new HashMap<>();
	
	private Point position = new Point();
	private Dimension size = new Dimension();
	private Rectangle rectangle = new Rectangle();
	private Timer timer = new Timer();
	private Color color = new Color(100, 100, 255);
	private Thread thread;
	private TimerTask timerTask;
	private String name;
	private boolean continues = false;
	
	protected Module(String name, boolean continues)
	{
		setSize(130, 170);
		
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				start();
			}
		};
		
		timer.schedule(task, 1000);
		this.name = name;
		this.continues = continues;
	}
	
	private void start()
	{
		Module module = this;
		
		if (continues)
		{
			thread = new Thread()
			{
				@Override
				public void run()
				{
					double time = 0;
					try
					{
						module.run(time);
					}
					catch (InterruptedException e)
					{
						// Do nothing
					}
				}
			};
			thread.start();
		}
		else
		{
			/*timerTask = new TimerTask()
			{
				@Override
				public void run()
				{
					try
					{
						module.run();
					}
					catch (InterruptedException e)
					{
						// Do nothing
					}
				}
			}; 
			timer.schedule(timerTask, 48, 48);*/
			thread = new Thread()
			{
				@Override
				public void run()
				{
					try
					{
						double time = 0;
						for (;;)
						{
							module.run(time);
							Thread.sleep(20);
							time += 20.0 / 1000.0 / 1000.0;
						}
					}
					catch (InterruptedException e)
					{
						// Do nothing
					}
				}
			};
			thread.start();
		}
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
		if (thread != null)
		{
			thread.interrupt();
			thread = null;
		}
		if (timerTask != null)
		{
			timerTask.cancel();
			timerTask = null;
		}
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void setColor(int r, int g, int b)
	{
		color = new Color(r, g, b);
	}
	
	public AudioInputStream createAudioInputStream(String name)
	{
		AudioInputStream stream = new AudioInputStream(this);
		inputs.put(name, stream);
		return stream;
	}
	
	public AudioOutputStream createAudioOutputStream(String name)
	{
		AudioOutputStream stream = new AudioOutputStream(this);
		outputs.put(name, stream);
		return stream;
	}
	
	public Map<String, AudioInputStream> getAudioInputStreams()
	{
		return inputs;
	}
	
	public Map<String, AudioOutputStream> getAudioOutputStreams()
	{
		return outputs;
	}
	
	public AudioInputStream getAudioInputStream(String name)
	{
		return inputs.get(name);
	}
	
	public AudioOutputStream getAudioOutputStream(String name)
	{
		return outputs.get(name);
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
		
		String name = getName();
		int textWidth = g.getFontMetrics().stringWidth(name);
		
		g.drawString(name, getX()+(getWidth()-textWidth)/2, getY()+20);
	}
	
	abstract public void run(double time) throws InterruptedException;
}












