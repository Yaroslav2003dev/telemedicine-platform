package ChatDiplom.Chat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String direction;
    private String university;
    private String year;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDirection() {
        return direction;
    }

//    public void setDegree(String degree) {
//        this.degree = degree;
//    }

    public String getUniversity() {
        return university;
    }

//    public void setUniversity(String university) {
//        this.university = university;
//    }

    public String getYear() {
        return year;
    }

//    public void setYear(String year) {
//        this.year = year;
//    }



    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
}
