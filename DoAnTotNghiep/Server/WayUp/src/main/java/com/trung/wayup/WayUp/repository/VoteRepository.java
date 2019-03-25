package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.model.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VoteRepository extends CrudRepository<Vote, Integer> {

    List<Vote> findAllByUser(User user);
}
