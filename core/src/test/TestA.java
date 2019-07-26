package test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TestA {
    @Test
    public void test() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            iterator.remove();
            System.out.println(list);

        }
    }
}
