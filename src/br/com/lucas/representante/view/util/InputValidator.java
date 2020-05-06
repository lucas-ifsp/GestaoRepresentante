package br.com.lucas.representante.view.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {

    public static boolean isNumeric(String input){
        String pattern = "\\d*";
        return applyPattern(pattern, input);
    }

    private static boolean applyPattern(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if(matcher.find())
            return true;
        else
            return false;
    }

    public static boolean isEmail(String input){
        String pattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        return applyPattern(pattern, input);
    }

    public static boolean isCEP(String input){
        String pattern = "^\\d{5}-\\d{3}$";
        return applyPattern(pattern, input);
    }

    public static boolean isTelefone(String input){
        String pattern = "^(\\(?\\d{2}\\)?\\s?)?(9?\\s?\\d{4}(\\-|\\s)?\\d{4})$";
        return applyPattern(pattern, input);
    }

    public static boolean isCNPJOrCPF(String input){
        String pattern = "([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})$";
        return applyPattern(pattern, input);
    }
}
