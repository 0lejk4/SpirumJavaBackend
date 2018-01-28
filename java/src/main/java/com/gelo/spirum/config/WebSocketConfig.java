package com.gelo.spirum.config;

import com.gelo.spirum.security.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by olejk4 on 06.11.17.
 */
@Configuration
@EnableScheduling
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer
{

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry)
    {
        stompEndpointRegistry.addEndpoint("/chatroom").setAllowedOrigins("http://localhost:4200", "https://spirum-191511.firebaseapp.com","https://spirum.tk").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry)
    {
        messageBrokerRegistry.enableSimpleBroker("/topic", "/queue");
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration)
    {
        registration.setInterceptors(new ChannelInterceptorAdapter()
        {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel)
            {

                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand()))
                {

                    String jwtToken = accessor.getFirstNativeHeader(tokenHeader);
                    String username = null;
                    String authToken = null;
                    if (jwtToken != null && jwtToken.startsWith("Bearer "))
                    {
                        authToken = jwtToken.substring(7);
                        try
                        {
                            username = jwtTokenUtil.getUsernameFromToken(authToken);
                        } catch (IllegalArgumentException e)
                        {
                            logger.error("an error occured during getting username from token", e);
                        } catch (ExpiredJwtException e)
                        {
                            //logger.warn("the token is expired and not valid anymore", e);
                        }
                    }

                    //logger.info("checking authentication for user " + username);
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
                    {

                        // It is not compelling necessary to load the use details from the database. You could also store the information
                        // in the token and read it from it. It's up to you ;)
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                        // the database compellingly. Again it's up to you ;)
                        if (jwtTokenUtil.validateToken(authToken, userDetails))
                        {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            //logger.info("authenticated user " + username + ", setting security context");
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            accessor.setUser(authentication);
                        }
                    }
                }
                return message;
            }

            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent)
            {
                StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);

                // ignore non-STOMP messages like heartbeat messages
                if (sha.getCommand() == null)
                {
                    return;
                }

                String sessionId = sha.getSessionId();

                switch (sha.getCommand())
                {
                    case CONNECT:
                        break;
                    case DISCONNECT:
                        break;
                    default:
                        break;

                }
            }
        });
    }

}
