package ChatDiplom.Chat.service;

import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class DoctorAuthService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Doctor registerDoctor(String email, String password, String firstname, String lastname, String patronymic, String speciality) {
        // Проверяем, существует ли уже такой email
        if (doctorRepository.findByMail(email).isPresent()) {
            throw new IllegalArgumentException("Врач с таким email уже зарегистрирован!");
        }

        Doctor doctor = new Doctor();
        doctor.setMail(email);
        doctor.setPassword(passwordEncoder.encode(password)); // Шифруем пароль
        doctor.setFirstname(firstname);
        doctor.setLastname(lastname);
        doctor.setPatronymic(patronymic);
        doctor.setSpeciality(speciality);

        return doctorRepository.save(doctor);
    }
}
