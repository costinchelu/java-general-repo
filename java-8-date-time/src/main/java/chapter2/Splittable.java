package chapter2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public interface Splittable<E> {

    List<E> split(LocalDateTime splitTime, ZoneId zone);

    LocalDateTime getLocalStartDateTime(ZoneId zone);
}