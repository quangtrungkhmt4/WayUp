package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Profile;
import com.trung.wayup.WayUp.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Integer> {

    List<Profile> findAllByUser(User user);

    Profile getFirstByUser(User user);

}
