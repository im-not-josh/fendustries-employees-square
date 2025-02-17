## App Requirements

Build an app which displays a list of employees from a given endpoint.
1. List All Employees page shows a collection of Employees and a summary of each - Name, Team, Small Photo
2. The list is sorted alphabetically on the name field
3. Reload/Refresh the list is from the Toolbar/Topbar Refresh action (top right corner). Note, this button hidden in error states.
4. Loading, Error and No Employees screens are shown during load/reload and for API errors. If json is malformed for any employee, error state shown.
5. Placeholder and error images are displayed when Photos do not load.
6. Photos load as a user scrolls and use disk level caching via Coil lib.
7. Employees list API call is executed on refresh and app start - it is not executed for orientation changes, system color them changes or other config changes.
8. Employees list is not persisted to disk - in memory storage only.
9. Unit Tests provided

## Build tools and versions used
Ive used a standard Android Studio template for this project, so its standard Gradle tools - version 8.8.0.
Target and compile SDK is 35, supporting back to 28.
I tested on a Pixel 8 Pro running Android 15 / SDK 35. I tested portrait and landscape, light + dark themes. I also tested no network errors, empty employees list and malformed json scenarios.
I also tested a few config changes - screen orientation changes for example - and reloading.

For code structure / architecture, I have followed the same pattern we have built and adapted in my last 2 companies this past 3/4 years. We call it MVVM but its actually MVI I feel.
We have a single immutable UI state which describes the current screen. It is observed by the View layer. Then we have Actions which are triggered by the user or lifecycle changes. These are sent
to the View Model who then updates the UI state according. There are also Events which the view layer observes.
These are single emissions of events which View needs to respond to - navigating from 1 screen to another for example.
The idea is that we have as much immutable state as possible, uni-directional data flow and de-coupled layers.

For this project I did 100% Kotlin (2.0.0), 100% Jetpack compose Views + navigation, Material3 theming as well.
Retrofit + KotlinX Serialization for Networking, Coil for Async Compose based Image loading + caching.
Co-routines + Stateflows for observables and threading.
I have also used Hilt + Dagger for Dependency Injection.
The .toml file lists all versions of these libs specifically.

## Steps to run the app
It should be as simple as opening the project, gradle sync, build and run.

## What areas of the app did you focus on?
I focussed on code structure and architecture. I tried to make everything 100% Unit Testable, Clean and without Side Effects.
I also tried to experiment a bit more with Jetpack compose that what I normally would.

## What was the reason for your focus? What problems were you trying to solve?
I always tend to focus on quality, testable code. For the last 4 or 5 years, Ive not had QA Engineers to test my code, so have 
ensured quality with Unit Tests, which is hard to do without good code structure.

## How long did you spend on this project?
I spent 4 nights of 2 or 3 hours each night. This project was fun and interesting for me and a nice chance to explore modern tech stack
on a greenfield project, so I spent a little more time, tinkering and trying different things.

## Did you make any trade-offs for this project? What would you have done differently with more time?
Biggest trade off in my mind is using Coil - feels a bit like cheating for an interview, even if the library is popular.
With more time I would have played around a lot more with UI, especially in landscape mode.

## What do you think is the weakest part of your project?
UI, styling and theming - I dont normally have to think about that from scratch

## Did you copy any code or dependencies? Include attributions here
No - but as I mentioned, I have followed similar architecture to my current and past companies.

## Is there any other info you would like us to know?
This code is production ready but before we go into production, I would still want to do a few more things like:
1. Accessibility testing - I dont not really consider this
2. Crash reporting like Firebase Crashlytics
3. Analytics - looking at user click counts, page loads etc
4. Metrics / Telemetry on page loads, API durations and errors - New Relic or Bugsnag for example. Bugsnag could cover 2 as well
5. Minification + obfuscation - there are missing pro-guard rules I am sure.
6. Perhaps having a way to toggle between prod and non-prod envs.
7. Multi modules - should we add more feature, that might start making more sense.
8. Extracting repeated code - theres not much right now, but more features would lead to that, so chances to extract common code exist.

## Particularly proud of?
The whole thing? A good opportunity to work with modern code - which is certainly not what I am used to.
This would good fun and a good reminder that building things is still very enjoyable :)