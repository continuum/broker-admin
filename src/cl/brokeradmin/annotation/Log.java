/*
 * Creado 10-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
	
	// level of log (info, debug)
	String level() default "info";
	// if show the parameters in the log
	boolean args() default false;
	
}
