package com.cobalt.bamboo.plugin.pipeline.Controllers;

import com.atlassian.bamboo.event.ChainCompletedEvent;
import com.atlassian.bamboo.event.HibernateEventListener;
import com.atlassian.bamboo.v2.build.events.BuildQueuedEvent;
import com.atlassian.event.Event;

/**
 * A custom event listener that listens to build activities: triggered, queued, 
 * started, finished, stage completed, and plan completed.
 */
public class BuildActivityListener implements HibernateEventListener {
	private CacheManager cacheManager;
	
	public BuildActivityListener(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@Override
	public Class[] getHandledEventClasses() {
		return new Class[]{BuildQueuedEvent.class, ChainCompletedEvent.class};
	}

	@Override
	public void handleEvent(Event event) {
		
		if (event instanceof BuildQueuedEvent) {
			BuildQueuedEvent e = (BuildQueuedEvent) event;
			
			// the given plan key is in the format of "PROJECT-PLAN-JOB"
			// need to strip to just "PROJECT-PLAN"
			String givenPlanKey = e.getPlanKey().getKey();
			int endIndex = givenPlanKey.lastIndexOf("-");
			final String planKey = givenPlanKey.substring(0, endIndex);
			
			cacheManager.updateCDResultForPlan(planKey);
			
		} else if (event instanceof ChainCompletedEvent) {
			ChainCompletedEvent e = (ChainCompletedEvent) event;
			
			// the given plan key is in the right format (PROJECT-PLAN)
			cacheManager.updateCDResultForPlan(e.getPlanKey().getKey());
		}
	}
}
