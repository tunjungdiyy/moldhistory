package com.anoman.machinehistory.repository.user;

import com.anoman.machinehistory.model.user.UserRead;
import com.anoman.machinehistory.model.user.UserRegister;
import com.anoman.machinehistory.model.user.UserUpdate;

import java.util.List;

public interface UserRepository {

    void createUser(UserRegister register);

    void updateUser(UserUpdate update);

    UserRead findByUsername(String username);

    UserRead findById(Integer id);

    List<UserRead> findAll();
}
