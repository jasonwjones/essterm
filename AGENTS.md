# Agent notes for essterm

Guidance for AI coding agents (Claude Code or similar) working in this repo. See
[CONTRIBUTING.md](CONTRIBUTING.md) for the human-facing version of most of this.

## What this project is

A terminal (Lanterna-based) ad hoc client for Essbase - a text-mode analog of the classic Essbase
Excel add-in / Smart View. It's also a deliberate testbed for
[essbase-rest-client](https://github.com/appliedolap/essbase-rest-client): most of the REST-side
wire-protocol discoveries in that library's git history were made *from* essterm, by driving real ad
hoc operations against a live server and watching what came back.

## Build / test / run

```
mvn package                 # default build - REST support only, no Oracle JARs involved
mvn package -Pjapi          # also compiles in Essbase Java API support (see below)
./run.sh                    # build + launch; --no-build skips the build, --swing forces
                             # Lanterna's Swing terminal window instead of the real terminal
```

There is no unit test suite in this repo. Verification is almost entirely live, interactive testing
against a real Essbase server (see "Live testing" below) - there isn't a good substitute for it, and
you should not assume a change works without having actually watched it happen.

## Architecture essentials

- **Two backends, one interface.** `EssGrid` (package `grid`) is the abstraction ad hoc operations go
  through; `RestEssGrid` (backed by essbase-rest-client's `EssCubeView`) and `EssbaseEssGrid` (backed
  by the real Essbase Java API) both implement it. `EssGridFactory`/`ConnectionResolver` are the
  matching factory/connection abstractions. Never make `AdhocGridWindow` or other UI code assume
  which backend is active.
- **JAPI is opt-in, not bundled.** Oracle's Essbase Java API JARs aren't on Maven Central and aren't
  ours to redistribute. Every class that touches them (`EssbaseConnectionResolver`, `EssbaseEssGrid`,
  `EssbaseEssGridFactory`, the dormant Jaybase mock) lives under `src/japi/java`, only added to the
  compile sourcepath by the `japi` Maven profile (see `pom.xml`). Code in `src/main/java` must never
  import `com.essbase.api.*` or `com.saxifrages.essbase.*` directly - reach the JAPI backend only
  through `ConnectionResolver`/`EssGridFactory`, injected with `@Autowired(required = false)` plus a
  `@Qualifier`, so its absence degrades to "unavailable" rather than a Spring startup failure. If you
  aren't sure whether a change keeps this working, build *without* `-Pjapi` and confirm it still
  compiles and starts.
- **`AdhocOptionCapability`** (package `grid`) is how the ad hoc options dialog knows what to grey
  out per backend - each `EssGrid` implementation reports `getSupportedOptions()`. When adding a new
  option, wire it through both backends (or explicitly leave it out of one's supported set) rather
  than assuming REST parity with JAPI, or vice versa.
- **Constructor parameter shadowing is a real, recurring hazard here.** Several windows take a
  mutable model object (`AdhocOptions`, etc.) as a constructor parameter and also hold it as a field
  under a similar/identical name, while defining anonymous inner classes (key bindings, button
  handlers) *inside that same constructor*. A same-named parameter shadows the field for every one of
  those inner classes, permanently - see the git history around `AdhocGridWindow`'s `initialOptions`
  rename for a real bug this caused (a saved setting silently never took effect or persisted). When
  adding a field+constructor-parameter pair in a class with inner classes defined in the constructor,
  give the parameter a distinct name.

## Live testing (the actual verification method in this repo)

There's a live Essbase REST server used for testing throughout this project's history
(essbase.appliedolap.com, Sample.Basic). Credentials, when needed, live in
`~/essbase-test.properties` (not tracked) - don't print them, and don't add new tests or scripts that
echo them.

Two proven ways to verify a change actually works, in order of preference:

1. **Drive the real UI.** Build, then run essterm under `tmux` (`tmux new-session -d -s essterm -x
   140 -y 45 'java -jar target/essterm-1.0.0.jar'`), send keys with `tmux send-keys`, and
   read the screen with `tmux capture-pane -p`. This is the only way to catch UI-layer bugs (the
   constructor-shadowing bug above was invisible from raw REST testing - it only showed up by
   actually opening the dialog twice in the running app).
2. **Raw REST calls**, when isolating whether a behavior is a wire-protocol fact versus an essterm
   bug. Use Python with `http.cookiejar` (Basic Auth alone does *not* give you a persistent session -
   several apparent bugs earlier in this project's history turned out to be test scripts not carrying
   a session cookie across calls). Reset state first: `DELETE
   /rest/v1/applications/{app}/databases/{db}/layouts/Session_Layout_{username}` - the REST API
   persists ad hoc state server-side per user, so a fresh `GET .../grid` is *not* guaranteed to be a
   pristine default view otherwise.

Whichever method you use: **verify empirically, don't infer from the OpenAPI model or Essbase
documentation.** This codebase's git history is full of cases where the wire format accepted a field
and returned 200 while silently doing nothing, or silently did something other than what the field
name implies. Assume the same is possible for anything not already confirmed in essbase-rest-client's
own javadoc.

## Code style notes specific to this repo

- No comments explaining *what* code does; comments here are almost all "why" - a non-obvious wire
  behavior, a workaround for a specific server quirk, a shadowing hazard. Match that.
- Don't add speculative abstraction for a single current backend/use case. The REST/JAPI split above
  is the deliberate exception, not a general license to add interfaces "for future flexibility."
