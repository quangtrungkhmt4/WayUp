package com.trung.wayup.WayUp.repository;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface JobRepository extends CrudRepository<Job, Integer> {

    List<Job> findAllByCompany(Company company);

    @Query(value="SELECT * FROM (SELECT * FROM tbl_jobs ORDER BY id_job DESC) AS \"item\" WHERE lock = 0 LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Job> getAll(int limit, int offset);

    @Query(value = "SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%Java%' UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%C#%'UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%PHP%'UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%Python%'UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%Swift%'UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%C++%'UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE '%JavaScript%'", nativeQuery = true)
    List<BigInteger> getQuantumSkill();

    @Query(value = "SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND title LIKE '%Senior%' UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND title LIKE '%Junior%' UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND title LIKE '%Fresher%' UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND title LIKE '%Intent%'", nativeQuery = true)
    List<BigInteger> getQuantumLevel();

    @Query(value = "SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND address LIKE '%Ha Noi%' UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND address LIKE '%Ho Chi Minh%' UNION ALL SELECT COUNT(*) FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND address LIKE '%Da Nang%'", nativeQuery = true)
    List<BigInteger> getQuantumCity();

    @Query(value = "SELECT * FROM tbl_jobs WHERE id_job = ?1", nativeQuery = true)
    Job searchJobWithId(int id);

    @Query(value="SELECT count(*) as count FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0", nativeQuery = true)
    int countAll();

    @Query(value="SELECT * FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND address LIKE %?1%", nativeQuery = true)
    List<Job> searchAddress(String address);

    @Query(value="SELECT * FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND skills LIKE %?1%", nativeQuery = true)
    List<Job> searchSkills(String skill);

    @Query(value="SELECT * FROM tbl_jobs WHERE tbl_jobs.\"lock\" = 0 AND title LIKE %?1%", nativeQuery = true)
    List<Job> searchTitle(String title);

    @Query(value = "SELECT * FROM \"tbl_jobs\" WHERE \"lock\" = 0 AND join_date >= ?2 AND skills LIKE %?1%", nativeQuery = true)
    List<Job> searchJobWithSkillAndJoinDate(String skill, String joinDate);
}
