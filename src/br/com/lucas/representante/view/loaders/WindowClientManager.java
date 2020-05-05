package br.com.lucas.representante.view.loaders;

import br.com.lucas.representante.persistence.utils.DatabaseBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowClientManager extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/br/com/lucas/representante/view/fxml/FXMLClientManager.fxml"));
        primaryStage.setTitle("Gestão de Carteira de Clientes");
        primaryStage.setScene(new Scene(root, 1280, 920));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
