package gui;

/**
 * Somt alle korte geluideffecten op die de GUI kan afspelen.
 * Elk effect verwijst naar een mp3-bestand in {@code src/main/resources/sounds}.
 */
public enum GeluidEffect {
    DOBBELSTENEN_ROLLEN("/sounds/dice_roll.mp3"),
    DOBBELSTEEN_SELECTEREN("/sounds/dice_select.mp3"),
    SCORE_INGEVULD("/sounds/score_success.mp3"),
    ONGELDIGE_ACTIE("/sounds/invalid_action.mp3"),
    SPEL_GEWONNEN("/sounds/game_won.mp3");

    private final String bestandspad;

    GeluidEffect(String bestandspad) {
        this.bestandspad = bestandspad;
    }

    /**
     * Geeft het resourcepad van het geluidsbestand terug.
     *
     * @return het absolute resourcepad binnen de applicatie
     */
    public String getBestandspad() {
        return bestandspad;
    }
}
