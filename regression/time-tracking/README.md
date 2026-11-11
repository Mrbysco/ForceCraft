# Reentrant time-tracking regression

This separate, development-only mod exercises `TimeHandler.onLevelTick` on a
localhost NeoForge dedicated server with no players. Do not install it in a
player world. It loads a fresh chunk and registers another tracked position
from the actual `ChunkEvent.Load` callback while the outer tick is processing.

With Java 25, build it against the ForceCraft jar under test:

```
./gradlew -p regression/time-tracking build -PforcecraftJar=/absolute/path/ForceCraft.jar
```

Place its jar and that ForceCraft jar in a fresh NeoForge 26.1.2.126 fixture.
Set `server-ip=127.0.0.1`, `online-mode=false`, use a disposable flat world,
and run `timeqa` from the server console after startup. Read `time-qa.json`.
No real accounts, external services, or production files are needed.

The original 7.0.2 jar reports `original_crash_reproduced` with
`ConcurrentModificationException`. The patch reports `passed`: empty tracking,
duplicate registration, real chunk-load reentrancy, preservation of the newly
added entry until the following tick, and pruning of missing block entities.
Both variants were run on NeoForge 26.1.2.126 and exited normally. This is a
regression for collection mutation, not a graphical gameplay or performance test.
