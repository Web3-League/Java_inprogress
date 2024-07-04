package irc_switch.custom;

import irc_switch.dto.InitialConversationDto;
import irc_switch.dto.PrivateMessageDto;
import irc_switch.model.User;
import irc_switch.service.PrivateMessageService;
import irc_switch.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;

public class CustomPrivateMessageWebSocketServer extends WebSocketServer {

    private Set<WebSocket> connections = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private Map<Long, WebSocket> userConnections = new ConcurrentHashMap<>();
    private final PrivateMessageService privateMessageService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public CustomPrivateMessageWebSocketServer(InetSocketAddress address, PrivateMessageService privateMessageService, UserService userService) {
        super(address);
        this.privateMessageService = privateMessageService;
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
            sendInitialConversation(conn, userId);
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
            Map<String, Object> receivedMessage = objectMapper.readValue(message, Map.class);
            System.out.println("Parsed message: " + receivedMessage);
    
            if (receivedMessage.containsKey("initialConversation") && (Boolean) receivedMessage.get("initialConversation")) {
                if (receivedMessage.containsKey("userId")) {
                    Long userId = ((Number) receivedMessage.get("userId")).longValue();
                    if (userId != null) {
                        sendInitialConversation(conn, userId);
                    } else {
                        throw new IllegalArgumentException("Invalid userId in initial conversation request");
                    }
                } else {
                    throw new IllegalArgumentException("Missing userId in initial conversation request");
                }
            } else if (receivedMessage.containsKey("senderId") && receivedMessage.containsKey("recipientId")) {
                PrivateMessageDto privateMessageDto = objectMapper.convertValue(receivedMessage, PrivateMessageDto.class);
                handlePrivateMessage(privateMessageDto);
            } else {
                throw new IllegalArgumentException("Invalid message format");
            }
        } catch (JsonProcessingException e) {
            System.out.println("JSON processing error: " + e.getMessage());
            conn.send("Error: Invalid message format.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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
        String query = handshake.getResourceDescriptor();
        System.out.println("Handshake resource descriptor: " + query);
    
        if (query.contains("?")) {
            String[] queryParams = query.split("\\?", 2);
            if (queryParams.length > 1) {
                String[] params = queryParams[1].split("&");
                for (String param : params) {
                    String[] pair = param.split("=");
                    System.out.println("Query param: " + param);
                    if (pair.length == 2 && pair[0].equals("userId")) {
                        try {
                            return Long.parseLong(pair[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid userId format: " + pair[1]);
                            return null;
                        }
                    }
                }
            }
        }
    
        System.out.println("userId not found in handshake");
        return null;
    }
    

    private void sendInitialConversation(WebSocket conn, Long userId) {
        List<User> recipients = privateMessageService.getPrivateMessageConversations(userId);
        for (User recipient : recipients) {
            List<PrivateMessageDto> conversation = privateMessageService.getMessagesBetweenUsers(userId, recipient.getId());
            try {
                InitialConversationDto initialConversationDto = new InitialConversationDto(recipient, conversation);
                String conversationJson = objectMapper.writeValueAsString(initialConversationDto);
                conn.send(conversationJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                conn.send("Error: " + e.getMessage());
            }
        }
    }

    private void handlePrivateMessage(PrivateMessageDto privateMessageDto) {
        User sender = userService.findById(privateMessageDto.getSenderId()).orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        privateMessageDto.setSenderPseudonym(sender.getPseudonym() != null ? sender.getPseudonym() : sender.getUsername());

        PrivateMessageDto savedMessageDto = privateMessageService.sendMessage(privateMessageDto.getSenderId(), privateMessageDto.getRecipientId(), privateMessageDto.getContent());

        try {
            String messageJson = objectMapper.writeValueAsString(savedMessageDto);

            WebSocket senderSocket = userConnections.get(savedMessageDto.getSenderId());
            WebSocket recipientSocket = userConnections.get(savedMessageDto.getRecipientId());

            if (senderSocket != null && senderSocket.isOpen()) {
                senderSocket.send(messageJson);
            }
            if (recipientSocket != null && recipientSocket.isOpen()) {
                recipientSocket.send(messageJson);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
