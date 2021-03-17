package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.tayrinn.telegram.coopdance.handlers.CallbackQueryHandler;
import ru.tayrinn.telegram.coopdance.handlers.InlineQueryHandler;
import ru.tayrinn.telegram.coopdance.handlers.MessageQueryHandler;
import ru.tayrinn.telegram.coopdance.models.Dances;

public class BotCommandsController {

    private final InlineKeyboardFactory keyboardFactory = new InlineKeyboardFactory();
    private final TelegramCommandsExecutor telegramCommandsExecutor;
    private final CallbackQueryHandler callbackQueryHandler;
    private final InlineQueryHandler inlineQueryHandler;
    private final MessageQueryHandler messageQueryHandler;
    private final Dances dances = new Dances();

    public BotCommandsController(TelegramCommandsExecutor telegramCommandsExecutor) {
        this.telegramCommandsExecutor = telegramCommandsExecutor;
        messageQueryHandler = new MessageQueryHandler(telegramCommandsExecutor, keyboardFactory);
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
