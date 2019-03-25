package com.trung.wayup.WayUp.controller;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.response.base.Response;
import com.trung.wayup.WayUp.response.extend.BooleanResponse;
import com.trung.wayup.WayUp.response.extend.CompaniesResponse;
import com.trung.wayup.WayUp.response.extend.CompanyResponse;
import com.trung.wayup.WayUp.response.extend.CountCompanyResponse;
import com.trung.wayup.WayUp.service.base.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
@AllArgsConstructor
public class CompanyController extends AbstractController{

    private CompanyService companyService;

    @GetMapping
    @RequestMapping("/companies")
    public ResponseEntity<Response> getAll(){
        List<Company> companies = (List<Company>) companyService.gettAll();
        return responseData(new CompaniesResponse(companies));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/companies")
    public ResponseEntity<Response> insertCompany(@RequestBody Company company){
        CompanyResponse objectResponse = new CompanyResponse(companyService.insert(company));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/companies")
    public ResponseEntity<Response> updateCompany(@RequestBody Company company){
        CompanyResponse objectResponse = new CompanyResponse(companyService.update(company));
        return responseData(objectResponse);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/companies")
    public ResponseEntity<Response> deleteCompany(@RequestParam("id_company") int id){
        companyService.delete(id);
        return responseData(new BooleanResponse(true));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/companies/search")
    public ResponseEntity<Response> findCompaniesByAddressLike(@RequestParam("address") String address, @RequestParam("page") int page){
        List<Company> companies = companyService.searchCompanyWithAddress(address, page);
        return responseData(new CompaniesResponse(companies));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/companies/name")
    public ResponseEntity<Response> findCompaniesByNameLike(@RequestParam("name") String name){
        Company companies = companyService.findCompanyByName(name);
        return responseData(new CompanyResponse(companies));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/companies/all")
    public ResponseEntity<Response> findConpaniesByPage(@RequestParam("page") int page){
        List<Company> companies = companyService.findAllByPage(5, page);
        return responseData(new CompaniesResponse(companies));
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/findCompanyById")
//    public ResponseEntity<Response> findConpanyById(@RequestParam("id") int id){
//        Company company = companyService.findCompanyById(id);
//        return responseData(new CompanyResponse(company));
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/companies/size")
    public ResponseEntity<Response> countAllCompany(){
        int count =  companyService.countAll();
        return responseData(new CountCompanyResponse(count));
    }
}
