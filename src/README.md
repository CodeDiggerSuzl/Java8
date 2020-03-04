# Learn Java 8

## 新的日期时间和 API
### 现有 API 的问题

- `Date` 定义为可修改的，使用 `SimpleDateFormat` 实现为非线程安全的。`Date` 和 `Calendar` 也不是线程安全的。
-  需要编写额外的代码处理线程安全的问题。
- API 的设置和不合理：使用 `Date` 和 `Calendar` 的设计无法完成日常日期的操作。

### 新的时间日期 API

- `ZoneId`：时区 id，用来确定 instant 和 LocalDateTime 互相转化的规则
- `Instant`: 用来表示时间线上的一个点
- `LocalDate`：表示没有时区的日期，LocalDate 是不可变并且线程安全的
- `LocalTime`：表示没有时区的时间，LocalTime 是不可变并且线程安全的
- `LocalDateTime`: 表示没有时区的日期和时间，也是不变、线程安全的
- `Clock`：用于访问当前时刻、日期、时间，用到时区
- `Duration`

### ZoneId

新的时区类是 `java.time.ZoneId` 是原来 `java.util.TimeZone` 类的代替品。

创建：

```java
 // 获取系统默认时区
ZoneId defaultZoneId = ZoneId.systemDefault();
ZoneId shanghaiZoneId = ZoneId.of("Asia/Shanghai");
// TimeZone 转化为 ZoneId
ZoneId zoneId = TimeZone.getDefault().toZoneId();
// 获取所有的时区
Set<String> availableZoneIds = ZoneId.getAvailableZoneIds();
```

和老的时间区间 `TimeZone` 的转化：`ZoneId oldToNewZoneId = TimeZone.getDefault().toZoneId();`

有了 `ZoneId`，我们就可以将一个 `LocalDate`、`LocalTime` 或 `LocalDateTime` 对象转化为 `ZonedDateTime` 对象：
```java
LocalDateTime localDateTime = LocalDateTime.now();
ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, shanghaiZoneId);
```

`ZonedDateTime` 对象由两部分构成，`LocalDateTime` 和 `ZoneId`，其中 `2018-03-03T15:26:56.147` 部分为 `LocalDateTime`，`+08:00[Asia/Shanghai]` 部分为 ZoneId。


另一种表示时区的方式是使用 `ZoneOffset`，它是以当前时间和 ** 世界标准时间（UTC）/ 格林威治时间（GMT）** 的偏差来计算，例如：

```java
ZoneOffset zoneOffset = ZoneOffset.of("+09:00");
LocalDateTime localDateTime = LocalDateTime.now();
OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, zoneOffset);
```

### Instant

Instant 类在 Java 日期与时间功能中，表示了时间线上一个确切的点，** 定义为距离初始时间的时间差 **（初始时间为 GMT 1970 年 1 月 1 日 00:00）经测量一天有 86400 秒，从初始时间开始不断向前移动。

```java
// crete
Instant now = Instant.now();
// 距离初始时间的秒钟数
long seconds = getEpochSecond()
// 在当前一秒内的第几纳秒
int nanos = getNano()
```
#### Instant 的计算
获取另一个 Instance
- `plusSeconds()`
- `plusMillis()`
- `plusNanos()`
- `minusSeconds()`
- `minusMillis()`
- `minusNanos()`

### Clock

Clock 类提供了访问当前日期和时间的方法，Clock 是时区敏感的，可以用来取代 `System.currentTimeMillis()` 来获取当前的微秒数。

某一个特定的时间点也可以使用 Instant 类来表示，Instant 类也可以用来创建老的 `java.util.Date` 对象。

```java
Clock clock = Clock.systemDefaultZone();
long millis = clock.millis();
Instant instant = clock.instant();
Date legacyDate = Date.from(instant);   // legacy java.util.Date
```

### LocalDate
获取 [ISO](https://www.iso.org/standards.html) 格式的时间 `yyyy-MM-dd` 的日期，不包含时区属性，纪念日，生日等。

#### 创建 `java.time.LocalDate`

e.g.
```java
LocalDate localDate = LocalDate.now(); // 2020-03-04
LocalDate birthday = LocalDate.of(1995, 4, 10); // 1995-04-10
```
#### 添加操作
- 添加天：`plusDays()`
- 添加周：`plusWeeks()`
- 添加年：`plusYears()`
- 添加月：`plusMonths()`
#### 解析操作
```java
DayOfWeek week = LocalDate.parse("2020-11-21").getDayOfWeek();// SATURDAY
int month = LocalDate.parse("1949-10-10").getDayOfMonth(); // 20
```
测试是否为闰年：
```java
LocalDate.now().isLeapYear()
```

判断是之前还是之后：
```java
LocalDate.parse("1999-02-01").isBefore(LocalDate.now());
```

获取第一天：
```java
LocalDate firstDayOfMonth = date.with(TemporalAdjusters.firstDayOfMonth()); // 1990-01-01
```

### LocalTime & LocalDateTime
同 LocalTime 操作类似

###  日期格式化
```java
LocalDateTime now = LocalDateTime.now(); // 默认格式化: 2020-03-04T20:34:28.327
DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 自定义格式化: 2020-03-04 20:34:28
localDateTime = LocalDateTime.parse("2017-07-20 15:27:44", dateTimeFormatter);// 字符串转 LocalDateTime: 2017-07-20T15:27:44
```
转化为字符串：
```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String today = formatter.format(LocalDateTime.now());
```

### 日期周期 `Period` 和时间周期 `Duration`
Period 用于修改给定日期的或者两个日期的区别。

```java
LocalDate initDate = LocalDate.parse("2019-02-28");
LocalDate plusDate = initDate.plus(Period.ofDays(5));
```

一个Duration对象里有两个域：纳秒值（小于一秒的部分），秒钟值（一共有几秒），他们的组合表达了时间长度。注意屯使用System.getCurrentTimeMillis()时不同，Duration不包含毫秒这个属性。
你可以通过以下两个方法得到它们的值：
```java
long seconds =  getSeconds()
int nanos   =   getNano()
```

计算：`plusXxx`

### 遗留代码的转换
#### Date <=> Instant 的转换

```java
 Date date = Date.from(Instant.now()); // Wed Mar 04 20:50:48 CST 2020
 Instant instant = date.toInstant(); // 2020-03-04T12:50:48.494Z
```
#### Date => LocalDateTime
```java
// Date => LocalDateTime
LocalDateTime time = LocalDateTime.from(new Date().toInstant());
```
#### LocalDateTime => Date
```java
 Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
```

#### LocalDate => Date
```java
Date from = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
```

- 什么叫做线程安全 ？

- [SimpleDateFormat 的线程安全问题与解决方案](http://www.cnblogs.com/zemliu/p/3290585.html)
- [为什么 SimpleDateFormat 不是线程安全的？](http://blog.csdn.net/yiifaa/article/details/73499053)
- [Java 获取 N 天前，N 天后的日期（如 3 天）](http://blog.csdn.net/liuwei0376/article/details/13620879)


> Thanks to biezhi,[视频链接](https://www.youtube.com/watch?v=A733pQxiEDk&list=PLK2w-tGRdrj749bbnxLrfPQAJ9B-w8Pg0)
