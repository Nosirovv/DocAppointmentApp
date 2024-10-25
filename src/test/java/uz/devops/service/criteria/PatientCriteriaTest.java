package uz.devops.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PatientCriteriaTest {

    @Test
    void newPatientCriteriaHasAllFiltersNullTest() {
        var patientCriteria = new PatientCriteria();
        assertThat(patientCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void patientCriteriaFluentMethodsCreatesFiltersTest() {
        var patientCriteria = new PatientCriteria();

        setAllFilters(patientCriteria);

        assertThat(patientCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void patientCriteriaCopyCreatesNullFilterTest() {
        var patientCriteria = new PatientCriteria();
        var copy = patientCriteria.copy();

        assertThat(patientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(patientCriteria)
        );
    }

    @Test
    void patientCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var patientCriteria = new PatientCriteria();
        setAllFilters(patientCriteria);

        var copy = patientCriteria.copy();

        assertThat(patientCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(patientCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var patientCriteria = new PatientCriteria();

        assertThat(patientCriteria).hasToString("PatientCriteria{}");
    }

    private static void setAllFilters(PatientCriteria patientCriteria) {
        patientCriteria.id();
        patientCriteria.name();
        patientCriteria.email();
        patientCriteria.dateOfBirth();
        patientCriteria.appointmentsId();
        patientCriteria.distinct();
    }

    private static Condition<PatientCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDateOfBirth()) &&
                condition.apply(criteria.getAppointmentsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PatientCriteria> copyFiltersAre(PatientCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDateOfBirth(), copy.getDateOfBirth()) &&
                condition.apply(criteria.getAppointmentsId(), copy.getAppointmentsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
