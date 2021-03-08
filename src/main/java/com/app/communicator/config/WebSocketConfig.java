package com.app.communicator.config;

import com.app.communicator.dto.securityDto.MessageDto;
import com.app.communicator.security.tokens.TokensService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.simp.config.AbstractMessageBrokerConfiguration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.*;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final TokensService tokensService;
    private final ObjectMapper objectMapper;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user", "/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-chat").setAllowedOrigins("http://localhost:4200").withSockJS();
        registry.addEndpoint("/websocket-chat").setAllowedOrigins("http://localhost:4200");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                try {
                    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        System.out.println("===============POLACZONO===============");
                        String authToken = accessor.getFirstNativeHeader("authorization");
                        UsernamePasswordAuthenticationToken auth = tokensService.parseToken(authToken);
                        accessor.setUser(auth);
                    }

                    if(StompCommand.SEND == accessor.getCommand()){
                        MessageDto messageDto = (MessageDto) getMessageConverter().fromMessage(message, MessageDto.class);
                        UsernamePasswordAuthenticationToken auth = tokensService.parseToken(messageDto.getAuthToken());
                        accessor.setUser(auth);
                        return MessageBuilder.createMessage(objectMapper.writeValueAsString(messageDto.getMessage()), accessor.getMessageHeaders());
                    }

                    if(StompCommand.DISCONNECT == accessor.getCommand()){
                        System.out.println("===============ROZLACZONO===============");
                    }

                    return message;
                }catch (Exception ex) {
                    return null;
                }
            }
        });
    }

    private CompositeMessageConverter getMessageConverter(){
        return new AbstractMessageBrokerConfiguration() {
            @Override
            protected SimpUserRegistry createLocalUserRegistry(Integer integer) {
                return null;
            }
        }.brokerMessageConverter();
    }
}
