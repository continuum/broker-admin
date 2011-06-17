/*
 * Creado 25/02/2008
 *
 * $Id$
 *
 * Copyright jrodriguez (2007). All rights reserved.
 * Retain all ownership and intellectual property rights in
 * the programs and any source code.
 */
package cl.brokeradmin.web.views;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;

/**
 * @author jars - <a href="mailto:jorge.rodriguez.suarez at gmail.com">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class JsonView extends AbstractView {

	// class log
	protected static final Log LOG = LogFactory.getLog(JsonView.class);
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#getContentType()
	 */
	@Override
	public String getContentType() {
		return "text/json";
	}

	/**
	 * 
	 */
	public JsonView() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void renderMergedOutputModel(Map model, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		Object json = (Object) model.get("json");
		if (LOG.isInfoEnabled()) {
			LOG.info("Rendering " + json + " como response del requerimiento");
		}
		if (null != json) {
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(json.toString());
		}
	}

}
