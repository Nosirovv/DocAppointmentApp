package uz.devops.service.impl;

import java.time.Instant;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.Appointment;
import uz.devops.domain.Doctor;
import uz.devops.repository.AppointmentRepository;
import uz.devops.repository.DoctorRepository;
import uz.devops.service.DoctorService;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.TimeSlotDto;
import uz.devops.service.mapper.DoctorMapper;

/**
 * Service Implementation for managing {@link uz.devops.domain.Doctor}.
 */
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private static final Logger LOG = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    private final AppointmentRepository appointmentRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorMapper doctorMapper, AppointmentRepository appointmentRepository) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public DoctorDTO save(DoctorDTO doctorDTO) {
        LOG.debug("Request to save Doctor : {}", doctorDTO);
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    @Override
    public DoctorDTO update(DoctorDTO doctorDTO) {
        LOG.debug("Request to update Doctor : {}", doctorDTO);
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(doctor);
    }

    @Override
    public Optional<DoctorDTO> partialUpdate(DoctorDTO doctorDTO) {
        LOG.debug("Request to partially update Doctor : {}", doctorDTO);

        return doctorRepository
            .findById(doctorDTO.getId())
            .map(existingDoctor -> {
                doctorMapper.partialUpdate(existingDoctor, doctorDTO);

                return existingDoctor;
            })
            .map(doctorRepository::save)
            .map(doctorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DoctorDTO> findOne(Long id) {
        LOG.debug("Request to get Doctor : {}", id);
        return doctorRepository.findById(id).map(doctorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Doctor : {}", id);
        doctorRepository.deleteById(id);
    }

    public Set<TimeSlotDto> freeTime(Instant scheduleStart, Instant scheduleEnd, Doctor doctor) {
        Set<TimeSlotDto> availableSlots = new LinkedHashSet<>();

        List<Appointment> bookedAppointment = appointmentRepository.findByDoctor(doctor);

        Instant currentStart = scheduleStart;
        for (Appointment booked : bookedAppointment) {
            Instant bookedStart = booked.getAppointmentStartTime();
            Instant bookedEnd = booked.getAppointmentEndTime();

            if (currentStart.isBefore(bookedStart)) {
                availableSlots.add(new TimeSlotDto(currentStart, bookedStart));
            }

            currentStart = bookedEnd;
        }

        if (currentStart.isBefore(scheduleEnd)) {
            availableSlots.add(new TimeSlotDto(currentStart, scheduleEnd));
        }
        return availableSlots;
    }
}
