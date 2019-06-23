package be.seeseemelk.easydsp;

import be.seeseemelk.easydsp.modules.Module;
import be.seeseemelk.easydsp.modules.ModuleFactory;
import be.seeseemelk.easydsp.modules.RunnableModule;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Engine
{
	private Logger logger = Logger.getLogger("DSPEngine");
	private List<Module> modules = new ArrayList<>();
	private List<RunnableModule> runnableModules = new LinkedList<>();
	private int nextModuleId = 0;
	private double runtime = 0;
	private boolean running = false;
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private Timer timer;
	private float sampleRate = 44100f;
	private long executionInterval = 10;
	private final AudioFormat format = new AudioFormat(sampleRate, 24, 1, true, false);

	public void removeModule(Module module)
	{
		module.delete();
		modules.remove(module);
		if (module instanceof RunnableModule)
			runnableModules.remove(module);
	}

	public Optional<Module> createModule(ModuleFactory factory)
	{
		var module = factory.create(this, nextModuleId++);
		try
		{
			module.init();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.severe("Failed to instaniatie module '" + factory.getName() + "': " + e.getMessage());
			return Optional.empty();
		}
		modules.add(module);
		if (module instanceof RunnableModule)
			runnableModules.add((RunnableModule) module);
		return Optional.of(module);
	}

	/**
	 * The number of samples per second to play.
	 * @return The number of samples per second to play.
	 */
	public float getSampleRate()
	{
		return sampleRate;
	}

	public long getExecutionInterval()
	{
		return executionInterval;
	}

	public long getSamplesPerExecution()
	{
		return 4096;
	}

	public AudioFormat getFormat()
	{
		return format;
	}

	public double getRuntime()
	{
		return runtime;
	}

	public void start()
	{
		running = true;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				execute();
			}
		}, executionInterval, executionInterval);
	}

	public void stop()
	{
		running = false;
		timer.cancel();
	}

	public boolean isRunning()
	{
		return running;
	}

	private void execute()
	{
		try
		{
			for (var module : runnableModules)
			{
				module.run();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.severe("An exception occured: " + e.getMessage());
		}
	}
}
