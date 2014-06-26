package com.cobalt.cdpipeline.Controllers;

import com.atlassian.bamboo.project.Project;
import com.atlassian.bamboo.project.ProjectManager;
import com.cobalt.cdpipeline.cdresult.CDResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainManagerTest {


    @Test( expected = IllegalArgumentException.class)
    public void testGetCDResultWithNullArgumentss() {
        MainManager main = new MainManager(null, null, null);
        List<CDResult> results = main.getCDResults();
    }

    @Test
    public void testDataIsWhatWeExpect() {
        Project project1 = mock(Project.class);
        Project project2 = mock(Project.class);

        when(project1.getName()).thenReturn("Project 1");
        when(project2.getName()).thenReturn("Project 2");

        Set<Project> projects = new HashSet<Project>();
        projects.add(project1);
        projects.add(project2);

        ProjectManager manager = mock(ProjectManager.class);
        when(manager.getAllProjects()).thenReturn(projects);

        //MainManager cut = ;
        int expectedCount = 3;
        int actualCount = manager.getAllProjects().size();
        assertEquals("The count was not what we expected", expectedCount, actualCount);
    }
}