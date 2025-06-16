package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 //Controller class for Social Media API endpoints
 public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    private ObjectMapper objectMapper;

    //Constructor - initialize services
    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
        this.objectMapper = new ObjectMapper();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //Account endpoints        
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);

        //Message endpoints
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);

        // User messages endpoint
        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    //Handler for POST /register
    private void registerHandler(Context context) throws JsonProcessingException {
        Account account = objectMapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        
        if (registeredAccount != null) {
            context.json(registeredAccount);
            context.status(200);
        } else {           
            context.status(400);
        }
    }

    //Handler for POST /login
    private void loginHandler(Context context) throws JsonProcessingException {
        Account loginRequest = objectMapper.readValue(context.body(), Account.class);
        Account loggedInAccount = accountService.login(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (loggedInAccount != null) {
            context.json(loggedInAccount);
            context.status(200);
        } else {
            context.status(401);
        }
    }

    //Handler for POST /messages
    private void createMessageHandler(Context context) throws JsonProcessingException {
        Message message = objectMapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        
        if (createdMessage != null) {
            context.json(createdMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    //Handler for GET /messages
    private void getAllMessagesHandler(Context context) {
        List<Message> messages = messageService.getAllMessages();
        context.json(messages);
        context.status(200);
    }

    //Handler for GET /messages/{message_id}
    private void getMessageByIdHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        
        if (message != null) {
            context.json(message);
        } else {
            context.result(""); // Empty response body
        }
        context.status(200);
    }

    //Handler for DELETE /messages/{message_id}
    private void deleteMessageHandler(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(messageId);
        
        if (deletedMessage != null) {
            context.json(deletedMessage);
        } else {
            context.result(""); // Empty response body
        }
        context.status(200);
    }

    //Handler for PATCH /messages/{message_id}
    private void updateMessageHandler(Context context) throws JsonProcessingException {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        
        // Parse the request body to get new message text
        Message updateRequest = objectMapper.readValue(context.body(), Message.class);
        String newMessageText = updateRequest.getMessage_text();
        
        Message updatedMessage = messageService.updateMessage(messageId, newMessageText);
        
        if (updatedMessage != null) {
            context.json(updatedMessage);
            context.status(200);
        } else {
            context.status(400);
        }
    }

    //Handler for GET /accounts/{account_id}/messages
    private void getMessagesByAccountHandler(Context context) {
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        context.json(messages);
        context.status(200);
    }
}