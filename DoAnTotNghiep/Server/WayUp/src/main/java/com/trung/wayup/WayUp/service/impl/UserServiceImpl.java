package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.repository.UserRepository;
import com.trung.wayup.WayUp.service.base.UserService;
import com.trung.wayup.WayUp.util.DateTime;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByEmailAndPassword(String email, String pass) {
        return userRepository.findUserByEmailAndPassword(email, pass);
    }


    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User searchUser(int id) {
        return userRepository.searchUser(id);
    }

    @Override
    public List<User> findAllByPermission(int permission) {
        return userRepository.findAllByPermission(permission);
    }

    @Override
    public List<User> getLockUser() {
        return userRepository.getLockUser();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User insert(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Collection<User> gettAll() {
        return (Collection<User>) userRepository.findAll();
    }
}
