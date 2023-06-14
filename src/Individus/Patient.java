package Individus;

import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Patient extends Personne {
    String idPatient;
    @FXML
    private Button retourButton;

    public Patient(String idPatient, String nom, String prenom, Date naissance, String adresse, String email,
            String mpasse) {
        super(nom, prenom, naissance, adresse, email, mpasse);
        this.idPatient = idPatient;
    }

    public String getidPatient() {
        return this.idPatient;
    }

}
