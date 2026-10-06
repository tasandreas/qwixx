package dto;

import domein.Kleur;

import java.util.ResourceBundle;

public record DobbelsteenDTO(Kleur kleur, int aantalOgen){

    @Override
    public String toString() {
        return String.format(ResourceBundle.getBundle("messages").getString("console.die"), kleur, aantalOgen);
    }
}
