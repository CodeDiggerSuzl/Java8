package java8.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import org.junit.Test;

/**
 * java 8 新时间 api 处理
 *
 * @author Suz1
 * @date 2020/3/4 6:34 下午
 */
public class TimeTest {
    @Test
    public void testZoneId() {
        // 获取系统默认时区
        ZoneId defaultZoneId = ZoneId.systemDefault();
        ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
        // TimeZone 转化为 ZoneId
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        // 获取所有的时区
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();

        System.out.println(defaultZoneId);
        System.out.println(shanghaiZoneId);
        System.out.println(zoneId);
        System.out.println(availableZoneIds);
    }

    @Test
    public void testInstance() {
        // 创建 instance 实例
        Instant now = Instant.now();
        System.out.println(now);
        // 访问 instant 时间
        long second = now.getEpochSecond();
        int nano = now.getNano();
        System.out.println("second " + second);
        System.out.println("nano   " + nano);
        // 3 sec later
        Instant later = now.plusSeconds(3);
        Instant earlier = now.minusSeconds(3);
    }

    @Test
    public void testClock() {
        Clock clock = Clock.systemDefaultZone();
        System.out.println(clock);
        long millis = clock.millis();
        System.out.println(millis);
        Instant instant = clock.instant();
        System.out.println(instant);
        Date legacyDate = Date.from(instant);
        System.out.println(legacyDate);
    }

    @Test
    public void testLocalDate() {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate); // 2020-03-04

        LocalDate birthday = LocalDate.of(1995, 4, 10);// 1995-04-10
        LocalDate plusWeeks = birthday.plusWeeks(3);
        System.out.println(birthday);

        int year = localDate.getYear();
        int dayOfMonth = localDate.getDayOfMonth();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        LocalDate WeeksLater = localDate.plusWeeks(42);

        DayOfWeek week = LocalDate.parse("2020-11-21").getDayOfWeek();// SATURDAY
        int month = LocalDate.parse("1949-10-10").getDayOfMonth();// 20
        System.out.println(month);

        boolean b = LocalDate.parse("1999-02-01").isBefore(LocalDate.now());
        System.out.println(b);

        LocalDate date = LocalDate.parse("1990-01-02");
        LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println(firstDayOfMonth);
    }

    @Test // local date time is just like this
    public void testLocalTime() {
        // final time; return a new localTime e.g. school starts at 8:00
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
        LocalTime plusHours = localTime.plusHours(3);
    }

    @Test // ZonedDateTime: with zone
    public void testZonedDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }

    @Test // DateTimeFormatter: 4 4mat and parse time
    public void testDateTime4mat() {
        // with constant defined
        DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;
        String formatDate = formatter.format(LocalDate.now());// 20200304
        System.out.println(formatDate);
        String format = formatter.format(ZonedDateTime.now());// 20200304+0800
        System.out.println(format);

        LocalDateTime dateTime = LocalDateTime.now();
        String isoLocalDate = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);// 2020-03-04
        String isoLocalTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_TIME);// 19:26:16.712
    }

    @Test // duration between 2 instances
    public void testDuration() throws InterruptedException {
        Instant now = Instant.now();
        Thread.sleep(2000);
        Instant then = Instant.now();
        Duration between = Duration.between(now, then);// PT2.003S
        System.out.println(between);
        System.out.println(between.getSeconds());// 2
    }

    @Test
    public void formatTest() {

        LocalDateTime now = LocalDateTime.now(); // 默认格式化: 2020-03-04T20:34:28.327
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 自定义格式化: 2020-03-04
                                                                                                  // 20:34:28
        System.out.println("默认格式化: " + now);
        System.out.println("自定义格式化: " + now.format(dateTimeFormatter));
        LocalDateTime localDateTime = LocalDateTime.parse("2017-07-20 15:27:44", dateTimeFormatter);// 字符串转LocalDateTime:
                                                                                                    // 2017-07-20T15:27:44
        System.out.println("字符串转LocalDateTime: " + localDateTime);

        // To String
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = formatter.format(LocalDateTime.now());
        System.out.println("Today: " + today);
    }

    @Test
    public void testPeriod() {
        LocalDate initDate = LocalDate.parse("2019-02-28");
        LocalDate plusDate = initDate.plus(Period.ofDays(5));
        System.out.println(plusDate);
        System.out.println(new Date());
        System.out.println(Date.from(Instant.now()));
    }

    @Test
    public void testTransOld() {
        Date date = Date.from(Instant.now());// Wed Mar 04 20:50:48 CST 2020
        System.out.println(date);
        Instant instant = date.toInstant(); // 2020-03-04T12:50:48.494Z
        System.out.println(instant);

        // LocalDateTime localDateTime = LocalDateTime.from((TemporalAccessor)new
        // Date());
        // System.out.println(localDateTime);
        LocalDateTime now = LocalDateTime.now();
        Date date2 = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());// Wed Mar 04 20:57:27 CST 2020
        System.out.println(date2);

        // Date => LocalDateTime
        LocalDateTime time = LocalDateTime.from(new Date().toInstant());

        Date from = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
