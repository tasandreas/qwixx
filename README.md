# Qwixx

Een digitale versie van het dobbelspel **Qwixx**, gemaakt als groepsproject in het eerste jaar Toegepaste Informatica aan HoGent (2e semester, 2025–2026).

De applicatie is geschreven in Java met een JavaFX-interface. Spelers worden opgeslagen in een MySQL-databank.

## Features

- **2 tot 5 spelers.** Je registreert spelers met een gebruikersnaam en geboortejaar, of je kiest bestaande spelers uit de databank.
- **Drie spelvarianten:**
  - **Basis**: het klassieke scoreblad.
  - **Variant**: een alternatief scoreblad.
  - **Random**: de waarden in elke rij worden willekeurig geschud.
- **Volledig speelverloop**: dobbelstenen rollen, optie 1 (som van de witte dobbelstenen) en optie 2 (wit + kleur), rijen afsluiten, mislukte worpen bijhouden en de eindscore berekenen.
- **Overzicht tijdens het spel**: punten van alle spelers met een mini-scoreblad per speler.
- **Twee talen**: Nederlands en Engels.
- **Donker en licht thema.**
- **Geluidseffecten** bij het rollen, aankruisen, ongeldige zetten en winnen.
- **Console-versie**: het spel kan ook volledig in de terminal gespeeld worden.

## Technologie

| Onderdeel | Gebruikt |
|---|---|
| Taal | Java 21 |
| Interface | JavaFX 21, ControlsFX |
| Databank | MySQL (via JDBC, `mysql-connector-j`) |
| Testen | JUnit 5 (50 unit tests) |
| Build | Maven (met Maven Wrapper) |

## Projectstructuur

```
src/main/java
├── domein/        Spellogica: Spel, Speler, Scoreblad, Rij, Dobbelsteen, DomeinController
├── dto/           Data transfer objects tussen domein en interface
├── gui/           JavaFX-schermen (start, registratie, spel, einde)
├── cui/           Console-versie van het spel
├── persistentie/  Databankconnectie en SpelerMapper
├── exceptions/    Eigen exceptions
├── startupGui/    Opstartklassen voor de grafische versie
└── startupCui/    Opstartklasse voor de console-versie
src/main/resources
├── css/           Donker en licht thema
├── sounds/        Geluidseffecten
└── messages_*.properties   Vertalingen (NL/EN)
```

De spellogica (`domein`) staat los van de interface. Daardoor kan hetzelfde spel zowel via JavaFX als via de console gespeeld worden, en kan de logica apart getest worden.

## Opstarten

### Vereisten

- JDK 21
- Een MySQL-databank

### 1. Databank voorzien

De applicatie verwacht een tabel `speler`:

```sql
CREATE TABLE speler (
    Gebruikersnaam VARCHAR(45) PRIMARY KEY,
    Geboortejaar   INT NOT NULL
);
```

### 2. Verbinding instellen

De verbindingsgegevens staan bewust **niet** in de code. De applicatie leest ze uit de omgevingsvariabele `QWIXX_DB_URL`:

```
jdbc:mysql://<host>:<poort>/<databank>?user=<gebruiker>&password=<wachtwoord>
```

**Windows (PowerShell):**
```powershell
$env:QWIXX_DB_URL = "jdbc:mysql://localhost:3306/qwixx?user=root&password=..."
```

**macOS / Linux:**
```bash
export QWIXX_DB_URL="jdbc:mysql://localhost:3306/qwixx?user=root&password=..."
```

### 3. Starten

```bash
# Grafische versie
./mvnw clean javafx:run

# Tests uitvoeren
./mvnw test
```

Op Windows gebruik je `mvnw.cmd` in plaats van `./mvnw`.

## Wat ik geleerd heb

Naast Java en databanken heb ik vooral geleerd hoe belangrijk goede communicatie en duidelijke afspraken in een team zijn. Een planning werkt pas als ze voor iedereen klopt en als je ze ook echt opvolgt.

## Over dit project

Dit project is gemaakt in groep, waarbij elk teamlid een gelijke rol had, in het kader van het eerste jaar Toegepaste Informatica aan HoGent. Deze repository is een persoonlijke kopie voor mijn portfolio. De originele databank van de opleiding is hier niet aan gekoppeld.
