package GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import Base.LienBase;
/**
 * Cet ecran au faible nombre de methodes est juste un intermediaire vers
 * l'ecran permettant de modifier les informations d'un patient supprimer un
 * patien ne necessite le lancement d'aucun ecran et renvoi donc au
 * precedent
 */
public class EcranModifVuePatient {
    @FXML
    private Label labcoordones, labprof;
    @FXML
    private Button btmodifier, btsupprimer;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
        CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecte en tant qu'Agent(" + rw1.getString("id")+")");
        labcoordones.setText(
                "Patient: " + rw.getString("prenom") + " " + rw.getString("nom") + " (" + rw.getString("idPatient")
                        + ")\nNaissance: " + rw.getDate("naissance")
                        + "\nAdresse: " + rw.getString("adresse")
                        + "\nEmail: " + rw.getString("email"));
    }

    public void Retour(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Agent");
    }

    public void Supprimer(ActionEvent ev) {
        try {
            Connection conn = LienBase.OuvertureConnection();
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM patient where idPatient = ?");
            String id = "";
            id = labcoordones.getText();
            id = id.substring(id.lastIndexOf("(") + 1, id.lastIndexOf(")"));
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            Retour(ev);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void ModifierInformations(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "ModifierInformations");

    }
}
