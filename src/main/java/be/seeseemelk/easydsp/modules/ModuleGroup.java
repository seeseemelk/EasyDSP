package be.seeseemelk.easydsp.modules;

public enum ModuleGroup
{
	BASIC("Basic"),
	GENERATOR("Generators"),
	EFFECT("Effects"),
	OTHER("Other");

	private final String name;

	ModuleGroup(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
