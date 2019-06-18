package be.seeseemelk.easydsp.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.streams.AudioInputStream;
import be.seeseemelk.easydsp.streams.AudioOutputStream;
import be.seeseemelk.easydsp.streams.AudioStream;

public class ModuleBox extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	
	private List<Module> modules = new ArrayList<>();
	private Module selectedModule;
	private Point moduleMouseOffset;
	private AudioStream draggingStream;
	private AudioInputStream draggingInputStream;
	private Point lastMouseLocation = new Point();

	public ModuleBox()
	{
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(new Color(230, 230, 230));
		g.fillRect(0, 0, getWidth(), getHeight());
		
				for (Module module : modules)
		{
			if (module.equals(selectedModule))
			{
				g.setColor(new Color(255, 100, 100));
				g.fillRect(module.getX(), module.getY(), module.getWidth(), module.getHeight());
			}
			module.paint(g);
		}
		
		paintLines(g);
	}
	
	private void paintLines(Graphics g)
	{
		g.setColor(new Color(255, 255, 0));
		
		for (Module module : modules)
		{
			for (AudioInputStream inputStream : module.getAudioInputStreams().values())
			{
				if (inputStream.getAudioStream() != null)
				{
					AudioOutputStream outputStream = inputStream.getAudioStream().getOutputStream();
					if (outputStream != null)
					{
						Module output = outputStream.getModule();
						g.drawLine(module.getX()+module.getWidth()/2,
								module.getY()+module.getHeight()/2,
								output.getX()+module.getWidth()/2,
								output.getY()+module.getHeight()/2);
					}
					else
					{
						g.drawLine(module.getX()+module.getWidth()/2,
								module.getY()+module.getWidth()/2,
								(int) lastMouseLocation.getX(), (int) lastMouseLocation.getY());
					}
				}
			}
		}
	}
	
	public void addModule(Module module)
	{
		module.setLocation(200, 200);
		modules.add(module);
		repaint();
	}
	
	public void removeModule(Module module)
	{
		for (AudioInputStream stream : module.getAudioInputStreams().values())
		{
			if (stream.getAudioStream() != null)
				removeAudioStream(stream.getAudioStream());
		}
		
		for (AudioOutputStream stream : module.getAudioOutputStreams().values())
		{
			for (AudioStream audioStream : stream.getStreams())
				removeAudioStream(audioStream);
		}
		
		module.delete();
		modules.remove(module);
		repaint();
	}
	
	public void setSelectedModule(Module module)
	{
		selectedModule = module;
		repaint();
	}
	
	public Module getSelectedModule()
	{
		return selectedModule;
	}
	
	public boolean hasSelectedModule()
	{
		return selectedModule != null;
	}
	
	public void clearSelectedModule()
	{
		selectedModule = null;
		moduleMouseOffset = null;
		repaint();
	}
	
	public void setDraggingStream(AudioStream stream, AudioInputStream inputStream)
	{
		draggingStream = stream;
		draggingInputStream = inputStream;
	}
	
	public boolean isDraggingStream()
	{
		return draggingStream != null;
	}
	
	public AudioStream getDraggingStream()
	{
		return draggingStream;
	}
	
	public AudioInputStream getDraggingInputStream()
	{
		return draggingInputStream;
	}
	
	public void clearDraggingStream()
	{
		draggingStream = null;
		draggingInputStream = null;
	}
	
	public void cancelDraggingStream()
	{
		draggingInputStream.setAudioStream(null);
		clearDraggingStream();
	}
	
	public void removeAudioStream(AudioStream stream)
	{
		stream.getInputStream().setAudioStream(null);
		stream.getOutputStream().removeStream(stream);
		repaint();
	}
	
	public void openPropertiesWindow(Module module)
	{
		PropertiesWindow window = new PropertiesWindow(module);
		window.setVisible(true);
	}
	
	public void showOutputOptions(Module module, int mx, int my)
	{
		JPopupMenu menu = new JPopupMenu("Outputs");
		
		JMenuItem nameItem = new JMenuItem(module.getName());
		nameItem.setEnabled(false);
		menu.add(nameItem);
		menu.addSeparator();
		
		for (Entry<String, AudioOutputStream> entry : module.getAudioOutputStreams().entrySet())
		{
			AudioOutputStream outputStream = entry.getValue();
			JMenuItem item = new JMenuItem(entry.getKey());
			item.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					AudioStream stream = getDraggingStream();
					outputStream.addStream(stream);
					clearDraggingStream();
					repaint();
				}
			});
			menu.add(item);
		}
		
		menu.show(this, mx, my);
	}

	public void showOptions(Module module, int mx, int my)
	{
		if (isDraggingStream())
		{
			showOutputOptions(module, mx, my);
			return;
		}
		
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem nameItem = new JMenuItem(module.getName());
		nameItem.setEnabled(false);
		menu.add(nameItem);
		
		menu.addSeparator();
		
		JMenuItem propertiesItem = new JMenuItem("Properties");
		propertiesItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				openPropertiesWindow(module);
			}
		});
		menu.add(propertiesItem);
		
		JMenuItem deleteItem = new JMenuItem("Remove");
		deleteItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				removeModule(module);
			}
		});
		menu.add(deleteItem);
		
		menu.addSeparator();
		
		for (Entry<String, AudioInputStream> entry : module.getAudioInputStreams().entrySet())
		{
			AudioInputStream inputStream = entry.getValue();
			JMenuItem item;
			boolean removeWhenClicked = false;
			
			if (inputStream.getAudioStream() == null)
				item = new JMenuItem(entry.getKey());
			else
			{
				item = new JMenuItem(entry.getKey() + " (Remove)");
				removeWhenClicked = true;
			}
			
			if (removeWhenClicked)
			{
				item.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						removeAudioStream(inputStream.getAudioStream());
					}
				});
			}
			else
			{
				item.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						AudioStream stream = new AudioStream();
						inputStream.setAudioStream(stream);
						setDraggingStream(stream, inputStream);
					}
				});
			}
			
			menu.add(item);
		}
		
		menu.show(this, mx, my);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		int mx = e.getX();
		int my = e.getY();
		
		Module clickedModule = null;
		for (Module module : modules)
		{
			if (module.getRectangle().contains(mx, my))
			{
				clickedModule = module;
			}
		}
		
		if (clickedModule == null)
			clearSelectedModule();
		else
		{
			if (isDraggingStream())
			{
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					cancelDraggingStream();
					repaint();
				}
				else
				{
					showOptions(clickedModule, mx, my);
					repaint();
				}
			}
			else
			{
				setSelectedModule(clickedModule);
				moduleMouseOffset = new Point(mx-clickedModule.getX(), my-clickedModule.getY());
				
				if (e.getButton() == MouseEvent.BUTTON3)
				{
					showOptions(clickedModule, mx, my);
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (hasSelectedModule())
		{
			int mx = e.getX();
			int my = e.getY();
			Module module = getSelectedModule();
			module.setLocation((int) (mx-moduleMouseOffset.getX()), (int) (my-moduleMouseOffset.getY()));
			repaint();
		}
		else if (isDraggingStream())
		{
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (isDraggingStream())
		{
			lastMouseLocation = e.getPoint();
			repaint();
		}
	}
}
















