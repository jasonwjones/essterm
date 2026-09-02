package com.jasonwjones.essterm.essgrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appliedolap.essbase.EssCube;
import com.appliedolap.essbase.EssServer;
import com.appliedolap.essbase.impl.EssServerImpl;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGridFactory;
import com.jasonwjones.essterm.grid.MemberInfoResolver;
import com.jasonwjones.essterm.model.ChosenConnection;

/**
 * REST analog of {@link EssbaseEssGridFactory}, backed by essbase-rest-client.
 */
public class RestEssGridFactory implements EssGridFactory {

	private static final Logger logger = LoggerFactory.getLogger(RestEssGridFactory.class);

	@Override
	public EssGrid createEssGrid(ChosenConnection connection) {
		logger.info("Connecting to Essbase REST API at {}", connection.getServer());
		EssServer server = new EssServerImpl(connection.getServer(), connection.getUsername(), connection.getPassword());
		EssCube cube = server.getApplication(connection.getApplication()).getCube(connection.getCube());
		return new RestEssGrid(connection, cube);
	}

	@Override
	public MemberInfoResolver getMemberInfoResolver() {
		return null;
	}

}
