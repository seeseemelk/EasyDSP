package be.seeseemelk.easydsp.ui;

import be.seeseemelk.easydsp.Engine;
import be.seeseemelk.easydsp.modules.ModuleFactory;
import be.seeseemelk.easydsp.modules.ModuleGroup;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ModuleTreeModel implements TreeModel
{
	private Engine engine;
	private List<TreeItem> items = new ArrayList<>();


	public ModuleTreeModel(Engine engine)
	{
		this.engine = engine;

		var map = new EnumMap<ModuleGroup, TreeItem>(ModuleGroup.class);
		for (var factory : engine.getModuleFactories())
		{
			var group = factory.getGroup();
			if (!map.containsKey(group))
				map.put(group, new TreeItem(group));

			map.get(group).factories.add(factory);
		}

		map.values().forEach(items::add);

		Logger.getAnonymousLogger().info("Found " + map.values().size() + " values");
	}

	@Override
	public Object getRoot()
	{
		return "Components";
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		if (parent instanceof String)
			return items.get(index);
		else
			return ((TreeItem) parent).factories.get(index);
	}

	@Override
	public int getChildCount(Object parent)
	{
		if (parent instanceof String)
			return items.size();
		else
			return ((TreeItem) parent).factories.size();
	}

	@Override
	public boolean isLeaf(Object node)
	{
		return !(node instanceof String || node instanceof TreeItem);
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue)
	{

	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		if (parent instanceof String)
			return items.indexOf(child);
		else
			return ((TreeItem) parent).factories.indexOf(child);
	}

	@Override
	public void addTreeModelListener(TreeModelListener l)
	{

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l)
	{

	}
}

class TreeItem
{
	final ModuleGroup group;
	List<ModuleFactory> factories = new ArrayList<>();

	TreeItem(ModuleGroup group)
	{
		this.group = group;
	}

	@Override
	public String toString()
	{
		return group.getName();
	}
}
