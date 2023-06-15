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
 * l'ecran permettant de modifier les informations d'un membre du personnel supprimer un
 * membre du personnel ne necessite le lancement d'aucun ecran et renvoi donc au
 * precedent
 */
public class EcranModifVuePers {
    @FXML
    private Label labcoordones, labprof;
    @FXML
    private Button btmodifier, btsupprimer;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
        CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecté en tant qu'agent (" + rw1.getString("id") + ") [" + rw1.getString("role") + "]");
        labcoordones.setText(
                "Personnel: " + rw.getString("prenom") + " " + rw.getString("nom") + " (" + rw.getString("id")
                        + ")\nNaissance: " + rw.getDate("naissance")
                        + "\nAdresse: " + rw.getString("adresse")
                        + "\nEmail: " + rw.getString("email")
                        + "\nProfession: " + rw.getString("profession")
                        + "\nRole: " + rw.getString("role"));
    }

    public void Retour(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "AgentPers");
    }

    public void Supprimer(ActionEvent ev) {
        try {
            Connection conn = LienBase.OuvertureConnection();
            PreparedStatement pstmt = conn
                    .prepareStatement("DELETE FROM personnel where id = ?");
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
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "ModifierInformationsPers");

    }
}
