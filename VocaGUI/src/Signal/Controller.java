package Signal;

@FunctionalInterface
public interface Controller {
    void send(Signal signal, Object data);
}
