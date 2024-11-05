package uz.devops.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DoctorWorkScheduleDto {

    private Integer doctorIdDto;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;

    public Integer getDoctorIdDto() {
        return doctorIdDto;
    }

    public void setDoctorIdDto(Integer doctorIdDto) {
        this.doctorIdDto = doctorIdDto;
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
