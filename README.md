# Lab 3 – CG Pipeline (Pipes & Filters)

**Authors:** Begüm Özkaya, Ayten Özer  
**Course:** System Architectures – FHV

## What it does

Software renderer of the Utah Teapot using a Pipes & Filters architecture, implemented as both a push and a pull pipeline. The window shows four views simultaneously:

| Quadrant | Style | Color |
|---|---|---|
| Top left | Point rendering | Orange |
| Top right | Wireframe | Dark green |
| Bottom left | Filled | Red |
| Bottom right | Filled + flat shading | Blue |

## Requirements

- Java 17+
- Gradle (wrapper included)

## How to run

From inside the `cgpipeline/` directory:

```bash
# Linux / Mac
./gradlew run

# Windows
gradlew.bat run
```

Or open the project in IntelliJ and run `Main.java` directly. Make sure the working directory is set to `cgpipeline/` so the teapot model can be found.

Close the window with **Alt+F4** or via the taskbar.

## Switching between push and pull pipeline

In `src/main/java/at/fhv/sysarch/lab3/Main.java`, line 29:

```java
private final static boolean USE_PUSH_PIPELINE = true;   // push (default)
private final static boolean USE_PUSH_PIPELINE = false;  // pull
```

Recompile and run after changing.

## Difference between push and pull

**Push pipeline** – the source iterates all faces and pushes them through every filter in order. Depth sorting is possible because all faces can be collected before rendering, resulting in correct visibility (Painter's Algorithm).

**Pull pipeline** – the renderer sink pulls faces one at a time from the pipeline. Depth sorting is not possible here since faces arrive individually. This causes minor visibility artefacts on the filled teapots at certain rotation angles, which is expected behaviour.
