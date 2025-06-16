package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    //Constructor
    public MessageService() {
        messageDAO = new MessageDAO();
        accountService = new AccountService();
    }

    // Constructor with dependency injection

    public MessageService(MessageDAO messageDAO, AccountService accountService) {
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    // Create new message

    public Message createMessage(Message message) {
        // Validate message text is not blank
        if (message.getMessage_text() == null || message.getMessage_text().trim().isEmpty()) {
            return null;
        }
        
        // Validate message text is under 255 characters
        if (message.getMessage_text().length() > 255) {
            return null;
        }
        
        // Validate that the user exists
        if (!accountService.accountExists(message.getPosted_by())) {
            return null;
        }
        
        // If all validations pass, create the message
        return messageDAO.insertMessage(message);
    }

    //Get all messages from the database

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    //Get message by message ID

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    //Delete message by message ID
    
    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }
    
    // Update message text by message ID

    public Message updateMessage(int messageId, String newMessageText) {
        // Validate new message text is not blank
        if (newMessageText == null || newMessageText.trim().isEmpty()) {
            return null;
        }
        
        // Validate new message text is under 255 characters
        if (newMessageText.length() > 255) {
            return null;
        }
        
        // Check if message exists (this will be checked in DAO too, but good to validate here)
        if (messageDAO.getMessageById(messageId) == null) {
            return null;
        }
        
        // If all validations pass, update the message
        return messageDAO.updateMessage(messageId, newMessageText);
    }

    //Get all messages posted by a specific account

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }

}
