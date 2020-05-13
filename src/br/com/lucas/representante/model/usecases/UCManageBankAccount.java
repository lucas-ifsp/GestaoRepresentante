package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.BankAccount;
import br.com.lucas.representante.model.entities.Client;
import br.com.lucas.representante.persistence.utils.DAO;

public class UCManageBankAccount {
    private DAO<BankAccount, String > daoAccount;

    public UCManageBankAccount(DAO<BankAccount, String> daoAccount) {
        this.daoAccount = daoAccount;
    }

    public void saveOrUpdate(BankAccount account){
        if(account != null)
            daoAccount.saveOrUpdate(account);
    }

    public void delete(BankAccount account){
        if(account != null) {
            Client owner = account.getOwner();
            String key = owner.getCnpjOrCpf();
            daoAccount.delete(key);
        }
    }
}
