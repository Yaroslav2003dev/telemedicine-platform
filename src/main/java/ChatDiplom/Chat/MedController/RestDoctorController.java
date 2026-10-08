package ChatDiplom.Chat.MedController;

import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.models.Education;
import ChatDiplom.Chat.models.Training;
import ChatDiplom.Chat.models.WorkExperience;
import ChatDiplom.Chat.repo.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class RestDoctorController {
    @Autowired
    private DoctorRepository doctorRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        return doctorOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor updatedDoctor) {

        System.out.println("Полученные данные: " + updatedDoctor);
        System.out.println("ID----------- "+ id);
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);

        if (!doctorOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Doctor doctor = doctorOptional.get();
//
//        Doctor doctor = doctorOptional.get();
//        doctor.setFirstname(updatedDoctor.getFirstname());
//        doctor.setLastname(updatedDoctor.getLastname());
//        doctor.setPatronymic(updatedDoctor.getPatronymic());
//        doctor.setSpeciality(updatedDoctor.getSpeciality());
//        doctor.setExperience(updatedDoctor.getExperience());
//        doctor.setExperience_text(updatedDoctor.getExperience_text());
//        doctor.setDescription(updatedDoctor.getDescription());
//        doctor.setWorkExperiences(updatedDoctor.getWorkExperiences());
//        doctor.setEducations(updatedDoctor.getEducations());
//        doctor.setTrainings(updatedDoctor.getTrainings());

        // Обновляем только измененные поля



        if (updatedDoctor.getFirstname() != null && !updatedDoctor.getFirstname().trim().isEmpty()) {
            doctor.setFirstname(updatedDoctor.getFirstname());
        }
        if (updatedDoctor.getLastname() != null && !updatedDoctor.getLastname().trim().isEmpty()) {
            doctor.setLastname(updatedDoctor.getLastname());
        }
        if (updatedDoctor.getSpeciality() != null && !updatedDoctor.getSpeciality().trim().isEmpty()) {
            doctor.setSpeciality(updatedDoctor.getSpeciality());
        }
        if (updatedDoctor.getPatronymic() != null && !updatedDoctor.getPatronymic().trim().isEmpty()) {
            doctor.setPatronymic(updatedDoctor.getPatronymic());
        }

//        if (updatedDoctor.getExperience_text() != null && !updatedDoctor.getExperience_text().isEmpty()) {
//            doctor.setExperience_text(updatedDoctor.getExperience_text());
//        }

        if (updatedDoctor.getExperience() != null &&!updatedDoctor.getExperience().isEmpty()) {
            doctor.setExperience(updatedDoctor.getExperience());
        }

        if (updatedDoctor.getMail() != null && !updatedDoctor.getMail().isEmpty()) {
            doctor.setMail(updatedDoctor.getMail());
        }
        if (updatedDoctor.getDescription() != null && !updatedDoctor.getDescription().isEmpty()) {
            doctor.setDescription(updatedDoctor.getDescription());
        }
        // Аналогично для других полей

        // Обновляем связанные сущности
        if (updatedDoctor.getWorkExperiences() != null && !updatedDoctor.getWorkExperiences().isEmpty()) {
            // Если хотите удалить старые записи:
            doctor.getWorkExperiences().clear();
            for (WorkExperience workExperience : updatedDoctor.getWorkExperiences()) {
                workExperience.setDoctor(doctor);
                doctor.getWorkExperiences().add(workExperience);
            }
        }

        if (updatedDoctor.getEducations() != null && !updatedDoctor.getEducations().isEmpty()) {
            doctor.getEducations().clear();
            for (Education education : updatedDoctor.getEducations()) {
                education.setDoctor(doctor);
                doctor.getEducations().add(education);
            }
        }

        if (updatedDoctor.getTrainings() != null && !updatedDoctor.getTrainings().isEmpty()) {
            doctor.getTrainings().clear();
            for (Training training : updatedDoctor.getTrainings()) {
                training.setDoctor(doctor);
                doctor.getTrainings().add(training);
            }
        }


        doctorRepository.save(doctor);  // Сохраняем обновления

        return ResponseEntity.ok(doctor);
    }
}
