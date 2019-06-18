package be.seeseemelk.easydsp.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

import be.seeseemelk.easydsp.modules.Module;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private DefaultListModel<String> moduleList = new DefaultListModel<>();
	private List<Class<? extends Module>> modules = new ArrayList<>();
	
	public MainWindow()
	{
		setTitle("EasyDSP");
		setSize(800, 600);
		setLocationByPlatform(true);
		
		initMenubar();
		initContent();
	}
	
	private void initMenubar()
	{
		JMenuBar menubar = new JMenuBar();
		
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('f');
		menubar.add(menuFile);
		
		JMenuItem btnExit = new JMenuItem("Exit");
		btnExit.setMnemonic('e');
		btnExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});
		menuFile.add(btnExit);
		
		JMenuItem btnHelp = new JMenuItem("Help");
		menuFile.add(btnHelp);
		
		setJMenuBar(menubar);
	}
	
	private void initContent()
	{
		JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		JList<String> list = new JList<String>(moduleList);
		list.setModel(moduleList);
			
		pane.add(list);
		
		ModuleBox box = new ModuleBox();
		pane.add(box);
		
		list.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					int selected = list.getSelectedIndex();
					if (selected != -1)
					{
						try
						{
							Module module = modules.get(selected).newInstance();
							box.addModule(module);
						}
						catch (IllegalAccessException | InstantiationException exception)
						{
							exception.printStackTrace();
						}
					}
				}
			}
		});
		
		add(pane);
	}
	
	public void registerModule(Class<? extends Module> module, String moduleName)
	{
		moduleList.addElement(moduleName);
		modules.add(module);
		System.out.println("New module " + moduleName + " added");
	}
}



















