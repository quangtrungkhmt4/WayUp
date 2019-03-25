package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Hr;
import com.trung.wayup.WayUp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface HrRepository extends CrudRepository<Hr, Integer> {

    Hr findHrByUser(User user);
}
