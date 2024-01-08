package skypro.teamworktelegrambot.saves;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.CommandAbstractClass;
import skypro.teamworktelegrambot.entity.AnimalOwner;
import skypro.teamworktelegrambot.exception.UploadFileException;
import skypro.teamworktelegrambot.message.SendMessageService;
import skypro.teamworktelegrambot.repository.AnimalOwnerRepository;
import skypro.teamworktelegrambot.service.file.FileService;
import skypro.teamworktelegrambot.telegram.TelegramBotService;

import java.io.IOException;

import static skypro.teamworktelegrambot.buttons.constants.ConstantsButtons.*;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCallData.*;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.*;

/**
 * Класс сохраняет отчет о животном пользователя в БД.
 */
@Slf4j
@Component
public class SavePhoto extends CommandAbstractClass {
    private final SendMessageService sendMessageService;
    private final AnimalOwnerRepository animalOwnerRepository;
    private final FileService fileService;
    String[] buttonsText = {MENU_BUTTON};
    String[] buttonsCallData = {MENU};

    public SavePhoto(SendMessageService sendMessageService,
                     AnimalOwnerRepository animalOwnerRepository,
                     FileService fileService) {
        this.sendMessageService = sendMessageService;
        this.animalOwnerRepository = animalOwnerRepository;
        this.fileService = fileService;
    }

    /**
     * Метод делегирует сохранение отчета о животном пользователя медоду uploadReport.
     *
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     */
    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {
        try {
            uploadReport(message, telegramBotService);
        } catch (IOException | NullPointerException e) {
            log.error("Exception in method execute SaveReportAboutPet class");
        }
    }

    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {

    }

    /**
     * Метод проверяет отчет на праильность заполнения.<br>
     * Если отчет соответствует требованиям указанным в классе CanSaveReportAboutPet
     * отчет сохраняется в БД. Иначе информирует пользователя о некорректном
     * заполнении отчета.
     *
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @throws IOException выбрасывается если загрузка отчета в БД не удалась.
     * @see SendMessageService
     * @see CanSaveReportAboutPet
     */
    private void uploadReport(Message message, TelegramBotService telegramBotService) throws IOException {
        log.info("Invoked a method for uploading animal report");

        if (message.hasPhoto() && message.getPhoto() != null && message.getCaption() != null) {

            try {
                fileService.animalReport(message);
                AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(message.getChatId());
                animalOwner.setCanSendReport(false);
                animalOwnerRepository.save(animalOwner);

                sendMessageService.SendMessageToUserWithButtons(
                        String.valueOf(message.getChatId()),
                        SAVE_REPORT_SUCCESSFULLY_MESSAGE,
                        buttonsText,
                        buttonsCallData,
                        telegramBotService
                );

            } catch (UploadFileException e) {
                log.error("UploadFileException");

                sendMessageService.SendMessageToUser(
                        String.valueOf(message.getChatId()),
                        SAVE_REPORT_ERROR_MESSAGE,
                        telegramBotService
                );
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        } else {
            sendMessageService.SendMessageToUser(
                    String.valueOf(message.getChatId()),
                    SAVE_REPORT_INVALID_MESSAGE,
                    telegramBotService
            );
        }
    }
}