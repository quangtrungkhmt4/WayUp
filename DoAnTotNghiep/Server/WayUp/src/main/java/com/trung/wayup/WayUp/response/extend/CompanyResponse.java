package com.trung.wayup.WayUp.response.extend;

import com.trung.wayup.WayUp.model.Company;
import com.trung.wayup.WayUp.response.base.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse extends AbstractResponse {

    private Company company;
}
