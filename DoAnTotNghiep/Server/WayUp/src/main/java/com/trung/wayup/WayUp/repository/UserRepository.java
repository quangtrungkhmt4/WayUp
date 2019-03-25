package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByEmailAndPassword(String email, String pass);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM tbl_users WHERE id_user = ?1", nativeQuery = true)
    User searchUser(int id);

    @Query(value = "SELECT * FROM tbl_users WHERE lock = 0 AND permission = ?1", nativeQuery = true)
    List<User> findAllByPermission(int permission);

    @Query(value = "SELECT * FROM tbl_users WHERE lock = 1", nativeQuery = true)
    List<User> getLockUser();

    User findUserByEmail(String email);

}
