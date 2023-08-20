package com.email.appuser.service;

import com.email.appuser.AppUser;
import com.email.repo.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final static String USER_NOT_FOUND_MSG="user with email %s not found";
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    public String signUpUser(AppUser appUser){

       boolean userExist= appUserRepository
               .findByEmail(appUser.getEmail())
                .isPresent();
       if (userExist){
           throw new IllegalStateException("email already taken");
       }
     String encodedPassword= bCryptPasswordEncoder
             .encode(appUser.getPassword());

       appUser.setPassword(encodedPassword);
       appUserRepository.save(appUser);

       //TODO: send confirmation token
        return "it works";
    }
}
