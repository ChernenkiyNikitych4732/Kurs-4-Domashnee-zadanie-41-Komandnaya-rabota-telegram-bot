package skypro.teamworktelegrambot.volunteer;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.CommandAbstractClass;
import skypro.teamworktelegrambot.entity.AnimalOwner;
import skypro.teamworktelegrambot.message.SendMessageService;
import skypro.teamworktelegrambot.repository.AnimalOwnerRepository;
import skypro.teamworktelegrambot.telegram.TelegramBotService;

import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.BECOME_VOLUNTEER_MESSAGE;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.SAVE_CONTACT_MESSAGE;

/**
 * Класс предоставляет шаблон заполнения для сохранения контакта волонтера.
 */
@Component
public class BecomeVolunteer extends CommandAbstractClass {

    private final SendMessageService sendMessageService;
    private final AnimalOwnerRepository animalOwnerRepository;

    public BecomeVolunteer(SendMessageService sendMessageService,
                           AnimalOwnerRepository animalOwnerRepository) {
        this.sendMessageService = sendMessageService;
        this.animalOwnerRepository = animalOwnerRepository;
    }

    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {

    }

    /**
     * Метод формирует шаблон заполнения контакта и отправляет его пользователю.<br>
     * Метод назначает пользователю AnimalOwner, boolean значение CanSaveContact(true).
     * Метод назначает пользователю AnimalOwner, boolean значение IsVolunteer(true).
     *
     * @param callbackQuery - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     * @see AnimalOwner
     */
    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {

        AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(callbackQuery.getFrom().getId());
        if (!animalOwner.getIsVolunteer() && !animalOwner.getInChat()) {

            animalOwner.setIsVolunteer(true);
            animalOwner.setCanSaveContact(true);

            animalOwnerRepository.save(animalOwner);
            sendMessageService.SendMessageToUser(
                    String.valueOf(callbackQuery.getFrom().getId()),
                    BECOME_VOLUNTEER_MESSAGE +
                            SAVE_CONTACT_MESSAGE,
                    telegramBotService
            );
        }
    }
}