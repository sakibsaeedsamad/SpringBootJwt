package com.scube.JWT_demo.controller;

import com.scube.JWT_demo.model.JwtRequest;
import com.scube.JWT_demo.model.JwtResponse;
import com.scube.JWT_demo.service.UserService;
import com.scube.JWT_demo.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public  String home(){
        return "Welcome to my home";
    }
@PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody  JwtRequest jwtRequest){

       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           jwtRequest.getUsername(),
                           jwtRequest.getPassword()
                   )
           );
       }
       catch (BadCredentialsException e){
           try {
               throw new Exception("INVALID_CREDENTIALS",e);
           } catch (Exception ex) {
               ex.printStackTrace();
           }
       }
    final UserDetails userDetails
            = userService.loadUserByUsername(jwtRequest.getUsername());

    final String token =
            jwtUtility.generateToken(userDetails);

    return  new JwtResponse(token);
   }

}
