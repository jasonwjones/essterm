#!/usr/bin/env bash
# Builds (unless --no-build) and launches essterm for local smoke testing.
set -euo pipefail

cd "$(dirname "$0")"

if [[ "${1:-}" != "--no-build" ]]; then
	mvn -q package
fi

exec java -jar target/essterm-0.0.1-SNAPSHOT.jar
