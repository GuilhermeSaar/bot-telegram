package com.gsTech.telegramBot.handlers.delete;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class deleteEventHandler implements CommandHandler {

    @Autowired
    private EventService eventService;
    @Autowired
    private SendMessageFactory sendMessage;

    @Override
    public boolean canHandle(Update update) {

        return update.hasCallbackQuery()
                && update.getCallbackQuery().getData().startsWith("DELETE_EVENT:");
    }

    @Override
    public BotApiMethod<?> handle(Update update) {

        String callBackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

        try {

            Long eventId = Long.parseLong(callBackData.split(":")[1]);
            eventService.delete(eventId);

            // lista atualizada
            List<EventDTO> events = eventService.findAllByChatId(chatId);

            if (events.isEmpty()) {

                EditMessageText noEventsMsg = new EditMessageText();
                noEventsMsg.setChatId(chatId.toString());
                noEventsMsg.setMessageId(messageId);
                noEventsMsg.setText("✅ Compromisso excluído.\n\n📭 Nenhum outro compromisso encontrado.");
                return noEventsMsg;

            }

            return sendMessage.editMessageEventList(chatId, messageId, events);

        } catch (NumberFormatException e) {
            return sendMessage.sendMessage(chatId, "Erro ao excluir: ID invalido");
        } catch (Exception e) {
            return sendMessage.sendMessage(chatId, "Ocorreu um erro ao tentar excluir");
        }
    }
}
