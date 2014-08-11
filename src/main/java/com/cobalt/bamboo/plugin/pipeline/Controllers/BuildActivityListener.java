package com.cobalt.bamboo.plugin.pipeline.Controllers;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.atlassian.bamboo.builder.BuildState;
import com.atlassian.bamboo.builder.LifeCycleState;
import com.atlassian.bamboo.event.BuildFinishedEvent;
import com.atlassian.bamboo.event.ChainCompletedEvent;
import com.atlassian.bamboo.event.HibernateEventListener;
import com.atlassian.bamboo.event.StageCompletedEvent;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.v2.build.events.BuildQueuedEvent;
import com.atlassian.bamboo.v2.build.events.BuildTriggeredEvent;
import com.atlassian.event.Event;
import com.cobalt.bamboo.plugin.pipeline.customevent.BuildStartedEvent;

/**
 * A custom event listener that listens to build activities: triggered, queued, 
 * started, finished, stage completed, and plan completed.
 */
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
		 * For debugging pusposes, print event details to a file.*/
		String s = "";
		
		if (event instanceof BuildTriggeredEvent) {
			BuildTriggeredEvent e = (BuildTriggeredEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ BuildTiggeredEvent ]]\n";
			s += "Plankey: " + pk + "\n";
			s += "Partial PlanKey: " + pk.getPartialKey() + "\n";
			s += "Plan Result Key: " + e.getPlanResultKey() + "\n";
		} else if (event instanceof BuildQueuedEvent) {
			BuildQueuedEvent e = (BuildQueuedEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ BuildQueuedEvent ]]\n";
			s += "Plankey: " + pk + "\n";
			s += "Partial PlanKey: " + pk.getPartialKey() + "\n";
			s += "Plan Result Key: " + e.getPlanResultKey() + "\n";
		} else if (event instanceof BuildStartedEvent) {
			BuildStartedEvent e = (BuildStartedEvent) event;
			
			LifeCycleState ls = e.getLifeCycleState(); 
			BuildState state = e.getBuildState();
			int buildNum = e.getBuildNumber();
			PlanKey pk = e.getPlanKey();

			s += "\n[[ BuildStartedEvent ]]\n";
			s += "Plankey: " + pk;
			s += "Partial PlanKey: " + pk.getPartialKey() + "\n";
			s += "Plan Result Key: " + e.getPlanResultKey() + "\n";
			s += "\n Build #: " + buildNum;
			s += "\n LifeCycleState: " + ls;
			s += "\n Build state: " + state + "\n";
		} else if (event instanceof BuildFinishedEvent) {
			BuildFinishedEvent e = (BuildFinishedEvent) event;

			LifeCycleState ls = e.getLifeCycleState(); 
			BuildState state = e.getBuildState();
			int buildNum = e.getBuildNumber();
			PlanKey pk = e.getPlanKey();

			s += "\n[[ BuildFinishedEvent ]]\n";
			s += "Plankey: " + pk;
			s += "Partial PlanKey: " + pk.getPartialKey() + "\n";
			s += "Plan Result Key: " + e.getPlanResultKey() + "\n";
			s += "\n Build #: " + buildNum;
			s += "\n LifeCycleState: " + ls;
			s += "\n Build state: " + state + "\n";
		} else if (event instanceof StageCompletedEvent) {
			StageCompletedEvent e = (StageCompletedEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ StageCompletedEvent ]]\n";
			s += "Plankey: " + pk + "\n";
			s += "Partial PlanKey: " + pk.getPartialKey() + "\n";
			s += "Plan Result Key: " + e.getPlanResultKey() + "\n";
		} else if (event instanceof ChainCompletedEvent) {
			ChainCompletedEvent e = (ChainCompletedEvent) event;
			
			PlanKey pk = e.getPlanKey();
			
			s += "\n[[ ChainCompletedEvent ]]\n";
			s += "Plankey: " + pk + "\n";
			s += "Partial PlanKey: " + pk.getPartialKey() + "\n";
			s += "Plan Result Key: " + e.getPlanResultKey() + "\n";
		}
			
		try {
			FileWriter fw = new FileWriter("../../../test-build-activity-result.txt", true);
			fw.write(s);
			fw.close();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

}
