STUN SLAM - Fabric Client Mod
=============================
BAUEN MIT GITHUB (kein lokales Setup noetig):
1. Auf github.com ein neues Repository erstellen (privat oder oeffentlich).
2. Den Inhalt dieses Ordners hochladen ("Add file" > "Upload files"),
   WICHTIG: auch den versteckten Ordner .github mit hochladen
   (am besten per Git: git init, git add ., git commit, git push).
3. gradle.properties anpassen: loader_version, loom_version, fabric_api_version
   fuer Minecraft 26.2 (siehe https://fabricmc.net/develop).
4. Im Repository auf den Tab "Actions" gehen. Der Build startet bei jedem Push
   (oder manuell: "Build Stun Slam" > "Run workflow").
5. Nach ca. 2-5 Minuten den fertigen Lauf oeffnen, unten bei "Artifacts"
   "stunslam-jar" herunterladen, entpacken und die .jar in den mods-Ordner legen.
   (Die Datei "-sources.jar" wird nicht gebraucht.)

Bedienung: Taste K schaltet die Mod an/aus. Axt auf blockenden Spieler zielen,
dann springen: Axt-Schlag, Wechsel auf Mace, Smash im Fall.
Nur fuer eigene/private Server verwenden.
