package dmfmm.catwalks.utils;

public class Pair<T, K> {

    private T firstThing;
    private K secondThing;

    public Pair(T s, K y){
        firstThing = s;
        secondThing = y;
    }

    public T getKey(){
        return firstThing;
    }

    public K getValue(){
        return secondThing;
    }
}
