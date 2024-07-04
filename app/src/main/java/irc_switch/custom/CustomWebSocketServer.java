package irc_switch.custom;

import irc_switch.dto.MessageDto;
import irc_switch.model.User;
import irc_switch.service.MessageService;
import irc_switch.service.UserService;
import irc_switch.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;

public class CustomWebSocketServer extends WebSocketServer {

    private Set<WebSocket> connections = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Map<Long, WebSocket> userConnections = new ConcurrentHashMap<>();
    private final MessageService messageService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public CustomWebSocketServer(InetSocketAddress address, MessageService messageService, UserService userService, ChatRoomRepository chatRoomRepository) {
        super(address);
        this.messageService = messageService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        connections.add(conn);
        Long userId = getUserIdFromHandshake(handshake);
        if (userId != null) {
            userConnections.put(userId, conn);
        }
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        connections.remove(conn);
        userConnections.values().remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received: " + message);
        try {
            if (message.contains("chatRoomId")) {
                MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
                if (messageDto.getChatRoomId() != null) {
                    if (messageDto.getContent().isEmpty()) {
                        handleInitialConnection(conn, messageDto.getChatRoomId(), messageDto.getUserId());
                    } else {
                        handleChatRoomMessage(messageDto);
                    }
                } else {
                    System.out.println("Error: chatRoomId is null.");
                }
            } else if (message.contains("fetchUsers")) {
                MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
                handleFetchUsers(conn, messageDto.getChatRoomId());
            } else {
                System.out.println("Error: Invalid message format.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            conn.send("Error: " + e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("Server started successfully");
    }

    private Long getUserIdFromHandshake(ClientHandshake handshake) {
        // Extract user ID from the handshake request (assuming it's passed as a query parameter)
        String query = handshake.getResourceDescriptor().split("\\?", 2)[1];
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals("userId")) {
                try {
                    return Long.parseLong(pair[1]);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private void handleInitialConnection(WebSocket conn, Long chatRoomId, Long userId) {
        List<MessageDto> messages = messageService.getMessagesByChatRoomId(chatRoomId);
        try {
            String messagesJson = objectMapper.writeValueAsString(messages);
            conn.send(messagesJson);
        } catch (Exception e) {
            e.printStackTrace();
            conn.send("Error: " + e.getMessage());
        }
    }

    private void handleChatRoomMessage(MessageDto messageDto) {
        User user = userService.findById(messageDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        messageDto.setPseudonym(user.getPseudonym() != null ? user.getPseudonym() : user.getUsername());
        messageService.createMessageFromDto(messageDto);

        try {
            String messageJson = objectMapper.writeValueAsString(messageDto);
            for (WebSocket socket : connections) {
                socket.send(messageJson);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleFetchUsers(WebSocket conn, Long chatRoomId) {
        List<User> users = userService.getUsersByChatRoomId(chatRoomId);
        try {
            String usersJson = objectMapper.writeValueAsString(users);
            conn.send(usersJson);
        } catch (Exception e) {
            e.printStackTrace();
            conn.send("Error: " + e.getMessage());
        }
    }
}
