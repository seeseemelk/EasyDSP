package be.seeseemelk.easydsp.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import be.seeseemelk.easydsp.Engine;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.streams.InputPipe;

public class ModuleBox extends JPanel implements MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger("ModuleBox");
	private List<Module> modules = new LinkedList<>();
	private Module selectedModule;
	private Point moduleMouseOffset;
	/*private AudioStream draggingPipe;
	private AudioInputStream draggingInputStream;*/
	private InputPipe draggingPipe;
	private Point lastMouseLocation = new Point();
	private final Engine engine;

	public ModuleBox(Engine engine)
	{
		this.engine = engine;
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
		g.setColor(new Color(255, 0, 255));

		for (Module module : modules)
		{
			for (var inputPipe : module.getInputs())
			{
				if (inputPipe.isConnected())
					drawPipe(g, module, inputPipe);
				else if (getDraggingPipe() == inputPipe)
					drawUnconnectedPipe(g, module, inputPipe);
			}
		}
	}

	private void drawPipe(Graphics g, Module module, InputPipe pipe)
	{
		var target = pipe.getOutput().getModule();

		g.drawLine(module.getX()+module.getWidth()/2, module.getY()+module.getWidth()/2,
				target.getX()+target.getWidth()/2, target.getY()+module.getHeight()/2);
	}

	private void drawUnconnectedPipe(Graphics g, Module module, InputPipe pipe)
	{
		g.drawLine(module.getX()+module.getWidth()/2,
				module.getY()+module.getWidth()/2,
				(int) lastMouseLocation.getX(), (int) lastMouseLocation.getY());
	}

	public void addModule(Module module)
	{
		module.setLocation(200, 200);
		modules.add(module);
		repaint();
	}
	
	public void removeModule(Module module)
	{
		//for (AudioInputStream stream : module.getAudioInputStreams().values())
		/*for (var stream : module.getInputs().values())
		{
			if (stream.getOutput() != null)
				removeAudioStream(stream.getAudioStream());
		}
		
		//for (AudioOutputStream stream : module.getAudioOutputStreams().values())
		for (var stream : module.getOutputs().values())
		{
			for (AudioStream audioStream : stream.getStreams())
				removeAudioStream(audioStream);
		}*/

		engine.removeModule(module);
		modules.remove(module);
		repaint();
	}

	public void moveToFront(Module module)
	{
		modules.remove(module);
		modules.add(module);
	}
	
	public void setSelectedModule(Module module)
	{
		selectedModule = module;
		moveToFront(module);
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

	public InputPipe getDraggingPipe()
	{
		return draggingPipe;
	}

	public boolean isDraggingPipe()
	{
		return draggingPipe != null;
	}

	public void setDraggingPipe(InputPipe pipe)
	{
		draggingPipe = pipe;
	}

	public void stopDraggingPipe()
	{
		draggingPipe = null;
	}
	
	public void openPropertiesWindow(Module module)
	{
		PropertiesWindow window = new PropertiesWindow(this, module);
		window.setVisible(true);
	}

	private JPopupMenu createModuleMenu(Module module, int mx, int my)
	{
		JPopupMenu menu = new JPopupMenu();

		JMenuItem nameItem = new JMenuItem(module.getName());
		nameItem.setEnabled(false);
		menu.add(nameItem);

		menu.addSeparator();
		return menu;
	}

	public void showOutputOptions(Module module, int mx, int my)
	{
		var menu = createModuleMenu(module, mx, my);

		for (var port : module.getOutputs())
		{
			var item = menu.add(port.toString());
			item.addActionListener(e -> {
				getDraggingPipe().connect(port);
				stopDraggingPipe();
				repaint();
			});
		}

		menu.show(this, mx, my);
	}

	public void showInputOptions(Module module, int mx, int my)
	{
		var menu = createModuleMenu(module, mx, my);

		JMenuItem propertiesItem = new JMenuItem("Properties");
		propertiesItem.addActionListener(e -> openPropertiesWindow(module));
		menu.add(propertiesItem);

		JMenuItem deleteItem = new JMenuItem("Remove");
		deleteItem.addActionListener(e -> removeModule(module));
		menu.add(deleteItem);

		menu.addSeparator();

		addInputOptions(menu, module);
		
		menu.show(this, mx, my);
	}

	private void addInputOptions(JPopupMenu menu, Module module)
	{
		if (module.getInputs().isEmpty())
			menu.add("No inputs").setEnabled(false);
		else
		{
			for (var pipe : module.getInputs())
			{
				var item = menu.add(pipe.toString());
				item.addActionListener(e -> {
					if (pipe.isConnected())
						pipe.disconnect();
					else
						setDraggingPipe(pipe);
					repaint();
				});
			}
		}
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

		if (isDraggingPipe())
		{
			if (e.getButton() == MouseEvent.BUTTON3)
				stopDraggingPipe();
			else if (clickedModule != null)
				showOutputOptions(clickedModule, mx, my);
			repaint();
		}
		else if (clickedModule == null)
		{
			clearSelectedModule();
		}
		else
		{
			setSelectedModule(clickedModule);
			moduleMouseOffset = new Point(mx-clickedModule.getX(), my-clickedModule.getY());

			if (e.getButton() == MouseEvent.BUTTON3)
				showInputOptions(clickedModule, mx, my);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (isDraggingPipe())
			repaint();
		else if (hasSelectedModule())
		{
			int mx = e.getX();
			int my = e.getY();
			Module module = getSelectedModule();
			module.setLocation((int) (mx-moduleMouseOffset.getX()), (int) (my-moduleMouseOffset.getY()));
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		lastMouseLocation = e.getPoint();
		if (isDraggingPipe())
			repaint();
	}
}
















