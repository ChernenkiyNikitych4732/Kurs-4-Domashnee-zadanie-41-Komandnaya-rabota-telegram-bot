package skypro.teamworktelegrambot.infromation;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.CommandAbstractClass;
import skypro.teamworktelegrambot.buttons.constants.ConstantsButtons;
import skypro.teamworktelegrambot.message.SendMessageService;
import skypro.teamworktelegrambot.telegram.TelegramBotService;

import static skypro.teamworktelegrambot.buttons.constants.ConstantsButtons.*;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCallData.*;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.START_GREETING_MESSAGE;

/**
 * Класс формирует кнопоки для раздела информации по приюту.
 */
@Component
public class Information extends CommandAbstractClass {
    private final SendMessageService sendMessageService;

    String[] buttonsText = {
            GET_MORE_INFO_SHELTER_BUTTON,
            SCHEDULE_BUTTON,
            SECURITY_BUTTON,
            SAFETY_PRECAUTIONS_BUTTON,
            POST_CONTACT_BUTTON,
            CALL_VOLUNTEER_BUTTON,
            MENU_BUTTON};
    String[] buttonsCallData = {
            ABOUT_SHELTER,
            SCHEDULE,
            SECURITY,
            SAFETY_PRECAUTIONS,
            POST_CONTACT,
            CALL_VOLUNTEER,
            MENU};

    public Information(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {

    }

    /**
     * Метод приветствует пользователя першедшего в меню информации о приюте
     * и отправляет ему кнопки с информацией по выбранному приюту.
     *
     * @param callbackQuery - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     */
    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {

        sendMessageService.SendMessageToUserWithButtons(
                String.valueOf(callbackQuery.getFrom().getId()),
                START_GREETING_MESSAGE,
                buttonsText,
                buttonsCallData,
                telegramBotService
        );
    }
}