package GUI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ThreadLocalRandom;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;

import javax.sql.rowset.CachedRowSet;
import Base.LienBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EcranCreationConsultation {

    @FXML
    private TextArea txtord;
    @FXML
    private Label labcoordones, labprof;
    @FXML
    private TextField tfidcons, tfcout;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
        CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecte en tant que Médecin (" + rw1.getString("id")+")");
        labcoordones.setText(
                "Patient: " + rw.getString("prenom") + " " + rw.getString("nom") + " (" + rw.getString("idPatient")
                        + ")\nNaissance: " + rw.getDate("naissance")
                        + "\nAdresse: " + rw.getString("adresse")
                        + "\nEmail: " + rw.getString("email"));
        String id = ("G" + Integer.toString(Year.now().getValue()).substring(2));
        int randomNum = 0;
        boolean inexistant = false;
        while (inexistant == false) {
            try (Connection conn = LienBase.OuvertureConnection()) {
                randomNum = ThreadLocalRandom.current().nextInt(0, 999);
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT idcons from consultations where idcons = ?");
                pstmt.setString(1, id + String.format("%03d", randomNum));
                ResultSet rs = pstmt.executeQuery();
                if (!rs.next()) {
                    inexistant = true;
                    id = id + String.format("%03d", randomNum);
                    tfidcons.setText(id);

                }
            } catch (SQLException e) {
                System.out.println(e);
            }
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
                            "INSERT INTO consultations VALUES (?,?,Null,?,?,?,?)");
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
            pstmt.executeUpdate();
            LienBase.FermetureConnection(conn);
            RetourPat(ev);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
