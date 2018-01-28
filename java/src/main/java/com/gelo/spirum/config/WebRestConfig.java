package com.gelo.spirum.config;

import com.gelo.spirum.model.Post;
import com.gelo.spirum.model.User;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class WebRestConfig extends RepositoryRestConfigurerAdapter
{

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
    {
        config.exposeIdsFor(Post.class,User.class);
    }
}