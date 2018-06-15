package com.awais2075gmail.awais2075.classification;

import android.content.Context;

import com.awais2075gmail.awais2075.R;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Muhammad Awais Rashid on 08-Feb-18.
 */

public class Spam {
    private Context context;
    private double p_plus, p_minus;
    private List<String> vocabularyList;
    private String[][] array2D;

    public Spam() {

    }

    public Spam(Context context) {
        this.context = context;
        readTrainedData();
    }


    private void readTrainedData() {
        Scanner scanner = new Scanner(context.getResources().openRawResource(R.raw.trained_file));
        p_plus = Double.parseDouble(scanner.nextLine());
        p_minus = Double.parseDouble(scanner.nextLine());
        vocabularyList = Arrays.asList(scanner.nextLine().split(", "));
        String[] data = scanner.nextLine().split(" ");
        array2D = new String[data.length][];
        int r = 0;
        for (String d : data) {
            array2D[r++] = d.split("\\|");
        }
        scanner.close();
    }

    public boolean getValue(String text) {
        text = text.toLowerCase();
        double pValue = 1, nValue = 1;
        String a[] = text.split(" ");

        for (int i = 0; i < a.length; i++) {
            if (!vocabularyList.contains(a[i])) {
                System.out.println("not found");
                pValue = (1 * pValue);
                nValue = (1 * nValue);
            } else {
                pValue = pValue * (Double.parseDouble(array2D[vocabularyList.indexOf(a[i])][0]));
                nValue = nValue * (Double.parseDouble(array2D[vocabularyList.indexOf(a[i])][1]));

            }

        }

        pValue = pValue * p_plus;
        nValue = nValue * p_minus;



        if (pValue > nValue) {
            return true;
        } else {
            return false;
        }
    }

}
