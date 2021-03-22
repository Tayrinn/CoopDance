package ru.tayrinn.telegram.coopdance.handlers;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.tayrinn.telegram.coopdance.InlineKeyboardFactory;
import ru.tayrinn.telegram.coopdance.TelegramCommandsExecutor;
import ru.tayrinn.telegram.coopdance.Utils;
import ru.tayrinn.telegram.coopdance.models.*;

import java.util.List;

public class MessageQueryHandler extends BotCommandsHandler<Message> {

    private final ChatDao chatDao;
    private final Dances dances;

    public MessageQueryHandler(TelegramCommandsExecutor telegramCommandsExecutor, InlineKeyboardFactory keyboardFactory, ChatDao chatDao, Dances dances) {
        super(telegramCommandsExecutor, keyboardFactory);
        this.chatDao = chatDao;
        this.dances = dances;
    }

    public void handle(Message data) {
        handleMessage(data);
    }

    private void handleStartMessage(Message msg) {
        SendMessage answer = new SendMessage();
        answer.setText("Введите имя своего партнера, команда должна начинаться с /user");
        answer.setChatId(msg.getChatId().toString());
        telegramCommandsExecutor.send(answer);

        writeUserMessageToDb(msg);
    }

    private void writeUserMessageToDb(Message msg) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(msg.getMessageId().toString());
        chatMessage.setChatId(msg.getChatId().toString());
        chatMessage.setText(msg.getText());
        chatMessage.setAuthorUsername(msg.getFrom().getUserName());
        chatMessage.setPayload("ddd");
        chatMessage.setBot(0);

        try {
            chatDao.writeChatMessage(chatMessage);
        } catch (Exception throwables) {
            telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), throwables.getMessage());
        }
    }

    private void handleMessage(Message msg) {
        Long chatId = msg.getChatId();
        telegramCommandsExecutor.sendChatMessage(chatId.toString(), "Hello world, " + msg.getText() + "!");
        if (msg.getText().startsWith("/start")) {
            handleStartMessage(msg);
        } else if (msg.getText().startsWith("/user")) {
            parseUsernameCommand(msg);
        }
    }

    private void parseUsernameCommand(Message msg) {
        try {
            List<ChatMessage> oldMessages = chatDao.getLastChatMessages(msg.getChatId().toString(), msg.getFrom().getUserName(), 1);
            oldMessages.forEach(chatMessage -> {
                telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), chatMessage.toString());
                parseStartCommandAnswer(chatMessage, msg);
            });
            telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), "Размер ответа = " + oldMessages.size());
        } catch (Exception throwables) {
            telegramCommandsExecutor.sendChatMessage(msg.getChatId().toString(), throwables.toString());
        }
    }

    private void parseStartCommandAnswer(ChatMessage chatMessage, Message origMessage) {
        CallbackData extractedInfo = Commands.parseCommand(Utils.decodeUtf8(chatMessage.getText().substring(7))); // "/start text"
        String command = extractedInfo.command;
        String messageId = extractedInfo.messageId;

        Dance dance = dances.getDanceByMessageId(messageId);

        Dancer partner = new Dancer();
        partner.stubName = origMessage.getText().substring(6); // "/user name"

        Dancer authorDancer = new Dancer();
        authorDancer.user = origMessage.getFrom();

        switch (command) {
            case Commands.ADD_GIRL_AND_BOY: {
                partner.sex = Dancer.Sex.BOY;
                authorDancer.sex = Dancer.Sex.GIRL;
                dance.addPair(partner, authorDancer);
                telegramCommandsExecutor.sendChatMessage(origMessage.getChatId().toString(), "ADD_GIRL_AND_BOY");
                break;
            }
            case Commands.ADD_BOY_AND_GIRL: {
                partner.sex = Dancer.Sex.GIRL;
                authorDancer.sex = Dancer.Sex.BOY;
                dance.addPair(authorDancer, partner);
                telegramCommandsExecutor.sendChatMessage(origMessage.getChatId().toString(), "ADD_BOY_AND_GIRL");
                break;
            }
        }
        dances.getDances().forEach(it -> telegramCommandsExecutor.sendChatMessage(origMessage.getChatId().toString(), it.toString()));

        EditMessageText newMessage = new EditMessageText();
        newMessage.setInlineMessageId(messageId);
        newMessage.setReplyMarkup(keyboardFactory.createStarterKeyboard(dance.message));
        newMessage.setParseMode(ParseMode.HTML);
        newMessage.setText(dance.toString());

        telegramCommandsExecutor.send(newMessage);
    }
}
