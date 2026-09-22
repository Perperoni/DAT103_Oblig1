package HVL.Scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.IntSupplier;

/**
 Two-level Multilevel Feedback Queue scheduler.
 
 Q1: Round Robin, time quantum = 4. All new tasks arrive here first. Tasks that dont finish their burst within the quantum is demoted to the back of Q2.
 Q2: FCFS. Runs only when Q1 is empty, and runs a task to completion once selected (no time-quantum preemption).
 */
public class MLFQScheduler implements Scheduler {

    private static final int QUANTUM = 4;  // RR time quantum
    private static final int AGING_THRESHOLD = 11;  

    private final Queue<Task> q1;
    private final Queue<Task> q2;
    private final Map<Task, Integer> q2EntryTime;
    private final IntSupplier time;

    private Task selected;
    private boolean selectedFromQ1;
    private int timestamp; 

    MLFQScheduler(IntSupplier time) {
        this.q1 = new ArrayDeque<>();
        this.q2 = new ArrayDeque<>();
        this.q2EntryTime = new HashMap<>();
        this.time = time;
        this.selected = null;
    }

    @Override
    public Optional<Integer> scheduled() {
        if (selected == null) return Optional.empty();
        return Optional.of(selected.getId());
    }

    @Override
    public List<Integer> ready() {
        List<Integer> ids = new ArrayList<>();
        q1.forEach(t -> ids.add(t.getId()));
        q2.forEach(t -> ids.add(t.getId()));
        return ids;
    }

    @Override
    public void addTask(Task task) {
        q1.add(task);
    }

    // Subtask 2(a): Complete the implementation of Multilevel feedback queue
    @Override
    public void schedule() {
        if (q1.isEmpty()) {
            FCFSQueue();
        }

        RRQueue();
    }

    private void RRQueue() {
        if (selected == null) {
            selected = q1.poll();
            if (selected == null) {
                return;
            }
            selected.start();
        } else  {
            selected.start();
        }

        int TIME_PASSED =  selected.getSize() - selected.getRemaining();
        System.out.println(selected.getId() + ": " + selected.getRemaining());

        if (TIME_PASSED <= QUANTUM) {
            if (selected.isDone()) {
                selected.stop();
                selected = q1.poll();
            }
        } else {
            q2.add(selected);
            selected.stop();
            selected = q1.poll();
        }

    }

    private void FCFSQueue() {
        if(selected == null) {
            selected = q2.poll();
            if(selected == null) {
                return;
            }
            selected.start();
        } else {
            if (selected.isDone()) {
                selected.stop();
                selected = null;
                schedule();
            }
        }
    }
}
