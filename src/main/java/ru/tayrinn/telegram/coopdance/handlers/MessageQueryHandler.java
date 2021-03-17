package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;

public class MessageQueryHandler extends BotCommandsHandler<Message> {

    public MessageQueryHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory) {
        super(telegramCommandsExecutor, keyboardFactory);
    }

    public BotApiMethod handle(Message data) {
        handleMessage(data);
        return null;
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
