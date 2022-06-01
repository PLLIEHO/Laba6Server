package server.core.comparators;


import server.data.HumanBeing;

import java.util.Comparator;

public class CoordsYComparator implements Comparator<HumanBeing> {
    @Override
    public int compare(HumanBeing o1, HumanBeing o2) {
        if(o1.getCoordinatesY() - o2.getCoordinatesY() > 0){
            return 1;
        } else return -1;
    }
}
