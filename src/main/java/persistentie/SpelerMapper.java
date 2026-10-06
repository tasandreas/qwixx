package persistentie;

import domein.Speler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpelerMapper {

    // Insert een speler in de databank. De ? betekent dat het nog mmoet worden ingevuld.
    private static final String INSERT_SPELER = "INSERT INTO speler(Gebruikersnaam, Geboortejaar) values (?, ?)";

    //Geeft de speler terug.
    private static final String GEEF_SPELER = "SELECT Gebruikersnaam, Geboortejaar FROM speler where Gebruikersnaam = ?";
    private static final String GEEF_SPELERS = "SELECT Gebruikersnaam, Geboortejaar FROM speler";
    private static final String VERWIJDER_SPELER = "DELETE FROM speler WHERE Gebruikersnaam = ?";

    public void voegToe(Speler speler) {
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {

            // Hier vul ik de Vraagtekens in voor de insert Speler.
            query.setString(1, speler.getGebruikersnaam());
            query.setInt(2, speler.getGeboortejaar());
            query.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Speler geefSpeler(String gebruikersnaam) {
        Speler speler = null;
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(GEEF_SPELER)) {

            // Hier stop ik de gebruikernaam gegeven in de methode in de string van geefSpeler.
            query.setString(1, gebruikersnaam);
            //Hier stop ik het resultaat van de executeQuery in een Resultset rs en haal ik de dataypes eruit en steek ze en de juiste variabele.
            try (ResultSet rs = query.executeQuery()) {
                if (rs.next()) {
                    String naam = rs.getString("Gebruikersnaam");
                    int geboortejaar = rs.getInt("Geboortejaar");
                    // als de datatypes kloppen maak ik een nieuwe speler aan. ik maak er een aan omdze dan te kunnen gebruiken en door te geven anders hebben we gewoon wat data.
                    try {
                        speler = new Speler(naam, geboortejaar);
                    } catch (IllegalArgumentException ex) {
                        speler = null;
                    }
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return speler;
    }

    public List<Speler> geefSpelers() {
        List<Speler> spelers = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(GEEF_SPELERS);
             ResultSet rs = query.executeQuery()) {

            while (rs.next()) {
                String gebruikersnaam = rs.getString("Gebruikersnaam");
                int geboortejaar = rs.getInt("Geboortejaar");

                try {
                    spelers.add(new Speler(gebruikersnaam, geboortejaar));
                } catch (IllegalArgumentException ex) {
                    // Oude databankgegevens kunnen buiten de huidige leeftijdsgrenzen vallen.
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return spelers;
    }
    public void verwijder(String gebruikersnaam) {
        try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
             PreparedStatement query = conn.prepareStatement(VERWIJDER_SPELER)) {
            query.setString(1, gebruikersnaam);
            query.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}

//public Speler geefSpelerDummy(String gebruikersnaam) {
//  return new Speler(gebruikersnaam, 1994);
//}

