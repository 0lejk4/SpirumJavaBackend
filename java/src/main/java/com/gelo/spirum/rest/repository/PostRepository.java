package com.gelo.spirum.rest.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gelo.spirum.model.Post;
import com.gelo.spirum.model.PostProj;
import com.gelo.spirum.model.User;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
@Repository
@CrossOrigin(origins = {"http://localhost:4200", "https://spirum-191511.firebaseapp.com","https://spirum.tk" },allowedHeaders = {"Authorization","Content-Type","Cache-Control"},allowCredentials = "true")
@RepositoryRestResource(excerptProjection = PostProj.class , collectionResourceRel = "posts", path = "posts")
public interface PostRepository extends PagingAndSortingRepository<Post, Long>
{
        Post findByUser(@Param("user") User user);

        @JsonIgnore
        @Override
        Post save(Post entity);


        @JsonIgnore
        @Override
        void delete(Long aLong);
        @JsonIgnore
        @Override
        void delete(Post entity);
        @JsonIgnore
        @Override
        void delete(Iterable<? extends Post> entities);
        @JsonIgnore
        @Override
        void deleteAll();
}