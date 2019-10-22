package codexe.han.leetcode.escapeplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class escape380 {

}
class RandomizedSet {
    /*
    Map<num, pos>
    list:


    */

    /** Initialize your data structure here. */
    private Map<Integer, Integer> posMap;
    private List<Integer> list;
    public RandomizedSet() {
        this.posMap = new HashMap<>();
        this.list = new ArrayList<>();
    }

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if(posMap.containsKey(val)) return false;
        posMap.put(val, list.size());
        list.add(val);
        return true;
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        if(!posMap.containsKey(val)) return false;
        int pos = posMap.get(val);
        //与list最后一位进行交换，然后移除最后一位
        if(pos != list.size() - 1) {
            swap(list, pos, list.size() - 1);
            posMap.put(list.get(pos), pos);
        }
        list.remove(list.size() - 1);
        posMap.remove(val);
        return true;
    }

    private void swap(List<Integer> list, int i, int j) {
        int tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }

    /** Get a random element from the set. */
    public int getRandom() {
        int random = (int) (Math.random() * list.size());
        return list.get(random);
    }
}

/**
 * Your RandomizedSet object will be instantiated and called as such:
 * RandomizedSet obj = new RandomizedSet();
 * boolean param_1 = obj.insert(val);
 * boolean param_2 = obj.remove(val);
 * int param_3 = obj.getRandom();
 */