package com.jasonwjones.essterm.essbase;

import java.util.List;

/**
 * Resolves the applications and cubes available on a server for a given login, independent of
 * whether the underlying connection is made via the Essbase Java API or the Essbase REST API.
 */
public interface ConnectionResolver {

	List<String> getApplications(String server, String username, String password) throws Exception;

	List<String> getCubes(String application) throws Exception;

	void disconnect() throws Exception;

}
