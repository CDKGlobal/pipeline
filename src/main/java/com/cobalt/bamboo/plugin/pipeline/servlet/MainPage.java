package com.cobalt.bamboo.plugin.pipeline.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.atlassian.bamboo.applinks.JiraApplinksService;
import com.atlassian.bamboo.plan.PlanExecutionManager;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.cobalt.bamboo.plugin.pipeline.Controllers.MainManager;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;
import com.cobalt.bamboo.plugin.pipeline.changelist.Change;

public class MainPage extends HttpServlet{
	private static final Logger log = LoggerFactory.getLogger(MainPage.class);
    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;
    private final MainManager mainManager;
   
    public MainPage(UserManager userManager, LoginUriProvider loginUriProvider,  TemplateRenderer renderer,
    				PlanManager planManager, ResultsSummaryManager resultsSummaryManager,
    				JiraApplinksService jiraApplinksService, PlanExecutionManager planExecutionManager)
    {
      this.userManager = userManager;
      this.loginUriProvider = loginUriProvider;
      this.renderer = renderer;
      this.mainManager = new MainManager(planManager, resultsSummaryManager, jiraApplinksService, planExecutionManager);
    }
   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {

      // Redirect the user if user is not admin
	  String username = userManager.getRemoteUsername(request);
	  if (username == null)
	  {
	    redirectToLogin(request, response);
	    return;
	  }
	  
	  String query = request.getParameter("data");
	  
	  if (query == null) {  // TODO
		  // Normal case: normal table page
		  response.setContentType("text/html;charset=utf-8");
		  renderer.render("cdpipeline.vm", response.getWriter());
	  } else if (query.equalsIgnoreCase("all")) {
		  // Special Case: JSON request
		  ObjectWriter writer = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
		  List<CDResult> resultList = mainManager.getCDResults();
		  String json = writer.writeValueAsString(resultList);
		  response.setContentType("application/json;charset=utf-8");
		  response.getWriter().write(json);
	  } else if (query.equalsIgnoreCase("changes") && request.getParameter("plankey") != null){
		  List<Change> changeList = mainManager.getChangeListForPlan(request.getParameter("plankey"));
		  ObjectWriter writer = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
		  String json = writer.writeValueAsString(changeList);
		  response.setContentType("application/json;charset=utf-8");
		  response.getWriter().write(json);
	  } else if (query.equalsIgnoreCase("completions") && request.getParameter("plankey") != null){
		  
	  } else{
		  response.setContentType("text/html;charset=utf-8");
		  renderer.render("cdpipeline.vm", response.getWriter());
	  }
    }
    
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
      response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }
      
    private URI getUri(HttpServletRequest request)
    {
      StringBuffer builder = request.getRequestURL();
      if (request.getQueryString() != null)
      {
        builder.append("?");
        builder.append(request.getQueryString());
      }
      return URI.create(builder.toString());
    }

}
