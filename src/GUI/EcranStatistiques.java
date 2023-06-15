package GUI;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import Base.LienBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

/**
 * La classe remplit chaque graphique a l'aide d'une requete sql dans la db et
 * l'affiche en fonction du choix dans la liste.
 * Chacune des graphiques utilise une classe fxml jugée adéquate, seul le
 * graphique ChartCasParAnParPaths emploient plusieurs séries de données pour
 * dissocier les cas par maladies
 */
public class EcranStatistiques implements Initializable {

    @FXML
    private LineChart<String, Number> ChartConsParAn;
    @FXML
    private LineChart<String, Number> ChartAppParAn;

    @FXML
    private LineChart<String, Integer> ChartCasParAnParPaths;

    @FXML
    private PieChart ChartConsParAge;

    @FXML
    private BarChart<String, Integer> ChartConsParPeriode;

    @FXML
    private ComboBox<String> Comboboxgraph;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Comboboxgraph.getItems().addAll("Nombre de consultations par tranche d'age",
                "Nombre de cas par pathologies par annees",
                "Nombres de consultations par an", "nombres de consultations par période de l'annee",
                "Nombres d'appareils assignes par an");
        ChartCasParAnParPaths.setVisible(false);
        ChartConsParPeriode.setVisible(false);
        ChartConsParAge.setVisible(false);
        ChartConsParAn.setVisible(false);
        ChartAppParAn.setVisible(false);
        try {
            CasParAnParPaths();
            ConsParAn();
            ConsParAges();
            ConsParPeriodes();
            AppParAn();
        } catch (SQLException e) {
            System.out.println(e);

        }
    }

    public void ChoixGraphique(ActionEvent e) {
        ChartCasParAnParPaths.setVisible(false);
        ChartConsParAn.setVisible(false);
        ChartConsParPeriode.setVisible(false);
        ChartConsParAge.setVisible(false);
        ChartAppParAn.setVisible(false);
        switch (Comboboxgraph.getSelectionModel().getSelectedItem()) {
            case ("Nombre de consultations par tranche d'age"):
                ChartConsParAge.setVisible(true);
                break;
            case ("Nombre de cas par pathologies par annees"):
                ChartCasParAnParPaths.setVisible(true);
                break;
            case ("Nombres de consultations par an"):
                ChartConsParAn.setVisible(true);
                break;
            case ("nombres de consultations par période de l'annee"):
                ChartConsParPeriode.setVisible(true);
                break;
            case ("Nombres d'appareils assignes par an"):
                ChartAppParAn.setVisible(true);
                break;
        }

    }

    private void ConsParPeriodes() throws SQLException {
        try (Connection conn = LienBase.OuvertureConnection()) {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT MONTH(creation) as mois FROM consultation";
            ResultSet rs = stmt.executeQuery(sql);
            int hiver = 0;
            int printemps = 0;
            int ete = 0;
            int automne = 0;
            while (rs.next()) {
                if (rs.getInt("mois") <= 2 || rs.getInt("mois") == 12)
                    hiver = hiver + 1;
                else if (rs.getInt("mois") >= 3 && rs.getInt("mois") < 6)
                    printemps = printemps + 1;
                else if (rs.getInt("mois") >= 06 && rs.getInt("mois") < 9)
                    ete = ete + 1;
                else
                    automne = automne + 1;
            }

            XYChart.Series<String, Integer> Series = new XYChart.Series<>();
            Series.getData().add(new XYChart.Data<String, Integer>("Hiver", hiver));
            Series.getData().add(new XYChart.Data<String, Integer>("Printemps", printemps));
            Series.getData().add(new XYChart.Data<String, Integer>("Eté", ete));
            Series.getData().add(new XYChart.Data<String, Integer>("Automne", automne));
            ChartConsParPeriode.getData().add(Series);

            LienBase.FermetureConnection(conn);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private void ConsParAges() {
        try (Connection conn = LienBase.OuvertureConnection()) {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT YEAR(naissance)as an, YEAR(c.creation) as cre from patient p, consultation c where c.idPatient = p.idPatient ";
            ResultSet rs = stmt.executeQuery(sql);
            int vingtcinq = 0;
            int cinquante = 0;
            int soixantequinze = 0;
            int cent = 0;
            while (rs.next()) {
                int age = rs.getInt("cre") - rs.getInt("an");
                if (age <= 25)
                    vingtcinq = vingtcinq + 1;
                else if (age <= 50)
                    cinquante = cinquante + 1;
                else if (age <= 75)
                    soixantequinze = soixantequinze + 1;
                else
                    cent = cent + 1;
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("1-25 Ans", vingtcinq),
                    new PieChart.Data("26-50 Ans", cinquante),
                    new PieChart.Data("51-75 Ans", soixantequinze),
                    new PieChart.Data("75 Ans +", cent));
            ChartConsParAge.setData(pieChartData);
            LienBase.FermetureConnection(conn);
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void CasParAnParPaths() throws SQLException {
        try (Connection conn = LienBase.OuvertureConnection()) {
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            TreeMap<Integer, TreeMap<String, Integer>> pathsParAns = new TreeMap<>();
            ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(p.nom) as compte, p.nom, YEAR(c.creation) as an from pathologie p, patient pa, malade m, consultation c where pa.idPatient = m.idPatient and p.idPathologie = m.idPathologie and c.idcons = m.idcons GROUP BY an, p.nom ORDER BY an");
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet rw = factory.createCachedRowSet();
            rw.populate(rs);
            ResultSet rs2 = stmt.executeQuery("Select nom from pathologie");

            for (int i = 2018; i <= Year.now().getValue(); i++) {
                TreeMap<String, Integer> pathparAn = new TreeMap<>();
                rw.beforeFirst();
                while (rw.next()) {
                    if (rw.getInt("an") == i) {
                    } else if (rw.getInt("an") > i) {
                        break;
                    }
                }
                pathsParAns.put(i, pathparAn);
            }
            while (rs2.next()) {
                XYChart.Series<String, Integer> Series = new XYChart.Series<>();
                Series.setName(rs2.getString("nom"));
                for (int i = 2018; i < Year.now().getValue(); i++) {
                    rw.beforeFirst();
                    boolean cas = false;
                    while (rw.next() && rw.getInt("an") <= i) {
                        if (rw.getString("nom").equals(rs2.getString("nom")) && rw.getInt("an") == (i)) {
                            Series.getData()
                                    .add(new XYChart.Data<>(Integer.toString(rw.getInt("an")),
                                            rw.getInt("compte")));
                            cas = true;
                            break;
                        }
                    }
                    if (cas == false)
                        Series.getData()
                                .add(new XYChart.Data<>(Integer.toString(i), 0));
                }
                ChartCasParAnParPaths.getData().add(Series);
            }

            LienBase.FermetureConnection(conn);
        }

    }

    public void ConsParAn() throws SQLException {
        try (Connection conn = LienBase.OuvertureConnection()) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT YEAR(creation) as an, COUNT(*) as compte FROM consultation group by YEAR(creation)";
            ResultSet rs = stmt.executeQuery(sql);
            XYChart.Series<String, Number> Series = new XYChart.Series<>();
            while (rs.next())
                Series.getData().add(new XYChart.Data<>(rs.getString("an"), rs.getInt("compte")));

            try {
                ChartConsParAn.getData().add(Series);
            } catch (Exception e) {
                System.out.println(e);
            }
            LienBase.FermetureConnection(conn);
        }

    }

    public void AppParAn() throws SQLException {
        try (Connection conn = LienBase.OuvertureConnection()) {
            Statement stmt = conn.createStatement();
            String sql = "SELECT YEAR(creation) as an, COUNT(*) as compte FROM consultation WHERE assignation_appareil IS NOT NULL group by YEAR(creation)";
            ResultSet rs = stmt.executeQuery(sql);
            XYChart.Series<String, Number> Series = new XYChart.Series<>();
            while (rs.next())
                Series.getData().add(new XYChart.Data<>(rs.getString("an"), rs.getInt("compte")));

            try {
                ChartAppParAn.getData().add(Series);
            } catch (Exception e) {
                System.out.println(e);
            }
            LienBase.FermetureConnection(conn);
        }

    }

    public void Retour(ActionEvent ev) throws IOException, SQLException {
        Interfaces.ChangementEcran(((Node) ev.getSource()).getScene(), "Login");
    }

}
