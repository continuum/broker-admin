/*
 * Creado 12-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.web.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class JsonHandlerExceptionResolver implements HandlerExceptionResolver {
	
	// class log
	protected static final Log LOG = LogFactory.getLog(JsonHandlerExceptionResolver.class);

	/**
	 * 
	 */
	public JsonHandlerExceptionResolver() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public ModelAndView resolveException(HttpServletRequest req,
			HttpServletResponse resp, Object handler, final Exception ex) {
		LOG.error(ex);
		return new ModelAndView(new View() {
			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.View#getContentType()
			 */
			public String getContentType() {
				return "text/json";
			}
			/* (non-Javadoc)
			 * @see org.springframework.web.servlet.View#render(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
			 */
			public void render(Map model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
				resp.setCharacterEncoding("utf-8");
				resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				resp.getWriter().write(new JSONObject().put("success", false).put("errorMsg", ex.getMessage()).toString());
			}
		});
	}

}
