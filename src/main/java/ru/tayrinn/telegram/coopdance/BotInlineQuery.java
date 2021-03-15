package ru.tayrinn.telegram.coopdance;

import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class BotInlineQuery {
    private final InlineQuery inlineQuery;

    public BotInlineQuery(InlineQuery inlineQuery) {
        this.inlineQuery = inlineQuery;
    }

    public AnswerInlineQuery answer() {
        return sendInlineAnswer();
    }

    private AnswerInlineQuery sendInlineAnswer() {
        List<InlineQueryResult> results = new ArrayList<>();
        InlineQueryResultArticle article = new InlineQueryResultArticle();
        InputTextMessageContent messageContent = new InputTextMessageContent();
        messageContent.setMessageText("1) " + inlineQuery.getQuery());
        article.setInputMessageContent(messageContent);
        article.setId("111");
        article.setTitle("Нажмите для создания голосовалки");
        article.setReplyMarkup(sendKeyboard());
        results.add(article);

        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(1000);
        answerInlineQuery.setResults(results);

        return answerInlineQuery;
    }

    private InlineKeyboardMarkup sendKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Тык2");
        inlineKeyboardButton1.setCallbackData("button_pressed");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
