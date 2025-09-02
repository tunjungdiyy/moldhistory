package com.anoman.machinehistory.service.user;

import com.anoman.machinehistory.model.user.UserAuth;
import com.anoman.machinehistory.model.user.UserRead;
import com.anoman.machinehistory.repository.user.UserRepository;
import com.anoman.machinehistory.repository.user.UserRepositoryImpl;
import com.anoman.machinehistory.security.BCrypt;

public class UserServiceImpl implements UserService{

    UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public Boolean login(UserAuth userAuth) {

        UserRead userRead = userRepository.findByUsername(userAuth.getUsername());

        if (userRead.getId() == null) {
            return false;
        } else {
            if (BCrypt.checkpw(userAuth.getPassword(), userRead.getPassword())) {
                return true;
            } else {
                return false;
            }
        }

    }
}
