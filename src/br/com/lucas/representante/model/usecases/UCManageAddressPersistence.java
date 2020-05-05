package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.Address;
import br.com.lucas.representante.model.entities.Client;
import br.com.lucas.representante.persistence.utils.DAO;

public class UCManageAddressPersistence {
    private DAO<Address, String > daoAddres;

    public UCManageAddressPersistence(DAO<Address, String> daoAddres) {
        this.daoAddres = daoAddres;
    }

    public void saveOrUpdate(Address address){
        if(address != null)
            daoAddres.saveOrUpdate(address);
    }

    public void delete(Address address){
        if(address != null) {
            Client resident = address.getResident();
            String key = resident.getCnpjOrCpf();
            daoAddres.delete(key);
        }
    }
}
