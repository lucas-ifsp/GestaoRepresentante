package br.com.lucas.representante.main;

import br.com.lucas.representante.persistence.utils.DatabaseBuilder;
import br.com.lucas.representante.persistence.utils.PathFinder;
import br.com.lucas.representante.view.loaders.WindowClientManager;

public class Main {
    public static void main(String[] args) {
        DatabaseBuilder dbBuilder = new DatabaseBuilder();
        System.out.println("jdbc:sqlite:"+ PathFinder.find()+"database.db");
        dbBuilder.buildDatabaseIfMissing();
        WindowClientManager.main(args);
    }
}
