package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.User;

import java.util.List;

public interface UserService extends Service<User> {

    User findUserByEmailAndPassword(String email, String pass);

    boolean existsByEmail(String email);

    User searchUser(int id);

    List<User> findAllByPermission(int permission);

    List<User> getLockUser();

    User findUserByEmail(String email);
}
