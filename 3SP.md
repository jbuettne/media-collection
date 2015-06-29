# Projektteam #

| **Philipp Dermitzel** | **Jens Büttner** | **David Pollehn** |
|:----------------------|:------------------|:------------------|
| Projektleitung        | GUI               | Backend           |
| p.dermitzel@agentur38.de | jens.buettner@tu-bs.de | d.pollehn@tu-bs.de |

# Motivation und Ausgangssituation #

Die immer weiter ausgebauten Möglichkeiten des Erwerbs von Medien wie Musik und Film in digitaler Form, ermöglichen es, einfacher den je einen Überblick über die eigene Sammlung zu erhalten und zu pflegen.
Die Zusammenführung der Sammlung mit den klassischen Datenträgern (LP, CD, DVD, etc.) erfordert jedoch einen großen Aufwand, da gewünschte Daten – Artist, Titel, Erscheinungsjahr, Cover, etc. – manuell gesucht und eingefügt werden müssen.

Glaubt man den Aussagen der Musik- (und Film-)Industrie, so sinken die Verkaufszahlen bereits seit der  massenkompatiblen Einführung der Kassette1 und doch existieren immer noch viele Fans, die ihre Medien auf einem klassischen Medium erwerben.

Zwar existieren Webportale wie Musik-Sammler.de oder dvdb.de, doch müssen die eigenen Datenträger noch immer per Hand eingegeben werden. An dieser Stelle setzt die App „MusicCollection“ an. Die Android-Applikation soll es ermöglichen, die Barcodes auf Datenträgern zu scannen und so mit einem Knopfdruck in die Sammlung hinzuzufügen. Auch soll die auf diese Art erzeugte Sammlung mit dem heimischen PC synchronisiert und in wählbaren Formaten (XML, SQL, DB, ...) erzeugt werden können, so dass das Einpflegen in bestehende Systeme ermöglicht wird.




Braunschweig, den 05. April 2011


Philipp Dermitzel		`___________________________`

Jens Büttner		`___________________________`

David Pollehn		`___________________________`

AndroidLab		`___________________________`

# Ziele #

Eine Applikation, die den auf Datenträger(-Verpackungen) aufgedruckten Barcode einlesen, entsprechende Daten aus dem Internet einholen und den Datenträger in die lokale Sammlung einfügen kann und diese dann mit der eigenen auf dem PC zu synchroniserien. Dabei sollen sowohl Tonträger (LPs, CDs) als auch Film-Medien (DVD, Bluray) und möglicherweise sogar Bücher gespeichert werden können. Eine Bearbeitung der Sammlung auf dem SmartPhone ist ebenfalls wünschenswert.

# Anforderungen #
  1. Ein Barcode-Scanner, welcher die Barcodes diverser Datenträger einlesen kann
  1. Webbasierte Dienste für das Fethcne der Informationen zu dem Codes, die man eingescannt hat
    * Diese Dienste sollen die Informationen bestenfalls über eine API bereitstellen
  1. Eine durchdachte grafische Oberfläche, die eine einfache Bedienung des Programms ermöglicht
  1. Eine Möglichkeit, die eingeholten Daten sinnvoll zu speichern
  1. Eine Möglichkeit, die Sammlung mit dem PC zu synchronisieren
    * Vorerst soll die synchronisation über (XML, CSV, SQL, ...) erfolgen, da die uns bisher bekannten Dienste keine Import-/Exportfunktion besitzen
    * Webbasierte Dienste, die das synchronisieren den Sammlung erlauben (Dropbox,...)
  1. Eine Möglichkeit, die Sammlung auf dem SmartPhone anzuzeigen
  1. Eine Möglichkeit, die Sammlung auf dem SmartPhone zu bearbeiten (Nice-to-have)
    * Das bearbeiten kann ansonsten direkt über die XML Files erfolgen

# Lösungsweg #
  1. **GUI** _Keine Anmerkungen_
  1. **Barcode-Scanner** _Geeignete Libraries sind bereits vorhanden und müssten auf die Einsetzbarkeit geprüft werden. Hierbei böte sich vor allem zxing (http://code.google.com/p/zxing/) an._
  1. **Fetching-Dienste** _Für Tonträger existieren APIs von MusicBrainz oder last.fm, die möglicherweise genutzt werden könnten. Hierbei müsste geprüft werden, wie ein Ansprechen der APIs mit den aus dem Scannen des Barcodes gewonnen Daten möglich ist._
  1. **Speicherung der Sammlung** _Sinnvoll wäre eine Speicherung in einer einfachen Datenbank (SQLite)._
  1. **Synchronisierung** _Eine Synchronisierung über das Internet wäre wünschenswert. Dafür könnten freie Cloud-Dienste wie Dropbox verwendet werden. Somit würde jedoch eine Abhängigkeit von anderen Apps erzeugt, was zu vermeiden wäre. Andere Möglichkeiten wären ein Dateiversand über E-Mail o.ä._

# Projektplan #

| 04.04.2011 - 08.04.2011| Erstellung 3-SP |
|:-----------------------|:----------------|
| 04.04.2011 - 15.04.2011| Einarbeitung    |
| 20.04.2011             | GUI Prototyp    |
| 01.05.2011             | BarCode einscannen |
| 10.05.2011             | Fetching an Informationen |
| 13.05.2011             | GUI verfeinern  |
| 16.05.2011             | 1. Review       |
| 19.06.2011             | Erste Integration, Prototyp |
| 27.06.2011             | Testen          |
| 27.06.2011             | 2. Review       |
| 29.06.2011             | Splashscreen, Hilfefunktion |
| 29.06.2011 - 11.07.2011 | Abschlusspräsentation erstellen |
| 11.07.2011             | DryRun          |
| 13.07.2011             | Projektabschluss |

# Potentielle Hindernisse und Maßnahmen #

  * Möglicherweise ergeben sich Probleme bei der Anbindung an die Fetching-Dienste. In diesem Fall müsste nach alternativen Möglichkeiten gesucht werden. Hierbei wäre beispielsweise das Parsen von Google-Ergebnissseiten eine Variante.

  * Da die Anwendung nicht auf andere Infrastrukturen aufbauen soll (z. B. Dropbox), muss ein Weg gefunden werden, die Dateien mit dem Heim-PC zu synchronisieren. Sollte hier keine Möglichkeit, die Daten über das Internet zu übertragen, gefunden werden, müsste man auf das Übertragen als Datei per USB-Verbindung zurückgegriffen werden - wobei dies "ultima ratio" wäre.

  * (Möglicherweise gelingt die direkte Übertragung und Speicherung der Datenträger-Informationen in der Datenbank auf dem Computer nicht. Alternativ könnte zum Beispiel eine XML-Datei erstellt werden, welche manuell auf den Computer gesendet und eingelesen wird.)