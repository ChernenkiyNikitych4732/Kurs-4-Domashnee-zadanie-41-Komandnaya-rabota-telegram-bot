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

/**
 * Класс обрабатывает входящие сообщения от пользователя и волонтера.
 */
@Component
public class HelpVolunteer extends CommandAbstractClass {
    private final SendMessageService sendMessageService;
    private final AnimalOwnerRepository animalOwnerRepository;
    private final VolunteersRepository volunteersRepository;

    public HelpVolunteer(SendMessageService sendMessageService,
                         AnimalOwnerRepository animalOwnerRepository,
                         VolunteersRepository volunteersRepository) {
        this.sendMessageService = sendMessageService;
        this.animalOwnerRepository = animalOwnerRepository;
        this.volunteersRepository = volunteersRepository;
    }

    /**
     * Метод принемает входящие сообщения от пользователя и волонтера, а также
     * перенаправляет их друг-другу.
     *
     * @param message - объект Telegram для получения значений из Telegram бота.
     * @param telegramBotService - объект передается в SendMessageService для возможности
     *                             вызвать метод execute и отправить сообщение пользователю.
     * @see SendMessageService
     */
    @Override
    public void messagesExtractor(Message message, TelegramBotService telegramBotService) {
        AnimalOwner animalOwner = animalOwnerRepository.findByIdChat(message.getFrom().getId());

        if (animalOwner.getIsVolunteer()) {
            Volunteer volunteer = volunteersRepository.findByIdChat(message.getFrom().getId());

            if (volunteer.getAnimalOwner() != null) {

                System.out.println("отправляется сообщение от волонтера пользователю HelpVolunteer");

                sendMessageService.SendMessageToUser(
                        String.valueOf(volunteer.getAnimalOwner().getIdChat()),
                        message.getText(),
                        telegramBotService
                );
            }
        }

        if (!animalOwner.getIsVolunteer()) {
            if (animalOwner.getVolunteer() != null) {

                System.out.println("отправляется сообщение от пользователя волонтёру HelpVolunteer");

                sendMessageService.SendMessageToUser( // отправляется сообщение волонтёру
                        String.valueOf(animalOwner.getVolunteer().getIdChat()),
                        message.getText(),
                        telegramBotService
                );
            }
        }
    }

    @Override
    public void callBackQueryExtractor(CallbackQuery callbackQuery, TelegramBotService telegramBotService) {

    }
}