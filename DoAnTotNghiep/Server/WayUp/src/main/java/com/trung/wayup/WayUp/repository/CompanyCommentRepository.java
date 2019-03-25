package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.CompanyComment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyCommentRepository extends CrudRepository<CompanyComment, Integer> {

    List<CompanyComment> findAllByCompany(Company company);
}
