package com.gsTech.telegramBot.handlers.newEvent;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.orm.User;
import com.gsTech.telegramBot.services.*;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;


/**
 * Handler responsável por conduzir o fluxo de criação de uma nova tarefa com base nas mensagens
 * de texto do usuário, utilizando o estado atual armazenado para determinar a próxima etapa.
 *
 * <p>Fluxo esperado:</p>
 * <ol>
 *   <li>Nome da tarefa ({@code WAITING_FOR_NAME})</li>
 *   <li>Descrição da tarefa ({@code WAITING_FOR_DESCRIPTION})</li>
 *   <li>Data da tarefa ({@code WAITING_FOR_DATE})</li>
 * </ol>
 *
 * <p>Conforme o estado do usuário, a entrada textual é interpretada e armazenada no {@code EventDTO}.
 * Quando todos os dados são coletados, a tarefa é exibida com opções de salvar ou voltar.</p>
 */
@Component
public class CreateEventFlowHandler implements CommandHandler {

    @Autowired
    private UserStateService userState;
    @Autowired
    private UserEventService userEvent;
    @Autowired
    private SendMessageFactory sendMessage;
    @Autowired
    private DateParseService dateParseService;

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    /**
     * Verifica se o handler pode processar a atualização recebida.
     * Este handler só processa mensagens de texto quando o estado do usuário indica
     * que ele está no meio do fluxo de criação de tarefa (estado iniciando com {@code "WAITING_FOR_"}).
     *
     * @param update Atualização recebida do Telegram.
     * @return {@code true} se o estado do usuário iniciar com "WAITING_FOR_", senão {@code false}.
     */
    @Override
    public boolean canHandle(Update update) {

        if (update.getMessage() == null || update.getMessage().getText() == null) return false;

        Long chatId = update.getMessage().getChat().getId();
        String state = userState.getUserState(chatId);

        return state != null && state.startsWith("WAITING_FOR_");
    }


    /**
     * Processa a mensagem de texto recebida, delegando ao método {@code processCreateState}
     * com base no estado atual do usuário.
     *
     * @param update Atualização do Telegram com mensagem de texto do usuário.
     * @return Resposta adequada à etapa atual do fluxo de criação de tarefa.
     */
    @Override
    public BotApiMethod<?> handle(Update update) {

        Long chatId = update.getMessage().getChat().getId();
        String messageText = update.getMessage().getText();

        return processCreateState(chatId, messageText);
    }


    /**
     * Processa a entrada do usuário conforme seu estado atual no fluxo de criação da tarefa.
     *
     * <ul>
     *   <li>{@code WAITING_FOR_NAME}: Salva o nome da tarefa e solicita a descrição.</li>
     *   <li>{@code WAITING_FOR_DESCRIPTION}: Salva a descrição e solicita a data.</li>
     *   <li>{@code WAITING_FOR_DATE}: Tenta interpretar a data, salvar no evento e mostrar resumo com opções.</li>
     * </ul>
     *
     * <p>Se ocorrer erro na leitura da data, retorna mensagem com exemplos válidos.</p>
     *
     * @param chatId ID do chat do usuário.
     * @param messageText Texto enviado pelo usuário.
     * @return Mensagem para próxima etapa ou resumo da tarefa criada.
     */
    private BotApiMethod<?> processCreateState(Long chatId, String messageText) {

        String state = userState.getUserState(chatId);
        EventDTO event = userEvent.getEvent(chatId);

        if (state == null || event == null) {
            return sendMessage.sendMessage(chatId, "Erro interno");
        }

        switch (state) {

            case "WAITING_FOR_NAME":
                userState.setUserState(chatId, "WAITING_FOR_DESCRIPTION");
                event.setEventName(messageText);
                return sendMessage.sendMessage(chatId, "Descrição da tarefa: ");

            case "WAITING_FOR_DESCRIPTION":
                userState.setUserState(chatId, "WAITING_FOR_DATE");
                event.setDescription(messageText);
                return sendMessage.sendMessage(chatId, "Data da tarefa: \n" +
                        "Exs: " +
                        "14/09/2025 19:30:\n" +
                        "segunda às 14:00\n" +
                        "1 de dezembro às 09:45\n" +
                        "hoje as 08:30");

            case "WAITING_FOR_DATE":
                try {
                    String dateRegex = dateParseService.parseDate(messageText);
                    LocalDateTime date = LocalDateTime.parse(dateRegex, DATE_TIME_FORMATTER);
                    event.setTime(date);

                    return sendMessage.sendMessageWithBackTAndSave(chatId, event.toString());

                } catch (DateTimeParseException e) {
                    return sendMessage.sendMessage(chatId, "Formato inválido! " +
                            "Exs:\n" +
                            "Tente algo como:\n" +
                            "14/09/2025 19:30\n" +
                            "segunda às 14:00\n" +
                            "1 de dezembro às 09:45\n" +
                            "hoje as 08:30");
                }
        }
        return sendMessage.sendMessage(chatId, "Tarefa criada com sucesso:\n" + event);
    }
}
