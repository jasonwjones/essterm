# Contributing to essterm

Thanks for taking a look! This started as a personal project and is still fairly small in scope, so
keep expectations modest, but real contributions - bug fixes, small features, documentation - are
genuinely welcome.

## Getting set up

```
git clone https://github.com/jasonwjones/essterm.git
cd essterm
mvn package
./run.sh
```

That gets you a REST-only build - no Oracle software required. If you want to work on the Essbase
Java API side (see [Two backends](#two-backends) below), you'll additionally need the real Oracle
Essbase Java API JARs available in your local Maven repository, and to build with `mvn package
-Pjapi`. Those JARs aren't publicly distributable, so this repo can't hand them to you - if you don't
already have an Essbase installation to pull them from, you likely can't work on that half of the
project, and that's fine: most useful contributions (UI, ad hoc option handling, bug fixes) apply
equally to the REST path.

## Two backends

essterm can talk to Essbase two ways:

- **REST**, via [essbase-rest-client](https://github.com/appliedolap/essbase-rest-client) - a
  sibling open-source project, also maintained here. A lot of essterm's real value has been
  discovering and documenting the REST API's actual (often undocumented, sometimes surprising) wire
  behavior by driving it live, then fixing that library up to match. If you find a REST ad hoc
  operation behaving oddly, there's a good chance the fix belongs in essbase-rest-client rather than
  essterm itself - check there first.
- **JAPI**, via the real Essbase Java API - opt-in only, see `AGENTS.md` for how that's wired.

## Updating the README's screenshots

If your change visibly affects a screen shown in the README, regenerate the screenshots rather than
leaving them stale: install [VHS](https://github.com/charmbracelet/vhs) (`brew install vhs`), build,
and run `vhs demo.tape` from the repo root - it drives a real session via "Recents" (never the raw
Connect dialog, so no password ever ends up on screen, even masked) and writes PNGs to
`docs/images/`. Optimize them losslessly before committing, e.g. `oxipng -o max --strip safe
docs/images/*.png`.

## Testing a change

There's no unit test suite. Verification here means actually running the app - build it, launch it
(`./run.sh`), and drive the specific feature you touched against a real Essbase server. If your
change touches the REST backend and you suspect the *server's* behavior rather than essterm's own
code, essbase-rest-client's own test suite (`EssCubeViewIT`) is the right place to add a real,
asserting integration test against a live server - see that project's own `EssCubeView` javadoc for
the verification conventions it follows.

## Code style

- No comments explaining *what* code does - names should do that. Comments here are for *why*:
  a non-obvious server quirk, a workaround, a hazard worth flagging for the next person.
- Don't add abstraction, configuration, or "for later" flexibility beyond what the current backend
  split (REST vs. JAPI) already requires.
- Match the existing formatting (tabs, brace style) in whatever file you're editing.

## Submitting changes

Open a PR with a short description of what changed and why, and how you verified it (a `tmux`
transcript, a screenshot, or just a description of the manual test is fine - there's no CI to lean
on here). Small, focused PRs are much easier to review than large ones.

## License

By contributing, you agree your contribution is licensed under this project's
[Apache 2.0 license](LICENSE).
