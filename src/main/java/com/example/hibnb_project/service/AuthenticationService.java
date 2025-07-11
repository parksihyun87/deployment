package com.example.hibnb_project.service;

import com.example.hibnb_project.data.dao.UserDAO;
import com.example.hibnb_project.data.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        String loginRole = "";
        String username2 = "";
        String[] split = username.split("@");
        if(split.length==2){
            loginRole = "ROLE_"+split[0];
            username2 = split[1];
        }

        UserEntity userEntity = this.userDAO.findById(username2);
        if(userEntity==null){
            throw new UsernameNotFoundException("not found");
        }

        if(loginRole.equals("ROLE_ADMIN") && !userEntity.getRole().equals(loginRole)){
            throw new AuthorizationDeniedException("not admin \nusername: " + username2 + " / role : "+userEntity.getRole().substring(5));
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(); // GrantedAutority 롤 정보를 담는 인터페이스

        if(loginRole.equals("ROLE_USER")){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if(loginRole.equals("ROLE_ADMIN")){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(userEntity.getUsername(), userEntity.getPassword(), grantedAuthorities);
    }



}
