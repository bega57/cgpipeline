package at.fhv.sysarch.lab3.pipeline.pull;

public abstract class PullFilter<I, O> implements PullPipe<O> {
    protected PullPipe<I> predecessor;

    public void setPredecessor(PullPipe<I> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public boolean hasNext() {
        return predecessor.hasNext();
    }
}
