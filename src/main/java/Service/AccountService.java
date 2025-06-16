package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    //Constructor
    public AccountService(){
        accountDAO = new AccountDAO();
    } 
    
    // Constructor with dependency
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    // Register a new account

    public Account registerAccount(Account account) {
        // Null check for account object
        if (account == null) {
            return null;
        }

        // Validate username is not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null;
        }
        
        // Validate password is at least 4 characters
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }
        
        // Check if username already exists
        if (accountDAO.usernameExists(account.getUsername())) {
            return null;
        }
        
        // If all validations pass, create the account
        return accountDAO.insertAccount(account);
    }

    //Login with username and password

        public Account login(String username, String password) {
        // Basic null checks
        if (username == null || password == null) {
            return null;
        }
        
        // Try to find account with matching credentials
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }

    //Check if account exists by ID

        public boolean accountExists(int accountId) {
        return accountDAO.accountExistsById(accountId);
    }

}
