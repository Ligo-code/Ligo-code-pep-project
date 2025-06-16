package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    //Insert a new message into the database

    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                int generated_message_id = (int) generatedKeys.getLong(1);
                return new Message(generated_message_id, message.getPosted_by(),
                message.getMessage_text(), message.getTime_posted_epoch());
            }
        } catch  (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    //Get all messages from the database

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql= "SELECT * FROM message";
            PreparedStatement preparedStatement= connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return messages;
    }

    //Get message by message_id

    public Message getMessageById(int messageID) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageID);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Delete message by message_id
   public Message deleteMessageById(int messageId) {
       Connection connection = ConnectionUtil.getConnection();

       Message messageToDelete = getMessageById(messageId);

       if (messageToDelete != null) {
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, messageId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return messageToDelete;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       }
       return null;
   }

   //Update message text by message_id

   public Message updateMessage(int messageId, String newMessageText) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, newMessageText);
        preparedStatement.setInt(2, messageId);

        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            return getMessageById(messageId); // renewed message
        }
    } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
   }

   // Get all messages by account_id
       public List<Message> getMessagesByAccountId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            preparedStatement.setInt(1, accountId);
            
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

}
