package com.gelo.spirum.config;

import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

/**
 * Created by olejk4 on 10.11.17.
 */
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer
{

    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages)
    {
        messages.simpDestMatchers("/user/*").authenticated();
    }

    @Override
    protected boolean sameOriginDisabled()
    {
        return true;
    }
}