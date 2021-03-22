package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;

import java.util.ArrayList;
import java.util.List;

public class InlineQueryHandler extends BotCommandsHandler<InlineQuery> {

    public InlineQueryHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory) {
        super(telegramCommandsExecutor, keyboardFactory);
    }

    @Override
    public void handle(InlineQuery data) {
        AnswerInlineQuery query = createShortcutAnswer(data);
        telegramCommandsExecutor.send(query);
    }

    /**
     * Создает ответ в inline mode - когда внутри строки набираешь имя бота
     */
    private AnswerInlineQuery createShortcutAnswer(InlineQuery inlineQuery) {
        List<InlineQueryResult> results = new ArrayList<>();
        InlineQueryResultArticle article = new InlineQueryResultArticle();
        InputTextMessageContent messageContent = new InputTextMessageContent();
        messageContent.setMessageText(inlineQuery.getQuery());
        article.setInputMessageContent(messageContent);
        article.setId("111");

        article.setTitle("Нажмите для создания голосовалки");
        article.setReplyMarkup(keyboardFactory.createStarterKeyboard(inlineQuery.getQuery(), null));
        results.add(article);

        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        answerInlineQuery.setCacheTime(10000);
        answerInlineQuery.setResults(results);

        return answerInlineQuery;
    }
}
