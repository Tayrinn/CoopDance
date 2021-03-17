package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;
import ru.tayrinn.telegram.coopdance.models.Commands;
import ru.tayrinn.telegram.coopdance.models.Dance;
import ru.tayrinn.telegram.coopdance.models.Dances;

public class CallbackQueryHandler extends BotCommandsHandler<CallbackQuery> {

    private final Dances dances;
    private String callData;
    private String messageId;
    private String command;
    private CallbackQuery callbackQuery;

    public CallbackQueryHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory, Dances dances) {
        super(telegramCommandsExecutor, keyboardFactory);
        this.dances = dances;
    }

    @Override
    public BotApiMethod handle(CallbackQuery data) {
        callbackQuery = data;
        callData = data.getData();
        messageId = data.getInlineMessageId();
        command = Commands.parseCommand(callData);

        EditMessageText editMessageText = createEditMessage(data);
        telegramCommandsExecutor.send(editMessageText);
        return editMessageText;
    }

    private EditMessageText createEditMessage(CallbackQuery callbackQuery) {
        Dance dance = dances.addDance(Commands.parseSystemInfo(callData), messageId);
        dance.processCommand(command, callbackQuery.getFrom());

        EditMessageText newMessage = new EditMessageText();
        newMessage.setInlineMessageId(messageId);
        newMessage.setReplyMarkup(keyboardFactory.createStarterKeyboard(dance.message));
        newMessage.setParseMode(ParseMode.HTML);
        newMessage.setText(dance.toString());
        return newMessage;
    }

    private void parseCommand(String callData) {

        switch (command) {
            case Commands.ADD_GIRL_AND_BOY:
            case Commands.ADD_BOY_AND_GIRL:
                sendInlineAnswer(command + "-" + messageId, callbackQuery); break;
        }
    }

    private void sendInlineAnswer(String command, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setUrl("t.me/CoopDanceBot?start=" + command);

        telegramCommandsExecutor.send(answerCallbackQuery);
    }
}
