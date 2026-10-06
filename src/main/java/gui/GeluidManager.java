package gui;

import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

/**
 * Beheert de korte geluideffecten van de grafische versie van Qwixx.
 * De manager laadt de geluidsbestanden uit de resources-map en speelt ze af
 * zonder dat de rest van de GUI moet weten waar die bestanden staan.
 */
public final class GeluidManager {

    private static final Map<GeluidEffect, AudioClip> GELUIDEN = new EnumMap<>(GeluidEffect.class);

    static {
        for (GeluidEffect effect : GeluidEffect.values()) {
            laadGeluid(effect);
        }
    }

    private GeluidManager() {
    }

    /**
     * Speelt het korte geluid af dat hoort bij het rollen van de dobbelstenen.
     */
    public static void speelDobbelstenenRollen() {
        speel(GeluidEffect.DOBBELSTENEN_ROLLEN);
    }

    /**
     * Speelt een kort selectiegeluid af wanneer de speler een dobbelsteen- of scorekeuze aanklikt.
     */
    public static void speelDobbelsteenSelecteren() {
        speel(GeluidEffect.DOBBELSTEEN_SELECTEREN);
    }

    /**
     * Speelt een positief geluid af wanneer een geldige score wordt ingevuld.
     */
    public static void speelScoreIngevuld() {
        speel(GeluidEffect.SCORE_INGEVULD);
    }

    /**
     * Speelt een foutgeluid af wanneer de speler een ongeldige actie probeert.
     */
    public static void speelOngeldigeActie() {
        speel(GeluidEffect.ONGELDIGE_ACTIE);
    }

    /**
     * Speelt het overwinningsgeluid af wanneer het spel afgelopen is.
     */
    public static void speelSpelGewonnen() {
        speel(GeluidEffect.SPEL_GEWONNEN);
    }

    /**
     * Speelt het gevraagde geluideffect af als het correct geladen kon worden.
     *
     * @param effect het geluideffect dat afgespeeld moet worden
     */
    public static void speel(GeluidEffect effect) {
        AudioClip clip = GELUIDEN.get(effect);
        if (clip != null) {
            clip.play();
        }
    }

    private static void laadGeluid(GeluidEffect effect) {
        URL geluidUrl = GeluidManager.class.getResource(effect.getBestandspad());
        if (geluidUrl != null) {
            GELUIDEN.put(effect, new AudioClip(geluidUrl.toExternalForm()));
        }
    }
}
