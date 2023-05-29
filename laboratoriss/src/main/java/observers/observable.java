package observers;

public interface observable {
    void notifyObservers();
    void add(observer o);
}
