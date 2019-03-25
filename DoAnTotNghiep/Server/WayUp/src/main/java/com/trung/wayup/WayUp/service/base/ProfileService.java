package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Profile;
import com.trung.wayup.WayUp.model.User;

import java.util.List;

public interface ProfileService extends Service<Profile> {

    Profile getFirstByUser(User user);

    List<Profile> findAllByUser(User user);
}
