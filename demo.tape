# Records a walkthrough of essterm for docs/images/*.png (see README.md).
#
# Run from the repo root, after a fresh `mvn package`:
#   vhs demo.tape
#
# Uses "Recents" to connect rather than the raw Connect dialog, so a real password never
# appears on screen (even masked) - see AGENTS.md/CONTRIBUTING.md on not exposing credentials.
# Requires a saved recent connection already in essterm.properties (gitignored, not part of
# this repo) pointing at a real reachable Essbase REST server.

Output docs/images/demo.gif

Set Shell "bash"
Set FontSize 16
Set Width 1400
Set Height 1150
Set Theme "Dracula"
Set Padding 20
Set TypingSpeed 60ms

Type "java -jar target/essterm-0.0.1-SNAPSHOT.jar"
Enter
Wait+Screen@30s /Essterm/

Screenshot docs/images/launcher.png

# "Recents" is the first, already-selected item.
Enter
Wait+Screen@10s /Recent Connections/
Sleep 500ms
Enter
Wait+Screen@30s /Ad hoc grid/
Sleep 500ms

Screenshot docs/images/adhoc-grid-default.png

# Zoom in on Product (the header at row 0, col 1 in Sample.Basic's pristine grid).
Right
Type "a"
Sleep 800ms
# ...then Market, nesting a second dimension onto the row axis.
Up
Right
Type "a"
Sleep 800ms

Screenshot docs/images/adhoc-grid-zoomed.png

# Member selection dialog. The extra Down+Up (forcing a fresh full redraw) works around an
# intermittent stale/overlapping title-bar render otherwise seen here.
Sleep 500ms
Type "m"
Sleep 1500ms
Down
Sleep 200ms
Up
Sleep 800ms

Screenshot docs/images/member-selection.png

Escape
Sleep 300ms

# Ad hoc options dialog.
Type "o"
Sleep 500ms

Screenshot docs/images/adhoc-options.png

Escape
Sleep 300ms

Ctrl+C
Sleep 500ms
