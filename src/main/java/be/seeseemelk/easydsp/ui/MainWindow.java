package be.seeseemelk.easydsp.ui;

import be.seeseemelk.easydsp.Engine;
import be.seeseemelk.easydsp.modules.ModuleFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger("MainWindow");
	private final Engine engine;
	private ModuleBox box;
	
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

		var modules = createModuleList();
			
		pane.add(modules);
		
		box = new ModuleBox(engine);
		pane.add(box);
		
		add(pane, BorderLayout.CENTER);
	}

	private JComponent createModuleList()
	{
		var model = new ModuleTreeModel(engine);

		var tree = new JTree();
		tree.setModel(model);
		tree.setRootVisible(false);
		for (int i = model.getChildCount(model.getRoot()) - 1; i >= 0; i--)
			tree.expandRow(i);

		tree.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					var object = tree.getLastSelectedPathComponent();
					if (model.isLeaf(object))
					{
						engine.createModule((ModuleFactory) object).ifPresent(box::addModule);
					}
				}
			}
		});

		var minimumSize = tree.getMinimumSize();
		minimumSize.width = 200;
		tree.setMinimumSize(minimumSize);

		return tree;
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
}



















