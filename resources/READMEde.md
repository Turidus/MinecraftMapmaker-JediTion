# Minecraft-MapMaker


Dieses Programm nimmt ein Bild und erstellt daraus mehrere Dateien, die bei der Erstellung von Minecraft Karten behilflich sind, welche dieses Bild darstellen.

Dieses Programm kann mit der Treppenmethode arbeiten, was 153 Farben auf der Karte ermöglicht.

Um dieses Programm auszuführen muss Java 1.8 installiert sein.

## Generierte Dateien

Eine Textdatei mit der Anzahl und Typ an benötigten Blöcke

Eine Textdatei mit der Position der benötigten Blöcke

Eine oder mehrere Schematics für World Edit (s. "Über große Bilder") und kompatiblen Programmen

Ein Bild welches ungefähr das Resultat zeigt

## Nutzung

Lade die Jar von https://github.com/Turidus/MinecraftMapmaker-JediTion/releases/latest runter.

Führe die Jar aus. Bei der ersten Nutzung wird ein Ordner namens "resources" erstellt. Dieser enthält wesentliche Dateien
wie diese Readme Datei. 

Dies öffnet eine GUI.

### GUI
#### Menüleiste

- File: Hier kann das Fenster geschlossen werden
- Edit: Hier can die aktuelle Konfiguration gespeichert oder zurücksetzt werden.
- About: Hier findet man mehr über das Programm und kann auf neue Versionen prüfen

#### Hauptfenster
Diese GUI hat drei Spalten.

In der ersten Spalte kann man das Bild welches umgewandelt werden soll und den Speicherort auswählen. Optional kann man ein Projekname festlegen,
unter dem die Dateien gespeichert werden sollen. Außerdem kann man die Farben und Blöcke bearbeiten welche für die Erstellung genutzt werden sollen.
Des Weiteren kann der Algorithmus zur Farbauswahl bestimmt werden. CIE deltaE2000 wählt Farben anhand der menschlichen Wahrnehmung aus,
Euclidian wählt sie nach einem simplen Verfahren aus, welches zu schärfen Abgrenzungen führt.

In der zweite Spalte kann festgelegt werden welche Dateien erstellt werden sollen.

In der dritten Spalte können die einschränkenden Parameter gesetzt werden.
Das Feld minY sollte auf dem Level liegen auf dem das Konstrukt später gesetzt wird. Es muss kleiner als 251 und mindestens 4 kleiner als maxY sein.
Das Feld maxY sollte so hoch wie möglich liegen, um Farbfehler zu vermeiden. Es muss größer als 4, kleiner als 256 und mindestens 4 größer als minY sein.
Das Feld maxS muss größer sein als 0. Dieses Feld bestimmt die maximale Größe der erstellten Schematic Dateien. Je kleiner maxS ist, 
desto mehr Schematic Dateien werden erstellt, auf der anderen Seite können zu große Schematic Dateien beim Importieren den Server zum Absturz bringen. 

#### Color and Blocks
Dieses Fenster erlaubt es, die Farben, welche genutzt werden sollen, auszuwählen und, wo möglich, den Block zu bestimmen,
der für diese Farbe genutzt wird.

## Über die Cobblestone Line
Wenn das Bild gebaut oder eingefügt wird, fällt auf das am Nordende des Konstrukts eine zusätzliche Line Cobblestone platziert wurde.
Dies ist notwendig, um zu verhindern, dass die erste Line des Bildes falsch schattiert wird.
Der einfachste Weg. um damit umzugehen ist. die Line Cobblestone außerhalb des Kartenradiuses zu platzieren. Ansonsten kann 
der Cobblestone auch mit einem anderen Block ersetzt werden, der besser in die Umgebung passt.

## Über große Bilder
Große Bilder, 128 x 128 Pixels und größer, können die Performance des Servers beim Einfügen stark beeinträchtigen.
Es sollte unbedingt Fast Asynchron World Edit oder ähnliches genutzt werden. Es es außerdem förderlich, die Schematic aufzuteilen,
indem ein kleineres **maxS** gewählt wird.

## Über sehr große Bilder
Sehr große Bilder, 250 x 250 pixels und größer, haben zusätzlich das Problem, dass sie evt. die maximale Welthöhe von 256
Blöcken überschreiten können. Insbesondere wenn das Bild sehr einfarbig ist oder besonders groß (~450 x 450 pixels und größer)
würde eine perfekte Repräsentation des Bildes mehr als 256 Blöcke brauchen. Um zu verhindern, dass das Bild **maxY** überschreitet, zwingt dieses Programm alle Blöcke über dem Höhenlimit unter dieses, was allerdings zu Pixelfehlern führen würde.
Dies fällt besonders bei eher einfarbigen Bildern auf, sehr bunte Bilder sind nicht so stark betroffen.

Außer die **-maxY** größer als die Bildhöhe zu setzten ist der beste Weg, damit umzugehen, das Bild in ImageSizeX x 256 oder gar
ImageSizeX x 128 Pixel Gebiete zu zerschneiden, diese einzelne Bereiche eigenständig zu verarbeiten und am Ende die Karten wie gewünscht
zusammen zu fügen.

Die größte getestet Bildgröße ist 1000x1000 Pixel. Größere Bilder können mehr Speicher benötigen als von der JVM
(Java Virtual Machine) freigegeben, was das Program zum Absturtz bringt.


## Über (mehrere) Schematics
Die Schematic expandiert von dem Block auf dem man steht in Richtung **Osten** und **Süden**. Wenn eine Schematic zwischen (0,0)
und (128,128) platziert werden soll, muss man sich auf (0,0) stellen. Wenn mehrere Schematics generiert werden, werden sie
nach ihrer relativen Platzierung zueinander benannt. PartZ0X1 liegt östlich von partZ0X0, 
partZ1X0 südlich von Z0X0.

## Über die BaseColorIDs Datei
Hier werden die zu den Farben gehörigen Blöcke gespeichert. Man kann diese Blöcke ändern, in dem man die BlockID values anpasst.
