package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Company;

import java.util.List;

public interface CompanyService extends Service<Company> {

    Company searchCompanyWithId(int id);

    List<Company> searchCompanyWithAddress(String key, int page);

    List<Company> findAllByNameLike(String name);

    Company findCompanyByName(String name);

    List<Company> findAllByPage(int limit, int page);

    int countAll();
}
