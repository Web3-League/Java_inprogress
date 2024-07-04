package irc_switch.config;

import irc_switch.dto.ChatRoomDto;
import irc_switch.dto.ChatRoomMessagesDto;
import irc_switch.dto.MessageDto;
import irc_switch.model.Message;
import irc_switch.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private Set<WebSocketSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private static final Logger logger = Logger.getLogger(WebSocketHandler.class.getName());

    @Autowired
    private MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        sessions.add(session);
        // Fetch and send existing messages
        String chatRoomId = getChatRoomIdFromSession(session);
        if (chatRoomId != null) {
            logger.info("Established connection for chat room ID: " + chatRoomId);
            sendInitialMessages(session, Long.valueOf(chatRoomId));
        } else {
            logger.warning("Chat room ID not found in session URI.");
        }
    }

    private String getChatRoomIdFromSession(WebSocketSession session) {
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        return parts.length > 3 ? parts[3] : null;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        Gson gson = new Gson();

        if (payload.contains("chatRoomId")) {
            MessageDto messageDto = gson.fromJson(payload, MessageDto.class);
            handleMessage(session, messageDto);
        } else {
            ChatRoomDto chatRoomDto = gson.fromJson(payload, ChatRoomDto.class);
            handleChatRoom(session, chatRoomDto);
        }
    }

    private void handleMessage(WebSocketSession session, MessageDto messageDto) throws IOException {
        try {
            // Check if userId is present
            if (messageDto.getUserId() == null) {
                session.sendMessage(new TextMessage("Error: User ID is required."));
                return;
            }

            Message savedMessage = messageService.createMessageFromDto(messageDto);
            MessageDto savedMessageDto = new MessageDto(
                    savedMessage.getUser().getId(),
                    savedMessage.getContent(),
                    savedMessage.getChatRoom().getId(),
                    savedMessage.getUser().getPseudonym(),
                    savedMessage.getUser().getUsername()
            );
            String savedMessageJson = new Gson().toJson(savedMessageDto);

            for (WebSocketSession webSocketSession : sessions) {
                if (webSocketSession.isOpen()) {
                    webSocketSession.sendMessage(new TextMessage(savedMessageJson));
                }
            }
        } catch (IllegalArgumentException e) {
            session.sendMessage(new TextMessage("Error: " + e.getMessage()));
        }
    }

    private void handleChatRoom(WebSocketSession session, ChatRoomDto chatRoomDto) throws IOException {
        // Fetch and send messages for the chat room
        Long chatRoomId = Long.valueOf(chatRoomDto.getName()); // Assuming chatRoomId is the name for simplicity
        sendInitialMessages(session, chatRoomId);
    }

    private void sendInitialMessages(WebSocketSession session, Long chatRoomId) throws IOException {
        List<MessageDto> messages = messageService.getMessagesByChatRoomId(chatRoomId);
        ChatRoomMessagesDto chatRoomMessagesDto = new ChatRoomMessagesDto(chatRoomId, messages);
        String initialMessagesJson = new Gson().toJson(chatRoomMessagesDto);
        session.sendMessage(new TextMessage(initialMessagesJson));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}

