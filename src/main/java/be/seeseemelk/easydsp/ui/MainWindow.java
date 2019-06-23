package be.seeseemelk.easydsp.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;

import be.seeseemelk.easydsp.Engine;
import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleFactory;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger("MainWindow");
	private DefaultListModel<ModuleFactory> moduleList = new DefaultListModel<>();
	private final Engine engine;
	
	public MainWindow(Engine engine)
	{
		this.engine = engine;
		setTitle("EasyDSP");
		setSize(800, 600);
		setLocationByPlatform(true);

		setLayout(new BorderLayout());

		initMenubar();
		initToolbar();
		initContent();
	}
	
	private void initMenubar()
	{
		JMenuBar menubar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('f');
		menubar.add(menuFile);
		
		JMenuItem btnHelp = new JMenuItem("Help");
		menuFile.add(btnHelp);
		
		JMenuItem btnExit = new JMenuItem("Exit");
		btnExit.setMnemonic('e');
		btnExit.addActionListener(event -> System.exit(0));
		menuFile.add(btnExit);
		
		setJMenuBar(menubar);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void initToolbar()
	{
		var tools = new JToolBar();
		tools.setFloatable(false);

		var playButton = new JToggleButton();
		updateSimulationButton(playButton);
		playButton.addActionListener(e -> {
			toggleSimulation();
			updateSimulationButton(playButton);
		});
		tools.add(playButton);

		add(tools, BorderLayout.NORTH);
	}
	
	private void initContent()
	{
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		JList<ModuleFactory> list = new JList<>(moduleList);
		list.setModel(moduleList);
		
		var minimumSize = list.getMinimumSize();
		minimumSize.width = 300;
		list.setMinimumSize(minimumSize);
			
		pane.add(list);
		
		ModuleBox box = new ModuleBox(engine);
		pane.add(box);
		
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					var factory = list.getSelectedValue();
					if (factory != null)
						engine.createModule(factory).ifPresent(box::addModule);
				}
			}
		});
		
		add(pane, BorderLayout.CENTER);
	}

	private void toggleSimulation()
	{
		if (engine.isRunning())
			engine.stop();
		else
			engine.start();
	}

	private void updateSimulationButton(JToggleButton button)
	{
		button.setSelected(engine.isRunning());
		if (engine.isRunning())
			button.setText("Stop Simulation");
		else
			button.setText("Start Simulation");
	}
	
	public void registerModule(ModuleFactory factory)
	{
		moduleList.addElement(factory);
		logger.info("Module " + factory.getName() + " was added");
	}
}



















