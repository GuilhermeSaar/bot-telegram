package com.gsTech.telegramBot.handlers.delete;

import com.gsTech.telegramBot.DTO.EventDTO;
import com.gsTech.telegramBot.handlers.CommandHandler;
import com.gsTech.telegramBot.services.EventService;
import com.gsTech.telegramBot.utils.SendMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class DeleteMenuHadler implements CommandHandler {


    @Autowired
    private EventService eventService;

    @Autowired
    private SendMessageFactory sendMessage;


    @Override
    public boolean canHandle(Update update) {
        return update.hasCallbackQuery()
                && "DELETE".equals(update.getCallbackQuery().getData());
    }

    @Override
    public BotApiMethod<?> handle(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        List<EventDTO> events = eventService.findAllByChatId(chatId);

        if (events.isEmpty()) {
            return sendMessage.sendMessage(chatId, "📭 Nenhum compromisso para excluir.");
        }

        // Mostra compromissos com botão ❌ Excluir
        return sendMessage.sendMessageEventDelete(chatId, events);
    }
}
