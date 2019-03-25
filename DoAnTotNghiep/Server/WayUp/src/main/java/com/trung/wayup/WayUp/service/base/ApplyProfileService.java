package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.ApplyProfile;
import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.model.User;

import java.util.List;

public interface ApplyProfileService extends Service<ApplyProfile> {

    List<ApplyProfile> getApplyIsWaiting();

    List<ApplyProfile> findAllByUser(User user);

    ApplyProfile findApplyProfileByUserAndJob(User user, Job job);

    List<ApplyProfile> searchApplyWothIdCompany(int id_company);
}
