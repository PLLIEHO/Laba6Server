package server.core.commands;


import server.core.Collection;
import server.data.HumanBeing;

import java.util.ArrayList;
import java.util.List;

public class DescendingSort {
    public String descendingSort(Collection collection){
            List<HumanBeing> sortList = new ArrayList<>(collection.getCollection());
            String answer = "";
            if(sortList.size()>0) {
                for (int i = 0; i < sortList.size(); i++) answer = answer + sortList.get(sortList.size() - 1 - i);
            } else {
                answer = "Коллекция пуста.";
            }
        return answer;
    }
}
