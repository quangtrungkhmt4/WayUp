package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.model.Vote;
import com.trung.wayup.WayUp.repository.VoteRepository;
import com.trung.wayup.WayUp.service.base.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote insert(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public Vote update(Vote vote) {
        return voteRepository.save(vote);
    }

    @Override
    public void delete(int id) {
        voteRepository.deleteById(id);
    }

    @Override
    public Collection<Vote> gettAll() {
        return null;
    }

    @Override
    public List<Vote> findAllByUser(User user) {
        return voteRepository.findAllByUser(user);
    }
}
