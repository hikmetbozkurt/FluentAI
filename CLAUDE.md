# CLAUDE.md — FluentAI

@AGENTS.md
@PHASES.md

# Claude-specific operating notes

`AGENTS.md` is the canonical project instruction file. Do not duplicate or override its architecture casually.

## Working style

Before implementing a substantial task:
1. Inspect the relevant code and project documents.
2. Check `TASKS.md` for ownership/conflicts.
3. Identify the current phase from `PHASES.md`.
4. Form a short implementation plan.
5. Implement incrementally.
6. Verify with relevant tests/build commands.

Do not stop at analysis when the task clearly requests implementation.

Do not broaden scope without a concrete reason.

## Repository modifications

Prefer editing existing files over creating speculative helpers.

Temporary scratch files are allowed during investigation, but remove them before finishing unless they have lasting project value.

Do not:
- force-push,
- rewrite git history,
- delete large directories,
- replace databases,
- change signing/configuration secrets,
- perform destructive migrations

unless explicitly requested.

## Collaboration with Codex / Antigravity

Assume another coding agent may work in parallel.

Before broad refactors, inspect:
- `TASKS.md`
- recent git diff/status
- relevant architectural decisions

Keep commits/diffs conceptually focused.

If existing code differs from documentation:
- investigate first,
- do not silently “correct” the code or docs,
- report the mismatch,
- update architecture only when the user accepts the decision or the task explicitly authorizes it.

## Validation expectations

For Android changes, prefer:
- targeted unit tests,
- Room tests for database work,
- Compose tests for behavior that merits UI automation,
- Gradle build after meaningful integration changes.

For content changes:
- run content validation,
- check IDs/relations,
- confirm generated database integrity when relevant.

Never report “done” when build/test verification failed.

## Output to the user

At the end of implementation tasks, report:
- what changed,
- important files touched,
- tests/builds run,
- result,
- any remaining limitation or follow-up.

Avoid long generic explanations when concrete implementation information is available.
