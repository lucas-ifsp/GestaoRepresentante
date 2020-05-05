package br.com.lucas.representante.view.loaders;

import br.com.lucas.representante.controller.CtrlWindowHistory;
import br.com.lucas.representante.model.entities.History;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowHistory {
    private History historyToSaveOrUpdate;
    private History historyToDelete;
    private History historyToSet;

    public WindowHistory() {
    }

    public WindowHistory(History selectedHistory) {
        this.historyToSet = selectedHistory;
    }

    public void startModal(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane pane = loader.load(getClass().getResource("/br/com/lucas/representante/view/fxml/FXMLHistory.fxml").openStream());
            CtrlWindowHistory ctrl = loader.getController();

            Stage stage = new Stage();

            if(theIsContactToSet())
                ctrl.setEntityToView(historyToSet);

            stage.setTitle("Dados do Histórico");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane, 600, 265));
            stage.setResizable(false);
            stage.showAndWait();

            historyToSaveOrUpdate = ctrl.toSaveOrUpdate();
            historyToDelete = ctrl.toDelete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean theIsContactToSet() {
        return historyToSet != null;
    }

    public History toSaveOrUpdate() {
        return historyToSaveOrUpdate;
    }

    public History toDelete() {
        return historyToDelete;
    }
}
