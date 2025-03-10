package com.wildiskin.cinema.telegramBot;

import com.wildiskin.cinema.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultContact;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collections;

@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Value("${telegram.bot.token}")
    private String TOKEN;

    @Value("${telegram.bot.username}")
    private String USERNAME;

    private TelegramClient telegramClient;

    private final UserService userService;

    @Autowired
    public TelegramBot(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
    public String getBotUsername() {
        return USERNAME;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {

            KeyboardButton button = new KeyboardButton("Share my phone number");
            button.setRequestContact(true);
            KeyboardRow row = new KeyboardRow(button);
            ReplyKeyboardMarkup board = ReplyKeyboardMarkup.builder()
                    .keyboardRow(row)
                    .resizeKeyboard(true)
                    .selective(true)
                    .build();

            long chat_id = update.getMessage().getChatId();

            SendMessage message0 = SendMessage // Create a message object
                    .builder()
                    .chatId(chat_id)
                    .replyMarkup(board)
                    .text("Share your number")
                    .build();
            try {
                telegramClient.execute(message0); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

        String phoneNumber = update.getMessage().getContact().getPhoneNumber();

        SendMessage message1 = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(update.getMessage().getContact().getPhoneNumber())
                .build();

//        try {
//            telegramClient.execute(message1); // Sending our message object to user
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }

    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
