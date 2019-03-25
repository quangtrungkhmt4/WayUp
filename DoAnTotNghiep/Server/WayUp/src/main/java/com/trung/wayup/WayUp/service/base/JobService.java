package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface JobService extends Service<Job> {

    List<Job> findAllByCompany(Company company);

    List<Job> getAll(int limit, int page);

    List<BigInteger> getQuantumSkills();

    List<BigInteger> getQuantumLevel();

    List<BigInteger> getQuantumCity();

    Job searchJobWithId(int id);

    int countAll();

    List<Job> searchAddress(String address);

    List<Job> searchSkills(String skill);

    List<Job> searchTitle(String title);

    List<Job> searchJobWithSkillAndJoinDate(String skill, String joinDate);
}
