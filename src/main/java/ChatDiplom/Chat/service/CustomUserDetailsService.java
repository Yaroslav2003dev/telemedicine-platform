package ChatDiplom.Chat.service;


import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.DoctorRepository;
import ChatDiplom.Chat.repo.PatientRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public CustomUserDetailsService(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Patient> patient = patientRepository.findByMail(username);
//        if (patient.isPresent()) {
//            return patient.get();
//        }
//
//        Optional<Doctor> doctor = doctorRepository.findByMail(username);
//        if (doctor.isPresent()) {
//            return doctor.get();
//        }
//
//        throw new UsernameNotFoundException("Пользователь не найден");
//    }
//@Override
//public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    Patient patient = patientRepository.findByMail(username)
//            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//    return new org.springframework.security.core.userdetails.User(
//            patient.getMail(),
//            patient.getPassword(),
//            List.of(new SimpleGrantedAuthority(patient.getRole())) // Передаем роль пациента
//    );
//}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Пытаемся найти пациента
        Optional<Patient> patientOpt = patientRepository.findByMail(username);
        if (patientOpt.isPresent()) {
            return patientOpt.get();
//            return new org.springframework.security.core.userdetails.User(
//                    patient.getMail(),
//                    patient.getPassword(),
//                    List.of(new SimpleGrantedAuthority(patient.getRole()))
//            );
        }

        // Если пациент не найден, пытаемся найти врача
        Optional<Doctor> doctorOpt = doctorRepository.findByMail(username);
        if (doctorOpt.isPresent()) {
            return doctorOpt.get();
        }
//        if (doctorOpt.isPresent()) {
//            Doctor doctor = doctorOpt.get();
//            return new org.springframework.security.core.userdetails.User(
//                    doctor.getMail(),
//                    doctor.getPassword(),
//                    List.of(new SimpleGrantedAuthority(doctor.getRole()))
//            );
//        }

        // Если пользователь не найден ни в одном из репозиториев, выбрасываем исключение
        throw new UsernameNotFoundException("Пользователь не найден");
    }
    }


