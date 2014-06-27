package com.cobalt.cdpipeline.cdresult;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AddAllAuthorsInCommitsTest.class, PipelineStagesTest.class,
		SetCurrentBuildInfoTest.class, SetLastDeploymentInfoTest.class })
public class CDResultFactoryTest {
	
}
