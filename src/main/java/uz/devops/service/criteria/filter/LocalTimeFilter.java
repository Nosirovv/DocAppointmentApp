package uz.devops.service.criteria.filter;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import tech.jhipster.service.filter.RangeFilter;

public class LocalTimeFilter extends RangeFilter<LocalTime> {

    private static final long serialVersionUID = 1L;

    public LocalTimeFilter() {}

    public LocalTimeFilter(LocalTimeFilter filter) {
        super(filter);
    }

    public LocalTimeFilter copy() {
        return new LocalTimeFilter(this);
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setEquals(LocalTime equals) {
        super.setEquals(equals);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setNotEquals(LocalTime equals) {
        super.setNotEquals(equals);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setIn(List<LocalTime> in) {
        super.setIn(in);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setNotIn(List<LocalTime> notIn) {
        super.setNotIn(notIn);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setGreaterThan(LocalTime equals) {
        super.setGreaterThan(equals);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setLessThan(LocalTime equals) {
        super.setLessThan(equals);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setGreaterThanOrEqual(LocalTime equals) {
        super.setGreaterThanOrEqual(equals);
        return this;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    public LocalTimeFilter setLessThanOrEqual(LocalTime equals) {
        super.setLessThanOrEqual(equals);
        return this;
    }
}
