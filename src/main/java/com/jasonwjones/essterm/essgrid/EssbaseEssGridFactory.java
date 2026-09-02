package com.jasonwjones.essterm.essgrid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.essbase.api.base.EssException;
import com.essbase.api.datasource.IEssCube;
import com.essbase.api.datasource.IEssOlapServer;
import com.essbase.api.session.IEssbase;
import com.jasonwjones.essterm.grid.EssGrid;
import com.jasonwjones.essterm.grid.EssGridException;
import com.jasonwjones.essterm.grid.EssGridFactory;
import com.jasonwjones.essterm.grid.MemberInfoResolver;
import com.jasonwjones.essterm.model.ChosenConnection;

public class EssbaseEssGridFactory implements EssGridFactory {

	private static final Logger logger = LoggerFactory.getLogger(EssbaseEssGridFactory.class);
	
	@Override
	public EssGrid createEssGrid(ChosenConnection connection) {
		
		IEssbase essbase = null;
		IEssOlapServer olapServer = null;

		try {
			essbase = IEssbase.Home.create(IEssbase.JAPI_VERSION);
			olapServer = essbase.signOn(connection.getUsername(), connection.getPassword(), false, null, "embedded",
					connection.getServer());
			IEssCube cube = olapServer.getApplication(connection.getApplication()).getCube(connection.getCube());
			return new EssbaseEssGrid(connection, cube);
		} catch (EssException e) {
			logger.error("Error connecting to Essbase: {}", e);
			throw new EssGridException("Error connecting to Essbase server", e);
		} finally {
//			try {
//				logger.info("Signing off Essbase");
//				if (olapServer != null)
//					olapServer.disconnect();
//				essbase.signOff();
//			} catch (EssException e) {
//				logger.error("Error signing off Essbase: {}", e.getMessage());
//			}
		}
		
		
		// TODO Auto-generated method stub
//		return null;
	}

	// Revives the in-memory YAML-driven mock Essbase (Jaybase) for fast local dev/testing
	// without a live Essbase server. See JaybaseEssbaseConnectionResolver for the sign-on
	// side; needs org.jaybase:jaybase-essbase wired into pom.xml before this compiles again.
	/*
	public EssGrid createEssGrid_(ChosenConnection connection) {
		
		IEssbase essbase = null;
		IEssOlapServer olapServer = null;

		try {
			InputStream input = new FileInputStream(new File("/Users/jasonwjones/Development/Jaybase/jaybase-test-suite/src/main/resources/sample-basic.yaml"));
			Yaml yaml = new Yaml();
			CubeServer cubeServer = yaml.loadAs(input, CubeServer.class);
			JaybaseYamlBuilder builder = new JaybaseYamlBuilder();
			essbase = builder.build(cubeServer);

			//essbase = IEssbase.Home.create(IEssbase.JAPI_VERSION);
			olapServer = essbase.signOn(connection.getUsername(), connection.getPassword(), false, null, "embedded",
					connection.getServer());
			IEssCube cube = olapServer.getApplication(connection.getApplication()).getCube(connection.getCube());
			return new EssbaseEssGrid(connection, cube);
		} catch (Exception e) {
			logger.error("Error connecting to Essbase: {}", e);
			throw new EssGridException("Error connecting to Essbase server", e);
		} finally {
//			try {
//				logger.info("Signing off Essbase");
//				if (olapServer != null)
//					olapServer.disconnect();
//				essbase.signOff();
//			} catch (EssException e) {
//				logger.error("Error signing off Essbase: {}", e.getMessage());
//			}
		}
		
		
		// TODO Auto-generated method stub
//		return null;
	}*/
	
	@Override
	public MemberInfoResolver getMemberInfoResolver() {
		//return new 
		// TODO Auto-generated method stub
		return null;
	}
	

}
