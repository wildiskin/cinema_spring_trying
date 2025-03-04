package com.wildiskin.cinema.services;

import com.wildiskin.cinema.telegramBot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.*;

import java.util.Collections;


@Service
public class TelegramService {

    private final TelegramBot telegramBot;

    @Autowired
    public TelegramService(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public boolean approvePhone() {

        KeyboardButton button = new KeyboardButton("NumberRequest");
        button.getRequestContact();
        KeyboardRow row = new KeyboardRow(button);
        ReplyKeyboardMarkup board = new ReplyKeyboardMarkup(Collections.singletonList(row));

        return false;
    }
}
