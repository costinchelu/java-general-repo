package chapter2;

import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.NavigableSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventPeriodCombinerTest {

	private Clock clock;
	private SchedulerCalendar calendar;
	private ZonedDateTime startZDateTime;
	private LocalDate startLocalDate;

	@Before
	public void setup() {
		calendar = new SchedulerCalendar();
		clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC);
		startZDateTime = ZonedDateTime.now(clock);
		startLocalDate = LocalDate.from(startZDateTime);
	}

	@Test
	public void testNoWorkPeriods() {
		calendar.addEvent(Event.of(startZDateTime, startZDateTime.plusHours(1),""));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertTrue(combined.isEmpty());
	}

	@Test
	public void testSingleWorkPeriod() {
		WorkPeriod p1 = WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(2, 0));
		calendar.addWorkPeriod(p1);

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p2 = combined.first();
		assertEquals(p2.getStartTime(), p1.getStartTime());
		assertEquals(p2.getEndTime(), p2.getEndTime());
	}

	@Test
	public void testNoOverlapPeriodFirst() {
		calendar.addEvent(Event.of(startZDateTime.withHour(3), startZDateTime.withHour(4),""));
		WorkPeriod period = WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(2, 0));
		calendar.addWorkPeriod(period);

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.first();
		assertEquals(period.getStartTime(), p.getStartTime());
		assertEquals(period.getEndTime(), p.getEndTime());
	}

	@Test
	public void testNoOverlapEventFirst() {
		calendar.addEvent(Event.of(startZDateTime.withHour(1), startZDateTime.withHour(2),""));
		WorkPeriod period = WorkPeriod.of(startLocalDate.atTime(3, 0), startLocalDate.atTime(4, 0));
		calendar.addWorkPeriod(period);

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.first();
		assertEquals(period.getStartTime(), p.getStartTime());
		assertEquals(period.getEndTime(), p.getEndTime());
	}

	@Test
	public void testSimpleOverlapPeriodFirst() {
		Event event = Event.of(startZDateTime.withHour(2), startZDateTime.withHour(4),"");
		calendar.addEvent(event);
		WorkPeriod period = WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(3, 0));
		calendar.addWorkPeriod(period);

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.first();
		assertEquals(period.getStartTime(), p.getStartTime());
		assertEquals(startZDateTime.withHour(2).toLocalDateTime(), p.getEndTime());
	}

	@Test
	public void testSimpleOverlapEventFirst() {
		calendar.addEvent(Event.of(startZDateTime.withHour(1), startZDateTime.withHour(3),""));
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atTime(2, 0), startLocalDate.atTime(4, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.first();
		assertEquals(startZDateTime.withHour(3).toLocalDateTime(), p.getStartTime());
		assertEquals(startLocalDate.atTime(4, 0), p.getEndTime());
	}

	@Test
	public void testPeriodSurroundsEvent() {
		Event event = Event.of(startZDateTime.withHour(2), startZDateTime.withHour(3),"");
		calendar.addEvent(event);
		WorkPeriod period = WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(4, 0));
		calendar.addWorkPeriod(period);

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(2, combined.size());
		WorkPeriod p = combined.pollFirst();
		assertEquals(startLocalDate.atTime(1, 0), p.getStartTime());
		assertEquals(startZDateTime.withHour(2).toLocalDateTime(), p.getEndTime());
		p = combined.pollFirst();
		assertEquals(startZDateTime.withHour(3).toLocalDateTime(), p.getStartTime());
		assertEquals(startLocalDate.atTime(4, 0), p.getEndTime());
	}

	@Test
	public void testEventSurroundsPeriod() {
		calendar.addEvent(Event.of(startZDateTime.withHour(1), startZDateTime.withHour(4),""));
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atTime(2, 0), startLocalDate.atTime(3, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertTrue(combined.isEmpty());
	}

	@Test
	public void testSimultaneousStartEventLonger() {
		calendar.addEvent(Event.of(startZDateTime, startZDateTime.withHour(3),""));
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atStartOfDay(), startLocalDate.atTime(3, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertTrue(combined.isEmpty());
	}

	@Test
	public void testSimultaneousStartPeriodLonger() {
		Event event = Event.of(startZDateTime.withHour(1), startZDateTime.withHour(3),"");
		calendar.addEvent(event);
		WorkPeriod period = WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(4, 0));
		calendar.addWorkPeriod(period);

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.pollFirst();
		assertEquals(startZDateTime.withHour(3).toLocalDateTime(), p.getStartTime());
		assertEquals(period.getEndTime(), p.getEndTime());
	}

	@Test
	public void testSimultaneousEndEventLonger() {
		calendar.addEvent(Event.of(startZDateTime.withHour(1), startZDateTime.withHour(4),""));
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atTime(2, 0), startLocalDate.atTime(4, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertTrue(combined.isEmpty());
	}

	@Test
	public void testSimultaneousEndPeriodLonger() {
		Event event = Event.of(startZDateTime.withHour(2), startZDateTime.withHour(4),"");
		calendar.addEvent(event);
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(4, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.pollFirst();
		assertEquals(startLocalDate.atTime(1, 0), p.getStartTime());
		assertEquals(startZDateTime.withHour(2).toLocalDateTime(), p.getEndTime());
	}

	@Test
	public void testAbuttingPeriodFirst() {
		calendar.addEvent(Event.of(startZDateTime.withHour(2), startZDateTime.withHour(3),""));
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atTime(1, 0), startLocalDate.atTime(2, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.pollFirst();
		assertEquals(startLocalDate.atTime(1, 0), p.getStartTime());
		assertEquals(startLocalDate.atTime(2, 0), p.getEndTime());
	}

	@Test
	public void testAbuttingEventFirst() {
		calendar.addEvent(Event.of(startZDateTime.withHour(1), startZDateTime.withHour(2),""));
		calendar.addWorkPeriod(WorkPeriod.of(startLocalDate.atTime(2, 0), startLocalDate.atTime(3, 0)));

		NavigableSet<WorkPeriod> combined = calendar.overwritePeriodsWithEvents(clock.getZone());

		assertEquals(1, combined.size());
		WorkPeriod p = combined.pollFirst();
		assertEquals(startLocalDate.atTime(2, 0), p.getStartTime());
		assertEquals(startLocalDate.atTime(3, 0), p.getEndTime());
	}
}
