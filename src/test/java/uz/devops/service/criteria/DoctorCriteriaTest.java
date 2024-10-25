package uz.devops.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DoctorCriteriaTest {

    @Test
    void newDoctorCriteriaHasAllFiltersNullTest() {
        var doctorCriteria = new DoctorCriteria();
        assertThat(doctorCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void doctorCriteriaFluentMethodsCreatesFiltersTest() {
        var doctorCriteria = new DoctorCriteria();

        setAllFilters(doctorCriteria);

        assertThat(doctorCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void doctorCriteriaCopyCreatesNullFilterTest() {
        var doctorCriteria = new DoctorCriteria();
        var copy = doctorCriteria.copy();

        assertThat(doctorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(doctorCriteria)
        );
    }

    @Test
    void doctorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var doctorCriteria = new DoctorCriteria();
        setAllFilters(doctorCriteria);

        var copy = doctorCriteria.copy();

        assertThat(doctorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(doctorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var doctorCriteria = new DoctorCriteria();

        assertThat(doctorCriteria).hasToString("DoctorCriteria{}");
    }

    private static void setAllFilters(DoctorCriteria doctorCriteria) {
        doctorCriteria.id();
        doctorCriteria.name();
        doctorCriteria.specialization();
        doctorCriteria.availableFrom();
        doctorCriteria.availableTo();
        doctorCriteria.appointmentsId();
        doctorCriteria.recordsId();
        doctorCriteria.distinct();
    }

    private static Condition<DoctorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getSpecialization()) &&
                condition.apply(criteria.getAvailableFrom()) &&
                condition.apply(criteria.getAvailableTo()) &&
                condition.apply(criteria.getAppointmentsId()) &&
                condition.apply(criteria.getRecordsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DoctorCriteria> copyFiltersAre(DoctorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getSpecialization(), copy.getSpecialization()) &&
                condition.apply(criteria.getAvailableFrom(), copy.getAvailableFrom()) &&
                condition.apply(criteria.getAvailableTo(), copy.getAvailableTo()) &&
                condition.apply(criteria.getAppointmentsId(), copy.getAppointmentsId()) &&
                condition.apply(criteria.getRecordsId(), copy.getRecordsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
