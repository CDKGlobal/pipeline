package com.cobalt.bamboo.plugin.pipeline.Controllers;

import com.atlassian.bamboo.build.CustomPreBuildAction;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.event.api.EventPublisher;
import com.cobalt.bamboo.plugin.pipeline.customevent.BuildStartedEvent;

/**
 * A event kicker that publish BuildStartEvent right before a job/build is executed.
 */
public class BuildStartedEventKicker implements CustomPreBuildAction {
	EventPublisher eventPublisher;
	BuildContext buildContext;
	
	/**
	 * Constructs a BuildStartedEventKicker object.
	 * 
	 * @param eventPublisher to publish the event.
	 */
	public BuildStartedEventKicker(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	@Override
	public BuildContext call() throws InterruptedException, Exception {
		eventPublisher.publish(new BuildStartedEvent(this, buildContext.getPlanResultKey(), 
								buildContext.getBuildResult().getBuildState(), buildContext.getBuildResult().getLifeCycleState()));
		return buildContext;
	}

	@Override
	public void init(BuildContext buildContext) {
		this.buildContext = buildContext;
	}

	@Override
	public ErrorCollection validate(BuildConfiguration arg0) {
		return null;
	}

}
