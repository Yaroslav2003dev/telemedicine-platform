package ChatDiplom.Chat.service;

import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.repo.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> findDoctors(String lastName, String firstName, String middleName, String speciality) {
        return doctorRepository.searchDoctors(lastName, firstName, middleName, speciality);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }

}
