package uz.devops.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.devops.domain.*;
import uz.devops.domain.enumeration.AppointmentStatus;
import uz.devops.repository.*;
import uz.devops.security.AuthoritiesConstants;
import uz.devops.service.DoctorService;
import uz.devops.service.dto.DoctorDTO;
import uz.devops.service.dto.TimeSlotDto;
import uz.devops.service.dto.WorkPlanDto;
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

    private final WorkPlanRepository workPlanRepository;

    private final DoctorWorkScheduleRepository doctorWorkScheduleRepository;

    private final UserRepository userRepository;

    public DoctorServiceImpl(
        DoctorRepository doctorRepository,
        DoctorMapper doctorMapper,
        AppointmentRepository appointmentRepository,
        WorkPlanRepository workPlanRepository,
        DoctorWorkScheduleRepository doctorWorkScheduleRepository,
        UserRepository userRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.appointmentRepository = appointmentRepository;
        this.workPlanRepository = workPlanRepository;
        this.doctorWorkScheduleRepository = doctorWorkScheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DoctorDTO save(DoctorDTO doctorDTO) {
        LOG.debug("Request to save Doctor : {}", doctorDTO);
        Doctor doctor = doctorMapper.toEntity(doctorDTO);

        User user = userRepository.findById(doctor.getUser().getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Authority doctorAuthority = new Authority();
        doctorAuthority.setName(AuthoritiesConstants.DOCTOR);

        user.getAuthorities().add(doctorAuthority);

        doctor = doctorRepository.save(doctor);

        return doctorMapper.toDto(doctor);
    }

    //    User user = userRepository.findById(doctor.getUser().getId())
    //        .orElseThrow(() -> new EntityNotFoundException("User not found"));
    //
    //    // User rolini o'zgartirish
    //        user.setRole(Role.DOCTOR); // DOCTOR rolini berish
    //        userRepository.save(user); // Yangilangan User ni saqlash
    //
    //    // Doctor ni saqlash
    //    doctor = doctorRepository.save(doctor);

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

    @Override
    public Set<TimeSlotDto> freeTime(LocalTime scheduleStart, LocalTime scheduleEnd, Integer doctorId, LocalDate date) {
        Set<TimeSlotDto> availableSlots = new LinkedHashSet<>();

        //        List<Appointment> bookedAppointment = appointmentRepository.findByDoctorId(doctorId);
        List<Appointment> bookedAppointment = appointmentRepository.findAppointmentByDoctorIdAndAndDate(doctorId, date);

        LocalTime currentStart = scheduleStart;
        for (Appointment booked : bookedAppointment) {
            LocalTime bookedStart = booked.getAppointmentStartTime();
            LocalTime bookedEnd = booked.getAppointmentEndTime();

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

    @Override
    public WorkPlan createWorkPlan(WorkPlanDto workPlanDto) {
        WorkPlan workPlan = new WorkPlan();
        workPlan.setDoctor(workPlanDto.getDoctorId());
        workPlan.setWeekDay(workPlanDto.getWeekDay());
        workPlan.setStartTime(workPlanDto.getStartTime());
        workPlan.setEndTime(workPlanDto.getEndTime());
        workPlanRepository.save(workPlan);
        return workPlan;
    }

    @Override
    public List<DoctorWorkSchedule> generateWeeklyScheduleForDoctor(Integer doctorId, LocalDate startDate, LocalDate endDate) {
        List<WorkPlan> workPlans = workPlanRepository.findByDoctorId(doctorId);
        List<DoctorWorkSchedule> doctorWorkSchedules = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            LocalDate finalDate = date;
            workPlans
                .stream()
                .filter(wp -> wp.getWeekDay().equals(dayOfWeek))
                .forEach(wp -> {
                    DoctorWorkSchedule schedule = new DoctorWorkSchedule();
                    schedule.setDoctor(doctorId);
                    schedule.setDate(finalDate);
                    schedule.setStartTime(wp.getStartTime());
                    schedule.setEndTime(wp.getEndTime());
                    schedule.setDescription("Generated from work plan");

                    doctorWorkScheduleRepository.save(schedule);
                    doctorWorkSchedules.add(schedule);
                });
        }
        return doctorWorkSchedules;
    }

    @Override
    public WorkPlan updateWorkPlanTimes(Integer id, LocalTime startTime, LocalTime endTime) {
        WorkPlan workPlan = workPlanRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("WorkPlan not found with id: " + id));

        workPlan.setStartTime(startTime);
        workPlan.setEndTime(endTime);

        return workPlanRepository.save(workPlan);
    }

    @Override
    public DoctorWorkSchedule updateWorkScheduleTimes(Integer id, LocalTime startTime, LocalTime endTime) {
        DoctorWorkSchedule workSchedule = doctorWorkScheduleRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("DoctorWorkSchedule not found with id: " + id));

        workSchedule.setStartTime(startTime);
        workSchedule.setEndTime(endTime);

        return doctorWorkScheduleRepository.save(workSchedule);
    }
}
