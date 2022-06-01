package server.core.comparators;

import server.data.HumanBeing;

import java.util.Comparator;

public class CoordsXComparator implements Comparator<HumanBeing> {
    @Override
    public int compare(HumanBeing o1, HumanBeing o2) {
        if(o1.getCoordinatesX() - o2.getCoordinatesX() > 0){
            return 1;
        } else return -1;
    }
}
