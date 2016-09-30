package com.example.android.sunshine.app.data;

import android.test.AndroidTestCase;

import java.util.Scanner;

public class TestPractice extends AndroidTestCase {
    /*
        This gets run before every test.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testThatDemonstratesAssertions() throws Throwable {
        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("X should be equal", a, c);
        assertTrue("Y should be true", d > a);
        assertFalse("Z should be false", a == b);

        if (b > d) {
            fail("XX should never happen");
        }
    }

    public void TestThatThing()
    {
        int[] current = new int[]{1, 2, 9, 5, 7};
        int[] desired = new int[]{1, 3, 2, 0, 7};
        testThings(current, desired);
    }
    private void testThings(int[] currentCombinations, int[] desiredCombinations)
    {
        int totalMoves = 0;
        for(int i =0; i < 5; i++ )
        {
            int currentCombo = currentCombinations[i];
            int desiredCombo = desiredCombinations[i];
            int difference = Math.abs(currentCombo - desiredCombo);
            if(currentCombo > 4 && difference > 5)
            {
                totalMoves += (9 - currentCombo) + (desiredCombo + 1);
            }
            else if(currentCombo <= 4 && difference > 5)
            {
                totalMoves += (9 - desiredCombo) + (currentCombo + 1);
            }
            else if(difference < 5)
            {
                totalMoves += difference;
            }


        }

    }


    public void findTheString(String firstString, String secondString)
    {
        boolean isPosssible = true;
        if(secondString.length() > firstString.length())
        {
            System.out.println("NO");
            return;
        }
        String capitalizedString = firstString.toUpperCase();
        String lowerCaseString = firstString;
        String resultFirstString = firstString;

        char[] charArray = secondString.toCharArray();
        int currPos = 0;
        firstString = firstString.toLowerCase();
        try{
            for(int i = 0; i < charArray.length; i++ )
            {


                int indexOfCurrentLower = capitalizedString.indexOf(charArray[i]);
                int indexOfCurrentUpper = lowerCaseString.indexOf(charArray[i]);
                if(indexOfCurrentLower == -1 && indexOfCurrentUpper == -1)
                {
                    isPosssible = false;
                    break;
                }

                else if(indexOfCurrentLower != -1)
                {
                    currPos = currPos + indexOfCurrentLower ;
                    capitalizedString = capitalizedString.substring(indexOfCurrentLower+1);
                    lowerCaseString = lowerCaseString.substring(indexOfCurrentLower+1);



                    resultFirstString = resultFirstString.substring(0, currPos) +
                            resultFirstString.substring(currPos+1);
                }
                else if(indexOfCurrentUpper != -1)
                {
                    currPos = currPos + indexOfCurrentLower +1;
                    capitalizedString = capitalizedString.substring(indexOfCurrentUpper+1);
                    lowerCaseString = lowerCaseString.substring(indexOfCurrentUpper+1);
                    resultFirstString = resultFirstString.substring(0, currPos) +
                            resultFirstString.substring(currPos+1);



                }


            }
        }
        catch(Exception e)
        {
            isPosssible = false;
        }

        boolean hasUpperCase = resultFirstString.equals(resultFirstString.toUpperCase());

        if(isPosssible && !hasUpperCase)
        {
            System.out.println("YES");
        }
        else{
            System.out.println("NO");
        }

    }



    public void doTheSum(int numberOfRowsAndColumnsOfSum, int[][] matrix)
    {
        int sum = 0;
        int rowLength = matrix.length-1;
        int columnLength = matrix[0].length-1;
        for(int i = 0; i < numberOfRowsAndColumnsOfSum; i++)
        {
            for(int j = 0; j < numberOfRowsAndColumnsOfSum; j++)
            {
                int first = matrix[i][j];
                int second = matrix[rowLength-i][columnLength-j];
                int third = matrix[i][columnLength-j];
                int fourth = matrix[rowLength-i][j];
                int y = Math.max(first, second);
                int x = Math.max(third, fourth);
                sum += Math.max(x, y);

            }
        }
        System.out.println(sum);

    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testTestThatDemonstratesAssertions() throws Exception {

    }

    public void testTestThatThing() throws Exception {

        //TestThatThing();
        int[][] matrix = {
                {112, 42, 83, 119},
                {56, 125, 56, 49},
                {15, 78, 101, 43},
                {62, 98, 114, 108}};
        //doTheSum(2, matrix);
        String first = "DDDABC";
        String second = "ABC";
        findTheString(first, second);


    }


    public void testTestTestThatDemonstratesAssertions() throws Exception {

    }

    public void testTestTestThatThing() throws Exception {

    }
}
