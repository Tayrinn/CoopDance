package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.tayrinn.telegram.coopdance.models.Commands;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardFactory {

    public InlineKeyboardMarkup createDanceKeyboard(String message, String messageId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton girl = new InlineKeyboardButton();
        girl.setText("\uD83D\uDC83");
        girl.setCallbackData(Commands.format(Commands.ADD_GIRL, message, messageId));

        InlineKeyboardButton boy = new InlineKeyboardButton();
        boy.setText("\uD83D\uDD7A");
        boy.setCallbackData(Commands.format(Commands.ADD_BOY, message, messageId));

        InlineKeyboardButton girlAndBoy = new InlineKeyboardButton();
        girlAndBoy.setText("\uD83D\uDC83 + \uD83D\uDD7A");
        girlAndBoy.setCallbackData(Commands.format(Commands.ADD_GIRL_AND_BOY, message, messageId));

        InlineKeyboardButton boyAndGirl = new InlineKeyboardButton();
        boyAndGirl.setText("\uD83D\uDD7A + \uD83D\uDC83");
        boyAndGirl.setCallbackData(Commands.format(Commands.ADD_BOY_AND_GIRL, message, messageId));

        InlineKeyboardButton cancel = new InlineKeyboardButton();
        cancel.setText("\u274C");
        cancel.setCallbackData(Commands.format(Commands.CANCEL, message, messageId));

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(girl);
        keyboardButtonsRow1.add(boy);
        keyboardButtonsRow1.add(girlAndBoy);
        keyboardButtonsRow1.add(boyAndGirl);
        keyboardButtonsRow1.add(cancel);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createStarterKeyboard(String message) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Создать коллективку");
        button.setCallbackData(Commands.format(Commands.CREATE, message, null));
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(button);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(row);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
