package domein;

import java.util.ResourceBundle;

final class Vertaling {

    static String tekst(String key, Object... args) {
        String tekst = ResourceBundle.getBundle("messages").getString(key);
        return args.length == 0 ? tekst : String.format(tekst, args);
    }
}
