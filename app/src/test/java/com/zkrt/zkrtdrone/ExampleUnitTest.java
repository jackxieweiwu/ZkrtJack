package com.zkrt.zkrtdrone;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        double userX = Math.max(Math.min(1350, 1650), 1350);
        double userY = Math.max(Math.min(1350, 1650), 1650);
        /*float mathYaw = (float) Math.toRadians(180 - 1.1409)+4.5f;
        float aa = Math.min(40, 40) / 1.2f;
        float bb = aa*1.2f;*/
        System.out.println(userX);
        System.out.println(userY);
      /*  System.out.println((float) Math.sin(mathYaw)* bb);
        System.out.println((float) Math.cos(mathYaw)* bb);*/
    }
}