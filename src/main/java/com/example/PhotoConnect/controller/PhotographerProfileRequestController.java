package com.example.PhotoConnect.controller;

import com.example.PhotoConnect.model.PhotographerProfile;
import com.example.PhotoConnect.model.Role;
import com.example.PhotoConnect.model.User;
import com.example.PhotoConnect.repository.PhotographerProfileRepository;
import com.example.PhotoConnect.repository.RoleRepository;
import com.example.PhotoConnect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class PhotographerProfileRequestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PhotographerProfileRepository profileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/submit-photographer-profile")
    public String submitPhotographerProfile(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "dob", required = false) String dob,
            @RequestParam(value = "nic", required = false) String nic,
            @RequestParam(value = "rate", required = false) Double rate,
            @RequestParam(value = "locations", required = false) String locations,
            @RequestParam(value = "language[]", required = false) List<String> languages,
            @RequestParam(value = "profile_pic", required = false) MultipartFile profilePic,
            @RequestParam(value = "past_pics[]", required = false) List<MultipartFile> pastPics
    ) {
        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(name)) {
            return "redirect:/pages/signin.html?exists=true";
        }

        Role photographerRole = roleRepository.findByName("ROLE_PHOTOGRAPHER")
                .orElseThrow(() -> new RuntimeException("Photographer role not found"));

        User user = new User();
        user.setEmail(email);
        user.setUsername(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setRole(photographerRole);
        userRepository.save(user);

        PhotographerProfile profile = new PhotographerProfile();
        profile.setUserId(user.getId());
        profileRepository.save(profile);

        return "redirect:/pages/signin.html?registered=true";
    }
}
