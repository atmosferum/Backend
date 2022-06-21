package ru.whattime.whattime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.whattime.whattime.model.Interval;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class WhattimeApplicationTests {

	@Test
	void contextLoads() {
		Interval a = new Interval();
		a.setStartTime(LocalDateTime.now());
		Interval a1 = new Interval();
		a1.setStartTime(LocalDateTime.now().plusMinutes(40));
		List<Interval> intervals = Arrays.asList(a1, a);
		intervals.sort(Comparator.comparing(Interval::getStartTime));
		intervals.forEach(o -> System.out.println(o.getStartTime()));
	}

}
