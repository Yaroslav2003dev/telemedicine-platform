package ChatDiplom.Chat.MedController;


import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.PatientRepository;
import ChatDiplom.Chat.service.PatientService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/patient")
public class RestPatientController {

    @Autowired
    private final PatientService patientService;

    public RestPatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Получить информацию о пациенте по id
    @GetMapping("/{id}")
    public  ResponseEntity<Map<String, String>> getPatientById(@PathVariable Long id) {
        Optional<Patient> patientOptional = patientService.getPatientById(id);
//        return patientOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//        private String firstname, lastname, patronymic, mail, phone, blood_type, allergy, operations, chronic_diseases,vaccinations, weight, height;
//        private String password;
//
//        private LocalDate birthDate;

        if (patientOptional.isPresent()) {
                Patient patient = patientOptional.get();

            // Создаем Map с нужными данными
            Map<String, String> response = new HashMap<>();
            response.put("lastname", patient.getLastname());
            response.put("firstname", patient.getFirstname());
            response.put("patronymic", patient.getPatronymic());
            response.put("mail", patient.getMail());
            response.put("phone", patient.getPhone());
//            response.put("blood_type", patient.getBlood_type());
            response.put("allergy", patient.getAllergy());
            response.put("operations", patient.getOperations());
            response.put("chronic_diseases", patient.getChronic_diseases());
            response.put("vaccinations", patient.getVaccinations());
            response.put("weight", patient.getWeight());
            response.put("height", patient.getHeight());
            response.put("birthDate", patient.getFormattedDate());

            // Возвращаем Map в формате JSON с кодом 200 OK
            return ResponseEntity.ok(response);
        } else {
            // Если пациента нет, возвращаем 404 Not Found
            return ResponseEntity.notFound().build();
        }

    }



    // Редактировать (обновить) информацию о пациенте
    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient updatedPatient) {
        Optional<Patient> optionalPatient = patientService.getPatientById(id);
        if (!optionalPatient.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("Получили обновлённого пациента: " + updatedPatient);
        Patient patient = optionalPatient.get();

        // Проверяем, что обновляемое значение не null и не пустая строка перед обновлением
        if (updatedPatient.getFirstname() != null && !updatedPatient.getFirstname().trim().isEmpty()) {
            patient.setFirstname(updatedPatient.getFirstname());
        }
        if (updatedPatient.getLastname() != null && !updatedPatient.getLastname().trim().isEmpty()) {
            patient.setLastname(updatedPatient.getLastname());
        }
        if (updatedPatient.getPatronymic() != null && !updatedPatient.getPatronymic().trim().isEmpty()) {
            patient.setPatronymic(updatedPatient.getPatronymic());
        }
        if (updatedPatient.getPhone() != null && !updatedPatient.getPhone().trim().isEmpty()) {
            patient.setPhone(updatedPatient.getPhone());
        }
        if (updatedPatient.getMail() != null && !updatedPatient.getMail().trim().isEmpty()) {
            patient.setMail(updatedPatient.getMail());
        }
        if (updatedPatient.getWeight() != null && !updatedPatient.getWeight().trim().isEmpty()) {
            patient.setWeight(updatedPatient.getWeight());
        }
        if (updatedPatient.getHeight() != null && !updatedPatient.getHeight().trim().isEmpty()) {
            patient.setHeight(updatedPatient.getHeight());
        }
//        if (updatedPatient.getBlood_type() != null && !updatedPatient.getBlood_type().trim().isEmpty()) {
//            patient.setBlood_type(updatedPatient.getBlood_type());
//        }
        if (updatedPatient.getAllergy() != null && !updatedPatient.getAllergy().trim().isEmpty()) {
            patient.setAllergy(updatedPatient.getAllergy());
        }
        if (updatedPatient.getOperations() != null && !updatedPatient.getOperations().trim().isEmpty()) {
            patient.setOperations(updatedPatient.getOperations());
        }
        if (updatedPatient.getChronic_diseases() != null && !updatedPatient.getChronic_diseases().trim().isEmpty()) {
            patient.setChronic_diseases(updatedPatient.getChronic_diseases());
        }
        if (updatedPatient.getVaccinations() != null && !updatedPatient.getVaccinations().trim().isEmpty()) {
            patient.setVaccinations(updatedPatient.getVaccinations());
        }
        if (updatedPatient.getBirthDate() !=null) {
            patient.setBirthDate(updatedPatient.getBirthDate());
        }
        Patient savedPatient = patientService.updatePatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

//    @GetMapping("/patient/{id}")
//    public ResponseEntity<Patient> getPatientById(@PathVariable("id") Long id) {
//        Optional<Patient> patient = patientRepository.findById(id);
//        return patient.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

}
