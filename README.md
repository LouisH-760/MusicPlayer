# MusicPlayer
> Barebones Java music player

- invocation: `java -jar player.jar /path/to/album`
- building: use the vlcj, vlcj-native and other libraries. vlcj4 only, not tested for other versions.
- replacing the default image: replace default.png
- rewriting player, scanner or GUI: implement the provided interfaces for a drop-in replacement
- javadoc for the existing classes: [Github Pages](https://louish-760.github.io/MusicPlayer/)

## Keyboard Shortcuts
- Next: `RIGHT` (right arrow)
- Previous: `LEFT` (left arrow)
- Play / Pause: `SPACE` (spacebar)
- Volume **Up**: `UP` (up arrow)
- Volume **Down**: `DOWN` (down arrow)

## Screenshots
### Current Swing GUI
The player alone:  
![Player](https://raw.githubusercontent.com/LouisH-760/MusicPlayer/master/Screenshots/player.png)

The player and it's associated terminal side by side:
![player and console](https://raw.githubusercontent.com/LouisH-760/MusicPlayer/master/Screenshots/Fullscreen.png)  

## VLCJ license

```
VLCJ is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
```

[GNU Licenses](https://www.gnu.org/licenses/)

```
VLCJ makes use of JNA (https://jna.dev.java.net/). JNA is licensed under LGPL.
```
