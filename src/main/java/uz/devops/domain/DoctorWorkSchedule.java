package uz.devops.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class DoctorWorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer doctorID;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    @Override
    public String toString() {
        return (
            "DoctorWorkSchedule{" +
            "id=" +
            id +
            ", doctorID=" +
            doctorID +
            ", date=" +
            date +
            ", startTime=" +
            startTime +
            ", endTime=" +
            endTime +
            ", description='" +
            description +
            '\'' +
            '}'
        );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDoctor() {
        return doctorID;
    }

    public void setDoctor(Integer doctorID) {
        this.doctorID = doctorID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
