package ru.tayrinn.telegram.coopdance.models;

import java.sql.SQLException;
import java.util.List;

public interface ChatDao {

    public void writeChatMessage(ChatMessage chatMessage) throws SQLException;

    public List<ChatMessage> getLastChatMessages(String chatId, String authorName, Integer count) throws SQLException;

    public void updateChatMessage(ChatMessage chatMessage);

}
