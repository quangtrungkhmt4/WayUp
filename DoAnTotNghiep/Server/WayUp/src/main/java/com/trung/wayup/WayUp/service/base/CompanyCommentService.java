package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.CompanyComment;

import java.util.List;

public interface CompanyCommentService extends Service<CompanyComment> {
    List<CompanyComment> findAllByCompany(Company company);
}
