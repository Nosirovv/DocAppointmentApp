package uz.devops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import uz.devops.domain.enumeration.Specialization;
import uz.devops.service.criteria.filter.LocalTimeFilter;

/**
 * Criteria class for the {@link uz.devops.domain.Doctor} entity. This class is used
 * in {@link uz.devops.web.rest.DoctorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /doctors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DoctorCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Specialization
     */
    public static class SpecializationFilter extends Filter<Specialization> {

        public SpecializationFilter() {}

        public SpecializationFilter(SpecializationFilter filter) {
            super(filter);
        }

        @Override
        public SpecializationFilter copy() {
            return new SpecializationFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private SpecializationFilter specialization;

    private LocalTimeFilter availableFrom;

    private LocalTimeFilter availableTo;

    private LongFilter appointmentsId;

    private LongFilter recordsId;

    private Boolean distinct;

    public DoctorCriteria() {}

    public DoctorCriteria(DoctorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.specialization = other.optionalSpecialization().map(SpecializationFilter::copy).orElse(null);
        this.availableFrom = other.optionalAvailableFrom().map(LocalTimeFilter::copy).orElse(null);
        this.availableTo = other.optionalAvailableTo().map(LocalTimeFilter::copy).orElse(null);
        this.appointmentsId = other.optionalAppointmentsId().map(LongFilter::copy).orElse(null);
        this.recordsId = other.optionalRecordsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DoctorCriteria copy() {
        return new DoctorCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public SpecializationFilter getSpecialization() {
        return specialization;
    }

    public Optional<SpecializationFilter> optionalSpecialization() {
        return Optional.ofNullable(specialization);
    }

    public SpecializationFilter specialization() {
        if (specialization == null) {
            setSpecialization(new SpecializationFilter());
        }
        return specialization;
    }

    public void setSpecialization(SpecializationFilter specialization) {
        this.specialization = specialization;
    }

    public LocalTimeFilter getAvailableFrom() {
        return availableFrom;
    }

    public Optional<LocalTimeFilter> optionalAvailableFrom() {
        return Optional.ofNullable(availableFrom);
    }

    public LocalTimeFilter availableFrom() {
        if (availableFrom == null) {
            setAvailableFrom(new LocalTimeFilter());
        }
        return availableFrom;
    }

    public void setAvailableFrom(LocalTimeFilter availableFrom) {
        this.availableFrom = availableFrom;
    }

    public LocalTimeFilter getAvailableTo() {
        return availableTo;
    }

    public Optional<LocalTimeFilter> optionalAvailableTo() {
        return Optional.ofNullable(availableTo);
    }

    public LocalTimeFilter availableTo() {
        if (availableTo == null) {
            setAvailableTo(new LocalTimeFilter());
        }
        return availableTo;
    }

    public void setAvailableTo(LocalTimeFilter availableTo) {
        this.availableTo = availableTo;
    }

    public LongFilter getAppointmentsId() {
        return appointmentsId;
    }

    public Optional<LongFilter> optionalAppointmentsId() {
        return Optional.ofNullable(appointmentsId);
    }

    public LongFilter appointmentsId() {
        if (appointmentsId == null) {
            setAppointmentsId(new LongFilter());
        }
        return appointmentsId;
    }

    public void setAppointmentsId(LongFilter appointmentsId) {
        this.appointmentsId = appointmentsId;
    }

    public LongFilter getRecordsId() {
        return recordsId;
    }

    public Optional<LongFilter> optionalRecordsId() {
        return Optional.ofNullable(recordsId);
    }

    public LongFilter recordsId() {
        if (recordsId == null) {
            setRecordsId(new LongFilter());
        }
        return recordsId;
    }

    public void setRecordsId(LongFilter recordsId) {
        this.recordsId = recordsId;
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
        final DoctorCriteria that = (DoctorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(specialization, that.specialization) &&
            Objects.equals(availableFrom, that.availableFrom) &&
            Objects.equals(availableTo, that.availableTo) &&
            Objects.equals(appointmentsId, that.appointmentsId) &&
            Objects.equals(recordsId, that.recordsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, specialization, availableFrom, availableTo, appointmentsId, recordsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalSpecialization().map(f -> "specialization=" + f + ", ").orElse("") +
            optionalAvailableFrom().map(f -> "availableFrom=" + f + ", ").orElse("") +
            optionalAvailableTo().map(f -> "availableTo=" + f + ", ").orElse("") +
            optionalAppointmentsId().map(f -> "appointmentsId=" + f + ", ").orElse("") +
            optionalRecordsId().map(f -> "recordsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
