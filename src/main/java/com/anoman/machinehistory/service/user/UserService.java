package com.anoman.machinehistory.service.user;

import com.anoman.machinehistory.model.user.UserAuth;

public interface UserService {

    Boolean login(UserAuth userAuth);

}
