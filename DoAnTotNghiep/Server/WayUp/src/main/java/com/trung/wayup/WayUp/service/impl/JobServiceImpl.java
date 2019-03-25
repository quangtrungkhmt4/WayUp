package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.repository.JobRepository;
import com.trung.wayup.WayUp.service.base.JobService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<Job> findAllByCompany(Company company) {
        return jobRepository.findAllByCompany(company);
    }


    @Override
    public List<Job> getAll(int limit, int page) {
        int offset = limit*(page - 1);
        return jobRepository.getAll(limit, offset);
    }

    @Override
    public List<BigInteger> getQuantumSkills() {
        return jobRepository.getQuantumSkill();
    }

    @Override
    public List<BigInteger> getQuantumLevel() {
        return jobRepository.getQuantumLevel();
    }

    @Override
    public List<BigInteger> getQuantumCity() {
        return jobRepository.getQuantumCity();
    }

    @Override
    public Job searchJobWithId(int id) {
        return jobRepository.searchJobWithId(id);
    }

    @Override
    public int countAll() {
        return jobRepository.countAll();
    }

    @Override
    public List<Job> searchAddress(String address) {
        return jobRepository.searchAddress(address);
    }

    @Override
    public List<Job> searchSkills(String skill) {
        return jobRepository.searchSkills(skill);
    }

    @Override
    public List<Job> searchTitle(String title) {
        return jobRepository.searchTitle(title);
    }

    @Override
    public List<Job> searchJobWithSkillAndJoinDate(String skill, String joinDate) {
        return jobRepository.searchJobWithSkillAndJoinDate(skill, joinDate);
    }

    @Override
    public Job insert(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job update(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public void delete(int id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Collection<Job> gettAll() {
        return (Collection<Job>) jobRepository.findAll();
    }
}
