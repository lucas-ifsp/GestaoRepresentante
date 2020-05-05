package br.com.lucas.representante.view.util;

import javafx.scene.control.TextField;

public class TextFieldFormater {

    public static void formatAsLiteralWithAccent(TextField textField){
        String pattern = "[A-Za-zÀ-ÿ '-]";
        applyPattern(textField, pattern);
    }

    private static void applyPattern(TextField textField, String pattern) {
        String s = pattern + "*";
        String s2 = "[^" + pattern + "]";
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(s)) {
                String value = newValue.replaceAll(s2, "");
                textField.setText(value);
            }
        });
    }

    public static void formatAsRG(TextField textField){
        String pattern = "[0-9a-zA-Z\\-\\./]";
        applyPattern(textField, pattern);
    }

    public static void formatAsNumeric(TextField textField){
        String pattern = "\\d";
        applyPattern(textField, pattern);
    }

    public static void formatAsNumericWithDashes(TextField textField) {
        String pattern = "[\\d\\-]";
        applyPattern(textField, pattern);
    }

    public static void formatAsNumericWithDashesAndDots(TextField textField){
        String pattern = "[\\d\\.\\-]";
        applyPattern(textField, pattern);
    }

    public static void formatAsNumericWithDashesDotsAndSlashes(TextField textField){
        String pattern = "[\\d\\.\\-/]";
        applyPattern(textField, pattern);
    }


    public static void formatAsPhoneNumber(TextField textField){
        String pattern = "[\\d\\s\\-()]";
        applyPattern(textField, pattern);
    }
}
