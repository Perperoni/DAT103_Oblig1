package HVL.Scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import org.junit.jupiter.api.Test;
import org.hamcrest.MatcherAssert;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationTest {

	Map<Integer, List<Task>> arrivals;
	Simulation simulation;
	private Integer test=1;  // DO NOT change this for Subtasks 1 and 2(a)
	// ONLY change the value from 1 to 2 for Subtask 2(b).

	@BeforeEach
	public void setUp() {
		simulation = new Simulation();
		switch (test) {
			//Test 1
			case 1:
				arrivals = Map.ofEntries(
						Map.entry(0, List.of(
								simulation.makeTask(5),
								simulation.makeTask(3),
								simulation.makeTask(1))),
						Map.entry(2, List.of(
								simulation.makeTask(1),
								simulation.makeTask(4))),
						Map.entry(6, List.of(
								simulation.makeTask(6),
								simulation.makeTask(4))),
						Map.entry(10, List.of(
								simulation.makeTask(2))),
						Map.entry(16, List.of(
								simulation.makeTask(1),
								simulation.makeTask(3))));
				break;
			//Test 2
			case 2:
				arrivals = Map.ofEntries(
						Map.entry(0, List.of(simulation.makeTask(7))),   
						Map.entry(1, List.of(simulation.makeTask(5))),   
						Map.entry(2, List.of(simulation.makeTask(10))),  
						Map.entry(4, List.of(simulation.makeTask(1))),   
						Map.entry(5, List.of(simulation.makeTask(4))),   
						Map.entry(12, List.of(simulation.makeTask(4))),  
						Map.entry(14, List.of(simulation.makeTask(4))),  
						Map.entry(16, List.of(simulation.makeTask(4))),  
						Map.entry(18, List.of(simulation.makeTask(4))),  
						Map.entry(20, List.of(simulation.makeTask(4)))   
				);
		}
		simulation.setArrivals(arrivals);
	}


        @Test
	public void testFCFS() {
	    var fcfsScheduler = new FCFSScheduler();
	    simulation.setScheduler(fcfsScheduler);

	    switch (test) {
			case 1:  //Assert Test 1
			    var steps = Stream.generate(() -> {
				    simulation.step();
				    var state = "T=%d %s".formatted(simulation.time(), fcfsScheduler.view());
				    simulation.clocktick();
				    return state;
				}).limit(30).collect(Collectors.toList());  //30 is the total number of execution steps

				// Subtask 1: Write out expected view for 30 steps of First Come First Served scheduling
				// Assert Test 1
				assertThat(steps,contains(
					    "T=0 Scheduled: T1 Ready: T2, T3",
                        "T=1 Scheduled: T1 Ready: T2, T3",
                        "T=2 Scheduled: T1 Ready: T2, T3, T4, T5",
                        "T=3 Scheduled: T1 Ready: T2, T3, T4, T5",
                        "T=4 Scheduled: T1 Ready: T2, T3, T4, T5",
                        "T=5 Scheduled: T2 Ready: T3, T4, T5",
                        "T=6 Scheduled: T2 Ready: T3, T4, T5, T6, T7",
                        "T=7 Scheduled: T2 Ready: T3, T4, T5, T6, T7",
                        "T=8 Scheduled: T3 Ready: T4, T5, T6, T7",
                        "T=9 Scheduled: T4 Ready: T5, T6, T7",
                        "T=10 Scheduled: T5 Ready: T6, T7, T8",
                        "T=11 Scheduled: T5 Ready: T6, T7, T8",
                        "T=12 Scheduled: T5 Ready: T6, T7, T8",
                        "T=13 Scheduled: T5 Ready: T6, T7, T8",
                        "T=14 Scheduled: T6 Ready: T7, T8",
                        "T=15 Scheduled: T6 Ready: T7, T8",
                        "T=16 Scheduled: T6 Ready: T7, T8, T9, T10",
                        "T=17 Scheduled: T6 Ready: T7, T8, T9, T10",
                        "T=18 Scheduled: T6 Ready: T7, T8, T9, T10",
                        "T=19 Scheduled: T6 Ready: T7, T8, T9, T10",
                        "T=20 Scheduled: T7 Ready: T8, T9, T10",
                        "T=21 Scheduled: T7 Ready: T8, T9, T10",
                        "T=22 Scheduled: T7 Ready: T8, T9, T10",
                        "T=23 Scheduled: T7 Ready: T8, T9, T10",
                        "T=24 Scheduled: T8 Ready: T9, T10",
                        "T=25 Scheduled: T8 Ready: T9, T10",
                        "T=26 Scheduled: T9 Ready: T10",
                        "T=27 Scheduled: T10 Ready: ",
                        "T=28 Scheduled: T10 Ready: ",
                        "T=29 Scheduled: T10 Ready: "

				));
				break;

			case 2: break;

		}
	}


	@Test
	public void testMLFQ() {
		var mlfqScheduler = new MLFQScheduler(simulation.getClock());
		simulation.setScheduler(mlfqScheduler);

		switch (test) {
			case 1:
				var steps1 = Stream.generate(() -> {
					simulation.step();
					var state = "T=%d %s".formatted(simulation.time(), mlfqScheduler.view());
					simulation.clocktick();
					return state;
				}).limit(30).collect(Collectors.toList());

				assertThat(steps1,contains(
						"T=0 Scheduled: T1 Ready: T2, T3",
						"T=1 Scheduled: T1 Ready: T2, T3",
						"T=2 Scheduled: T1 Ready: T2, T3, T4, T5",
						"T=3 Scheduled: T1 Ready: T2, T3, T4, T5",
						"T=4 Scheduled: T2 Ready: T3, T4, T5, T1",
						"T=5 Scheduled: T2 Ready: T3, T4, T5, T1",
						"T=6 Scheduled: T2 Ready: T3, T4, T5, T6, T7, T1",
						"T=7 Scheduled: T3 Ready: T4, T5, T6, T7, T1",
						"T=8 Scheduled: T4 Ready: T5, T6, T7, T1",
						"T=9 Scheduled: T5 Ready: T6, T7, T1",
						"T=10 Scheduled: T5 Ready: T6, T7, T8, T1",
						"T=11 Scheduled: T5 Ready: T6, T7, T8, T1",
						"T=12 Scheduled: T5 Ready: T6, T7, T8, T1",
						"T=13 Scheduled: T6 Ready: T7, T8, T1",
						"T=14 Scheduled: T6 Ready: T7, T8, T1",
						"T=15 Scheduled: T6 Ready: T7, T8, T1",
						"T=16 Scheduled: T6 Ready: T7, T8, T9, T10, T1",
						"T=17 Scheduled: T7 Ready: T8, T9, T10, T1, T6",
						"T=18 Scheduled: T7 Ready: T8, T9, T10, T1, T6",
						"T=19 Scheduled: T7 Ready: T8, T9, T10, T1, T6",
						"T=20 Scheduled: T7 Ready: T8, T9, T10, T1, T6",
						"T=21 Scheduled: T8 Ready: T9, T10, T1, T6",
						"T=22 Scheduled: T8 Ready: T9, T10, T1, T6",
						"T=23 Scheduled: T9 Ready: T10, T1, T6",
						"T=24 Scheduled: T10 Ready: T1, T6",
						"T=25 Scheduled: T10 Ready: T1, T6",
						"T=26 Scheduled: T10 Ready: T1, T6",
						"T=27 Scheduled: T1 Ready: T6",
						"T=28 Scheduled: T6 Ready: ",
						"T=29 Scheduled: T6 Ready: "
				));
				break;
				
			case 2:
				var steps = Stream.generate(() -> {
					simulation.step();
					var state = "T=%d %s".formatted(simulation.time(), mlfqScheduler.view());
					simulation.clocktick();
					return state;
				}).limit(48).collect(Collectors.toList()); // 48 is the total number of execution steps

				// Subtask 2(b): Write out expected view for 48 steps of Multi-level feedback scheduling
				// Assert Test 2
				assertThat(steps, contains(

				));
		}
	}
}
