STUN SLAM - Fabric mod for Minecraft 1.21.11
=============================================
Keys:  J = toggle on/off   [ = open config (Instant or 10 ticks)
Needs an axe and a mace in your hotbar.

HOW TO BUILD THE JAR (one time, ~2 minutes)
1. Install Java 21 (Adoptium Temurin 21).
2. Easiest: download the Fabric example mod template
   (github.com/FabricMC/fabric-example-mod, branch 1.21.11) and copy its
   gradlew, gradlew.bat and gradle/ folder into this folder.
   OR install Gradle 9.x and run "gradle build" instead of "./gradlew build".
3. In this folder run:   ./gradlew build     (Windows: gradlew.bat build)
4. Your mod is build/libs/stunslam-1.0.0.jar  (NOT the -sources one).
5. Put it in .minecraft/mods/ together with Fabric API for 1.21.11,
   then launch with the Fabric 1.21.11 profile.

If Gradle says a version doesn't exist, open gradle.properties and use the
latest 1.21.11 numbers from fabricmc.net/develop.

NO JAVA? BUILD IT ONLINE FOR FREE
1. Make a free account at github.com and create a new repository.
2. Upload everything in this folder (including the hidden .github folder).
3. Open the repo's Actions tab, click "Build mod", wait ~3 minutes.
4. Open the finished run, download "stunslam-jar" at the bottom, unzip it,
   and put stunslam-1.0.0.jar in .minecraft/mods with Fabric API.
