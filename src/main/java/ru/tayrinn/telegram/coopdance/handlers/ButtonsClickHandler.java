package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;
import ru.tayrinn.telegram.coopdance.Utils;
import ru.tayrinn.telegram.coopdance.models.CallbackData;
import ru.tayrinn.telegram.coopdance.models.Commands;
import ru.tayrinn.telegram.coopdance.models.Dance;
import ru.tayrinn.telegram.coopdance.models.Dances;

/**
 * Класс для обработки коллбеков - событий при нажатии на кнопки бота
 */
public class ButtonsClickHandler extends BotCommandsHandler<CallbackQuery> {

    private final Dances dances;
    private CallbackData callbackData; // данные, которые зашиваются в кнопку при создании
    private CallbackQuery callbackQuery;
    private String messageId;

    public ButtonsClickHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory, Dances dances) {
        super(telegramCommandsExecutor, keyboardFactory);
        this.dances = dances;
    }

    @Override
    public void handle(CallbackQuery data) {
        callbackQuery = data;
        String callData = data.getData();
        messageId = data.getInlineMessageId();
        callbackData = Commands.parseCommand(callData);
        callbackData.i = messageId;
        try {
            parseCommand();
        } catch (Exception e) {
            Utils.sendException(telegramCommandsExecutor, callbackQuery.getId(), e);
        }
    }

    private void parseCommand() {
        Dance dance = dances.getDance(callbackData.m, messageId);
        User user = callbackQuery.getFrom();
        switch (callbackData.c) {
            case Commands.ADD_GIRL_AND_BOY:
            case Commands.ADD_BOY_AND_GIRL:
                String utf = callbackData.c + Commands.SEPARATOR + callbackData.i;
                dance.findSingleDancerAndRemove(user);
                if (dance.hasDancer(user)) {
                    telegramCommandsExecutor.sendAlertMessage(callbackQuery.getId(), "Вы уже записаны");
                }
                sendInlineAnswer(utf);
                break;
            case Commands.ADD_GIRL :
            case Commands.ADD_BOY :
                if (dance.hasDancer(user)) {
                    telegramCommandsExecutor.sendAlertMessage(callbackQuery.getId(), "Вы уже записаны");
                    return;
                }
                dance.processCommand(callbackData.c, user);
                addDanceAndEditMessage(dance);
                break;
            case Commands.CANCEL:
                if (!dance.findSingleDancerAndRemove(user)) {
                    dance.findPairAndRemoveDancer(user);
                }
                telegramCommandsExecutor.sendAlertMessage(callbackQuery.getId(), "Вы не пойдёте");
                addDanceAndEditMessage(dance);
                break;
        }
    }

    private void addDanceAndEditMessage(Dance dance) {
        EditMessageText newMessage = new EditMessageText();
        newMessage.setInlineMessageId(messageId);
        newMessage.setReplyMarkup(keyboardFactory.createDanceKeyboard(null, messageId));
        newMessage.setParseMode(ParseMode.HTML);
        newMessage.setText(dance.toString());

        telegramCommandsExecutor.send(newMessage);
    }

    private void sendInlineAnswer(String command) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setUrl("t.me/CoopDanceBot?start=" + command);

        telegramCommandsExecutor.send(answerCallbackQuery);
    }
}
