/*
 * Creado 13-09-2008
 *
 * $Id$
 *
 * Copyright Continuum Ltda. (2008).
 *
 */
package cl.brokeradmin.web.views;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.servlet.view.AbstractView;

import cl.brokeradmin.model.LocalConfigManagerConnectionParameters;

/**
 * @author jars <a href="mailto:jorge.rodriguez at continuum.cl">Jorge Al. Rodriguez Suarez</a>
 *
 */
public class TreeJsonView extends AbstractView {

	/**
	 * 
	 */
	public TreeJsonView() {
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#getContentType()
	 */
	@Override
	public String getContentType() {
		return "text/json";
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		
		List<LocalConfigManagerConnectionParameters> params = (List<LocalConfigManagerConnectionParameters>) model.get("cmp");
		
		JSONArray tree = new JSONArray();
		for (LocalConfigManagerConnectionParameters c : params) {
			tree.add(new JSONObject().
					put("text", c.getName()).
					put("type", c.getClass().getName()));
		}
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(tree.toString());
	}

}
