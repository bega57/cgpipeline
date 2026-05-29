package at.fhv.sysarch.lab3.pipeline.push;

public abstract class PushFilter<I, O> implements PushPipe<I> {
    protected PushPipe<O> successor;

    public void setSuccessor(PushPipe<O> successor) {
        this.successor = successor;
    }
}
