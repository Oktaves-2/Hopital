package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
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
 * Cette classe est similaire a la classe creationpatient à l'exception que
 * puisque celle ci permet une insertion dans la table personnel elle demande un
 * role
 * superAdmin. en plus du nom du membre et d'un integer aléatoire, l'identifiant
 * utilise la premiere
 * lettre de leur profession ce qui nécessite d'écouter les changement de chacun
 * des deux textfield (a travers un seul changelistener )
 * a la maniere du fonctionnement de plusieurs sites modernes, le mot de passe
 * est généré aléatoirement de manière opaque à partir de données du patient.
 */
public class EcranCreationPersonnel implements Initializable {
    @FXML
    private DatePicker dpnaissance;
    @FXML
    private TextField tfnom, tfprenom, tfadresse, tfemail, tfidentifiant, tfprof, tfrole;
    @FXML
    private Button Creer, btretourlog;
    @FXML
    private Label laberr, labprof;

    public void RemplissageInformations() throws SQLException {

        CachedRowSet rw = ((Donnees) labprof.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecté en tant qu'agent (" + rw.getString("id") + ") [" + rw.getString("role") + "]");
    }

    public void Creation(ActionEvent ev) throws SQLException {
        if (tfnom.getText().trim().isEmpty() || tfprenom.getText().trim().isEmpty()
                || tfadresse.getText().trim().isEmpty()
                || tfemail.getText().trim().isEmpty() || tfadresse.getText().trim().isEmpty()) {
            laberr.setText("Un ou plusieurs champs sont vides");
            return;
        }
        try {
            Connection conn = LienBase.OuvertureConnection();
            PreparedStatement pstmt = conn
                    .prepareStatement("INSERT INTO personnel VALUES(?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, tfidentifiant.getText());
            pstmt.setString(2, tfnom.getText());
            pstmt.setString(3, tfprenom.getText());
            pstmt.setDate(4, java.sql.Date.valueOf(dpnaissance.getValue()));
            pstmt.setString(5, tfadresse.getText());
            pstmt.setString(6, tfemail.getText());
            int randomNum = 0;
            randomNum = ThreadLocalRandom.current().nextInt(0, 999);
            pstmt.setString(7, tfprenom.getText().charAt(tfprenom.getText().length() - 1) + randomNum + ""
                    + tfnom.getText().charAt(0));
            pstmt.setString(8, tfprof.getText());
            pstmt.setString(9, tfrole.getText());
            pstmt.executeUpdate();
            laberr.setText("Personnel bien enregistré");
            laberr.setVisible(true);

            LienBase.FermetureConnection(conn);
            Thread.sleep(2000);
            RetourPat(ev);

        } catch (Exception e) {
            laberr.setText("Echec, Au moins un champ contient \r\n" + //
                    "une valeurs incompatibe");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChangeListener<String> cl = new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    if (tfprof.getText().trim().isEmpty() || tfnom.getText().trim().isEmpty())
                        return;
                    String id = (Character.toUpperCase(tfprof.getText().charAt(0)) + ""
                            + Character.toUpperCase(tfnom.getText().charAt(0)));
                    int randomNum = 0;
                    boolean inexistant = false;
                    while (inexistant == false) {
                        try (Connection conn = LienBase.OuvertureConnection()) {
                            Random r = new Random();
                            randomNum = r.nextInt((999 - 1) + 1) + 1;
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
                } catch (Exception e) {

                }
            }
        };

        tfnom.textProperty().addListener(cl);
        tfprof.textProperty().addListener(cl);
    }

    public void RetourPat(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "AgentPers");
    }
}