package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public abstract class TelegramCommandsExecutor {
    public abstract void send(BotApiMethod method);

    public void sendChatMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(chatId);
        send(message);
    }
}
