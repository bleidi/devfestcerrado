package me.bleidi.devfestcerrado;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="pic", urlPatterns="/api/pic")
public class PicServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2886064304808175598L;

	private static final Logger log = Logger.getLogger(PicServlet.class.getCanonicalName());
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info(" ===> request");
		resp.setStatus(200);
		resp.setContentType("application/json");
		PrintWriter writer = resp.getWriter();
		writer.write("{\"url\" : \"sample\"}");
		writer.flush();
		//super.doPost(req, resp);
	}
	
}
