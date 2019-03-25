package com.trung.wayup.WayUp.service.base;

import com.trung.wayup.WayUp.model.Hr;
import com.trung.wayup.WayUp.model.User;

public interface HrService extends Service<Hr> {

    Hr findHrByUser(User user);
}
