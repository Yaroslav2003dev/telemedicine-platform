package ChatDiplom.Chat.repo;

import ChatDiplom.Chat.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE " +
            "(:lastName IS NULL OR p.lastname LIKE %:lastName%) AND " +
            "(:firstName IS NULL OR p.firstname LIKE %:firstName%) AND " +
            "(:middleName IS NULL OR p.patronymic LIKE %:middleName%) AND " +
            "(:birthDate IS NULL OR p.birthDate = :birthDate)")
    List<Patient> searchPatients(@Param("lastName") String lastName,
                               @Param("firstName") String firstName,
                               @Param("middleName") String middleName,
                               @Param("birthDate") LocalDate birthDate);


    Optional<Patient> findByMail(String username);
}
