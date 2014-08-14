package com.cobalt.bamboo.plugin.pipeline.cache;

import com.cobalt.bamboo.plugin.pipeline.cdperformance.Grade;
import com.cobalt.bamboo.plugin.pipeline.cdresult.CDResult;

public class WallBoardData {
	public String planKey;
	public CDResult cdresult;
	public Grade grade;
	
	public WallBoardData(String planKey, CDResult cdresult, Grade grade) {
		this.planKey = planKey;
		this.cdresult = cdresult;
		this.grade = grade;
	}
}
