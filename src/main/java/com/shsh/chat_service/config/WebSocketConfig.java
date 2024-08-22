package com.shsh.chat_service.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        config.enableStompBrokerRelay("/topic", "/queue")
//                .setRelayHost("localhost")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest")
//                .setSystemLogin("guest")
//                .setSystemPasscode("guest")
//                .setVirtualHost("/");
//        config.setApplicationDestinationPrefixes("/app");
//        config.setUserDestinationPrefix("/user");
//    }
@Override
public void configureMessageBroker(MessageBrokerRegistry config) {
    // Включаем встроенный брокер сообщений
            config.enableSimpleBroker("/topic", "/queue", "/user");
            config.setUserDestinationPrefix("/user"); // Задаем префикс для назначения сообщений пользователю

         config.setApplicationDestinationPrefixes("/app"); // Префикс для маршрутизации сообщений приложения
}
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor());
    }
}