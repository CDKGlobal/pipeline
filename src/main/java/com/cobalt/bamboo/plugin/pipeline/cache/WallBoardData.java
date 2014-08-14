package com.cobalt.bamboo.plugin.pipeline.cache;

import com.cobalt.bamboo.plugin.pipeline.cdperformance.UptimeGrade;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class WallBoardData {
	public String planKey;
	public CDResult cdresult;
	public UptimeGrade grade;
	
	public WallBoardData(String planKey, CDResult cdresult, UptimeGrade grade) {
		this.planKey = planKey;
		this.cdresult = cdresult;
		this.grade = grade;
	}
}
