package uz.devops.service.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import uz.devops.domain.Doctor;

public class WorkPlanDto {

    private Doctor doctorId;
    private DayOfWeek weekDay;
    private LocalTime startTime;
    private LocalTime endTime;

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
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
