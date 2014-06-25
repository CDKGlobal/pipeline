package com.cobalt.cdpipeline.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.project.ProjectManager;
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
   
    public MainPage(UserManager userManager, LoginUriProvider loginUriProvider,  TemplateRenderer renderer,
    				ProjectManager projectManager, PlanManager planManager, ResultsSummaryManager resultsSummaryManager)
    {
      this.userManager = userManager;
      this.loginUriProvider = loginUriProvider;
      this.renderer = renderer;
      this.mainManager = new MainManager(projectManager, planManager, resultsSummaryManager);
    }
   
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
      String username = userManager.getRemoteUsername(request);
      if (username == null || !userManager.isSystemAdmin(username))
      {
        redirectToLogin(request, response);
        return;
      }
      
      response.setContentType("text/html;charset=utf-8");
      renderer.render("cdpipeline.vm", response.getWriter());
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