package br.com.lucas.representante.view.loaders;

import br.com.lucas.representante.controller.CtrlWindowClient;
import br.com.lucas.representante.model.entities.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowClient {

    private Client client;

    public WindowClient(Client selectedClient) {
        client = selectedClient;
    }

    public WindowClient() { }

    public void startModal(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane pane = loader.load(getClass().getResource("/br/com/lucas/representante/view/fxml/FXMLClient.fxml").openStream());
            CtrlWindowClient ctrl = loader.getController();

            Stage stage = new Stage();

            if(client != null) {
                ctrl.setEntityToView(client);
            }

            stage.setTitle("Dados do Cliente");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane, 940, 745));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
