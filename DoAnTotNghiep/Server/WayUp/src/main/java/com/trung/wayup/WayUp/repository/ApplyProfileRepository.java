package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.ApplyProfile;
import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyProfileRepository extends CrudRepository<ApplyProfile, Integer> {

    @Query(value = "SELECT * FROM tbl_apply WHERE status = 0", nativeQuery = true)
    List<ApplyProfile> getApplyIsWaiting();

    List<ApplyProfile> findAllByUser(User user);

    ApplyProfile findApplyProfileByUserAndJob(User user, Job job);

    @Query(value = "SELECT tbl_apply.id_apply, tbl_apply.email, tbl_apply.\"name\", tbl_apply.created_at, tbl_apply.status, tbl_apply.sent_mail, tbl_apply.id_user, tbl_apply.id_job, tbl_apply.profile FROM tbl_jobs INNER JOIN tbl_apply ON tbl_jobs.id_job = tbl_apply.id_job INNER JOIN tbl_companies ON tbl_jobs.id_company = tbl_companies.id_company WHERE tbl_companies.id_company = ?1", nativeQuery = true)
    List<ApplyProfile> searchApplyWothIdCompany(int id_company);
}
