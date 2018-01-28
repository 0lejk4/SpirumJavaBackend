package com.gelo.spirum.rest.repository;

import com.gelo.spirum.model.Comment;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long>
{
}
