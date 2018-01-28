package com.gelo.spirum.rest.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gelo.spirum.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Created by stephan on 20.03.16.
 */
@CrossOrigin(origins = {"http://localhost:4200", "https://spirum-191511.firebaseapp.com","https://spirum.tk" })
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    User findByUsername(@Param("username") String username);

    @JsonIgnoreProperties
    @Override
    User findOne(Long aLong);
}
