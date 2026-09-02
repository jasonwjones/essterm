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
public class EssbaseConnectionResolver implements ConnectionResolver {

	private static final Logger logger = LoggerFactory.getLogger(EssbaseConnectionResolver.class);

	private IEssbase essbase;

	private IEssOlapServer server;

	private boolean isFirstSignOn = true;

	public EssbaseConnectionResolver() throws EssException {
		logger.info("Instantiating Essbase connection resolver");
		this.essbase = IEssbase.Home.create(IEssbase.JAPI_VERSION);
		
//		Yaml yaml = new Yaml();
//		try {
//			InputStream input = new FileInputStream(new File("/Users/jasonwjones/Development/Jaybase/jaybase-test-suite/src/main/resources/sample-basic.yaml"));
//			CubeServer cubeServer = yaml.loadAs(input, CubeServer.class);
//			JaybaseYamlBuilder builder = new JaybaseYamlBuilder();
//			this.essbase = builder.build(cubeServer);
//		} catch (FileNotFoundException e) {
//			throw new EssException(e);
//		}		
	}

	@Override
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

		try {
			disconnect();
			this.server = this.essbase.signOn(username, password, false, null, "embedded", server);
			List<String> apps = IteratorUtil.iteratorToList(this.server.getApplications(),
					new ConversionDelegate<IEssOlapApplication, String>() {
						@Override
						public String convert(IEssOlapApplication from) throws EssException {
							return from.getName();
						}
					});
			return apps;
		} finally {
			if (isFirstSignOn) {
				System.setOut(out);
				System.setErr(err);
				isFirstSignOn = false;
			}
		}
		
	}

	@Override
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

	@Override
	public void disconnect() throws EssException {
		if (this.server != null && this.server.isConnected()) {
			logger.info("Disconnecting Essbase server");
			this.server.disconnect();
		}
		if (this.essbase.isSignedOn()) {
			this.essbase.signOff();	
		}
	}
	
}
