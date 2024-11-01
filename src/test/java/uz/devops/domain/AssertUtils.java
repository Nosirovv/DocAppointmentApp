package uz.devops.domain;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class AssertUtils {

    public static Comparator<BigDecimal> bigDecimalCompareTo = Comparator.nullsFirst((e1, a2) -> e1.compareTo(a2));
}
