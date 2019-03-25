package com.trung.wayup.WayUp.response.extend;

import com.trung.wayup.WayUp.model.Job;
import com.trung.wayup.WayUp.response.base.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountJobResponse extends AbstractResponse {

//    private Job job;

    private int countJob;

//    public CountJobResponse(int countJob){
//        this.countJob = countJob;
//    }
//
//    public CountJobResponse(Job job){
//        this.job = job;
//    }
}
