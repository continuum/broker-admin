/*
 * Creado 10-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.aop.aspect;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
@Aspect
public class Log {
	
	// class log
	private org.apache.commons.logging.Log LOG = LogFactory.getLog(Log.class);
	
	// map level => int constant to use with switch sentence
	private static final Map<String, Integer> LEVEL;
	
	static {
		
		LEVEL = new HashMap<String, Integer>();
		LEVEL.put("info", SimpleLog.LOG_LEVEL_INFO);
		LEVEL.put("debug", SimpleLog.LOG_LEVEL_DEBUG);
		LEVEL.put("off", SimpleLog.LOG_LEVEL_OFF);
	}
	
	/**
	 * 
	 */
	public Log() {
		// TODO Auto-generated constructor stub
	}
	
	@Before("@annotation(log)")
	public void log(JoinPoint jp, cl.brokeradmin.annotation.Log log) {
		// find for a log
		StringBuffer sb = new StringBuffer(jp.getSignature().toLongString());
		if (log.args()) {
			sb.append(", parameters : (");
			for (int i = 0; i < jp.getArgs().length; i++, sb.append(", ")) {
				sb.append(jp.getArgs()[i]);
			}
			sb.append(")");
		}
		int level = LEVEL.get(log.level());
		switch (level) {
		case SimpleLog.LOG_LEVEL_INFO:
			LOG.info(sb);
			break;
		
		case SimpleLog.LOG_LEVEL_DEBUG:
			LOG.debug(sb);
			break;

		case SimpleLog.LOG_LEVEL_OFF:
			break;
			
		default:
			break;
		}
	}

}
