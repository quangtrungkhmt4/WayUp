package com.trung.wayup.WayUp.service.impl;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.CompanyComment;
import com.trung.wayup.WayUp.repository.CompanyCommentRepository;
import com.trung.wayup.WayUp.service.base.CompanyCommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyCommentServiceImpl implements CompanyCommentService {

    @Autowired
    private CompanyCommentRepository commentRepository;

    @Override
    public CompanyComment insert(CompanyComment companyComment) {
        return commentRepository.save(companyComment);
    }

    @Override
    public CompanyComment update(CompanyComment companyComment) {
        return commentRepository.save(companyComment);
    }

    @Override
    public void delete(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Collection<CompanyComment> gettAll() {
        return (Collection<CompanyComment>) commentRepository.findAll();
    }

    @Override
    public List<CompanyComment> findAllByCompany(Company company) {
        return commentRepository.findAllByCompany(company);
    }
}
