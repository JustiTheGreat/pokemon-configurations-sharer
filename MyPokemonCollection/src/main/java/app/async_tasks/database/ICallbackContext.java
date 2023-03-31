package app.async_tasks.database;

public interface ICallbackContext {
    void callback(Object caller, Object result);
    void timedOut();
}
