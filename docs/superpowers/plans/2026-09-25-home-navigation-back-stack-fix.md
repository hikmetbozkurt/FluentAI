# Home Navigation Back-Stack Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Keep Home as the canonical root when Home cards open other destinations and when the sidebar returns to Home.

**Architecture:** Retain `NavHostController` as the only navigation state. Express the sidebar's state-save policy as a small pure navigation contract: Home clears to the graph root without saving or restoring child state, while other top-level destinations retain the existing save/restore behavior. Home-card callbacks continue using ordinary push navigation so system Back returns to the originating Home entry.

**Tech Stack:** Kotlin, Navigation Compose, JUnit 4, Gradle.

**Spec:** `C:/Users/user/.codex/attachments/629dd353-ce1d-4284-ad6b-bd3f46058150/Yapıştırılan metin.txt`

## Global Constraints

- Do not use subagents.
- Change navigation behavior only; do not change UI, feature logic, content, databases, ViewModels, or themes.
- Preserve assigned/deep-link routing and the existing non-Home sidebar behavior.
- Run focused navigation tests, `:app:compileDebugKotlin`, and `:app:assembleDebug`.

## Review Focus

- Selecting Home from Progress or Speak must not restore the child destination.
- Home-card pushes to Speak, Progress, Learn/content, and Review must leave Home beneath the target for normal Back.
- Static dashboard routes and assigned/deep-link routes must keep their existing ownership and matching behavior.
- Non-Home sidebar destinations must retain the existing saved-state behavior.
- Sidebar selection must continue to derive from the active NavController route.

---

### Task 1: Canonical Home sidebar policy

**Files:**
- Modify: `app/src/main/java/com/seanora/fluentai/app/navigation/TopLevelDestination.kt`
- Modify: `app/src/main/java/com/seanora/fluentai/app/FluentAiApp.kt`
- Test: `app/src/test/java/com/seanora/fluentai/app/navigation/NavigationTest.kt`

**Interfaces:**
- Consumes: `TopLevelDestination` and the existing sidebar `NavHostController.navigate` call.
- Produces: `TopLevelNavigationPolicy` and `topLevelNavigationPolicy(destination)` for configuring `saveState` and `restoreState`.

- [x] **Step 1: Write focused failing policy and route-contract tests**

Add literal assertions that Home disables state save/restore, non-Home destinations preserve it, Home-launched target routes remain distinct from `home`, and assigned/deep-link route ownership remains unchanged.

- [x] **Step 2: Run the focused test and verify it fails**

Run: `./gradlew :app:testDebugUnitTest --tests "com.seanora.fluentai.app.navigation.NavigationTest"`

Expected: compilation failure because `topLevelNavigationPolicy` does not exist.

- [x] **Step 3: Implement the minimal policy and apply it in the sidebar**

Return `saveState = false` and `restoreState = false` only for `TopLevelDestination.HOME`; retain `true`/`true` for all other top-level destinations. Keep `popUpTo(graph.findStartDestination().id)` non-inclusive and `launchSingleTop = true`.

- [x] **Step 4: Run focused tests**

Run: `./gradlew :app:testDebugUnitTest --tests "com.seanora.fluentai.app.navigation.NavigationTest"`

Expected: PASS.

- [x] **Step 5: Run integration build checks**

Run: `./gradlew :app:compileDebugKotlin :app:assembleDebug`

Expected: BUILD SUCCESSFUL.
