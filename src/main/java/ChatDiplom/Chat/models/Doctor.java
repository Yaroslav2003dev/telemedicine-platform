package ChatDiplom.Chat.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
public class Doctor implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String role;

//    public String getRole() {
//        return role;
//    }

    public void setRole(String role) {
        this.role = role;
    }

    public Doctor(Long id, String role, String firstname, String lastname, String patronymic, String mail, String speciality, String experience, String password, String description, List<WorkExperience> workExperiences, List<Education> educations, List<Training> trainings) {
        this.id = id;
        this.role = role;
        this.firstname = firstname;
        this.lastname = lastname;
        this.patronymic = patronymic;
        this.mail = mail;
        this.speciality = speciality;
        this.experience = experience;
        this.password = password;
        this.description = description;
        this.workExperiences = workExperiences;
        this.educations = educations;
        this.trainings = trainings;
    }



    private String firstname, lastname, patronymic, mail, speciality, experience_text;
    private String experience;
    private String password;
    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WorkExperience> workExperiences;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Training> trainings;

    public Doctor() {

    }


    public List<WorkExperience> getWorkExperiences() {
        return workExperiences;
    }

//    public void setWorkExperiences(List<WorkExperience> workExperiences) {
//        this.workExperiences = workExperiences;
//    }

    public List<Education> getEducations() {
        return educations;
    }

//    public void setEducations(List<Education> educations) {
//        this.educations = educations;
//    }

    public List<Training> getTrainings() {
        return trainings;
    }

//    public void setTrainings(List<Training> trainings) {
//        this.trainings = trainings;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }






    public Doctor(String password) {

        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }


}
