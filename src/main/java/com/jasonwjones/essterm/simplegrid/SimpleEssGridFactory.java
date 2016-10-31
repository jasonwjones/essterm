package com.jasonwjones.essterm.simplegrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGridFactory;
import com.jasonwjones.essterm.grid.MemberInfoResolver;
import com.jasonwjones.essterm.model.ChosenConnection;

public class SimpleEssGridFactory implements EssGridFactory {

	private static final Logger logger = LoggerFactory.getLogger(SimpleEssGridFactory.class);
	
	@Override
	public EssGrid createEssGrid(ChosenConnection connection) {
		logger.info("Creating EssGrid");
		return new SimpleEssGrid(10, 5);
	}

	@Override
	public MemberInfoResolver getMemberInfoResolver() {
		throw new RuntimeException("Not implemented");
	}

}
