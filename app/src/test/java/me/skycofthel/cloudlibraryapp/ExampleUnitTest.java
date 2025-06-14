package me.skycofthel.cloudlibraryapp;

import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 计算未来30天的日期
        LocalDate futureDate = currentDate.plusDays(30);

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 格式化并输出日期
        String formattedCurrentDate = currentDate.format(formatter);
        String formattedFutureDate = futureDate.format(formatter);

        System.out.println("当前日期: " + formattedCurrentDate);
        System.out.println("未来30天日期: " + formattedFutureDate);


        assertEquals(4, 2 + 2);
    }
}