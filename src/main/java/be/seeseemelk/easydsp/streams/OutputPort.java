package be.seeseemelk.easydsp.streams;

/**
 * An output port allows to send back audio samples to a destination.
 */
@FunctionalInterface
public interface OutputPort
{
	boolean read(float[] buffer);
}
