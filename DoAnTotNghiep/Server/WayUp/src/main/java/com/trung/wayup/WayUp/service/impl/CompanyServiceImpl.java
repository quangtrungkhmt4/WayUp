package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.constant.SqlKey;
import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.repository.CompanyRepository;
import com.trung.wayup.WayUp.service.base.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company insert(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Company update(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public void delete(int id) {
        companyRepository.deleteById(id);
    }

    @Override
    public Collection<Company> gettAll() {
        return (Collection<Company>) companyRepository.findAll();
    }

    @Override
    public Company searchCompanyWithId(int id) {
        return companyRepository.searchCompanyWithId(id);
    }

    @Override
    public List<Company> searchCompanyWithAddress(String key, int page) {
        int offset = SqlKey.LIMIT *(page - 1);
        return companyRepository.searchCompanyWithAddress(key, offset);
    }

    @Override
    public List<Company> findAllByNameLike(String name) {
        String param = "%" + name + "%";
        return companyRepository.findAllByNameLike(param);
    }

    @Override
    public Company findCompanyByName(String name) {
        return companyRepository.findCompanyByName(name);
    }

    @Override
    public List<Company> findAllByPage(int limit, int page) {
        int offset = limit*(page - 1);
        return companyRepository.findAllByPage(limit, offset);
    }

    @Override
    public int countAll() {
        return companyRepository.countAll();
    }

}
