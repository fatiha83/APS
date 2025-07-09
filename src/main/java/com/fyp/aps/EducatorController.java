package com.fyp.aps;

import com.fyp.aps.model.User;
import com.fyp.aps.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/educators")
@CrossOrigin(origins = "*") // Allow frontend access
public class EducatorController {

    private final UserRepository userRepository;

    public EducatorController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getEducators() {
        return userRepository.findAll().stream()
                .filter(user -> "user".equalsIgnoreCase(user.getRole()))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void deleteEducator(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PostMapping
    public User addEducator(@RequestBody User newUser) {
        newUser.setRole("user");
        return userRepository.save(newUser);
    }


}
