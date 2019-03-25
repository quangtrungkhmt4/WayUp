package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.model.Vote;

import java.util.List;

public interface VoteService extends Service<Vote> {

    List<Vote> findAllByUser(User user);
}
