package ChatDiplom.Chat.MedController;

import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.DoctorRepository;
import ChatDiplom.Chat.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager; // Для выполнения аутентификации

    // Отображение страницы регистрации
    @GetMapping("/register")
    public String showRegisterPage() {
        return "reg-auf/reg"; // Шаблон страницы регистрации (Thymeleaf)
    }

    @GetMapping("/")
    public String showRegisterPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        return "redirect:/register";
    }

    // Отображение страницы авторизации
    @GetMapping("/login")
    public String showLoginPage() {
        return "reg-auf/login"; // Шаблон страницы логина
    }

    // Обработка формы регистрации
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam(required = false) String patronymic,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role, // Ожидается "patient" или "doctor"
            Model model
    ) {
        // Регистрируем пациента
        if ("patient".equalsIgnoreCase(role)) {
            if (patientRepository.findByMail(email).isPresent()) {
                model.addAttribute("error", "Пациент с таким email уже зарегистрирован!");
                return "reg-auf/reg";
            }

            Patient patient = new Patient();
            patient.setFirstname(firstname);
            patient.setLastname(lastname);
            patient.setPatronymic(patronymic);
            patient.setMail(email);
            // Шифруем пароль перед сохранением
            patient.setPassword(passwordEncoder.encode(password));
            patient.setRole("ROLE_PATIENT");
            patientRepository.saveAndFlush(patient);

            // Выполняем автоматический вход
//            authenticateUser(email, password);
            // Перенаправляем пациента в его личный кабинет
            return "redirect:/patient";
        }
        // Регистрируем врача
        else if ("doctor".equalsIgnoreCase(role)) {
            if (doctorRepository.findByMail(email).isPresent()) {
                model.addAttribute("error", "Врач с таким email уже зарегистрирован!");
                model.addAttribute("error", "Врач с таким email уже зарегистрирован!");
                return "reg-auf/reg";
            }

            Doctor doctor = new Doctor();
            doctor.setFirstname(firstname);
            doctor.setLastname(lastname);
            doctor.setPatronymic(patronymic);
            doctor.setMail(email);
            // Шифруем пароль перед сохранением
            doctor.setPassword(passwordEncoder.encode(password));
            doctor.setRole("ROLE_DOCTOR");
            doctorRepository.saveAndFlush(doctor);

            // Выполняем автоматический вход
//            authenticateUser(email, password);

//            SecurityContextHolder.getContext().getAuthentication();
            // Перенаправляем врача в его личный кабинет
            return "redirect:/doctor";
        }
        // Если передана неизвестная роль
        else {
            model.addAttribute("error", "Некорректный тип пользователя!");
            return "reg-auf/reg";
        }
    }

    // Метод для автоматической аутентификации пользователя после регистрации
    private void authenticateUser(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authToken);
            System.out.println("Authentication successful for " + email + ". Authorities: " + authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.err.println("Ошибка аутентификации для " + email + ": " + e.getMessage());
            throw e;
        }
    }


}
