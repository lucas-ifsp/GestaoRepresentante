package br.com.lucas.representante.persistence.dao;

import br.com.lucas.representante.model.entities.*;
import br.com.lucas.representante.persistence.utils.AbstractTemplateSqlDAO;
import br.com.lucas.representante.persistence.utils.DAO;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DAOClient extends AbstractTemplateSqlDAO<Client, String> {

    @Override
    protected String createSaveSql() {
        return "INSERT INTO Client(companyName, tradeName, companyId, stateRegistration, " +
                "cityRegistration, clientSince, phone1, phone2, fax, prospection, active, cnpjOrCpf) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String createUpdateSql() {
        return "UPDATE Client SET companyName = ?, tradeName = ?, companyId = ?, stateRegistration = ?, " +
                "cityRegistration = ?, clientSince = ?, phone1 = ?, phone2 = ?, fax = ? , prospection = ?, " +
                "active = ? WHERE cnpjOrCpf = ?";
    }

    @Override
    protected String createDeleteSql() {
        return "DELETE FROM Client WHERE cnpjOrCpf = ?";
    }

    @Override
    protected String createSelectSql() {
        return "SELECT * FROM Client WHERE cnpjOrCpf = ?";
    }

    @Override
    protected String createSelectAllSql() {
        return "SELECT * FROM Client";
    }

    @Override
    protected String createSelectBySql(String field) {
        return "SELECT * FROM Client WHERE "+ field +" = ?";
    }

    @Override
    protected void setEntityToPreparedStatement(@NotNull Client entity, @NotNull PreparedStatement stmt) throws SQLException {
        LocalDate date = entity.getClientSince();

        stmt.setString(1, entity.getCompanyName());
        stmt.setString(2, entity.getTradeName());
        stmt.setString(3, entity.getCompanyId());
        stmt.setString(4, entity.getStateRegistration());
        stmt.setString(5, entity.getCityRegistration());
        stmt.setString(6, date != null? date.toString() : null); //TODO
        stmt.setString(7, entity.getPhone1());
        stmt.setString(8, entity.getPhone2());
        stmt.setString(9, entity.getFax());
        stmt.setInt(10, entity.isProspection()? 1 : 0);
        stmt.setInt(11, entity.isActive()? 1: 0);
        stmt.setString(12, entity.getCnpjOrCpf());
    }

    @Override
    protected void setKeyToPreparedStatement(@NotNull String key, @NotNull PreparedStatement stmt) throws SQLException {
        stmt.setString(1, key);
    }

    @Override
    protected void setFilterToPreparedStatement(@NotNull Object filter, @NotNull PreparedStatement stmt) throws SQLException {
        if(filter instanceof  String)
            stmt.setString(1, filter.toString());
        else if(filter instanceof Integer)
            stmt.setInt(1, (Integer)filter);
        else
            throw new SQLException("O tipo do filtro fornecido não é suportado pela consulta.");
    }

    @Override
    protected String getEntityKey(@NotNull Client entity) {
        return entity.getCnpjOrCpf();
    }

    @Override
    protected Client getEntityFromResultSet(@NotNull ResultSet rs) throws SQLException {
        String dateString = rs.getString("clientSince");
        Client client = new Client(
                rs.getString("companyName"),
                rs.getString("tradeName"),
                rs.getString("companyId"),
                rs.getString("cnpjOrCpf"),
                rs.getString("stateRegistration"),
                rs.getString("cityRegistration"),
                dateString != null ? LocalDate.parse(dateString) : null,
                rs.getString("phone1"),
                rs.getString("phone2"),
                rs.getString("fax"),
                rs.getInt("prospection") == 1,
                rs.getInt("active") == 1);
        return client;
    }

    @Override
    protected void loadNestedEntitiesHook(List<Client> entities) throws SQLException{
        entities.stream().forEach((x) -> {
                selectAndBindAddress(x);
                selectAndBindAccount(x);
                selectAndBindContacts(x);
                selectAndBindHistory(x);
        });
     }

    private void selectAndBindAddress(Client client) {
        DAO<Address, String> daoAddress = new DAOAddress();
        Optional<Address> account = daoAddress.select(client.getCnpjOrCpf());
        account.ifPresent(a -> client.setAddress(a));
    }

    private void selectAndBindAccount(Client client) {
        DAO<BankAccount, String> daoAccount = new DAOBankAccount();
        Optional<BankAccount> account = daoAccount.select(client.getCnpjOrCpf());
        account.ifPresent(a -> client.setAccount(a));
    }

    private void selectAndBindContacts(Client client) {
        DAO<Contact, String> daoContact = new DAOContact();
        List<Contact> contacts = daoContact.selectBy("client", client.getCnpjOrCpf());
        contacts.stream().forEach(c -> client.addContact(c));
    }

    private void selectAndBindHistory(Client client) {
        DAO<History, Integer> daoHistory = new DAOHistory();
        List<History> history = daoHistory.selectBy("client", client.getCnpjOrCpf());
        history.stream().forEach(h -> client.addHistory(h));
    }
}
