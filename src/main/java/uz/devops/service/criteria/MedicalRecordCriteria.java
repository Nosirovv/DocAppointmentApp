package uz.devops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.devops.domain.MedicalRecord} entity. This class is used
 * in {@link uz.devops.web.rest.MedicalRecordResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medical-records?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalRecordCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter createdDate;

    private StringFilter sicknessInfo;

    private StringFilter treatmentInto;

    private LongFilter doctorId;

    private LongFilter historyId;

    private Boolean distinct;

    public MedicalRecordCriteria() {}

    public MedicalRecordCriteria(MedicalRecordCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(LocalDateFilter::copy).orElse(null);
        this.sicknessInfo = other.optionalSicknessInfo().map(StringFilter::copy).orElse(null);
        this.treatmentInto = other.optionalTreatmentInto().map(StringFilter::copy).orElse(null);
        this.doctorId = other.optionalDoctorId().map(LongFilter::copy).orElse(null);
        this.historyId = other.optionalHistoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicalRecordCriteria copy() {
        return new MedicalRecordCriteria(this);
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

    public LocalDateFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<LocalDateFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public LocalDateFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new LocalDateFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(LocalDateFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getSicknessInfo() {
        return sicknessInfo;
    }

    public Optional<StringFilter> optionalSicknessInfo() {
        return Optional.ofNullable(sicknessInfo);
    }

    public StringFilter sicknessInfo() {
        if (sicknessInfo == null) {
            setSicknessInfo(new StringFilter());
        }
        return sicknessInfo;
    }

    public void setSicknessInfo(StringFilter sicknessInfo) {
        this.sicknessInfo = sicknessInfo;
    }

    public StringFilter getTreatmentInto() {
        return treatmentInto;
    }

    public Optional<StringFilter> optionalTreatmentInto() {
        return Optional.ofNullable(treatmentInto);
    }

    public StringFilter treatmentInto() {
        if (treatmentInto == null) {
            setTreatmentInto(new StringFilter());
        }
        return treatmentInto;
    }

    public void setTreatmentInto(StringFilter treatmentInto) {
        this.treatmentInto = treatmentInto;
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

    public LongFilter getHistoryId() {
        return historyId;
    }

    public Optional<LongFilter> optionalHistoryId() {
        return Optional.ofNullable(historyId);
    }

    public LongFilter historyId() {
        if (historyId == null) {
            setHistoryId(new LongFilter());
        }
        return historyId;
    }

    public void setHistoryId(LongFilter historyId) {
        this.historyId = historyId;
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
        final MedicalRecordCriteria that = (MedicalRecordCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(sicknessInfo, that.sicknessInfo) &&
            Objects.equals(treatmentInto, that.treatmentInto) &&
            Objects.equals(doctorId, that.doctorId) &&
            Objects.equals(historyId, that.historyId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, sicknessInfo, treatmentInto, doctorId, historyId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalRecordCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalSicknessInfo().map(f -> "sicknessInfo=" + f + ", ").orElse("") +
            optionalTreatmentInto().map(f -> "treatmentInto=" + f + ", ").orElse("") +
            optionalDoctorId().map(f -> "doctorId=" + f + ", ").orElse("") +
            optionalHistoryId().map(f -> "historyId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
