package skypro.teamworktelegrambot.saves;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.CommandAbstractClass;
import skypro.teamworktelegrambot.entity.AnimalOwner;
import skypro.teamworktelegrambot.message.SendMessageService;
import skypro.teamworktelegrambot.repository.AnimalOwnerRepository;
import skypro.teamworktelegrambot.telegram.TelegramBotService;

import static skypro.teamworktelegrambot.buttons.constants.ConstantsButtons.MENU_BUTTON;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCallData.MENU;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.SAVE_CONTACT_WITH_MENU_SUCCESSFULLY_MESSAGE;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.SAVE_CONTACT_WITH_START_SUCCESSFULLY_MESSAGE;

/**
 * Класс сохраняет контакт пользователя в БД.
 */
@Slf4j
@Component
public class SaveContacts extends CommandAbstractClass {
    private final SendMessageService sendMessageService;
    private final AnimalOwnerRepository animalOwnerRepository;
    String[] buttonsText = {MENU_BUTTON};
    String[] buttonsCallData = {MENU};

    public SaveContacts(SendMessageService sendMessageService, AnimalOwnerRepository animalOwnerRepository) {
        this.sendMessageService = sendMessageService;
        this.animalOwnerRepository = animalOwnerRepository;
    }

    /**
     * Метод сохраняет в БД контактный телефон пользователя или волонтера.<br>
     * Метод назначает пользователю AnimalOwner, boolean значение CanSaveContact(false).
     *
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     */
    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {
        AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(message.getChatId());

        if (message.hasText() && animalOwner.getCanSaveContact() && !animalOwner.getIsVolunteer()) {

            animalOwner.setContactInformation(String.valueOf(message.getText()));
            animalOwner.setCanSaveContact(false);
            animalOwnerRepository.save(animalOwner);

            sendMessageService.SendMessageToUserWithButtons(
                    String.valueOf(message.getChatId()),
                    SAVE_CONTACT_WITH_MENU_SUCCESSFULLY_MESSAGE,
                    buttonsText,
                    buttonsCallData,
                    telegramBotService
            );
        }
        else if (message.hasText() && animalOwner.getCanSaveContact() && animalOwner.getIsVolunteer()) {

            animalOwner.setContactInformation(String.valueOf(message.getText()));
            animalOwner.setCanSaveContact(false);
            animalOwnerRepository.save(animalOwner);

            sendMessageService.SendMessageToUser(
                    String.valueOf(message.getChatId()),
                    SAVE_CONTACT_WITH_START_SUCCESSFULLY_MESSAGE,
                    telegramBotService
            );
        }
    }

    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {

    }
}