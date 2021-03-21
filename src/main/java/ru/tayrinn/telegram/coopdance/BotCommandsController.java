package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tayrinn.telegram.coopdance.handlers.CallbackQueryHandler;
import ru.tayrinn.telegram.coopdance.handlers.InlineQueryHandler;
import ru.tayrinn.telegram.coopdance.handlers.MessageQueryHandler;
import ru.tayrinn.telegram.coopdance.models.ChatDao;
import ru.tayrinn.telegram.coopdance.models.Dances;

import javax.sql.DataSource;
import java.sql.SQLException;

public class BotCommandsController {

    private final InlineKeyboardFactory keyboardFactory = new InlineKeyboardFactory();
    private final CallbackQueryHandler callbackQueryHandler;
    private final InlineQueryHandler inlineQueryHandler;
    private final MessageQueryHandler messageQueryHandler;
    private final Dances dances = new Dances();
    private ChatDao chatDao;

    public BotCommandsController(TelegramCommandsExecutor telegramCommandsExecutor,
                                 DataSource dataSource) {
        try {
            chatDao = new ChatDaoImpl(dataSource);
        } catch (SQLException throwables) {
            //telegramCommandsExecutor.sendChatMessage();
        }
        messageQueryHandler = new MessageQueryHandler(telegramCommandsExecutor, keyboardFactory, chatDao);
        callbackQueryHandler = new CallbackQueryHandler(telegramCommandsExecutor, keyboardFactory, dances);
        inlineQueryHandler = new InlineQueryHandler(telegramCommandsExecutor, keyboardFactory);
    }

    public void handle(Update update) {
        if (update.hasInlineQuery()) {
            inlineQueryHandler.handle(update.getInlineQuery());
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.handle(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            messageQueryHandler.handle(update.getMessage());
        }
    }
}
