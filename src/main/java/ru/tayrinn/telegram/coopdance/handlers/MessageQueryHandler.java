package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;
import ru.tayrinn.telegram.coopdance.models.ChatDao;
import ru.tayrinn.telegram.coopdance.models.ChatMessage;

import java.sql.SQLException;
import java.util.List;

public class MessageQueryHandler extends BotCommandsHandler<Message> {

    private final ChatDao chatDao;

    public MessageQueryHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory, ChatDao chatDao) {
        super(telegramCommandsExecutor, keyboardFactory);
        this.chatDao = chatDao;
    }

    public void handle(Message data) {
        handleMessage(data);
    }

    private void handleStartMessage(Message msg) {
        SendMessage answer = new SendMessage();
        answer.setText("Это сообщение ответ на старт");
        answer.setChatId(msg.getChatId().toString());
        telegramCommandsExecutor.send(answer);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(msg.getMessageId().toString());
        chatMessage.setChatId(msg.getChatId().toString());
        chatMessage.setText(msg.getText());
        chatMessage.setAuthorUsername(msg.getFrom().getUserName());
        chatMessage.setPayload("ddd");
        chatMessage.setBot(0);

        try {
            chatDao.writeChatMessage(chatMessage);
        } catch (SQLException throwables) {
            telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), throwables.toString());
        }
    }

    private void handleMessage(Message msg) {
        Long chatId = msg.getChatId();
        telegramCommandsExecutor.sendChatMessage(chatId.toString(), "Hello world, " + msg.getText() + "!");
        if (msg.getText().startsWith("/start")) {
            handleStartMessage(msg);
        } else if (msg.getText().startsWith("/user")){
            telegramCommandsExecutor.sendChatMessage(chatId.toString(), "Parse user command " + msg.getText() + "!");
            parseUsernameCommand(msg);
        }
    }

    private void parseUsernameCommand(Message msg) {
        try {
            List<ChatMessage> oldMessages = chatDao.getLastChatMessages(msg.getChatId().toString(), msg.getFrom().getUserName(), 10);
            oldMessages.forEach(chatMessage -> {
                telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), chatMessage.getText());
            });
            telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), "Размер ответа = " + oldMessages.size());
        } catch (Exception throwables) {
            telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), throwables.getMessage());
        }
    }
}
