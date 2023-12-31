package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javax.sql.rowset.CachedRowSet;

import Base.LienBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Cet ecran permet de modifier les informations d'un patient, l'identifiant est
 * une valeur immuable, acceder à cet ecran demande le role SuperAdmin.
 * les modifications sont insérées dans la table. Les champs nuls ne sont pas
 * traités.
 */
public class EcranModifierInformationsPers implements Initializable {
    @FXML
    private DatePicker dpnaissance1;
    @FXML
    private TextField tfnom, tfprenom, tfadresse, tfemail, tfidentifiant, tfnaissance, tfrole;
    @FXML
    private TextField tfnom1, tfprenom1, tfadresse1, tfemail1, tfrole1, tfmpasse1;
    @FXML
    private Button Creer, btretourlog;
    @FXML
    private Label laberr, labprof;

    public void RemplissageInformations() throws SQLException {

        CachedRowSet rw = ((Donnees) labprof.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecté en tant qu'agent (" + rw.getString("id") + ") [" + rw.getString("role") + "]");
        CachedRowSet rw2 = ((Donnees) labprof.getScene().getWindow().getUserData()).getrwPat();
        tfnom.setText(rw2.getString("nom"));
        tfprenom.setText(rw2.getString("prenom"));
        tfadresse.setText(rw2.getString("adresse"));
        tfemail.setText(rw2.getString("email"));
        tfidentifiant.setText(rw2.getString("id"));
        tfnaissance.setText(rw2.getDate("naissance").toString());
        tfrole.setText(rw2.getString("role"));

    }

    public void MiseAJour(ActionEvent ev) throws SQLException {
        try {
            Connection conn = LienBase.OuvertureConnection();
            PreparedStatement pstmt;

            pstmt = conn
                    .prepareStatement(
                            "UPDATE personnel set nom = ?, prenom = ?, naissance = ?, adresse = ?, email = ?, role = ? where id = ?");

            if (tfnom1.getText().trim().isEmpty())
                pstmt.setString(1, tfnom.getText());
            else
                pstmt.setString(1, tfnom1.getText());
            if (tfprenom1.getText().trim().isEmpty())
                pstmt.setString(2, tfprenom.getText());
            else
                pstmt.setString(2, tfprenom1.getText());
            if (dpnaissance1.getValue() == null)
                pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.parse(tfnaissance.getText())));
            else
                pstmt.setDate(3, java.sql.Date.valueOf(dpnaissance1.getValue()));
            if (tfadresse1.getText().trim().isEmpty())
                pstmt.setString(4, tfadresse.getText());
            else
                pstmt.setString(4, tfadresse1.getText());
            if (tfemail1.getText().trim().isEmpty())
                pstmt.setString(5, tfemail.getText());
            else
                pstmt.setString(5, tfemail1.getText());
            if (tfrole1.getText().trim().isEmpty())
                pstmt.setString(6, tfrole.getText());
            else
                pstmt.setString(6, tfrole1.getText());

            pstmt.setString(7, tfidentifiant.getText());
            pstmt.executeUpdate();

            if (!tfmpasse1.getText().trim().isEmpty()) {
                pstmt = conn
                        .prepareStatement(
                                "UPDATE personnel set mpasse = ? where id = ?");
                pstmt.setString(1, tfmpasse1.getText());
                pstmt.setString(2, tfidentifiant.getText());
                pstmt.executeUpdate();
            }
            laberr.setText("Informations modifiées");
            laberr.setVisible(true);
            LienBase.FermetureConnection(conn);
            Thread.sleep(2000);
            RetourPat(ev);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tfnom.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String id = ("P" + Character.toUpperCase(newValue.charAt(0)));
                int randomNum = 0;
                boolean inexistant = false;
                while (inexistant == false) {
                    try (Connection conn = LienBase.OuvertureConnection()) {
                        randomNum = ThreadLocalRandom.current().nextInt(0, 999);
                        PreparedStatement pstmt = conn
                                .prepareStatement("SELECT id from personnel where id = ?");
                        pstmt.setString(1, id + String.format("%03d", randomNum));
                        ResultSet rs = pstmt.executeQuery();
                        if (!rs.next()) {
                            inexistant = true;
                            id = id + String.format("%03d", randomNum);
                        }
                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }
                tfidentifiant.setText(id);
            }
        });
    }

    public void RetourPat(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "AgentPers");
    }
}