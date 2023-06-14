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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class EcranAgentPers
        implements Initializable {

    @FXML
    private TextField tfnom, tfprenom, tfidm;

    @FXML
    private Label laberreurmed, labprof;
    @FXML
    private ListView<String> listreg;
    @FXML
    private Button Examnom, Examid, Creer;
    @FXML
    private MenuButton menurecherche;
    private String entree1;
    private String entree2;
    private PreparedStatement pstmt;
    private Statement stmt;

    public void RemplissageInformations() throws SQLException {
        CachedRowSet rw = ((Donnees) labprof.getScene().getWindow().getUserData()).getrwLogin();
        labprof.setText("Connecté en tant qu'agent (" + rw.getString("id") + ") [" + rw.getString("role") + "]");
        if (!rw.getString("role").equals("SuperAdmin"))
            menurecherche.setVisible(false);
        MenuItem recherchepat = new MenuItem("RecherchePatient");
        menurecherche.getItems().clear();
        menurecherche.getItems().add(recherchepat);
        recherchepat.setOnAction(eventreccpers);
    }

    EventHandler<ActionEvent> eventreccpers = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent ev) {
            try {
                Interfaces.ChangementEcran((labprof).getScene(), "Agent");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    public int RechercheParCoordones(ActionEvent ev) throws SQLException, NullPointerException, IOException {
        Connection conn = LienBase.OuvertureConnection();
        try {
            if (ev.getSource() == Examnom) {
                entree1 = tfnom.getText();
                entree2 = tfprenom.getText();
                pstmt = conn
                        .prepareStatement(
                                "SELECT * from personnel where nom = ?");
                pstmt.setString(1, entree1);
            } else if (ev.getSource() == Examid) {
                entree1 = tfidm.getText();
                pstmt = conn
                        .prepareStatement(
                                "SELECT * from personnel where id = ?");
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
                Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "ModifVuePers");
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        laberreurmed.setText("Aucun compte existant pour ces coordones");
        LienBase.FermetureConnection(conn);
        return -1;
    }

    public void CreerCompte(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "CreationPersonnel");
    }

    public void SelectionListe() throws IOException, SQLException {
        String nomprenom = listreg.getSelectionModel().getSelectedItem();
        String[] npsepare = nomprenom.split(" ");
        entree1 = npsepare[0];
        entree2 = npsepare[1];
        Connection conn = LienBase.OuvertureConnection();
        pstmt = conn
                .prepareStatement(
                        "SELECT * from personnel where nom = ?");
        pstmt.setString(1, entree1);
        ResultSet rs = pstmt.executeQuery();
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rw = factory.createCachedRowSet();
        rw.populate(rs);
        ((Donnees) labprof.getScene().getWindow().getUserData()).setrwPat(rw);
        rw.next();
        LienBase.FermetureConnection(conn);
        Interfaces.ChangementEcran(listreg.getScene(), "ModifVuePers");
    }

    public void RetourLog(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Login");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try (Connection conn = LienBase.OuvertureConnection()) {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM personnel";
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
    }

}
