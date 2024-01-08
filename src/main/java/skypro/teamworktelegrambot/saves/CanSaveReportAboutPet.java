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

import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.SAVE_REPORT_MESSAGE;

/**
 * Класс предоставляет шаблон заполнения для сохранения отчета о животном.
 */
@Slf4j
@Component
public class CanSaveReportAboutPet extends CommandAbstractClass {
    private final SendMessageService sendMessageService;
    private final AnimalOwnerRepository animalOwnerRepository;

    public CanSaveReportAboutPet(SendMessageService sendMessageService,
                                 AnimalOwnerRepository animalOwnerRepository) {
        this.sendMessageService = sendMessageService;
        this.animalOwnerRepository = animalOwnerRepository;
    }

    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {

    }

    /**
     * Метод формирует шаблон заполнения для сохранения отчета о животном и отправляет его пользователю.<br>
     * Метод назначает пользователю AnimalOwner, boolean значение CanSendReport(true).
     *
     * @param callbackQuery - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     * @see AnimalOwner
     */
    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {
        log.info("Invoked a method for save animal report");

        AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(callbackQuery.getFrom().getId());
        animalOwner.setCanSendReport(true);
        animalOwnerRepository.save(animalOwner);

        sendMessageService.SendMessageToUser(
                String.valueOf(callbackQuery.getFrom().getId()),
                SAVE_REPORT_MESSAGE,
                telegramBotService
        );
    }
}