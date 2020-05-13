package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.Contact;
import br.com.lucas.representante.persistence.dao.DAOContact;

import java.util.Optional;

public class UCManageContact {

    private DAOContact daoContact;

    public UCManageContact(DAOContact daoContact) {
        this.daoContact = daoContact;
    }

    public void saveOrUpdate(Contact contact){
        if(contact != null)
            daoContact.saveOrUpdate(contact);
    }

    public Optional<Contact> select(String email){
        return daoContact.select(email);
    }
}
