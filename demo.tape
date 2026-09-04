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
# A plain black-background terminal (standard ANSI palette) instead of a named VHS theme -
# named themes recolor the *unpainted terminal background* (visible around Lanterna's own
# window) to whatever accent that theme picked, which read as an unexpected pink/blue cast.
Set Theme '{"name": "Plain", "background": "#000000", "foreground": "#e5e5e5", "cursor": "#e5e5e5", "selectionBackground": "#5c5cff", "selectionForeground": "#000000", "black": "#000000", "red": "#cd0000", "green": "#00cd00", "yellow": "#cdcd00", "blue": "#0000ee", "purple": "#cd00cd", "cyan": "#00cdcd", "white": "#e5e5e5", "brightBlack": "#7f7f7f", "brightRed": "#ff0000", "brightGreen": "#00ff00", "brightYellow": "#ffff00", "brightBlue": "#5c5cff", "brightPurple": "#ff00ff", "brightCyan": "#00ffff", "brightWhite": "#ffffff"}'
Set Padding 20
Set TypingSpeed 100ms
Set PlaybackSpeed 0.6

Type "java -jar target/essterm-1.0.0.jar"
Enter
Wait+Screen@30s /Essterm/
Sleep 500ms

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
