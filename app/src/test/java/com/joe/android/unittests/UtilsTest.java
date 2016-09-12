package com.joe.android.unittests;

import com.joe.android.helpers.Utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class UtilsTest {

    @Test
    public void convertChronometerReadingToMillisecondsIsCorrectForSecondsOnly() throws Exception {
        long result = Utils.convertCounterReadingToMilliseconds("5");
        assertTrue("result is: " + result, result == 5000);
    }

    @Test
    public void convertChronometerReadingToMillisecondsIsCorrectForMinutesAndSeconds() throws Exception {
        long actResult = Utils.convertCounterReadingToMilliseconds("03:05");
        long expResult = (5 * 1000) + (3 * 60 * 1000);
        assertTrue("Expected= " + expResult + ", Act.= " + actResult, expResult == actResult);

    }

    @Test
    public void convertChronometerReadingToMillisecondsIsCorrectForHoursAndMinutesAndSeconds() throws Exception {
        long actResult = Utils.convertCounterReadingToMilliseconds("15:03:05");
        long expResult = (5 * 1000) + (3 * 60 * 1000) + (15 * 60 * 60 * 1000);
        assertTrue("Expected= " + expResult + ", Act.= " + actResult, expResult == actResult);

    }
}