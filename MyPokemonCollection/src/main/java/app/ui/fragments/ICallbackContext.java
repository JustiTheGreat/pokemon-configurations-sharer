package app.ui.fragments;

public interface ICallbackContext {
    void callback(Object caller, Object result);
    void timedOut(Object caller);
}
