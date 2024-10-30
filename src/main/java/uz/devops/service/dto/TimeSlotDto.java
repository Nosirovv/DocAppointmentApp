package uz.devops.service.dto;

import java.time.Instant;
import java.time.ZonedDateTime;

public class TimeSlotDto {

    private Instant startTime;
    private Instant endTime;

    public TimeSlotDto(Instant startTime, Instant endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TimeSlot{" + "startTime=" + startTime + ", endTime=" + endTime + '}';
    }
}
