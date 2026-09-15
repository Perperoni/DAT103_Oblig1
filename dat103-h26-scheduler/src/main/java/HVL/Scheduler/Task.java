package HVL.Scheduler;

public interface Task {
    int getId();
    boolean isDone();
    void start();
    void stop();
    int getSize();
    int getRemaining();
    int getPriority();
    void setPriority(int priority);
}
