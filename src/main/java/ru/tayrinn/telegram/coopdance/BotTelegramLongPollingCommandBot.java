package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static java.lang.Math.toIntExact;

public class BotTelegramLongPollingCommandBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public BotTelegramLongPollingCommandBot(String botName, String botToken) {
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public void onUpdateReceived(Update update) {
        processNonCommandUpdate(update);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(update -> processNonCommandUpdate(update));
    }

    public void processNonCommandUpdate(Update update) {
        if (update.hasInlineQuery()) {
            handleInlineQuery(update.getInlineQuery());
        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }
    }

    /**
     * Формирование имени пользователя
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void handleInlineQuery(InlineQuery inlineQuery) {
        try {
            execute(new BotInlineQuery(inlineQuery).answer());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(CallbackQuery callbackQuery) {
        String callData = callbackQuery.getData();
        handleMessage(callbackQuery.getMessage());
        long messageId = callbackQuery.getMessage().getMessageId();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        EditMessageText newMessage = new EditMessageText();
        newMessage.setChatId(chatId);
        newMessage.setMessageId(toIntExact(messageId));
        newMessage.setText(callData);
        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Message msg) {
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);
        setAnswer(chatId, "Hello world, " + userName + "!");
    }

    /**
     * Отправка ответа
     * @param chatId id чата
     * @param text текст ответа
     */
    private void setAnswer(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //логируем сбой Telegram Bot API, используя userName
        }
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
