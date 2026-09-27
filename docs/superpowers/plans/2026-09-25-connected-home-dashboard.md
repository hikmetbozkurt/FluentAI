# Connected Home Dashboard Implementation Plan

> **For agentic workers:** Execute directly in this session. Do not delegate or use subagents.

**Goal:** Repair the personalized learning loop and replace the prototype Home screen with the approved responsive LingoPink dashboard.

**Architecture:** Preserve the existing Compose → ViewModel → domain/repository → Room flow. Add only the missing migration, startup decision, typed Today navigation context, completion reconciliation, playback errors, and Home-specific light design components required by the supplied specification.

**Tech Stack:** Kotlin, Jetpack Compose, Navigation Compose, Hilt, Room, Coroutines/Flow, Media3.

**Spec:** `docs/ui/HomeDashboardSpec.md` and `docs/ui/HomePageReference.png`

## Global Constraints

- Preserve existing user progress with explicit Room migrations.
- Keep local learning features offline-first.
- Do not embed curriculum business data in Compose.
- Use real persisted state when available and honest empty states otherwise.
- Do not add third-party libraries or unrelated refactors.

## Tasks

- [ ] Add regression tests for profile persistence, startup destination, planner prioritization/date determinism, refresh preservation, and Today completion.
- [ ] Add non-destructive user database migrations and persist Reading/Listening estimates.
- [ ] Gate startup on the persisted profile and keep onboarding as the first experience for new users.
- [ ] Surface Media3 failures through playback/UI state without pretending missing packaged audio exists.
- [ ] Make planner inputs date-aware, improve weak-skill and recent-history prioritization, and preserve completed items on refresh.
- [ ] Add typed Today activity navigation and complete items only from feature activity callbacks.
- [ ] Replace developer-facing and stale hardcoded UI copy with real or neutral state.
- [ ] Add Home-specific light tokens and reusable dashboard components.
- [ ] Recompose Home and the navigation rail to match the supplied screenshot responsively.
- [ ] Run targeted tests, the unit suite, and a debug build; report exact results and missing media/art limitations.

## Review Focus

- Existing version-3 user databases retain every row after upgrading to version 4.
- A missing profile routes to onboarding; a persisted profile routes to Home without flicker loops.
- Refreshing today's plan retains completed item state and timestamps.
- Due review and mistake items open Review; curriculum and speaking items open their exact targets.
- Missing listening assets produce a readable error and do not leave playback stuck.
