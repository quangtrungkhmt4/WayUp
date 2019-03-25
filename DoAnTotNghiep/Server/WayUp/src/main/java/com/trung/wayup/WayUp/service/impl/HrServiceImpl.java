package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.Hr;
import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.repository.HrRepository;
import com.trung.wayup.WayUp.service.base.HrService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class HrServiceImpl implements HrService {

    @Autowired
    private HrRepository hrRepository;

    @Override
    public Hr insert(Hr hr) {
        return hrRepository.save(hr);
    }

    @Override
    public Hr update(Hr hr) {
        return hrRepository.save(hr);
    }

    @Override
    public void delete(int id) {
        hrRepository.deleteById(id);
    }

    @Override
    public Collection<Hr> gettAll() {
        return (Collection<Hr>) hrRepository.findAll();
    }

    @Override
    public Hr findHrByUser(User user) {
        return hrRepository.findHrByUser(user);
    }
}
