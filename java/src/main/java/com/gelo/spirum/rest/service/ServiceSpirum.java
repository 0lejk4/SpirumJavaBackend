package com.gelo.spirum.rest.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface ServiceSpirum
{
    @Transactional(propagation= Propagation.REQUIRED)
    void insertFriendship(long userid, long friendid);
}
