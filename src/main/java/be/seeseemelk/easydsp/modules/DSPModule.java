/**
 * 
 */
package be.seeseemelk.easydsp.modules;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
/**
 * Makes the module detectable and loadable by the EasyDSP engine.
 */
public @interface DSPModule
{
	/**
	 * The name of the module.
	 */
	String value();
}
