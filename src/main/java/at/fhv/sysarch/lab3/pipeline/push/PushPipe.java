package at.fhv.sysarch.lab3.pipeline.push;

public interface PushPipe<T> {
    void push(T data);
}
