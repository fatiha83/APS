package com.fyp.aps;

import com.fyp.aps.model.PasswordResetToken;
import com.fyp.aps.model.StudentPrediction;
import com.fyp.aps.model.User;
import com.fyp.aps.repository.PasswordResetTokenRepository;
import com.fyp.aps.repository.StudentPredictionRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fyp.aps.service.MailService;
import com.fyp.aps.service.PredictionService;
import com.fyp.aps.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class MainController {
    
    @GetMapping("/")
    public String home() {
        return "login"; // redirect to login page
    }

    // Serve the login page
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
        model.addAttribute("errorMessage", "Incorrect email or password");
    }
    return "login";
    }


    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userService.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Store the user ID in the session
            session.setAttribute("userId", user.getId());

            if ("admin".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admindashboard";
            } else {
                return "redirect:/index";
            }
        }

        return "redirect:/login?error=true";
    }

    @GetMapping("/admindashboard")
    public String adminDashboard() {
        return "admindashboard"; // Must match your admindashboard.html file in templates
    }

     @Autowired
    private StudentPredictionRepository predictionRepository;

    @GetMapping("/index")
    public String dashboard(HttpSession session, Model model) {
    Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            User user = userService.findById(userId); // implement this method in UserService
            model.addAttribute("userName", user.getName()); // assuming you have getName()

        } else {
            model.addAttribute("userName", "Guest");
        }
    
    // Add latest prediction data
    StudentPrediction latest = predictionRepository.findTopByEducatorIdOrderByIdDesc(userId);
    if (latest != null) {
        model.addAttribute("name", latest.getStuName());
        model.addAttribute("gender", latest.getGender());
        model.addAttribute("prediction", latest.getPrediction());
    }
        return "index"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }


    @GetMapping("/prediction")
    public String prediction() {
        return "prediction"; //serve the prediction page
    }


    @GetMapping("/predictionresult")
    public String resultPage() {
        return "predictionresult"; //serve the prediction result page
    }

    @GetMapping("/educatorprofile")
    public String profile() {
        return "educatorprofile"; //serve the prediction page
    }

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/predictionresult")
    public String getPrediction(@RequestParam String studentName,
                                @RequestParam int age,
                                @RequestParam int gender,
                                @RequestParam int studyHours,
                                @RequestParam int attendance,
                                @RequestParam double previousGrades,
                                HttpSession session,
                                Model model) {

        // Map numerical gender to string
        String genderStr = "";
        switch (gender) {
            case 0: genderStr = "Male"; break;
            case 1: genderStr = "Female"; break;
            default: genderStr = "Unknown"; break;
        }

        // Map studyHours numeric code to string
        String studyHoursStr = "";
        switch (studyHours) {
            case 1: studyHoursStr = "0-1 Hour"; break;
            case 2: studyHoursStr = "1-2 Hours"; break;
            case 3: studyHoursStr = "2-3 Hours"; break;
            case 4: studyHoursStr = "More than 3 Hours"; break;
            default: studyHoursStr = "Unknown"; break;
        }

        // Map attendance numeric code to string
        String attendanceStr = "";
        switch (attendance) {
            case 1: attendanceStr = "Below 40%"; break;
            case 2: attendanceStr = "40%-59%"; break;
            case 3: attendanceStr = "60%-79%"; break;
            case 4: attendanceStr = "80%-100%"; break;
            default: attendanceStr = "Unknown"; break;
        }

        String prediction = "0.0";

        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "python", "mlmodel/predict_runner_v2.py",
                    String.valueOf(previousGrades),
                    String.valueOf(attendance),
                    String.valueOf(studyHours)
            );

            pb.redirectErrorStream(false);
            Process process = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder fullOutput = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("PYTHON OUTPUT: " + line);
                fullOutput.append(line);
            }
            prediction = (fullOutput.length() > 0) ? fullOutput.toString() : "Unavailable";

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            errorReader.lines().forEach(err -> System.err.println("PYTHON ERROR: " + err));

            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
            prediction = "Error";
        }

        // Save into MySQL
        StudentPrediction result = new StudentPrediction();
        result.setStuName(studentName);
        result.setAge(age);
        result.setGender(genderStr);
        result.setStuHours(studyHoursStr);
        result.setAttendance(attendanceStr);
        result.setPrevGrades(previousGrades);
        result.setPrediction(prediction);
        // predictionRepository.save(result);

        // ✅ Retrieve educator ID from session
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            predictionService.savePrediction(result, userId);
        } else {
            // Fallback: log, throw error, or redirect
            System.err.println("Educator ID not found in session!");
        }

        boolean showTutoringNote = false;
        try {
            double predictedCGPA = Double.parseDouble(prediction);
            if (predictedCGPA < 2.0) {
                showTutoringNote = true;
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid predicted CGPA format: " + prediction);
        }
        model.addAttribute("showTutoringNote", showTutoringNote);



        // Pass both numerical and string values as needed
        model.addAttribute("name", studentName);
        model.addAttribute("age", age);
        model.addAttribute("gender", genderStr);         // Pass string here
        model.addAttribute("hours", studyHoursStr);      // Pass string here
        model.addAttribute("attendance", attendanceStr); // Pass string here
        model.addAttribute("gpa", previousGrades);
        model.addAttribute("prediction", prediction);

        return "predictionresult";
    }


    @Autowired
    private MailService emailService;

    @Autowired
    private PasswordResetTokenRepository tokenService;

    @GetMapping("/forgotpassword")
    public String showForgotPasswordForm() {
        return "forgotpassword";
    }


    // @PostMapping("/forgotpassword")
    // public String processForgotPassword(@RequestParam("email") String email, Model model) {
    //     User user = userService.findByEmail(email);

    //     if (user == null) {
    //         System.out.println("No user found with email: " + email);
    //         model.addAttribute("errorMessage", "No account found with that email.");
    //         return "forgotpassword";
    //     }

    //     String token = UUID.randomUUID().toString();
    //     userService.createPasswordResetToken(user, token);

    //     String resetLink = "http://localhost:8080/resetpassword?token=" + token;
    //     emailService.sendResetEmail(email, resetLink);

    //     model.addAttribute("successMessage", "Password reset link sent to your email.");
    //     return "forgotpassword";
    // }

    // @PostMapping("/forgotpassword")
    // public String processForgotPassword(@RequestParam("email") String email, Model model) {
    //     User user = userService.findByEmail(email);

    //     if (user == null) {
    //         System.out.println("No user found with email: " + email);
    //         model.addAttribute("errorMessage", "No account found with that email.");
    //         return "forgotpassword";
    //     }

    //     String token = UUID.randomUUID().toString();
    //     userService.createPasswordResetToken(user, token);

    //     String resetLink = "http://localhost:8080/resetpassword?token=" + token;
    //     emailService.sendResetEmail(email, resetLink);

    //     System.out.println("Reset link sent: " + resetLink);
    //     model.addAttribute("successMessage", "Password reset link sent to your email.");
    //     System.out.println("Returning forgotpassword view with success message");
    //     return "forgotpassword";
    // }

    @PostMapping("/forgotpassword")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        User user = userService.findByEmail(email);

        if (user == null) {
            System.out.println("No user found with email: " + email);
            model.addAttribute("errorMessage", "No account found with that email.");
            return "forgotpassword";
        }

        String token = UUID.randomUUID().toString();
        userService.createPasswordResetToken(user, token);

        String resetLink = "http://localhost:8080/resetpassword?token=" + token;

        try {
            emailService.sendResetEmail(email, resetLink);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to send reset email. Please try again later.");
            return "forgotpassword";
        }

        model.addAttribute("successMessage", "Password reset link sent to your email.");
        return "forgotpassword";
    }



    @GetMapping("/resetpassword")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenService.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            model.addAttribute("errorMessage", "Invalid or expired token.");
            return "resetpassword";
        }

        model.addAttribute("token", token);
        return "resetpassword"; // assumes resetpassword.html has a form that includes the token
    }

    // @PostMapping("/resetpassword")
    // public String handleResetPassword(@RequestParam("token") String token,
    //                                 @RequestParam("newPassword") String newPassword,
    //                                 Model model) {
    //     PasswordResetToken resetToken = tokenService.findByToken(token);

    //     if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
    //         model.addAttribute("errorMessage", "Invalid or expired token.");
    //         return "resetpassword";
    //     }

    //     User user = resetToken.getUser(); // Assuming your PasswordResetToken entity has getUser()
    //     user.setPassword(newPassword);  // 

    //     // Delete or invalidate the token
    //     tokenService.delete(resetToken);

    //     model.addAttribute("successMessage", "Password successfully reset. You can now log in.");
    //     return "login"; // or redirect to login page
    // }


    @PostMapping("/resetpassword")
    public String handleResetPassword(@RequestParam("token") String token,
                                    @RequestParam("newPassword") String newPassword,
                                    RedirectAttributes redirectAttributes) {
        PasswordResetToken resetToken = tokenService.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid or expired token.");
            return "redirect:/resetpassword?token=" + token;
        }

        User user = resetToken.getUser();
        user.setPassword(newPassword); // hash the password here if needed
        userService.saveUser(user);    // save updated user
        tokenService.delete(resetToken); // delete token after use

        redirectAttributes.addFlashAttribute("successMessage", "Password successfully reset. You can now log in.");
        return "redirect:/login";  // Redirect to login page
    }

    @GetMapping("/systemmonitor")
    public String systemMonitorPage() {
        return "systemmonitor"; // This looks for systemmonitor.html in templates/
    }


    @GetMapping("/testmail")
    @ResponseBody
    public String testMail() {
        try {
            emailService.sendResetEmail("fatihaatiera83@gmail.com", "http://localhost:8080/resetpassword?token=test123");
            return "Email sent!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }






}

