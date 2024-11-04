package uz.devops.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uz.devops.domain.Appointment;
import uz.devops.repository.AppointmentRepository;
import uz.devops.repository.DoctorRepository;
import uz.devops.service.impl.DoctorServiceImpl;
import uz.devops.service.mapper.DoctorMapperImpl;

public class DoctorServiceTest {

    private AppointmentRepository repository;
    private DoctorService service;

    @BeforeEach
    public void init() {
        var mapper = new DoctorMapperImpl();
        this.repository = Mockito.mock(AppointmentRepository.class);
        //        this.service = new DoctorServiceImpl(Mockito.mock(DoctorRepository.class), mapper, repository);
    }

    @Test
    public void testGetFreeTimesOfDoctorByRange() {
        Appointment a1 = new Appointment();
        a1.setAppointmentStartTime(LocalTime.of(10, 0));
        a1.setAppointmentEndTime(LocalTime.of(11, 0));

        Appointment a2 = new Appointment();
        a2.setAppointmentStartTime(LocalTime.of(14, 30));
        a2.setAppointmentEndTime(LocalTime.of(15, 0));

        Mockito.when(repository.findAll()).thenReturn(List.of(a1, a2));

        var start = LocalTime.of(9, 0);
        var end = LocalTime.of(16, 0);
        LocalDate date = LocalDate.now();

        var response = service.freeTime(start, end, 1, date);
        System.out.println(response);
    }
}
