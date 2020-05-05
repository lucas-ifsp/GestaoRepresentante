package br.com.lucas.representante.view.loaders;

import br.com.lucas.representante.controller.CtrlWindowContact;
import br.com.lucas.representante.model.entities.Contact;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class WindowContact {

    private Contact contactToSaveOrUpdate;
    private Contact contactToDelete;
    private Contact contactToSet;

    public WindowContact() {
    }

    public WindowContact(Contact selectedContact) {
        this.contactToSet = selectedContact;
    }

    public void startModal(){
        try {
            FXMLLoader loader = new FXMLLoader();
            Pane pane = loader.load(getClass().getResource("/br/com/lucas/representante/view/fxml/FXMLContact.fxml").openStream());
            CtrlWindowContact ctrl = loader.getController();

            Stage stage = new Stage();

            if(theIsContactToSet())
                ctrl.setEntityToView(contactToSet);

            stage.setTitle("Dados do Contato");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(pane, 650, 230));
            stage.setResizable(false);
            stage.showAndWait();

            contactToSaveOrUpdate = ctrl.toSaveOrUpdate();
            contactToDelete = ctrl.toDelete();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean theIsContactToSet() {
        return contactToSet != null;
    }

    public Contact toSaveOrUpdate() {
        return contactToSaveOrUpdate;
    }

    public Contact toDelete() {
        return contactToDelete;
    }
}
