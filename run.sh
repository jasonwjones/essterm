#!/usr/bin/env bash
# Builds (unless --no-build) and launches essterm for local smoke testing.
set -euo pipefail

cd "$(dirname "$0")"

if [[ "${1:-}" != "--no-build" ]]; then
	mvn -q package
fi

# Spring Boot 1.4.1's CGLIB-based @Configuration proxying reflectively opens java.lang, which
# modern JDKs (17+) block by default without this flag. Remove once essterm is off this old
# Spring Boot version.
exec java --add-opens java.base/java.lang=ALL-UNNAMED -jar target/essterm-0.0.1-SNAPSHOT.jar
