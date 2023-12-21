package skypro.teamworktelegrambot.start;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.CommandAbstractClass;
import skypro.teamworktelegrambot.entity.AnimalOwner;
import skypro.teamworktelegrambot.message.SendMessageService;
import skypro.teamworktelegrambot.repository.AnimalOwnerRepository;
import skypro.teamworktelegrambot.telegram.TelegramBotService;


import static skypro.teamworktelegrambot.buttons.constants.ConstantsButtons.*;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCallData.*;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCommands.START_TELEGRAM_BOT_COMMAND;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.*;

/**
 * Класс приветсвтвует пользователя и дают выбрать приют кошек или собак,
 * а также возможность стать волонтером.
 */
@Slf4j
@Component
public class Start extends CommandAbstractClass {
    private final SendMessageService sendMessageService;
    private final AnimalOwnerRepository animalOwnerRepository;

    String[] buttonsText = {DOG_SHELTER_BUTTON,
            CAT_SHELTER_BUTTON,
            BECOME_A_VOLUNTEER_BUTTON};
    String[] buttonsCallData = {DOG,
            CAT,
            BEST_VOLUNTEER};

    public Start(SendMessageService sendMessageService,
                 AnimalOwnerRepository animalOwnerRepository) {
        this.sendMessageService = sendMessageService;
        this.animalOwnerRepository = animalOwnerRepository;
    }

    /**
     * Метод формирует приветствие для нового пользователя и предоставляет
     * выбрать один из приютов. Приют для кошек или приют для собак. А также предоставляет
     * возможность стать волонтером.<br>
     * Метод задает набор стартовых boolean значений для объекта AnimalOwner.
     *
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see AnimalOwner
     * @see SendMessageService
     */
    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {
        AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(message.getChatId());

        if (message.getText().equals(START_TELEGRAM_BOT_COMMAND) && animalOwner.getRegistered() == null) {
            animalOwner.setRegistered(true);
            animalOwner.setCanSaveContact(false);
            animalOwner.setIsVolunteer(false);
            animalOwner.setTookTheAnimal(false);
            animalOwner.setHelpVolunteer(false);
            animalOwner.setCanSendReport(false);
            animalOwner.setInChat(false);
            animalOwnerRepository.save(animalOwner);

            sendMessageService.SendMessageToUser(String.valueOf(message.getChatId()),
                    START_GREETING_MESSAGE,
                    telegramBotService
            );

            sendMessageService.SendMessageToUserWithButtons(
                    String.valueOf(message.getChatId()),
                    getInfo("src/main/resources/bot-files/stage0/about_shelter.txt"),
                    buttonsText,
                    buttonsCallData,
                    telegramBotService
            );
        } else if ((message.getText().equals(START_TELEGRAM_BOT_COMMAND) && animalOwner.getRegistered())) {
            animalOwner.setCanSaveContact(false);
            animalOwner.setCanSendReport(false);
            animalOwner.setInChat(false);
            animalOwnerRepository.save(animalOwner);

            sendMessageService.SendMessageToUserWithButtons(
                    String.valueOf(message.getChatId()),
                    CHOOSE_A_SHELTER_OR_BECOME_A_VOLUNTEER_MESSAGE,
                    buttonsText,
                    buttonsCallData,
                    telegramBotService
            );
        }
    }

    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {
    }
}