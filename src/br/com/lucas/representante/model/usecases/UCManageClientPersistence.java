package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.*;
import br.com.lucas.representante.persistence.utils.DAO;

import java.util.List;

public class UCManageClientPersistence {

    private DAO<Client, String> daoClient;
    private UCManageAddressPersistence ucManageAddressPersistence;
    private UCManageBankAccountPersistence ucManageBankAccountPersistence;
    private UCManageContactPersistence ucManageContactPersistence;
    private UCManageHistoryPersistence ucManageHistoryPersistence;

    public UCManageClientPersistence(DAO<Client, String> daoClient){
        this(daoClient, null, null, null, null);
    }

    public UCManageClientPersistence(DAO<Client, String> daoClient, DAO<Address,String> daoAddress,
                                     DAO<BankAccount, String> daoAccount, DAO<Contact, String> daoContact,
                                     DAO<History, Integer> daoHistory){
        this.daoClient = daoClient;
        ucManageAddressPersistence = new UCManageAddressPersistence(daoAddress);
        ucManageBankAccountPersistence = new UCManageBankAccountPersistence(daoAccount);
        ucManageContactPersistence = new UCManageContactPersistence(daoContact);
        ucManageHistoryPersistence = new UCManageHistoryPersistence(daoHistory);
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
        List<Contact> contactsInDatabase = ucManageContactPersistence.selectAllFromClient(client);

        deleteRemovedContacts(contactsToUpdate, contactsInDatabase);
        updateExistingContacts(contactsToUpdate);
    }

    private void deleteRemovedContacts(List<Contact> contactsToUpdate, List<Contact> contactsInDatabase) {
        contactsInDatabase.stream().forEach(c-> {
            if(!contactsToUpdate.contains(c))
                ucManageContactPersistence.delete(c);
        });
    }

    private void updateExistingContacts(List<Contact> contactsToUpdate) {
        contactsToUpdate.stream().forEach(c -> ucManageContactPersistence.saveOrUpdate(c));
    }

    private void updateHistoryList(Client client) {
        List<History> historyToUpdate = client.getHistory();
        List<History> historyInDatabase = ucManageHistoryPersistence.selectAllFromClient(client);

        deleteRemovedHistory(historyToUpdate, historyInDatabase);
        updateExistingHistory(historyToUpdate);
    }

    private void deleteRemovedHistory(List<History> historyToUpdate, List<History> historyInDatabase) {
        historyInDatabase.stream().forEach(h-> {
            if(!historyToUpdate.contains(h))
                ucManageHistoryPersistence.delete(h);
        });
    }

    private void updateExistingHistory(List<History> historyToUpdate) {
        historyToUpdate.stream().forEach(h -> ucManageHistoryPersistence.saveOrUpdate(h));
    }

    public void delete(Client client){
        ucManageAddressPersistence.delete(client.getAddress());
        ucManageBankAccountPersistence.delete(client.getAccount());

        List<Contact> contactsToDelete = ucManageContactPersistence.selectAllFromClient(client);
        contactsToDelete.stream().forEach(c->ucManageContactPersistence.delete(c));

        List<History> historyToDelete = ucManageHistoryPersistence.selectAllFromClient(client);
        historyToDelete.stream().forEach(c->ucManageHistoryPersistence.delete(c));
        
        daoClient.delete(client.getCnpjOrCpf());
    }

    public List<Client> selectAll(){
        return daoClient.selectAll();
    }


}
