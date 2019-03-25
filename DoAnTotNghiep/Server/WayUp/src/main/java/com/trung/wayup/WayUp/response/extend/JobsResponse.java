package com.trung.wayup.WayUp.response.extend;

import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.response.base.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobsResponse extends AbstractResponse {

    private List<Job> jobs;
}
