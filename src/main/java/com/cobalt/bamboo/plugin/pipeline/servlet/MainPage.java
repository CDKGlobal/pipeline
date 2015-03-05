package com.cobalt.bamboo.plugin.pipeline.servlet;

import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import com.cobalt.bamboo.plugin.pipeline.cache.CacheManager;
import com.cobalt.bamboo.plugin.pipeline.cache.WallBoardData;
import com.cobalt.bamboo.plugin.pipeline.domain.model.Change;
import com.cobalt.bamboo.plugin.pipeline.domain.model.PerformanceSummary;
import com.cobalt.bamboo.plugin.pipeline.domain.services.PlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class MainPage extends HttpServlet{

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;
    private final CacheManager cacheManager;
    private final PlanService planService;
   
    public MainPage(UserManager userManager, LoginUriProvider loginUriProvider,  TemplateRenderer renderer,
                    CacheManager cacheManager, PlanService planService)
    {
      this.userManager = userManager;
      this.loginUriProvider = loginUriProvider;
      this.renderer = renderer;
      this.cacheManager = cacheManager;
        this.planService = planService;
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
		  List<WallBoardData> resultList = cacheManager.getAllWallBoardData();
		  String json = writer.writeValueAsString(resultList);
		  response.setContentType("application/json;charset=utf-8");
		  response.getWriter().write(json);
	  } else if (query.equalsIgnoreCase("changes") && request.getParameter("plankey") != null){
          List<Change> changeList = planService.getChangeListForPlan(request.getParameter("plankey"));
          ObjectWriter writer = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
          String json = writer.writeValueAsString(changeList);
		  response.setContentType("application/json;charset=utf-8");
		  response.getWriter().write(json);
	  } else if (query.equalsIgnoreCase("completions") && request.getParameter("plankey") != null){
          PerformanceSummary performance = planService.getPerformanceStatsForPlan(request.getParameter("plankey"));
          ObjectWriter writer = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
          String json = writer.writeValueAsString(performance);
		  response.setContentType("application/json;charset=utf-8");
		  response.getWriter().write(json);
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
