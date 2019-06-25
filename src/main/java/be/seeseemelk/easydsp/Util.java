package be.seeseemelk.easydsp;

public class Util
{
	public static void fmul(float[] arr, float mul)
	{
		for (int i = 0; i < arr.length; i++)
			arr[i] *= mul;
	}
}
