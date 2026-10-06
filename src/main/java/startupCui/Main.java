package startupCui;

import cui.QwixxApplicatie;
import domein.DomeinController;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ResourceBundle nlBundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("nl"));
        ResourceBundle enBundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("en"));

        System.out.println(nlBundle.getString("language.choose") + " / " + enBundle.getString("language.choose"));
        System.out.println(nlBundle.getString("language.optionDutch"));
        System.out.println(enBundle.getString("language.optionEnglish"));

        int keuze;
        try {
            keuze = Integer.parseInt(scanner.nextLine());
            if (keuze != 1 && keuze != 2) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(nlBundle.getString("language.invalid") + "\n" + enBundle.getString("language.invalid"));
            keuze = 1;
        }

        Locale locale;
        if (keuze == 2) {
            locale = Locale.forLanguageTag("en");
        } else {
            locale = Locale.forLanguageTag("nl");
        }
        Locale.setDefault(locale);

        DomeinController dc = new DomeinController();
        QwixxApplicatie applicatie = new QwixxApplicatie(dc);

        applicatie.run();
    }
}
