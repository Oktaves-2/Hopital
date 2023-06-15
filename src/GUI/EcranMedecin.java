package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import Base.LienBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * La classe assigne au stage une seconde donnee qui est le cachedrowset sql
 * contenant les informations du patients recherché
 * Elle propose 3 plateformes de recherches différentes qui servent toutes
 * d'intermediaires vers l'écran patient qui permet de gerer les consultations
 * du patient choisi.
 */
public class EcranMedecin implements Initializable {

    @FXML
    private TextField tfnom, tfprenom, tfidm, tfpath;
    @FXML
    private Label laberreurmed, labprof;
    @FXML
    private ListView<String> listreg;
    @FXML
    private Button Examnom, Examid, Creer;
    private String entree1;
    private String entree2;
    private PreparedStatement pstmt;
    private Statement stmt;
    int x;

    public void remplissageInformations() throws SQLException {

        CachedRowSet rw = ((Donnees) labprof.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecte en tant que: " + rw.getString("profession") + " ("
                + rw.getString("id") + ")");

    }

    public int RechercheParCoordones(ActionEvent ev) throws SQLException, NullPointerException, IOException {
        Connection conn = LienBase.OuvertureConnection();
        try {
            if (ev.getSource() == Examnom) {
                entree1 = tfnom.getText();
                entree2 = tfprenom.getText();
                pstmt = conn
                        .prepareStatement(
                                "SELECT * from patient where nom = ?");
                pstmt.setString(1, entree1);
            } else if (ev.getSource() == Examid) {
                entree1 = tfidm.getText();
                pstmt = conn
                        .prepareStatement(
                                "SELECT * from patient where idPatient = ?");
                pstmt.setString(1, entree1);
            }
            ResultSet rs = pstmt.executeQuery();
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rw = factory.createCachedRowSet();
            rw.populate(rs);
            ((Donnees) labprof.getScene().getWindow().getUserData()).setrwPat(rw);
            rw.next();
            if (entree1.equals(rw.getString("idPatient")) || entree2.equals(rw.getString("prenom"))) {
                LienBase.FermetureConnection(conn);
                Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Patient");
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        laberreurmed.setText("Aucun compte existant pour ces coordones");
        LienBase.FermetureConnection(conn);
        return -1;
    }

    public void SelectionListe() throws IOException, SQLException {
        String nomprenom = listreg.getSelectionModel().getSelectedItem();
        String[] npsepare = nomprenom.split(" ");
        entree1 = npsepare[0];
        entree2 = npsepare[1];
        Connection conn = LienBase.OuvertureConnection();
        pstmt = conn
                .prepareStatement(
                        "SELECT * from patient where nom = ?");
        pstmt.setString(1, entree1);
        ResultSet rs = pstmt.executeQuery();
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rw = factory.createCachedRowSet();
        rw.populate(rs);
        ((Donnees) labprof.getScene().getWindow().getUserData()).setrwPat(rw);
        rw.next();
        LienBase.FermetureConnection(conn);
        try {
            Interfaces.ChangementEcran(listreg.getScene(), "Patient");
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void RetourLog(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Login");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection conn = LienBase.OuvertureConnection()) {

            stmt = conn.createStatement();
            String sql = "SELECT * FROM patient";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                listreg.getItems().add(rs.getString("nom") + " " + rs.getString("prenom"));
            }
            LienBase.FermetureConnection(conn);
            listreg.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

                @Override
                public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
                    try {
                        SelectionListe();
                    } catch (IOException | SQLException e) {
                        System.out.println(e);
                    }
                }
            });
        } catch (SQLException e) {
            System.out.println(e);
        }
        tfpath.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                listreg.getItems().clear();
                try (Connection conn = LienBase.OuvertureConnection()) {
                    if (newValue.trim().isEmpty()) {
                        pstmt = conn
                                .prepareStatement(
                                        "SELECT DISTINCT p.prenom, p.nom from patient p");
                    } else {
                        pstmt = conn
                                .prepareStatement(
                                        "SELECT DISTINCT p.prenom, p.nom, p.idPatient, pa.nom from patient p, pathologie pa, malade m where pa.nom LIKE ? AND pa.idPathologie = m.idPathologie And p.idPatient = m.idPatient");
                        pstmt.setString(1, newValue + "%");
                    }
                    ResultSet rs = pstmt.executeQuery();

                    while (rs.next()) {
                        if (newValue.trim().isEmpty()) {
                            listreg.getItems().add(
                                    rs.getString("p.nom") + " " + rs.getString("p.prenom"));

                        } else {
                            listreg.getItems().add(
                                    rs.getString("p.nom") + " " + rs.getString("p.prenom") + " ("
                                            + rs.getString("pa.nom") + ")");
                        }
                    }

                    RowSetFactory factory = RowSetProvider.newFactory();
                    CachedRowSet rwPat = factory.createCachedRowSet();
                    rwPat.populate(rs);
                    ((Donnees) labprof.getScene().getWindow().getUserData()).setrwPat(rwPat);
                    LienBase.FermetureConnection(conn);
                } catch (Exception e) {
                    System.out.println(e);
                }

            }
        });
    }

}
