package irc_switch;

import irc_switch.custom.CustomWebSocketServer;
import irc_switch.custom.CustomPrivateMessageWebSocketServer;
import irc_switch.service.MessageService;
import irc_switch.service.PrivateMessageService;
import irc_switch.service.UserService;
import irc_switch.repository.ChatRoomRepository;
import org.java_websocket.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Component
    public static class WebSocketServerRunner implements CommandLineRunner {

        @Autowired
        private MessageService messageService;

        @Autowired
        private PrivateMessageService privateMessageService;

        @Autowired
        private UserService userService;

        @Autowired
        private ChatRoomRepository chatRoomRepository;

        @Override
        public void run(String... args) throws Exception {
            WebSocketServer chatServer = new CustomWebSocketServer(new InetSocketAddress("localhost", 59001), messageService, userService, chatRoomRepository);
            chatServer.start();
            System.out.println("Chat WebSocket server started successfully on ws://localhost:59001");

            WebSocketServer privateMessageServer = new CustomPrivateMessageWebSocketServer(new InetSocketAddress("localhost", 59002), privateMessageService, userService);
            privateMessageServer.start();
            System.out.println("Private Message WebSocket server started successfully on ws://localhost:59002");
        }
    }
}

