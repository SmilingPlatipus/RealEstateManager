package com.openclassrooms.realestatemanager;

import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UnitTest
{
    @Test
    public void priceConversion_dollarToEuro_isCorrect() {
        int testPrice = 100, priceInEuro =  (int) Math.round(testPrice * 0.812);
        assertEquals(priceInEuro, Utils.convertDollarToEuro(testPrice));
    }

    @Test
    public void priceConversion_euroToDollar_isCorrect() {
        int testPrice = 100, priceInDollar =  (int) Math.round(testPrice / 0.812);
        assertEquals(priceInDollar, Utils.convertEuroToDollar(testPrice));
    }

    @Test
    public void dateFormat_YMD_isCorrect() {
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy/MM/dd");
        try {
            assertNotNull(pattern.parse(Utils.getTodayDateAsYearMonthDay()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dateFormat_DMY_isCorrect() {
        SimpleDateFormat pattern = new SimpleDateFormat("dd/MM/yyyy");
        try {
            assertNotNull(pattern.parse(Utils.getTodayDateAsDayMonthYear()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}