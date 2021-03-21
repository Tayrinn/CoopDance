package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;
import ru.tayrinn.telegram.coopdance.models.ChatDao;

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
    }

    private void handleMessage(Message msg) {
        Long chatId = msg.getChatId();
        telegramCommandsExecutor.sendChatMessage(chatId.toString(), "Hello world, " + msg.getText() + "!");
        if (msg.getText().startsWith("/start")) {
            handleStartMessage(msg);
        }
    }

}
