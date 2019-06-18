package be.seeseemelk.easydsp.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

import be.seeseemelk.easydsp.modules.Module;

public class PropertiesWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private Module module;

	public PropertiesWindow(Module module)
	{
		setSize(400, 100 * (module.getOptions().size()+1));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationByPlatform(true);
		
		this.module = module;
		
		setTitle("Properties of " + module.getName());
		
		GridLayout layout = new GridLayout(0, 2, 5, 5);
		
		setLayout(layout);
		
		initOptions();
		
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		add(close);
	}
	
	private void initOptions()
	{
		for (JComponent component : module.getOptions())
		{
			String name = module.getOptionName(component);
			JLabel label = new JLabel(name);
			
			add(label);
			add(component);
		}
	}
}
























