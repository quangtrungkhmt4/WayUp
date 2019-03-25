package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.model.CompanyComment;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.CompanyCommentsResponse;
import com.trung.wayup.WayUp.response.extend.JobResponse;
import com.trung.wayup.WayUp.response.extend.JobsResponse;
import com.trung.wayup.WayUp.service.base.CompanyCommentService;
import com.trung.wayup.WayUp.service.base.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class CompanyCommentController extends AbstractController {

    private CompanyCommentService companyCommentService;
    private CompanyService companyService;


    @RequestMapping(method = RequestMethod.GET, value = "/comments/{id_company}")
    public ResponseEntity<Response> findJobsByCompany(@PathVariable("id_company") int id_company){
        Company company = companyService.searchCompanyWithId(id_company);
        List<CompanyComment> comments = companyCommentService.findAllByCompany(company);
        return responseData(new CompanyCommentsResponse(comments));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/comments")
    public ResponseEntity<Response> insertComment(@RequestBody CompanyComment companyComment){
        CompanyComment objectResponse = companyCommentService.insert(companyComment);
        return responseData(objectResponse==null?new BooleanResponse(false):new BooleanResponse(true));
    }
}
