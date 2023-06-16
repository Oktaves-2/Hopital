package GUI;

import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La classe Interfaces sert a lancer l'application et centralise par la methode
 * changementEcran le passage d'une scene a une autre en fonction d'un string
 * pris en parametre,
 * chaque ecran est nomme en relation avec la profession qui lui correspond.
 */
public class Interfaces extends Application {

    public static void main(String[] args) {
        Application.launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(Interfaces.class.getResource("EcranLogin.fxml"));
            Parent root = loader.load();
            Scene scenelog = new Scene(root);
            scenelog.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
            stage.setScene(scenelog);
            try {
                stage.show();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void ChangementEcran(Scene sceneactive, String role)
            throws IOException, SQLException {
        try {
            Stage stage = new Stage();
            String Ecran = "Ecran" + role + ".fxml";
            FXMLLoader loader = new FXMLLoader(Interfaces.class.getResource(Ecran));
            Parent root = loader.load();
            stage = (Stage) sceneactive.getWindow();
            Scene scenecent = new Scene(root);
            scenecent.getStylesheets().add(Interfaces.class.getResource("Style.css").toExternalForm());
            stage.setScene(scenecent);
            stage.show();
            switch (role) {
                case ("Medecin"):
                    EcranMedecin ecranMedecin = loader.getController();
                    ecranMedecin.remplissageInformations();
                    break;
                case ("Patient"):
                    EcranPatient ecranPatient = loader.getController();
                    ecranPatient.RemplissageInformations();
                    break;
                case ("Ordonnance"):
                    EcranCreationConsultation ecranOrdonnance = loader.getController();
                    ecranOrdonnance.RemplissageInformations();
                    break;
                case ("Agent"):
                    EcranAgent ecranAgent = loader.getController();
                    ecranAgent.RemplissageInformations();
                    break;
                case ("AgentPers"):
                    EcranAgentPers ecranAgentPers = loader.getController();
                    ecranAgentPers.RemplissageInformations();
                    break;
                case ("ModifVuePatient"):
                    EcranModifVuePatient ecranModifVuePatient = loader.getController();
                    ecranModifVuePatient.RemplissageInformations();
                    break;
                case ("ModifVuePers"):
                    EcranModifVuePers ecranModifVuePers = loader.getController();
                    ecranModifVuePers.RemplissageInformations();
                    break;

                case ("CreationPatient"):
                    EcranCreationPatient ecranCreationPatient = loader.getController();
                    ecranCreationPatient.RemplissageInformations();
                    break;
                case ("CreationPers"):
                    EcranCreationPersonnel ecranCreationPersonnel = loader.getController();
                    ecranCreationPersonnel.RemplissageInformations();
                    break;
                case ("CreationConsultation"):
                    EcranCreationConsultation ecranCreationConsultation = loader.getController();
                    ecranCreationConsultation.RemplissageInformations();
                    break;
                case ("ModifierInformations"):
                    EcranModifierInformations ecranModifierInformations = loader.getController();
                    ecranModifierInformations.RemplissageInformations();
                    break;
                case ("ModifierInformationsPers"):
                    EcranModifierInformationsPers ecranModifierInformationsPers = loader.getController();
                    ecranModifierInformationsPers.RemplissageInformations();
                    break;
                case ("Technicien"):
                    EcranTechnicien ecranTechnicien = loader.getController();
                    ecranTechnicien.RemplissageInformations();

            }
        } catch (

        Exception e) {
            System.out.println(e);
        }

    }

}