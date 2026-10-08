package ChatDiplom.Chat.repo;

import ChatDiplom.Chat.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d WHERE " +
            "(:lastName IS NULL OR d.lastname LIKE %:lastName%) AND " +
            "(:firstName IS NULL OR d.firstname LIKE %:firstName%) AND " +
            "(:middleName IS NULL OR d.patronymic LIKE %:middleName%) AND " +
            "(:speciality IS NULL OR d.speciality LIKE %:speciality%)")
    List<Doctor> searchDoctors(@Param("lastName") String lastName,
                               @Param("firstName") String firstName,
                               @Param("middleName") String middleName,
                               @Param("speciality") String speciality);

    Optional<Doctor> findByMail(String username);
}
