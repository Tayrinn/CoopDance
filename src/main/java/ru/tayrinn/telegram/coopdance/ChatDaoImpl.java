package ru.tayrinn.telegram.coopdance;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import ru.tayrinn.telegram.coopdance.models.ChatDao;
import ru.tayrinn.telegram.coopdance.models.ChatMessage;
import ru.tayrinn.telegram.coopdance.models.ChatMessageRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatDaoImpl implements ChatDao {
    private static final String SELECT_LAST_CHAT_MESSAGES_QUERY = "SELECT top ? * FROM CHAT_MESSAGES WHERE CHAT_ID = ?";

    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Override
    public void writeChatMessage(ChatMessage chatMessage) {
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("CHAT_MESSAGES");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(ChatMessage.KEY_TEXT, chatMessage.getText());
        parameters.put(ChatMessage.KEY_MESSAGE_ID, chatMessage.getMessageId());
        parameters.put(ChatMessage.KEY_CHAT_ID, chatMessage.getChatId());
        parameters.put(ChatMessage.KEY_AUTHOR_USERNAME, chatMessage.getAuthorUsername());
        parameters.put(ChatMessage.KEY_IS_BOT, chatMessage.isBot());
        parameters.put(ChatMessage.KEY_PAYLOAD, chatMessage.getPayload());
        parameters.put(ChatMessage.KEY_TIMESTAMP, chatMessage.getTimestamp());
        simpleJdbcInsert.execute(parameters);
    }

    @Override
    public List<ChatMessage> getLastChatMessages(String chatId, Integer count) {

        return jdbcTemplate.query(SELECT_LAST_CHAT_MESSAGES_QUERY, new Object[]{count, chatId}, new ChatMessageRowMapper());
    }

    @Override
    public void updateChatMessage(ChatMessage chatMessage) {

    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
