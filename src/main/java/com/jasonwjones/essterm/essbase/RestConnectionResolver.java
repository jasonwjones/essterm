package com.jasonwjones.essterm.essbase;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.appliedolap.essbase.EssApplication;
import com.appliedolap.essbase.EssCube;
import com.appliedolap.essbase.EssServer;
import com.appliedolap.essbase.impl.EssServerImpl;

/**
 * Resolves applications and cubes via the Essbase REST API (essbase-rest-client), instead of the
 * classic Java API. Note that the "server" passed to {@link #getApplications} is expected to be a
 * full REST endpoint base URL (e.g. <code>https://essbase.example.com/essbase</code>), not a bare
 * hostname like the Java API resolver accepts.
 *
 * <p>Both this and {@link EssbaseConnectionResolver} implement {@link ConnectionResolver}, so
 * callers that need to hold both side by side (like {@code EssTerm}, for the JAPI/REST toggle in
 * the connection dialog) must autowire each by its concrete type rather than by the shared
 * interface, to avoid an ambiguous-bean error.
 */
@Component
public class RestConnectionResolver implements ConnectionResolver {

	private static final Logger logger = LoggerFactory.getLogger(RestConnectionResolver.class);

	private EssServer server;

	@Override
	public List<String> getApplications(String server, String username, String password) throws Exception {
		logger.info("Signing on to Essbase REST API at {}", server);
		this.server = new EssServerImpl(server, username, password);

		List<String> applications = new ArrayList<>();
		for (EssApplication application : this.server.getApplications()) {
			applications.add(application.getName());
		}
		return applications;
	}

	@Override
	public List<String> getCubes(String application) throws Exception {
		List<String> cubes = new ArrayList<>();
		for (EssCube cube : server.getApplication(application).getCubes()) {
			cubes.add(cube.getName());
		}
		return cubes;
	}

	@Override
	public void disconnect() throws Exception {
		// Nothing to do: the REST API is authenticated per-request (Basic auth), with no persistent
		// connection on the client side to tear down the way the Java API's signOn/signOff requires.
	}

}
