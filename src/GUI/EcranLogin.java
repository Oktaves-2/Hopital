package GUI;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import Base.*;

/**
 * La classe effectue une recherche SQl pour chercher une correspondance avec un
 * compte de la db si le compte existe,
 * le resulset de la requete est converti en cachedrowset
 * afin de pouvoir être accedé dans les ecrans suivi apres avoir ferme la
 * connexion avec la baseresulte de la recherche SQL est assigne
 * au stage fxml par la methode setuserdata afin de conserver en continu les
 * informations du compte actif.
 */
public class EcranLogin {

    @FXML
    private TextField tfid;
    @FXML
    private TextField tfmps;
    @FXML
    private Label laberreurmp;

    public int Verification(ActionEvent ev) throws SQLException, NullPointerException, IOException {
        Connection conn = LienBase.OuvertureConnection();

        try {
            String identree = tfid.getText();
            String mpentree = tfmps.getText();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * from personnel where id = ?");
            pstmt.setString(1, identree);
            ResultSet rs = pstmt.executeQuery();
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rwLogin = factory.createCachedRowSet();
            rwLogin.populate(rs);
            rwLogin.next();
            if (mpentree.equals(rwLogin.getString("mpasse"))) {
                String prof = rwLogin.getString("profession");
                LienBase.FermetureConnection(conn);
                Donnees donnees = new Donnees();
                donnees.setrwLogin(rwLogin);
                ((Node) ev.getSource()).getScene().getWindow().setUserData(donnees);
                Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), prof);
                return 0;
            } else if (!mpentree.equals(rwLogin.getString("mpasse")))
                laberreurmp.setText("Mot de passe non concordant");
        } catch (Exception e) {
            laberreurmp.setText("Aucun compte existant à cet identifiant");
        }
        LienBase.FermetureConnection(conn);
        return -1;
    }

    public void Stats(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Statistiques");
    }
}