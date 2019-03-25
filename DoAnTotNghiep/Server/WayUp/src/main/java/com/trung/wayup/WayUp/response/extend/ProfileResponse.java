package com.trung.wayup.WayUp.response.extend;

import com.trung.wayup.WayUp.model.Profile;
import com.trung.wayup.WayUp.response.base.AbstractResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse extends AbstractResponse {

    private Profile profile;
}
