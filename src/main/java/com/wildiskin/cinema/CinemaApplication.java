package com.wildiskin.cinema;

//import com.wildiskin.cinema.telegramBot.TelegramBot;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.validation.BindingResult;
//import org.telegram.telegrambots.longpolling.BotSession;
//import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
//import org.telegram.telegrambots.longpolling.starter.TelegramBotInitializer;
//import org.telegram.telegrambots.longpolling.starter.TelegramBotStarterConfiguration;


@SpringBootApplication
public class CinemaApplication {

//	@Value("${telegram.bot.token}")
//	private static String TOKEN;

	public static void main(String[] args) {

//
//		TelegramBotStarterConfiguration config = new TelegramBotStarterConfiguration();
//
//		TelegramBotsLongPollingApplication app = config.telegramBotsApplication();
//
//		app.registerBot(TOKEN, telegramBot);

		SpringApplication.run(CinemaApplication.class, args);

	}
}
