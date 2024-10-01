package com.boubyan.api.service;

import com.boubyan.api.dao.UserDao;
import com.boubyan.api.dto.UserDto;
import com.boubyan.api.model.User;
import com.boubyan.api.dto.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
        this.encoder = new BCryptPasswordEncoder(12); // Initialize encoder in constructor
    }

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User 404"));

        return new UserPrincipal(user);
    }

    public User saveUser(UserDto userDto) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        return userDao.save(userDto.getUser());
    }
}
