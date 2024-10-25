package uz.devops.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MedicalHistoryCriteriaTest {

    @Test
    void newMedicalHistoryCriteriaHasAllFiltersNullTest() {
        var medicalHistoryCriteria = new MedicalHistoryCriteria();
        assertThat(medicalHistoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void medicalHistoryCriteriaFluentMethodsCreatesFiltersTest() {
        var medicalHistoryCriteria = new MedicalHistoryCriteria();

        setAllFilters(medicalHistoryCriteria);

        assertThat(medicalHistoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void medicalHistoryCriteriaCopyCreatesNullFilterTest() {
        var medicalHistoryCriteria = new MedicalHistoryCriteria();
        var copy = medicalHistoryCriteria.copy();

        assertThat(medicalHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(medicalHistoryCriteria)
        );
    }

    @Test
    void medicalHistoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var medicalHistoryCriteria = new MedicalHistoryCriteria();
        setAllFilters(medicalHistoryCriteria);

        var copy = medicalHistoryCriteria.copy();

        assertThat(medicalHistoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(medicalHistoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var medicalHistoryCriteria = new MedicalHistoryCriteria();

        assertThat(medicalHistoryCriteria).hasToString("MedicalHistoryCriteria{}");
    }

    private static void setAllFilters(MedicalHistoryCriteria medicalHistoryCriteria) {
        medicalHistoryCriteria.id();
        medicalHistoryCriteria.createdDate();
        medicalHistoryCriteria.recordsId();
        medicalHistoryCriteria.distinct();
    }

    private static Condition<MedicalHistoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getRecordsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MedicalHistoryCriteria> copyFiltersAre(
        MedicalHistoryCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getRecordsId(), copy.getRecordsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
