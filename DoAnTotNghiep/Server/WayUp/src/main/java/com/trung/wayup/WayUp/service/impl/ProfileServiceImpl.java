package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.Profile;
import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.repository.ProfileRepository;
import com.trung.wayup.WayUp.service.base.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public Profile insert(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Profile update(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public void delete(int id) {
        profileRepository.deleteById(id);
    }

    @Override
    public Collection<Profile> gettAll() {
        return null;
    }

    @Override
    public Profile getFirstByUser(User user) {
        return profileRepository.getFirstByUser(user);
    }

    @Override
    public List<Profile> findAllByUser(User user) {
        return profileRepository.findAllByUser(user);
    }
}
