package test;

import com.badlogic.gdx.Input;
import org.junit.Test;

import java.awt.*;

public class TestA {
    @Test
    public void test() {
        int extend = (int) (18 - 8) / 2;
        Rectangle rectangle = new Rectangle((int) 10, 40, 8, 8);
        switch (29) {
            case Input.Keys.W:
            case Input.Keys.S:
                rectangle.setBounds(rectangle.x - extend, rectangle.y, 18, rectangle.height);
                break;
            case Input.Keys.A:
            case Input.Keys.D:
                rectangle.setBounds(rectangle.x, rectangle.y - extend, rectangle.width, 18);
                break;
        }
        System.out.println(rectangle);

    }
}
