package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.atlassian.bamboo.builder.BuildState;
import com.atlassian.bamboo.builder.LifeCycleState;
import com.atlassian.bamboo.event.AgentConfigurationUpdatedEvent;
import com.atlassian.bamboo.event.AgentDisabledOrEnabledEvent;
import com.atlassian.bamboo.event.AllAgentsUpdatedEvent;
import com.atlassian.bamboo.event.BambooErrorEvent;
import com.atlassian.bamboo.event.BuildCanceledEvent;
import com.atlassian.bamboo.event.BuildCommentDeletedEvent;
import com.atlassian.bamboo.event.BuildCommentedEvent;
import com.atlassian.bamboo.event.BuildCompletedEvent;
import com.atlassian.bamboo.event.BuildConfigurationUpdatedEvent;
import com.atlassian.bamboo.event.BuildDeletedEvent;
import com.atlassian.bamboo.event.BuildFinishedEvent;
import com.atlassian.bamboo.event.BuildHungEvent;
import com.atlassian.bamboo.event.BuildQueueTimeoutEvent;
import com.atlassian.bamboo.event.BuildRequirementUpdatedEvent;
import com.atlassian.bamboo.event.BuildResultDeletedEvent;
import com.atlassian.bamboo.event.BuildResultEvent;
import com.atlassian.bamboo.event.BuildStateResultEvent;
import com.atlassian.bamboo.event.ChainCompletedEvent;
import com.atlassian.bamboo.event.ChainCreatedEvent;
import com.atlassian.bamboo.event.ChainDeletedEvent;
import com.atlassian.bamboo.event.ChainResultDeletedEvent;
import com.atlassian.bamboo.event.ChainResultEvent;
import com.atlassian.bamboo.event.ChainUpdatedEvent;
import com.atlassian.bamboo.event.DeletionFinishedEvent;
import com.atlassian.bamboo.event.ElasticConfigUpdatedEvent;
import com.atlassian.bamboo.event.HibernateEventListener;
import com.atlassian.bamboo.event.JobCompletedEvent;
import com.atlassian.bamboo.event.ManualStageResumedEvent;
import com.atlassian.bamboo.event.MultipleChainsDeletedEvent;
import com.atlassian.bamboo.event.MultipleJobsDeletedEvent;
import com.atlassian.bamboo.event.PlanSuspensionRequestedEvent;
import com.atlassian.bamboo.event.ResultLabelAddedEvent;
import com.atlassian.bamboo.event.ResultLabelRemovedEvent;
import com.atlassian.bamboo.event.StageCompletedEvent;
import com.atlassian.bamboo.event.agent.AgentAssignmentsUpdatedEvent;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.v2.build.CurrentlyBuilding;
import com.atlassian.bamboo.v2.build.events.AgentOfflineEvent;
import com.atlassian.bamboo.v2.build.events.BuildQueuedEvent;
import com.atlassian.bamboo.v2.build.events.BuildTriggeredEvent;
import com.atlassian.bamboo.v2.build.events.PostBuildCompletedEvent;
import com.atlassian.bamboo.v2.events.BuildCreatedEvent;
import com.atlassian.bamboo.v2.events.ChangeDetectionRequiredEvent;
import com.atlassian.event.Event;
import com.cobalt.bamboo.plugin.pipeline.customevent.BuildStartedEvent;

public class BuildActivityListener implements HibernateEventListener {

	@Override
	public Class[] getHandledEventClasses() {
		return new Class[]{BuildTriggeredEvent.class, BuildQueuedEvent.class, 
							BuildStartedEvent.class, BuildFinishedEvent.class, 
							StageCompletedEvent.class, ChainCompletedEvent.class};
	}

	@Override
	public void handleEvent(Event event) {
		// TODO: Get the plankey and update that plan in the cache.
		
		/* Do NOT delete for now. - 08/08/2014
		 * TODO
		 * For debugging pusposes, print event details to a file.
		String s = "";
		
		if (event instanceof BuildTriggeredEvent) {
			BuildTriggeredEvent e = (BuildTriggeredEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ BuildTiggeredEvent ]]\n";
			s += "Plankey: " + pk + "\n";
		} else if (event instanceof BuildQueuedEvent) {
			BuildQueuedEvent e = (BuildQueuedEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ BuildQueuedEvent ]]\n";
			s += "Plankey: " + pk + "\n";
		} else if (event instanceof BuildStartedEvent) {
			BuildStartedEvent e = (BuildStartedEvent) event;
			
			LifeCycleState ls = e.getLifeCycleState(); 
			BuildState state = e.getBuildState();
			int buildNum = e.getBuildNumber();
			PlanKey plankey = e.getPlanKey();

			s += "\n[[ BuildStartedEvent ]]\n";
			s += "Plankey: " + plankey;
			s += "\n Build #: " + buildNum;
			s += "\n LifeCycleState: " + ls;
			s += "\n Build state: " + state + "\n";
		} else if (event instanceof BuildFinishedEvent) {
			BuildFinishedEvent e = (BuildFinishedEvent) event;

			LifeCycleState ls = e.getLifeCycleState(); 
			BuildState state = e.getBuildState();
			int buildNum = e.getBuildNumber();
			PlanKey plankey = e.getPlanKey();

			s += "\n[[ BuildFinishedEvent ]]\n";
			s += "Plankey: " + plankey;
			s += "\n Build #: " + buildNum;
			s += "\n LifeCycleState: " + ls;
			s += "\n Build state: " + state + "\n";
		} else if (event instanceof StageCompletedEvent) {
			StageCompletedEvent e = (StageCompletedEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ StageCompletedEvent ]]\n";
			s += "Plankey: " + pk + "\n";
		} else if (event instanceof ChainCompletedEvent) {
			ChainCompletedEvent e = (ChainCompletedEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ ChainCompletedEvent ]]\n";
			s += "Plankey: " + pk + "\n";
		}
			
		try {
			FileWriter fw = new FileWriter("../../../test-build-activity-result.txt", true);
			fw.write(s);
			fw.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		*/
	}

}
