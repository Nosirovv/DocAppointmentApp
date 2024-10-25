package uz.devops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import uz.devops.domain.enumeration.AppointmentStatus;

/**
 * Criteria class for the {@link uz.devops.domain.Appointment} entity. This class is used
 * in {@link uz.devops.web.rest.AppointmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appointments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointmentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AppointmentStatus
     */
    public static class AppointmentStatusFilter extends Filter<AppointmentStatus> {

        public AppointmentStatusFilter() {}

        public AppointmentStatusFilter(AppointmentStatusFilter filter) {
            super(filter);
        }

        @Override
        public AppointmentStatusFilter copy() {
            return new AppointmentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter appointmentDateTime;

    private AppointmentStatusFilter status;

    private LongFilter doctorId;

    private LongFilter patientId;

    private Boolean distinct;

    public AppointmentCriteria() {}

    public AppointmentCriteria(AppointmentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.appointmentDateTime = other.optionalAppointmentDateTime().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(AppointmentStatusFilter::copy).orElse(null);
        this.doctorId = other.optionalDoctorId().map(LongFilter::copy).orElse(null);
        this.patientId = other.optionalPatientId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppointmentCriteria copy() {
        return new AppointmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public Optional<InstantFilter> optionalAppointmentDateTime() {
        return Optional.ofNullable(appointmentDateTime);
    }

    public InstantFilter appointmentDateTime() {
        if (appointmentDateTime == null) {
            setAppointmentDateTime(new InstantFilter());
        }
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(InstantFilter appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatusFilter getStatus() {
        return status;
    }

    public Optional<AppointmentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public AppointmentStatusFilter status() {
        if (status == null) {
            setStatus(new AppointmentStatusFilter());
        }
        return status;
    }

    public void setStatus(AppointmentStatusFilter status) {
        this.status = status;
    }

    public LongFilter getDoctorId() {
        return doctorId;
    }

    public Optional<LongFilter> optionalDoctorId() {
        return Optional.ofNullable(doctorId);
    }

    public LongFilter doctorId() {
        if (doctorId == null) {
            setDoctorId(new LongFilter());
        }
        return doctorId;
    }

    public void setDoctorId(LongFilter doctorId) {
        this.doctorId = doctorId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public Optional<LongFilter> optionalPatientId() {
        return Optional.ofNullable(patientId);
    }

    public LongFilter patientId() {
        if (patientId == null) {
            setPatientId(new LongFilter());
        }
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppointmentCriteria that = (AppointmentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(appointmentDateTime, that.appointmentDateTime) &&
            Objects.equals(status, that.status) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appointmentDateTime, status, doctorId, patientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointmentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAppointmentDateTime().map(f -> "appointmentDateTime=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalDoctorId().map(f -> "doctorId=" + f + ", ").orElse("") +
            optionalPatientId().map(f -> "patientId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
