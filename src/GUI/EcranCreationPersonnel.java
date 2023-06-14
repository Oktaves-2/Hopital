package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            pstmt.setString(7, tfprenom.getText().charAt(tfprenom.getText().length()-1) + randomNum + "" + tfnom.getText().charAt(0));
            pstmt.setString(8, tfprof.getText());
            pstmt.setString(9, tfrole.getText());
            pstmt.executeUpdate();
            laberr.setText("Personnel bien enregistré");
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
                if (tfprof.getText().trim().isEmpty())

                    return;
                String id = (tfprof.getText().charAt(0) + "" + Character.toUpperCase(newValue.charAt(0)));
                int randomNum = 0;
                boolean inexistant = false;
                while (inexistant == false) {
                    try (Connection conn = LienBase.OuvertureConnection()) {
                        randomNum = ThreadLocalRandom.current().nextInt(0, 999);
                        PreparedStatement pstmt = conn
                                .prepareStatement("SELECT idPatient from patients where idPatient = ?");
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