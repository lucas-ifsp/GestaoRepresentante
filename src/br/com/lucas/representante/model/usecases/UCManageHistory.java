package br.com.lucas.representante.model.usecases;

import br.com.lucas.representante.model.entities.Client;
import br.com.lucas.representante.model.entities.History;
import br.com.lucas.representante.persistence.utils.DAO;

import java.util.List;

public class UCManageHistory {
    private DAO<History, Integer> daoHistory;

    public UCManageHistory(DAO<History, Integer> daoHistory) {
        this.daoHistory = daoHistory;
    }

    public void saveOrUpdate(History history){
        if(history != null)
            daoHistory.saveOrUpdate(history);
    }

    public void delete(History history){ ;
        daoHistory.delete(history.getId());
    }

    public List<History> selectAllFromClient(Client client){
        List<History> history = daoHistory.selectBy("client", client.getCnpjOrCpf());
        return history;
    }
}
