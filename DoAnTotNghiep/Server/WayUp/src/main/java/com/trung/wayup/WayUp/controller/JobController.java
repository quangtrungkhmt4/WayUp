package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.*;
import com.trung.wayup.WayUp.service.base.CompanyService;
import com.trung.wayup.WayUp.service.base.JobService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class JobController extends AbstractController {

    private JobService jobService;
    private CompanyService companyService;

    @RequestMapping(method = RequestMethod.GET, value = "/jobs")
    public ResponseEntity<Response> findJobsByPage(@RequestParam("page") int page){
        List<Job> jobs = jobService.getAll(5, page);
        return responseData(new JobsResponse(jobs));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/{id_company}")
    public ResponseEntity<Response> findJobsByCompany(@PathVariable("id_company") int id_company){
        Company company = companyService.searchCompanyWithId(id_company);
        List<Job> jobs = jobService.findAllByCompany(company);
        return responseData(new JobsResponse(jobs));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/jobs")
    public ResponseEntity<Response> insertJob(@RequestBody Job job){
        JobResponse objectResponse = new JobResponse(jobService.insert(job));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/jobs")
    public ResponseEntity<Response> updateJob(@RequestBody Job job){
        JobResponse objectResponse = new JobResponse(jobService.update(job));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/skills")
    public ResponseEntity<Response> getQuantum(){
        List<BigInteger> quantum = jobService.getQuantumSkills();
        return responseData(new QuantumLanguageResponse(quantum));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/level")
    public ResponseEntity<Response> getLevelQuantum(){
        List<BigInteger> quantum = jobService.getQuantumLevel();
        return responseData(new QuantumLanguageResponse(quantum));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/city")
    public ResponseEntity<Response> getLevelCity(){
        List<BigInteger> quantum = jobService.getQuantumCity();
        return responseData(new QuantumLanguageResponse(quantum));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/getAll")
    public ResponseEntity<Response> get(){
        List<Job> quantum = (List<Job>) jobService.gettAll();
        return responseData(new JobsResponse(quantum));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/jobs")
    public ResponseEntity<Response> deleteJob(@RequestParam("id_job") int id_job){
        jobService.delete(id_job);
        BooleanResponse objectResponse = new BooleanResponse(true);
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/size")
    public ResponseEntity<Response> countAllJob(){
        int count =  jobService.countAll();
        return responseData(new CountJobResponse(count));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/address")
    public ResponseEntity<Response> findJobsByAddress(@RequestParam("address") String address){
        List<Job> jobs = jobService.searchAddress(address);
        return responseData(new JobsResponse(jobs));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/skill")
    public ResponseEntity<Response> findJobsBySkills(@RequestParam("skill") String skills){
        List<Job> jobs = jobService.searchSkills(skills);
        return responseData(new JobsResponse(jobs));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/jobs/title")
    public ResponseEntity<Response> findJobsByTitle(@RequestParam("title") String title){
        List<Job> jobs = jobService.searchTitle(title);
        return responseData(new JobsResponse(jobs));
    }
}
