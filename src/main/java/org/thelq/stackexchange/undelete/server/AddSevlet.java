/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thelq.stackexchange.undelete.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.thelq.stackarchive.bot.se.SEAPI;
import org.thelq.stackexchange.undelete.Controller;
import org.thelq.stackexchange.undelete.question.QuestionEntry;

/**
 *
 * @author Leon
 */
@Slf4j
public class AddSevlet extends HttpServlet {
	protected final Controller controller;
	protected final ObjectMapper jsonMapper;
	public AddSevlet(Controller controller) {
		this.controller = controller;
		jsonMapper = new ObjectMapper();
		jsonMapper.registerModule(new JodaModule());
		jsonMapper.registerModule(new GuavaModule());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String questionsRaw = req.getParameter("questions");
		if(StringUtils.isBlank(questionsRaw))
			throw new ServletException("No questions specified");
		
		AddRequest addRequest = jsonMapper.readValue(questionsRaw, AddRequest.class);
		for(QuestionEntry curQuestion : addRequest.getQuestions()) {
			
			log.debug("Question id: " + curQuestion);
		}
			
	}
	
	@Getter
	public static class AddRequest {
		protected ImmutableList<QuestionEntry> questions;
		
	}
}
