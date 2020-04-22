package main.util;

public class Pair<F,S> {
    F first;
    S second;

    public F getFirst() {
        return first;
    }
    public S getSecond(){
        return second;
    }

    public void put(F first, S second){
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
