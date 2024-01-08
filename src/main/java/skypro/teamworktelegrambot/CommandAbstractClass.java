package skypro.teamworktelegrambot;


import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.telegram.TelegramBotService;

/**
 * Обстрактный класс реализующий интерфейс Command и
 * позволяющий классам наследникам использовать необходимый
 * метод интерфейса Command без его реализации.
 */
public abstract class CommandAbstractClass implements Command{
    public abstract void messagesExtractor(Message message, TelegramBotService telegramBotService);

    public abstract void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService);
}