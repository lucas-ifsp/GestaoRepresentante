package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.*;
import br.com.lucas.representante.persistence.dao.DAOClient;
import br.com.lucas.representante.persistence.dao.DAOContact;
import br.com.lucas.representante.persistence.utils.DAO;

import java.util.List;

public class UCManageClient {

    private DAOClient daoClient;
    private UCManageAddress ucManageAddressPersistence;
    private UCManageBankAccount ucManageBankAccountPersistence;
    private UCManageContact ucManageContactPersistence;
    private UCManageHistory ucManageHistoryPersistence;

    public UCManageClient(DAOClient daoClient){
        this(daoClient, null, null, null, null);
    }

    public UCManageClient(DAOClient daoClient, DAO<Address,String> daoAddress,
                          DAO<BankAccount, String> daoAccount, DAOContact daoContact,
                          DAO<History, Integer> daoHistory){
        this.daoClient = daoClient;
        ucManageAddressPersistence = new UCManageAddress(daoAddress);
        ucManageBankAccountPersistence = new UCManageBankAccount(daoAccount);
        ucManageContactPersistence = new UCManageContact(daoContact);
        ucManageHistoryPersistence = new UCManageHistory(daoHistory);
    }

    public void saveOrUpdate(Client client){
        daoClient.saveOrUpdate(client);
        ucManageAddressPersistence.saveOrUpdate(client.getAddress());
        ucManageBankAccountPersistence.saveOrUpdate(client.getAccount());
        updateContactList(client);
        updateHistoryList(client);
    }

    private void updateContactList(Client client) {
        List<Contact> contactsToUpdate = client.getContacts();
        List<Contact> contactsInDatabase = selectClientContacts(client);

        deleteRemovedContacts(contactsToUpdate, contactsInDatabase);
        updateExistingContacts(contactsToUpdate);
    }

    private List<Contact> selectClientContacts(Client client){
        List<Contact> contacts = daoClient.selectClientContacts(client);
        return contacts;
    }

    private void deleteRemovedContacts(List<Contact> contactsToUpdate, List<Contact> contactsInDatabase) {
        contactsInDatabase.forEach(c-> {
            if(!contactsToUpdate.contains(c))
                daoClient.removeClientContact(c);
        });
    }

    private void updateExistingContacts(List<Contact> contactsToUpdate) {
        contactsToUpdate.forEach(c -> {
            ucManageContactPersistence.saveOrUpdate(c);
            daoClient.addClientContact(c);
        });
    }

    private void updateHistoryList(Client client) {
        List<History> historyToUpdate = client.getHistory();
        List<History> historyInDatabase = ucManageHistoryPersistence.selectAllFromClient(client);

        deleteRemovedHistory(historyToUpdate, historyInDatabase);
        updateExistingHistory(historyToUpdate);
    }

    private void deleteRemovedHistory(List<History> historyToUpdate, List<History> historyInDatabase) {
        historyInDatabase.forEach(h-> {
            if(!historyToUpdate.contains(h))
                ucManageHistoryPersistence.delete(h);
        });
    }

    private void updateExistingHistory(List<History> historyToUpdate) {
        historyToUpdate.forEach(h -> ucManageHistoryPersistence.saveOrUpdate(h));
    }

    public void delete(Client client){
        ucManageAddressPersistence.delete(client.getAddress());
        ucManageBankAccountPersistence.delete(client.getAccount());

        List<Contact> contactsToRemove = selectClientContacts(client);
        contactsToRemove.forEach(c->daoClient.removeClientContact(c));

        List<History> historyToDelete = ucManageHistoryPersistence.selectAllFromClient(client);
        historyToDelete.forEach(c->ucManageHistoryPersistence.delete(c));
        
        daoClient.delete(client.getCnpjOrCpf());
    }

    public List<Client> selectAll(){
        return daoClient.selectAll();
    }


}
