package ChatDiplom.Chat.service;

import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }


    // Метод для поиска людей с учетом опциональных параметров
    public List<Patient> findPatients(String lastName, String firstName, String middleName, LocalDate birthDate) {
        return patientRepository.searchPatients(lastName, firstName, middleName, birthDate);
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient updatePatient(Patient patient) {
        return patientRepository.save(patient);
    }

}
