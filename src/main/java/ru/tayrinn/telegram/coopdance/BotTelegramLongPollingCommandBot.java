package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class BotTelegramLongPollingCommandBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final String DATABASE_URL;
    private final BotCommandsController botCommandsController;

    public BotTelegramLongPollingCommandBot(String botName, String botToken, String database_url) {
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
        DATABASE_URL = database_url;
        botCommandsController = new BotCommandsController(new TelegramCommandsExecutorImpl());
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
        botCommandsController.handle(update);
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private class TelegramCommandsExecutorImpl extends TelegramCommandsExecutor {

        @Override
        public void send(BotApiMethod method) {
            try {
                execute(method);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
