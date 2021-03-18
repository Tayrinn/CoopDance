package ru.tayrinn.telegram.coopdance.models;

import java.util.List;

public interface ChatDao {

    public void writeChatMessage(ChatMessage chatMessage);

    public List<ChatMessage> getLastChatMessages(String chatId, Integer count);

    public void updateChatMessage(ChatMessage chatMessage);

}
