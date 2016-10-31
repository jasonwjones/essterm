package com.jasonwjones.essterm.essbase;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.essbase.api.base.EssException;
import com.essbase.api.datasource.IEssCube;
import com.essbase.api.datasource.IEssOlapApplication;
import com.essbase.api.datasource.IEssOlapServer;
import com.essbase.api.session.IEssbase;
import com.saxifrages.essbase.util.ConversionDelegate;
import com.saxifrages.essbase.util.IteratorUtil;

@Component
public class EssbaseConnectionResolver {

	private static final Logger logger = LoggerFactory.getLogger(EssbaseConnectionResolver.class);

	private IEssbase essbase;

	private IEssOlapServer server;

	private boolean isFirstSignOn = true;
	
	public EssbaseConnectionResolver() throws EssException {
		logger.info("Instantiating Essbase connection resolver");
		this.essbase = IEssbase.Home.create(IEssbase.JAPI_VERSION);

	}

	public List<String> getApplications(String server, String username, String password) throws EssException {
		
		PrintStream out = null;
		PrintStream err = null;
		
		if (isFirstSignOn) {
			out = System.out;
			err = System.err;
			
			PrintStream ignore = new PrintStream(new ByteArrayOutputStream());				
			
			System.setOut(ignore);
			System.setErr(ignore);
		}
		
		disconnect();
		this.server = this.essbase.signOn(username, password, false, null, "embedded", server);
		List<String> apps = IteratorUtil.iteratorToList(this.server.getApplications(),
				new ConversionDelegate<IEssOlapApplication, String>() {
					@Override
					public String convert(IEssOlapApplication from) throws EssException {
						return from.getName();
					}
				});
		
		if (isFirstSignOn) {
			System.setOut(out);
			System.setErr(err);
			isFirstSignOn = false;
		}
		
		return apps;
	}

	public List<String> getCubes(String application) throws EssException {
		List<String> cubes = IteratorUtil.iteratorToList(this.server.getApplication(application).getCubes(),
				new ConversionDelegate<IEssCube, String>() {
					@Override
					public String convert(IEssCube from) throws EssException {
						return from.getName();
					}
				});
		return cubes;
	}

	public void disconnect() throws EssException {
		if (this.server != null && this.server.isConnected()) {
			logger.info("Disconnecting Essbase server");
			this.server.disconnect();
			this.essbase.signOff();
		}
	}

}
