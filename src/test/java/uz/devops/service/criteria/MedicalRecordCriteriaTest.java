package uz.devops.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicalRecordCriteriaTest {

    @Test
    void newMedicalRecordCriteriaHasAllFiltersNullTest() {
        var medicalRecordCriteria = new MedicalRecordCriteria();
        assertThat(medicalRecordCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void medicalRecordCriteriaFluentMethodsCreatesFiltersTest() {
        var medicalRecordCriteria = new MedicalRecordCriteria();

        setAllFilters(medicalRecordCriteria);

        assertThat(medicalRecordCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void medicalRecordCriteriaCopyCreatesNullFilterTest() {
        var medicalRecordCriteria = new MedicalRecordCriteria();
        var copy = medicalRecordCriteria.copy();

        assertThat(medicalRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(medicalRecordCriteria)
        );
    }

    @Test
    void medicalRecordCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicalRecordCriteria = new MedicalRecordCriteria();
        setAllFilters(medicalRecordCriteria);

        var copy = medicalRecordCriteria.copy();

        assertThat(medicalRecordCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(medicalRecordCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicalRecordCriteria = new MedicalRecordCriteria();

        assertThat(medicalRecordCriteria).hasToString("MedicalRecordCriteria{}");
    }

    private static void setAllFilters(MedicalRecordCriteria medicalRecordCriteria) {
        medicalRecordCriteria.id();
        medicalRecordCriteria.createdDate();
        medicalRecordCriteria.sicknessInfo();
        medicalRecordCriteria.treatmentInto();
        medicalRecordCriteria.doctorId();
        medicalRecordCriteria.historyId();
        medicalRecordCriteria.distinct();
    }

    private static Condition<MedicalRecordCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getSicknessInfo()) &&
                condition.apply(criteria.getTreatmentInto()) &&
                condition.apply(criteria.getDoctorId()) &&
                condition.apply(criteria.getHistoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicalRecordCriteria> copyFiltersAre(
        MedicalRecordCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getSicknessInfo(), copy.getSicknessInfo()) &&
                condition.apply(criteria.getTreatmentInto(), copy.getTreatmentInto()) &&
                condition.apply(criteria.getDoctorId(), copy.getDoctorId()) &&
                condition.apply(criteria.getHistoryId(), copy.getHistoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
