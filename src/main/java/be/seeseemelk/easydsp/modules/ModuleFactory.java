package be.seeseemelk.easydsp.modules;

import be.seeseemelk.easydsp.Engine;

import java.lang.reflect.InvocationTargetException;

public class ModuleFactory
{
	private final Class<? extends Module> module;
	private final String name;
	private final ModuleGroup group;
	
	public ModuleFactory(Class<? extends Module> module)
	{
		this.module = module;
		this.name = module.getAnnotation(DSPModule.class).value();
		this.group = module.getAnnotation(DSPModule.class).group();
	}

	public Class<? extends Module> getModule()
	{
		return module;
	}

	public String getName()
	{
		return name;
	}

	public ModuleGroup getGroup()
	{
		return group;
	}

	public Module create(Engine engine, int id)
	{
		try
		{
			Module instance = module.getDeclaredConstructor().newInstance();
			instance.setEngine(engine);
			instance.setName(name);
			instance.setId(id);
			return instance;
		}
		catch (NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException e)
		{
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString()
	{
		return name;
	}
}
