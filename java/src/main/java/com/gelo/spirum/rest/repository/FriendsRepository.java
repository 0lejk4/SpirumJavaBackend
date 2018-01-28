package com.gelo.spirum.rest.repository;

import com.gelo.spirum.model.User;
import com.gelo.spirum.model.Friendship;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepository extends PagingAndSortingRepository<Friendship, Long>
{
    Friendship findByUserRefAndFriendRef(@Param("userRef") User userRef,@Param("friendRef") User friendRef);
}