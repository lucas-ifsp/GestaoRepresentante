package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.Address;
import br.com.lucas.representante.model.entities.BankAccount;
import br.com.lucas.representante.model.entities.Client;
import br.com.lucas.representante.model.entities.Contact;
import br.com.lucas.representante.persistence.utils.PathFinder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

public class UCImportData {

    private StringBuilder logBuilder = new StringBuilder();
    private Map<String, Client> clients = new LinkedHashMap<>();
    private Path path;
    private int collisionCount = 1;

    public List<Client> importFromCSV(String... filePaths) {
        for(String filePath : filePaths) {
            setFilePath(filePath);
            importClientsFromFile(path);
        }
        exportLog();
        List<Client> importedClients = new ArrayList(clients.values());
        return importedClients;
    }

    private void setFilePath(String filePath) {
        path = Paths.get(filePath);
    }

    private void importClientsFromFile(Path path) {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                if(hasClientData(line)){
                    sb.append(line);
                    sb.append("\n");
                }else{
                    String rawClient = sb.toString();
                    importClient(rawClient);
                    sb.setLength(0);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean hasClientData(String line) {
        return !line.equals("&&&");
    }

    private void importClient(String rawData) {
        Client client = parseClient(rawData);

        if(clientNotAlreadyImported(client)) {
            clients.put(client.getCnpjOrCpf(), client);
            System.out.println(String.format("Imported client #%d: %s", clients.size()+1, client));
        }
        else{
            logImportCollision(client);
            System.out.println(String.format("Collision #%d while client: %s", ++collisionCount, client));
        }
    }

    private Client parseClient(String line) {
        Client client = new Client();
        String[] rawData = line.split("\n");
        client.setClientSince(extractDate(rawData[0],9));
        client.setProspection(isProspection(rawData[1],1));
        client.setCompanyName(extractText(rawData[3], 1));
        client.setTradeName(extractText(rawData[4], 1));
        client.setCompanyId(extractText(rawData[5], 1));
        client.setCnpjOrCpf(extractCnpjCpf(rawData[9], 1));
        client.setStateRegistration(extractStateRegistration(rawData[10], 1));
        client.setCityRegistration(extractText(rawData[11], 1));
        client.setPhone1(extractPhone(rawData[11], 5));
        client.setPhone2(extractPhone(rawData[12], 3));
        client.setFax(extractPhone(rawData[13], 3));

        Address address = parseAddress(rawData);
        client.setAddress(address);

        BankAccount account = parseBankAccount(rawData);
        client.setAccount(account);

        parseContact(rawData, 23).ifPresent(c->client.addContact(c));
        parseContact(rawData, 28).ifPresent(c->client.addContact(c));
        parseContact(rawData, 33).ifPresent(c->client.addContact(c));
        parseContact(rawData, 38).ifPresent(c->client.addContact(c));
        return client;
    }

    private LocalDate extractDate(String line, int position) {
        String[] data = line.split(",");
        int safePosition = Math.min(position, data.length - 1);
        String clearedData = data[safePosition].replaceAll("[a-zA-Z()\\s\"]", "");
        if(!clearedData.isEmpty() && !clearedData.equals("00/00/00")){
            String[] dateParts = clearedData.split("/");
            String formatedYear = Integer.parseInt(dateParts[2].charAt(0)+"") < 5 ?  "20" + dateParts[2] :  "19" + dateParts[2];
            String formatedData = String.format("%s-%s-%s", formatedYear, dateParts[1], dateParts[0]);
            return LocalDate.parse(formatedData);
        }
        return null;
    }

    private boolean isProspection(String line, int i) {
        String text = extractText(line, i);
        return text.equals("0000-00/00/00");
    }

    private String extractText(String line, int position) {
        String [] data = line.split(",");
        int safePosition = Math.min(position, data.length - 1);
        String clearedData = data[safePosition].replaceAll("\"", "");
        return clearedData.trim();
    }

    private String extractCnpjCpf(String line, int i) {
        String text = extractText(line, i);
        char[] resultChars = text.toCharArray();
        resultChars[resultChars.length - 3] = '-';
        return String.valueOf(resultChars);
    }

    private String extractStateRegistration(String line, int i) {
        String text = extractText(line, i);
        String fromDashesAndSlashesToDots = text.replaceAll("-", ".");
        return fromDashesAndSlashesToDots;
    }

    private String extractPhone(String line, int i) {
        String text = extractText(line, i);
        String area = "";
        String mainPhone = "";
        String extension = "";

        if(text.startsWith(".9", 2)){
            String firstPart = text.substring(0,2);
            String lastPart = text.substring(3);
            text = firstPart + " " + lastPart;
        }

        String[] phoneParts = text.split("\\s");

        if(phoneParts.length < 2 || text.length() < 8){
            return "";
        }else if(phoneParts.length < 3 && phoneParts[0].length() > 3) {
            mainPhone = phoneParts[0].replaceAll("\\.", "-");
            extension = phoneParts[1].replaceAll("\\D", "");
        }else{
            area = phoneParts[0].replaceAll("\\.", "");
            mainPhone = phoneParts[1].replaceAll("\\.", "-");
            if(phoneParts.length > 2)
                extension = phoneParts[2].replaceAll("\\D", "");
        }
        String formattedExtension = extension.contains("0000") || extension.isEmpty()? "" : String.format(" (%s)", extension);
        String formattedArea = area.isEmpty() ? "" : String.format("(%s) ", area);
        String formattedPhone = formattedArea.concat(mainPhone.concat(formattedExtension));

        return formattedPhone;
    }

    private Address parseAddress(String[] rawData){
        Address address = new Address();
        address.setStreet(extractText(rawData[6], 1));
        address.setNumber(0);
        address.setArea(extractText(rawData[7], 1));
        address.setCity(extractCity(rawData[8], 1));
        address.setZipCode(extractCep(rawData[8], 1));
        address.setState("SP");
        address.setPointOfReference("");
        return address;
    }

    private BankAccount parseBankAccount(String[] rawData){
        BankAccount account = new BankAccount();
        String bankName = extractBankPart(rawData[5], 3, 2);
        account.setBankName(bankName.isEmpty()? "NomeBanco" : bankName);
        account.setBankNumber(extractBankPart(rawData[5], 3, 0));
        account.setAgency(extractBankPart(rawData[5], 3, 1));
        account.setAccount(extractText(rawData[8], 3));
        return account;
    }

    private Optional<Contact> parseContact(String[] rawData, int position){
        String name = extractText(rawData[position], 1);
        if(name.isEmpty()){
            return Optional.empty();
        }else {
            String email = extractText(rawData[position + 1], 1);
            email = createFakeIfUnavailable(name, email);
            Contact contact = new Contact();
            contact.setEmail(email);
            contact.setName(name);
            contact.setPhone(extractPhone(rawData[position], 3));
            contact.setPosition(extractText(rawData[position + 2], 1));
            contact.setBirthday(null);
            contact.setRg(extractText(rawData[position + 3], 1));
            contact.setCpf(extractText(rawData[position + 3], 5));
            return Optional.of(contact);
        }
    }

    private String createFakeIfUnavailable(String name, String email) {
        if(email.isEmpty()) {
            email = String.format("%s@emailfalso.migracao.com", name.split("\\s")[0]);
            addLog(String.format("E-mail ausente: %s.\n\tCriado e-mail provisório: %s\n",  name, email));
        }
        return email;
    }

    private boolean clientNotAlreadyImported(Client client) {
        return !clients.containsKey(client.getCnpjOrCpf());
    }

    private void logImportCollision(Client client) {
        addLog("Colisão de CNPJ/CPF:");
        addLog("\tImportado: " + clients.get(client.getCnpjOrCpf()));
        addLog("\tIgnorado: " + client + "\n");
    }

    private String extractBankPart(String line, int i, int part) {
        String text = extractText(line, i);
        String [] addressParts = text.replaceAll("\\s\\s", " ").split(" ");
        return addressParts[part];
    }

    private String extractCity(String line, int i) {
        String text = extractText(line, i);
        String [] addressParts = text.split("-");
        return addressParts[1];
    }

    private String extractCep(String line, int i) {
        String text = extractText(line, i);
        String [] addressParts = text.split("-");
        String cep = addressParts[0].replaceAll("\\.", "-");
        return cep;
    }

    public void addLog(String log) {
        logBuilder.append(log);
        logBuilder.append("\n");
    }

    public void exportLog() {
        String destinationPath = PathFinder.find()+"import_log.txt";
        try(PrintWriter out = new PrintWriter(new FileOutputStream(new File(destinationPath)))){
            out.append(logBuilder.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
