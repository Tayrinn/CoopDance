package ru.tayrinn.telegram.coopdance;

import ru.tayrinn.telegram.coopdance.models.ChatDao;
import ru.tayrinn.telegram.coopdance.models.ChatMessage;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDaoImpl implements ChatDao {
    private static final String SELECT_LAST_CHAT_MESSAGES_QUERY = "SELECT top ? * FROM CHAT_MESSAGES WHERE CHAT_ID = ?";
    private static final String CREATE = "CREATE TABLE IF NOT EXISTS CHAT_MESSAGES (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            ChatMessage.KEY_TEXT + " TEXT," +
            ChatMessage.KEY_MESSAGE_ID + " TEXT," +
            ChatMessage.KEY_CHAT_ID + " TEXT," +
            ChatMessage.KEY_AUTHOR_USERNAME + " TEXT," +
            ChatMessage.KEY_IS_BOT + " INT," +
            ChatMessage.KEY_PAYLOAD + " TEXT," +
            ChatMessage.KEY_TIMESTAMP + " TIMESTAMP" +
            ")";

    private final Statement stmt;

    public ChatDaoImpl(DataSource dataSource) throws SQLException {
        stmt = dataSource.getConnection().createStatement();
        stmt.execute(CREATE);
    }

    @Override
    public void writeChatMessage(ChatMessage chatMessage) {
        //simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("CHAT_MESSAGES");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ChatMessage.KEY_TEXT, chatMessage.getText());
        parameters.put(ChatMessage.KEY_MESSAGE_ID, chatMessage.getMessageId());
        parameters.put(ChatMessage.KEY_CHAT_ID, chatMessage.getChatId());
        parameters.put(ChatMessage.KEY_AUTHOR_USERNAME, chatMessage.getAuthorUsername());
        parameters.put(ChatMessage.KEY_IS_BOT, chatMessage.isBot());
        parameters.put(ChatMessage.KEY_PAYLOAD, chatMessage.getPayload());
        parameters.put(ChatMessage.KEY_TIMESTAMP, chatMessage.getTimestamp());
        //simpleJdbcInsert.execute(parameters);
    }

    @Override
    public List<ChatMessage> getLastChatMessages(String chatId, Integer count) {

        return null;//jdbcTemplate.query(SELECT_LAST_CHAT_MESSAGES_QUERY, new Object[]{count, chatId}, new ChatMessageRowMapper());
    }

    @Override
    public void updateChatMessage(ChatMessage chatMessage) {

    }

}
