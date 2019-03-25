package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.ApplyProfile;
import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.model.User;
import com.trung.wayup.WayUp.repository.ApplyProfileRepository;
import com.trung.wayup.WayUp.service.base.ApplyProfileService;
import com.trung.wayup.WayUp.util.DateTime;
import com.trung.wayup.WayUp.util.SendMail;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class ApplyProfileServiceImpl implements ApplyProfileService {

    @Autowired
    private ApplyProfileRepository applyProfileRepository;

    @Override
    public ApplyProfile insert(ApplyProfile applyProfile) {
        return applyProfileRepository.save(applyProfile);
    }

    @Override
    public ApplyProfile update(ApplyProfile applyProfile) {
        return applyProfileRepository.save(applyProfile);
    }

    @Override
    public void delete(int id) {
        applyProfileRepository.deleteById(id);
    }

    @Override
    public Collection<ApplyProfile> gettAll() {
        return null;
    }


    @Override
    public List<ApplyProfile> getApplyIsWaiting() {
        return applyProfileRepository.getApplyIsWaiting();
    }

    @Override
    public List<ApplyProfile> findAllByUser(User user) {
        return applyProfileRepository.findAllByUser(user);
    }

    @Override
    public ApplyProfile findApplyProfileByUserAndJob(User user, Job job) {
        return applyProfileRepository.findApplyProfileByUserAndJob(user, job);
    }

    @Override
    public List<ApplyProfile> searchApplyWothIdCompany(int id_company) {
        return applyProfileRepository.searchApplyWothIdCompany(id_company);
    }

}
