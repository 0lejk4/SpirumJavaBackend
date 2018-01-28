package com.gelo.spirum.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ServiceSpirumImpl implements ServiceSpirum
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(propagation= Propagation.REQUIRED)
    @Override
    public void insertFriendship(long userid, long friendid)
    {
        jdbcTemplate.update("INSERT INTO friendship VALUES(NULL ,TRUE ,?,?)",userid,friendid);
    }
}
