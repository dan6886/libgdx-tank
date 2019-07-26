package test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import org.junit.Test;


public class TestA {
    @Test
    public void test() {
        Rectangle rectangle = new Rectangle(0, 0, 10, 10);
        rectangle.merge(-10, -15);
        System.out.println(rectangle);

    }
}
