package skypro.teamworktelegrambot;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.telegram.TelegramBotService;
import skypro.teamworktelegrambot.telegram.TelegramBotService;

/**
 * Интерфейс для обработок значений от Telegram бота.
 */
public interface Command {
    /**
     * Метод обрабатывает Message сообщения.
     *
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     */
    default void messagesExtractor(Message message, TelegramBotService telegramBotService){}

    /**
     * * Метод обрабатывает CallbackQuery сообщения.
     *
     * @param callbackQuery - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     */
    default void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService){}
}