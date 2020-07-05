public abstract class Source extends Element {
    double offset, amplitude, frequency, phase;

    public Source(String name, Nodes node1, Nodes node2, double offset,double amplitude,double frequency,double phase){
        super(name,node1,node2);
        this.offset=offset; this.amplitude=amplitude; this.frequency=frequency;this.phase=phase;
    }

    public double value(){
        return offset+amplitude*Math.sin(2*Math.PI*frequency*Simulator.t+phase);
    }

}

