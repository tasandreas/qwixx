package exceptions;

import java.util.ResourceBundle;

public class GebruikersnaamInGebruikException extends RuntimeException {
  public GebruikersnaamInGebruikException() {
    super(ResourceBundle.getBundle("messages").getString("exception.usernameInUse"));
  }

  public GebruikersnaamInGebruikException(String message) {
    super(message);
  }
}
