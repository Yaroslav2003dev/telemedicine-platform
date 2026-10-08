package ChatDiplom.Chat.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Например, название или тип случая (консультация, протокол и т.д.)
    private String complaints, anamnesis_Disease, anamnesis_Life, conditionp, pulse, recommendations, conclusion;

    private LocalDateTime dateTime;

    public Event(Long id, String complaints, String anamnesis_Disease, String anamnesis_Life, String conditionp, String pulse, String recommendations, String conclusion, LocalDateTime dateTime, Patient patient) {
        this.id = id;
        this.complaints = complaints;
        this.anamnesis_Disease = anamnesis_Disease;
        this.anamnesis_Life = anamnesis_Life;
        this.conditionp = conditionp;
        this.pulse = pulse;
        this.recommendations = recommendations;
        this.conclusion = conclusion;
        this.dateTime = dateTime;
        this.patient = patient;
    }

    public Event() {

    }


    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getAnamnesis_Disease() {
        return anamnesis_Disease;
    }

    public void setAnamnesis_Disease(String anamnesis_Disease) {
        this.anamnesis_Disease = anamnesis_Disease;
    }

    public String getAnamnesis_Life() {
        return anamnesis_Life;
    }

    public void setAnamnesis_Life(String anamnesis_Life) {
        this.anamnesis_Life = anamnesis_Life;
    }

    public String getCondition() {
        return conditionp;
    }

    public void setCondition(String condition) {
        this.conditionp = condition;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "patient_id",nullable = false)
    private Patient patient;



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.dateTime = LocalDateTime.now();
    }

    public String getFormattedDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        String formatted = dateTime != null ? dateTime.format(formatter) : "Дата не установлена";
        System.out.println("Форматированная дата: " + formatted); // Отладка
        return formatted;
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }



}
