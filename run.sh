#!/usr/bin/env bash
# Builds (unless --no-build) and launches essterm for local smoke testing.
#
# Flags (any order):
#   --no-build   skip the mvn package step, just run the jar already in target/
#   --swing      force Lanterna's Swing terminal emulator window instead of the real
#                terminal - useful when you want a screenshot-able window, or when running
#                somewhere a real TTY isn't available/reliable
set -euo pipefail

cd "$(dirname "$0")"

build=1
java_args=()

for arg in "$@"; do
	case "$arg" in
	--no-build)
		build=0
		;;
	--swing)
		java_args+=("-Dessterm.swing=true")
		;;
	*)
		echo "Unknown flag: $arg" >&2
		exit 1
		;;
	esac
done

if [[ "$build" -eq 1 ]]; then
	mvn -q package
fi

# Discovered rather than hardcoded so this doesn't need editing on every version bump.
jar_files=(target/essterm-*.jar)
if [[ ${#jar_files[@]} -ne 1 ]]; then
	echo "Expected exactly one target/essterm-*.jar, found ${#jar_files[@]} - build first?" >&2
	exit 1
fi
jar="${jar_files[0]}"

# Not "${java_args[@]}" directly: macOS ships bash 3.2 (the last GPLv2 release) as /bin/bash, and in
# that version expanding an empty array under `set -u` throws "unbound variable" instead of nothing.
if [[ ${#java_args[@]} -gt 0 ]]; then
	exec java "${java_args[@]}" -jar "$jar"
else
	exec java -jar "$jar"
fi
