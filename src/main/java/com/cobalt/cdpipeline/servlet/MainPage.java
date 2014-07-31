package com.cobalt.cdpipeline.servlet;

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
import com.atlassian.bamboo.plan.ExecutionStatus;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanExecutionManager;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.plan.TopLevelPlan;
import com.atlassian.bamboo.progressbar.ProgressBar;
import com.atlassian.bamboo.progressbar.ProgressBarImpl;
import com.atlassian.bamboo.resultsummary.ResultsSummary;
import com.atlassian.bamboo.resultsummary.ResultsSummaryManager;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.cobalt.cdpipeline.Controllers.MainManager;
import com.cobalt.cdpipeline.cdresult.CDResult;

public class MainPage extends HttpServlet{
	private static final Logger log = LoggerFactory.getLogger(MainPage.class);
    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;
    private final MainManager mainManager;
    
    /*
     * TODO: Debugging info for build progress. Do not delete for now. (07/31/14)
    private final PlanExecutionManager planExecutionManager;
    private final PlanManager planManager;
    private final ResultsSummaryManager resultsSummaryManager;
    */
   
    public MainPage(UserManager userManager, LoginUriProvider loginUriProvider,  TemplateRenderer renderer,
    				PlanManager planManager, ResultsSummaryManager resultsSummaryManager,
    				JiraApplinksService jiraApplinksService, PlanExecutionManager planExecutionManager)
    {
      this.userManager = userManager;
      this.loginUriProvider = loginUriProvider;
      this.renderer = renderer;
      this.mainManager = new MainManager(planManager, resultsSummaryManager, jiraApplinksService, planExecutionManager);
      
      /*
       * TODO: Debugging info for build progress. Do not delete for now. (07/31/14)
      this.planExecutionManager = planExecutionManager;
      this.planManager = planManager;
      this.resultsSummaryManager = resultsSummaryManager;
      */
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
	  
	  List<CDResult> resultList = mainManager.getCDResults();
	  String query = request.getParameter("type");
	  
	  /*
	   * TODO: Debugging info for build progress. Do not delete for now. (07/31/14)
	  if (query != null && query.equals("progress")) {
		  String s = "";
		  s += "PlanExecutionManger == null: " + (planExecutionManager == null) + "<br>";
		  
		  if (planExecutionManager != null) {
			  List<TopLevelPlan> plans = planManager.getAllPlans(TopLevelPlan.class);
			  for (Plan plan : plans) {
				  s += "<strong>Plan: " + plan.getName() + "</strong><br>";
				  List<ResultsSummary> buildList = resultsSummaryManager.getResultSummariesForPlan(plan, 0, 0);
				  s += "build list size: " + buildList.size() + "<br>";
				  if (buildList.size() > 0) {
					  ExecutionStatus exstatus = planExecutionManager.getExecutionStatus(buildList.get(0).getPlanResultKey());
					  s += "build: #" + buildList.get(0).getBuildNumber() + "<br>";
					  s += "ExecutionStatus == null: " + (exstatus == null) + "<br>";
					  if (exstatus != null) {
						  ProgressBar progress = new ProgressBarImpl(exstatus);
						  s += "Estimated Duration: " + progress.getEstimatedDuration() + "<br>";
						  s += "Percentage Completed: " + progress.getPercentageCompletedAsString() + "<br>";
						  s += "Time Remaining: " + progress.getPrettyTimeRemaining() + "<br>";
						  s += "Time Remaining (long): " + progress.getPrettyTimeRemaining(true) + "<br>";
						  s += "Time Remaining (short): " + progress.getPrettyTimeRemaining(false) + "<br>";
					  }
				  }
				  s += "<br>";
			  }
		  }
		  
		  response.setContentType("text/html");
		  response.getWriter().write(s);
	  } else */
	  if (query == null || !query.equalsIgnoreCase("json")) {
		  // Normal case: normal table page
		  
		  Map<String, Object> context =  new HashMap<String, Object>();
		  context.put("results", resultList);
		  response.setContentType("text/html;charset=utf-8");
		  renderer.render("cdpipeline.vm", context, response.getWriter());
	  } else {
		  // Special Case: JSON request
		  ObjectWriter writer = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
		  String json = writer.writeValueAsString(resultList);
		  response.setContentType("application/json;charset=utf-8");
		  response.getWriter().write(json);
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
