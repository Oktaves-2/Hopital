package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.sql.rowset.CachedRowSet;
import Base.LienBase;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * La classe alterne entre deux listview,la methode selectionlistecons() fait
 * apparaitre la deuxieme en fonction d'un resultat sql
 * de la selection d'un appareil dans la deuxieme liste la methode
 * selectionlistapp update l'attribut statut_appareil de la table consultation a
 * "assigné", et l'appareil assigné voit ses attributs idpatient et octroyé (un
 * boolean)
 * update égalements. il a ete necessaire d'implementer la methode runlater de
 * la classe runnable dans le changelistener de la premiere liste car modifier
 * la listview depuis un thread non main entrainait une exception.
 */
public class EcranTechnicien implements Initializable {

    @FXML
    private TextField tfid;

    @FXML
    private Label labprof, labmulti, labapp;
    @FXML
    private ListView<String> listcons, listapp;
    private PreparedStatement pstmt;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labprof.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecté en tant que technicien (" + rw.getString("id"));
        labapp.setText("Selectionnez une consultation pour octroyer un appareil");
        labapp.setVisible(false);
        listapp.setVisible(false);
    }

    public void SelectionListecons() throws IOException, SQLException {
        try {
            Connection conn = LienBase.OuvertureConnection();
            tfid.setVisible(false);
            String nomappareil = listcons.getSelectionModel().getSelectedItem().substring(63);
            pstmt = conn
                    .prepareStatement("SELECT * from appareil where type_appareil = ? AND octroye = 0");
            pstmt.setString(1, nomappareil);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
                listapp.getItems().add(nomappareil + " no: " + rs.getString("idappareil"));

            LienBase.FermetureConnection(conn);
            labapp.setText("Octroyer un appareil pour cette consultation parmi ceux disponibles");
            labapp.setVisible(true);
            listapp.setVisible(true);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void SelectionListeapp() {
        try {
            if (labapp.getText().equals("Appareil bien assigne"))
                return;
            Connection conn = LienBase.OuvertureConnection();
            String idappareil = listapp.getSelectionModel().getSelectedItem();
            idappareil = idappareil.substring(idappareil.lastIndexOf(":") + 2);
            String idcons = listcons.getSelectionModel().getSelectedItem().substring(17, 23);
            String idPatient = listcons.getSelectionModel().getSelectedItem().substring(36, 41);
            pstmt = conn.prepareStatement("UPDATE consultation set statut_appareil = ? where idcons = ?");
            pstmt.setString(1, "assigne");
            pstmt.setString(2, idcons);
            pstmt.executeUpdate();
            pstmt = conn
                    .prepareStatement("UPDATE appareil SET octroye = 1, idPatient = ? where idappareil = ?");
            pstmt.setString(1, idPatient);
            pstmt.setString(2, idappareil);
            pstmt.executeUpdate();
            tfid.setVisible(true);
            tfid.setText("");
            Remplirliste();
            listapp.getItems().clear();
            LienBase.FermetureConnection(conn);
            labapp.setText("Appareil bien assigne");
            labapp.setVisible(true);
            listapp.setVisible(false);
        } catch (Exception e) {
        }

    }

    public void RetourLog(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Login");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection conn = LienBase.OuvertureConnection()) {
            pstmt = conn.prepareStatement(
                    "SELECT * FROM consultation where assignation_appareil IS NOT null AND statut_appareil = ?");
            pstmt.setString(1, "a_assigner");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listcons.getItems().add("Id consultation: " + rs.getString("idcons") + " id patient: " +
                        rs.getString("idPatient") + " appareil a assigner: " + rs.getString("assignation_appareil"));
            }
            LienBase.FermetureConnection(conn);
            listcons.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SelectionListecons();
                            } catch (Exception e) {
                            }
                        }
                    });

                }
            });
            listapp.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SelectionListeapp();
                            } catch (Exception e) {
                            }
                        }

                    });

                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }

        tfid.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Remplirliste();
                        } catch (Exception e) {
                        }
                    }

                });

            }

        });
    }

    public void Remplirliste() {
        listcons.getItems().clear();
        try (Connection conn = LienBase.OuvertureConnection()) {
            pstmt = conn
                    .prepareStatement(
                            "SELECT * FROM consultation where assignation_appareil IS NOT null AND statut_appareil = ? AND idPatient LIKE ?");
            pstmt.setString(1, "a_assigner");
            pstmt.setString(2, tfid.getText() + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listcons.getItems().add("Id consultation:" + rs.getString("idcons") + " id patient: " +
                        rs.getString("idPatient") + " appareil a assigner: "
                        + rs.getString("assignation_appareil"));

            }
            LienBase.FermetureConnection(conn);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
