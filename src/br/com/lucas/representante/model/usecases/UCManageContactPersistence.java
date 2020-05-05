package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.Client;
import br.com.lucas.representante.model.entities.Contact;
import br.com.lucas.representante.persistence.utils.DAO;

import java.util.List;

public class UCManageContactPersistence {

    private DAO<Contact, String > daoContact;

    public UCManageContactPersistence(DAO<Contact, String> daoContact) {
        this.daoContact = daoContact;
    }

    public void saveOrUpdate(Contact contact){
        if(contact != null)
            daoContact.saveOrUpdate(contact);
    }

    public void delete(Contact contact){
        daoContact.delete(contact.getEmail());
    }

    public List<Contact> selectAllFromClient(Client client){
        List<Contact> contacts = daoContact.selectBy("client", client.getCnpjOrCpf());
        return contacts;
    }
}
