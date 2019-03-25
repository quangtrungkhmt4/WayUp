package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Integer> {

    @Query(value = "SELECT * FROM tbl_companies WHERE id_company = ?1", nativeQuery = true)
    Company searchCompanyWithId(int id);

    @Query(value="SELECT * FROM tbl_companies WHERE address LIKE %?1% LIMIT 5 OFFSET ?2", nativeQuery = true)
    List<Company> searchCompanyWithAddress(String key, int page);

    List<Company> findAllByNameLike(String name);

    Company findCompanyByName(String name);

    @Query(value="SELECT * FROM  tbl_companies LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Company> findAllByPage(int limit, int offset);

    @Query(value="SELECT count(*) as count FROM tbl_companies", nativeQuery = true)
    int countAll();


}
