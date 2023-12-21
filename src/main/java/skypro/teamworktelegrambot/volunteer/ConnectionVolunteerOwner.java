package skypro.teamworktelegrambot.volunteer;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import skypro.teamworktelegrambot.CommandAbstractClass;
import skypro.teamworktelegrambot.entity.AnimalOwner;
import skypro.teamworktelegrambot.entity.Volunteer;
import skypro.teamworktelegrambot.message.SendMessageService;
import skypro.teamworktelegrambot.repository.AnimalOwnerRepository;
import skypro.teamworktelegrambot.repository.VolunteersRepository;
import skypro.teamworktelegrambot.telegram.TelegramBotService;


import static skypro.teamworktelegrambot.buttons.constants.ConstantsButtons.INTERRUPT_CHAT_BUTTON;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCallData.CHAT;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsCommands.REPORT_CONNECT_TO_USER_COMMAND;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.ABOUT_REPORT_TO_USER_MESSAGE;
import static skypro.teamworktelegrambot.buttons.constants.ConstantsText.ABOUT_REPORT_TO_VOLUNTEER_MESSAGE;

/**
 * Класс соединяет волонтера с пользователем.
 */
@Component
public class ConnectionVolunteerOwner extends CommandAbstractClass {

    private final AnimalOwnerRepository animalOwnerRepository;
    private final VolunteersRepository volunteersRepository;
    private final SendMessageService sendMessageService;

    public ConnectionVolunteerOwner(AnimalOwnerRepository animalOwnerRepository,
                                    VolunteersRepository volunteersRepository,
                                    SendMessageService sendMessageService) {
        this.animalOwnerRepository = animalOwnerRepository;
        this.volunteersRepository = volunteersRepository;
        this.sendMessageService = sendMessageService;
    }

    String[] buttonsText = {INTERRUPT_CHAT_BUTTON};
    String[] buttonsCallData = {CHAT};

    /**
     * Метод получает из объекта Message текст волонтера с chatId пользователя
     * и соединяет волонтера с пользователем.<br>
     * Метод присваивает волонтеру Volunteer, пользователя AnimalOwner(animalOwner).<br>
     * Метод назначает волонтеру Volunteer, boolean значение IsBusy(true).<br>
     * Метод присваивает волонтеру AnimalOwner, boolean значение InChat(true).<br>
     * Метод назначает пользователю AnimalOwner, boolean значение HelpVolunteer(true).<br>
     * Метод назначает пользователю AnimalOwner, boolean значение InChat(true).<br>
     * Метод присваивает пользователю AnimalOwner, волонтера который хочет с ним
     * связаться Volunteer(volunteer).<br>
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     */
    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {
        AnimalOwner volunteerOwner = animalOwnerRepository.findByIdChat(message.getFrom().getId());

        if (volunteerOwner.getIsVolunteer()) {
            Volunteer volunteer = volunteersRepository.findByIdChat(message.getFrom().getId());

            if (message.getText().contains(REPORT_CONNECT_TO_USER_COMMAND)) {
                String resultId = message.getText().replaceAll(REPORT_CONNECT_TO_USER_COMMAND, "");

                System.out.println("отправляется сообщение от волонтера пользователю ConnectionVolunteerOwner");

                sendMessageService.SendMessageToUser(
                        String.valueOf(resultId),
                        ABOUT_REPORT_TO_USER_MESSAGE,
                        telegramBotService
                );

                sendMessageService.SendMessageToUserWithButtons(
                        String.valueOf(volunteerOwner.getIdChat()),
                        ABOUT_REPORT_TO_VOLUNTEER_MESSAGE + resultId,
                        buttonsText,
                        buttonsCallData,
                        telegramBotService
                );
                AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(Long.valueOf(resultId));
                volunteer.setAnimalOwner(animalOwner);
                volunteer.setIsBusy(true);
                volunteerOwner.setInChat(true);
                animalOwner.setHelpVolunteer(true);
                animalOwner.setVolunteer(volunteer);
                animalOwner.setInChat(true);
                volunteersRepository.save(volunteer);
                animalOwnerRepository.save(animalOwner);
                animalOwnerRepository.save(volunteerOwner);
            }
        }
    }

    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {

    }
}