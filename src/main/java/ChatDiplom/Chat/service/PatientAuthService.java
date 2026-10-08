package ChatDiplom.Chat.service;

import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PatientAuthService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Patient registerPatient(String email, String password, String firstname, String lastname, String patronymic) {
        // Проверяем, существует ли уже такой email
        if (patientRepository.findByMail(email).isPresent()) {
            throw new IllegalArgumentException("Пациент с таким email уже зарегистрирован!");
        }

        Patient patient = new Patient();
        patient.setMail(email);
        patient.setPassword(passwordEncoder.encode(password)); // Шифруем пароль
        patient.setFirstname(firstname);
        patient.setLastname(lastname);
        patient.setPatronymic(patronymic);

        return patientRepository.save(patient);
    }
}
