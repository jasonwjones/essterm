# Essterm

Essterm is a text mode Java runnable JAR file that connects to and queries Essbase cubes. It can 
be thought of as a terminal based version of the classic Essbase Excel add-in or Smart View. It
uses a pure-Java library to create a text mode interface that works over a terminal. 


## Building

Run mvn package

## Running

`./run.sh` builds and launches essterm for local smoke testing (pass `--no-build` to skip the
build and just run the existing jar). It launches with `--add-opens java.base/java.lang=ALL-UNNAMED`,
required because Spring Boot 1.4.1's `@Configuration` class proxying needs reflective access that
modern JDKs (17+) block by default - drop this flag once essterm is off that old Spring Boot version.

App logging goes to `testFile.log`, not the console, by default - see the note in logback.xml.
Lanterna owns the terminal's cursor positioning while the app runs, so any concurrent console
output visually corrupts the screen.

## Notes

### Most-Specific-Wins Formatting

A formatting technique that can be thought of as Cascading Style Sheets for an multi-dimensional 
data set. For example, some general formatting rules that are useful:

 * Two decimals globally
 * No decimals for Actual and Budget
 * Something about a measure

## Roadmap / Feature Ideas

 * Query operations in a separate thread
 * Add member selector
 * Global formatting
 * Exporting data
 * Configuring keybindings

## Version History

# 0.0.2

 * Respects use aliases option
 * Setting indentation works
 * Cleaned up error handing on connections
 * Don't show duplicate on recent server list
 * Show custom #Missing text if it's set
 * Zoom in and keep selection now works (capital A hotkey instead of a)
 * Can now clear a single data cell (from data cell actions menu)
 * Can now enter cell data
 * Cleaned up launcher menu, made grid bigger, enhanced about dialog

# 0.0.1

 * Minimum viable product release
 * Recent connections saved
 * Choose connection (ships with Essbase 11.1.2.4 driver
 * Connect to grid
 * Zoom in, zoom out, keep only, remove only, pivot
 * Pretty buggy
 * Options not supported
 		