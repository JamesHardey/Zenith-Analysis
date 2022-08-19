package com.jcoding.zenithanalysis.services;

import com.jcoding.zenithanalysis.dto.user.CustomAppUser;
import com.jcoding.zenithanalysis.entity.AppUser;
import com.jcoding.zenithanalysis.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<AppUser> user = appUserRepo.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("No User found");
        }
        AppUser appUser = user.get();
        return new CustomAppUser(appUser);
    }
}
