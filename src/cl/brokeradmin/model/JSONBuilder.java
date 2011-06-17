/*
 * Creado 15-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.model;

import org.json.JSONObject;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public interface JSONBuilder {
	
	/**
	 * @return
	 */
	JSONObject toJson(Object target);

}
