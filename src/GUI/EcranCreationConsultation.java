package GUI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Year;

import javax.sql.rowset.CachedRowSet;
import Base.LienBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Comme pour la creation d'un patient l'identifiant de la consultaiton est
 * généré automatiquement,
 * puisqu'il se base sur des données connues (date de création et profession du
 * createur de la consultation)
 * il ne necessite pas de changelistener et le textfield contenant l'id est
 * defini une seule fois à l'ouverture de l'ecran. La classe export imprime dans
 * la console plutôt qu'en sortie fichier pour une portabilité du projet
 * simplifié relativement aux accès et chemins de sortie. si un appareil est
 * assigné la consultation a l'attribut a_assigner avant que le technicien ne
 * l'octroye, sans appareil assigné l'attribut est null par défaut
 */
public class EcranCreationConsultation {

    @FXML
    private TextArea txtord;
    @FXML
    private Label labcoordones, labprof;
    @FXML
    private TextField tfidcons, tfcout, tfappareil;
    @FXML
    private MenuButton menuappareil;
    @FXML
    private ComboBox<String> comboappareil;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
        CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connécté en tant que médecin (" + rw1.getString("id") + ")");
        labcoordones.setText(
                "Patient: " + rw.getString("prenom") + " " + rw.getString("nom") + " (" + rw.getString("idPatient")
                        + ")\nNaissance: " + rw.getDate("naissance")
                        + "\nAdresse: " + rw.getString("adresse")
                        + "\nEmail: " + rw.getString("email"));
        String id = ("G" + Integer.toString(Year.now().getValue()).substring(2));
        int randomNum = 0;
        boolean inexistant = false;
        try (Connection conn = LienBase.OuvertureConnection()) {
            while (inexistant == false) {

                randomNum = ThreadLocalRandom.current().nextInt(0, 999);
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT idcons from consultation where idcons = ?");
                pstmt.setString(1, id + String.format("%03d", randomNum));
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    inexistant = true;
                    id = id + String.format("%03d", randomNum);
                    tfidcons.setText(id);
                }
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM type_appareil");
            ArrayList<String> liste = new ArrayList<String>();
            comboappareil.getItems().add("Aucun");
            while (rs.next()) {
                liste.add(rs.getString(1));
                comboappareil.getItems().add(rs.getString(1));
            }
            LienBase.FermetureConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void Export(ActionEvent ev) {
        System.out.println(txtord.getText());
    }

    public void RetourPat(ActionEvent ev) throws IOException, SQLException {
        try {
            Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Patient");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void Enregistrer(ActionEvent ev) throws SQLException {

        Connection conn = LienBase.OuvertureConnection();
        try {
            PreparedStatement pstmt = conn
                    .prepareStatement(
                            "INSERT INTO consultation VALUES (?,?,Null,?,?,?,?, ?)");
            CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
            CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
            pstmt.setString(1, tfidcons.getText());
            pstmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
            pstmt.setString(3, rw.getString("idPatient"));
            pstmt.setString(4, rw1.getString("id"));
            pstmt.setString(5, txtord.getText());
            if (!tfcout.getText().trim().isEmpty())
                pstmt.setInt(6, Integer.parseInt(tfcout.getText()));
            else
                pstmt.setInt(6, 35);
            if (comboappareil.getValue() == "Aucun")
                pstmt.setString(7, null);
            else
                pstmt.setString(7, comboappareil.getValue());
            pstmt.executeUpdate();
            LienBase.FermetureConnection(conn);
            RetourPat(ev);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
