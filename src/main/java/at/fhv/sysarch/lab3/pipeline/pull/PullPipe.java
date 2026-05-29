package at.fhv.sysarch.lab3.pipeline.pull;

public interface PullPipe<T> {
    boolean hasNext();
    T pull();
}
