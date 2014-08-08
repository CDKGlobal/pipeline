package com.cobalt.bamboo.plugin.pipeline.customevent;

import com.atlassian.bamboo.event.BuildStateResultEvent;
import com.atlassian.bamboo.builder.BuildState;
import com.atlassian.bamboo.builder.LifeCycleState;
import com.atlassian.bamboo.plan.PlanResultKey;
import com.atlassian.event.api.AsynchronousPreferred;

import org.apache.log4j.Logger;

/**
 * Fired when BuildStartedEventKicker (Pre-Build Action) is called right before a
 * job/build is run.
 */
@AsynchronousPreferred
public class BuildStartedEvent extends BuildStateResultEvent {
	private static final Logger log = Logger.getLogger(BuildStartedEvent.class);
    // ------------------------------------------------------------------------------------------------------- Constants
    // ------------------------------------------------------------------------------------------------- Type Properties
    // ---------------------------------------------------------------------------------------------------- Dependencies
    // ---------------------------------------------------------------------------------------------------- Constructors

    public BuildStartedEvent(final Object source, PlanResultKey planResultKey, final BuildState buildState, final LifeCycleState lifeCycleState)
    {
        super(source, planResultKey, buildState, lifeCycleState);
    }

    // ----------------------------------------------------------------------------------------------- Interface Methods
    // -------------------------------------------------------------------------------------------------- Action Methods
    // -------------------------------------------------------------------------------------------------- Public Methods
    // -------------------------------------------------------------------------------------- Basic Accessors / Mutators

}
