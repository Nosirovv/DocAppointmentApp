package uz.devops.service.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import uz.devops.domain.Doctor;

public class WorkPlanDto {

    private DoctorDTO doctorDTO;
    private DayOfWeek weekDay;
    private LocalTime startTime;
    private LocalTime endTime;

    public DoctorDTO getDoctorDTO() {
        return doctorDTO;
    }

    public void setDoctorDTO(DoctorDTO doctorDTO) {
        this.doctorDTO = doctorDTO;
    }

    public DayOfWeek getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(DayOfWeek weekDay) {
        this.weekDay = weekDay;
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
}
