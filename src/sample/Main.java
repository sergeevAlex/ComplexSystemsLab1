package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main { // extends Application {

    static double a;
    static int gridParts = 18;
   // @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }



    static double integrateTrapezoidal(PolynomialSplineFunction psf,double a, double b, int N) {
        double h = (b - a) / N;              // step size
        double sum = 0.5 * (psf.value(a) + psf.value(b));    // area
        for (int i = 1; i < N; i++) {
            double x = a + h * i;
            sum = sum + psf.value(x);
        }
        return sum * h;
    }


    public static void main(String[] args) throws IOException {
       // launch(args);
        //double a = 0;
        double aEnd = 18;
        double b = 0;
        double bEnd = 22;

//        double[] y1 = new double[]{17.5,18.5,20.5}; // I
//        double[] y2 = new double[]{15,14.5,15.5}; // II
//        double[] y3 = new double[]{9.5,11,15.5}; // III
//        double[] y4 = new double[]{5.2,5.8,10}; // IV
//        double[] y5 = new double[]{3.2,2.9,2}; // V

        double[][] yValues = new double[5][];

        File dataFile = new File("/Users/alexey/IdeaProjects/ComplexSystemsLab1/src/sample/data.txt");
        ArrayList<Double> gridList = new ArrayList();
        double[] x = new double[]{};

        Scanner sc = new Scanner(dataFile);
     //   sc.useDelimiter(",");
        int iteration = 0;
        String[] strArray = new String[6];
        while (sc.hasNext()) {
            if(iteration == 0) {
                a = Double.parseDouble(sc.nextLine());
            }
            strArray[iteration] = sc.nextLine();
            iteration++;
        }


        for (int i = 0; i < strArray.length; i++) {
            if(i == 0) {
                String[] temp = strArray[i].split(",");
                x = new double[temp.length];
                for (int j = 0; j < temp.length; j++) {
                    x[j] = Double.parseDouble(temp[j]);
                }

            }
            if(i != strArray.length-1) {
                String[] temp = strArray[i+1].split(",");
                yValues[i] = new double[temp.length];
                for (int j = 0; j < temp.length; j++) {
                    yValues[i][j] = Double.parseDouble(temp[j]);
                }
            }
        }


        double step = a/(gridParts);
        double[] gridValuesForX = new double[gridParts+1];
        for (int i = 0; i < gridParts+1; i++) {
            gridValuesForX[i] = i*step;
        }

        double[] areaValues = new double[yValues.length];  //Square values


        SplineInterpolator si = new SplineInterpolator();

        PolynomialSplineFunction[] psfArray = new PolynomialSplineFunction[yValues.length];
        PrintWriter bw = new PrintWriter( new FileWriter("/Users/alexey/IdeaProjects/ComplexSystemsLab1/src/sample/ResultLog/outPutValues.txt"));

        bw.println("GridValues");
        for (double x1 : x) {
            bw.println(x1);
        }
        bw.println("Experiment");
        for (int i = 0; i < yValues.length; i++) {
            psfArray[i] = si.interpolate(x,yValues[i]);

            bw.println("For " + i + " layer");
            for (int j = 0; j < gridValuesForX.length; j++) {
                bw.println(psfArray[i].value(gridValuesForX[j]));
            }
        }
        double previous = 0;
        PrintWriter aw = new PrintWriter( new FileWriter("/Users/alexey/IdeaProjects/ComplexSystemsLab1/src/sample/ResultLog/outPutAreas.txt"));
        for (int i = yValues.length-1; i >= 0; i--) {
            areaValues[i] = integrateTrapezoidal(psfArray[i],0,a,1000);
            if(i!= yValues.length-1){
                areaValues[i] = areaValues[i] - previous;
            }
            previous = integrateTrapezoidal(psfArray[i],0,a,1000);
        }

        for (int i = 0; i < yValues.length; i++) {
        aw.println(areaValues[i]);
        }

        bw.flush();
        aw.flush();

    //    String[] tempArr = strArray[0].split(",");
      //  SplineInterpolator sp = new SplineInterpolator();
  //      PolynomialSplineFunction fc = sp.interpolate(x,);


    }
}
