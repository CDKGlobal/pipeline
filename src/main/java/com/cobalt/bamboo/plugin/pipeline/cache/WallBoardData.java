package com.cobalt.bamboo.plugin.pipeline.cache;

import com.cobalt.bamboo.plugin.pipeline.domain.model.ProjectReport;
import com.cobalt.bamboo.plugin.pipeline.domain.model.UptimeGrade;

public class WallBoardData {
	public String planKey;
    public ProjectReport cdresult;
    public UptimeGrade uptimeGrade;

    public WallBoardData(String planKey, ProjectReport cdresult, UptimeGrade uptimeGrade) {
        this.planKey = planKey;
        this.cdresult = cdresult;
		this.uptimeGrade = uptimeGrade;
	}
}
