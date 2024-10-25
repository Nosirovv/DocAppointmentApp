package uz.devops.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link uz.devops.domain.MedicalHistory} entity. This class is used
 * in {@link uz.devops.web.rest.MedicalHistoryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medical-histories?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedicalHistoryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter createdDate;

    private LongFilter recordsId;

    private Boolean distinct;

    public MedicalHistoryCriteria() {}

    public MedicalHistoryCriteria(MedicalHistoryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(LocalDateFilter::copy).orElse(null);
        this.recordsId = other.optionalRecordsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MedicalHistoryCriteria copy() {
        return new MedicalHistoryCriteria(this);
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
        final MedicalHistoryCriteria that = (MedicalHistoryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(recordsId, that.recordsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, recordsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicalHistoryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalRecordsId().map(f -> "recordsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
