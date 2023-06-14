package GUI;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.sql.rowset.CachedRowSet;
import Base.LienBase;

public class EcranPatient implements Initializable {
    @FXML
    private Label labcoordones, labobserver, labprof, laberr;
    @FXML
    private ListView<String> listcon;
    @FXML
    private TextArea areaord;
    @FXML
    private Button rtord;
    @FXML
    private TextField tfindex;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
        CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecte en tant que Médecin (" + rw1.getString("id") + ")");
        areaord.setVisible(false);
        rtord.setVisible(false);
        laberr.setVisible(false);
        labcoordones.setText(
                "Patient: " + rw.getString("prenom") + " " + rw.getString("nom") + " (" + rw.getString("idPatient")
                        + ")\nNaissance: " + rw.getDate("naissance")
                        + "\nAdresse: " + rw.getString("adresse")
                        + "\nEmail: " + rw.getString("email"));
        try (Connection conn = LienBase.OuvertureConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * from consultations c, patients p where nom = ? and p.idPatient = c.idPatient");
            pstmt.setString(1, rw.getString("nom"));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listcon.getItems().add("Id: " + rs.getString("idcons") + " Date: " + rs.getString("creation"));
            }
            LienBase.FermetureConnection(conn);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void Retour(ActionEvent ev) throws IOException, SQLException {
        try {
            Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Medecin");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void ApparitionOrds(ActionEvent ev) {
        areaord.setVisible(false);
        rtord.setVisible(false);
        listcon.setVisible(true);
        labobserver.setText("Observer/modifier une consultation");

    }

    public void LancementExport(ActionEvent ev) throws IOException, SQLException {
        try {
            String[] s = tfindex.getText().split(",");
            List<String> liste = listcon.getItems();

            for (int i = 0; i < s.length; i++) {
                int n = Integer.parseInt(s[i]);
                String id = liste.get(n - 1).substring(4, 10);
                Connection conn = LienBase.OuvertureConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT * from consultations c, personnel p, patients pa where idcons = ? and c.idMedecin = p.id and c.idPatient = pa.idPatient");
                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                System.out.println("\n\n------Impression consultation no: " + rs.getString("idcons")
                        + "------\n\n Créée le " + rs.getDate("creation")
                        + "\n Medecin: " + rs.getString("p.nom") + " " + rs.getString("prenom")
                        + "\n Patient: " + rs.getString("pa.nom") + " " + rs.getString("pa.prenom")
                        + "\n prescription: " + rs.getString("prescription"));
            }

        } catch (IndexOutOfBoundsException e) {
            laberr.setText("Entier entr\u00E9 sup\u00E9rieur \r\n" + //
                    "au nombre de \r\n" + //
                    "consultations disponibles");
            return;
        } catch (Exception e) {
            laberr.setText("Insertion de donnees invalides\r\nentrez des entiers \r\n" +
                    "separes par des virgule");
            return;

        }

        Retour(ev);
    }

    public void NouvelleConsultation(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "CreationConsultation");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            CachedRowSet rw = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwPat();
            CachedRowSet rw1 = ((Donnees) labcoordones.getScene().getWindow().getUserData()).getrwLogin();
            labprof.setText("Connecte en tant que Médecin (" + rw1.getString("id") + ")");
            areaord.setVisible(false);
            rtord.setVisible(false);
            labcoordones.setText(
                    "Patient: " + rw.getString("prenom") + " " + rw.getString("nom") + " (" + rw.getString("idPatient")
                            + ")\nNaissance: " + rw.getDate("naissance")
                            + "\nAdresse: " + rw.getString("adresse")
                            + "\nEmail: " + rw.getString("email"));
            Connection conn = LienBase.OuvertureConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT * from consultations c, patients p where nom = ? and p.idPatient = c.idPatient");
            pstmt.setString(1, rw.getString("nom"));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listcon.getItems().add("Id: " + rs.getString("idcons") + " Date: " + rs.getString("creation"));
            }
            LienBase.FermetureConnection(conn);

        } catch (Exception e) {
            System.out.println(e);
        }
        listcon.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                listcon.setVisible(false);
                areaord.setVisible(true);
                rtord.setVisible(true);
                try (Connection conn = LienBase.OuvertureConnection()) {
                    PreparedStatement pstmt = conn.prepareStatement(
                            "SELECT prescription from consultations c where idcons = ?");
                    pstmt.setString(1, listcon.getSelectionModel().getSelectedItem().substring(4, 10));
                    ResultSet rs = pstmt.executeQuery();
                    rs.next();
                    String s = rs.getString("prescription");
                    List<String> parts = new ArrayList<String>();
                    int len = s.length();
                    for (int i = 0; i < len; i += 53) {
                        parts.add(s.substring(i, Math.min(len, i + 53)) + "\n");
                    }
                    String texte = "";
                    for (int i = 0; i < parts.size(); i++) {
                        texte = texte + parts.get(i);

                    }
                    areaord.setText(texte);
                } catch (SQLException e) {
                    System.out.println(e);
                }
                labobserver.setText(listcon.getSelectionModel().getSelectedItem().substring(3));
            }
        });
    }
}