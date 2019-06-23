package be.seeseemelk.easydsp.ui;

import be.seeseemelk.easydsp.modules.Module;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class PropertiesWindow extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private Module module;

	public PropertiesWindow(Component parent, Module module)
	{
		setSize(400, 500); //50 * (module.getOptions().size()+1));

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
		setType(Type.NORMAL);
		setResizable(false);
		
		this.module = module;
		
		setTitle("Properties of " + module.getName());

		MigLayout layout = new MigLayout("fill, insets 8px", "[]3[grow]");
		setLayout(layout);

		var description = new JLabel("<html>" + module.getDescription() + "</html>");
		add(description, "north, gap 8 8 8 20");

		initOptions();

		JButton close = new JButton("Close");
		close.addActionListener(e -> dispose());
		add(close, "south, gap 8 8 20 8");
	}
	
	private void initOptions()
	{
		for (JComponent component : module.getOptions())
		{
			String name = module.getOptionName(component);
			JLabel label = new JLabel(name);
			
			add(label);
			add(component, "wrap, growx");
		}
	}
}
























