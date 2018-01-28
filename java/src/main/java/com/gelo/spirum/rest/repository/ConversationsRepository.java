package com.gelo.spirum.rest.repository;

import com.gelo.spirum.model.ConversationType;
import com.gelo.spirum.model.Conversation;
import com.gelo.spirum.model.User;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface ConversationsRepository extends PagingAndSortingRepository<Conversation, Long>
{
    Conversation  findByTypeAndAndName(@Param("type") ConversationType type,@Param("name") String name);
}