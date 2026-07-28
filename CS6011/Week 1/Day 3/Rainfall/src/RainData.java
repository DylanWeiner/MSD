//Created by Dylan Weiner for CS 6011
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RainData {
    private ArrayList<String> month = new ArrayList<>();
    private ArrayList<Integer> year = new ArrayList<>();
    private ArrayList<Float> rain = new ArrayList<>();

    //Opens supplied file to scan through it.
    RainData(String fileName) throws FileNotFoundException {
        Scanner fileReader = new Scanner(new FileInputStream(fileName));
        String state = fileReader.nextLine();
        while (fileReader.hasNext()) {
            month.add(fileReader.next());
            year.add(fileReader.nextInt());
            rain.add(fileReader.nextFloat());
        }
        fileReader.close();
    }

    // Finds overall average for rainfall.
    public Float calculateOverallAverage() {
        float totAvg = 0;
        for(int i = 0;i < rain.size();i++){
            totAvg += rain.get(i);
        }
        return totAvg /= rain.size();
    }

    //Finds average rainfall for each month.
    public ArrayList<String> calculateMonthlyAverage() {
        ArrayList<String> averages = new ArrayList<>();
        float sumRainfall = 0;
        //Using nested loops to grab every value for all 12 months.
        for(int i = 0; i < 12; i++){
            for (int j = 0; j < (year.size()/12); j++){
                sumRainfall += rain.get(i+(12*j));
            }
            sumRainfall /= (year.size()/12);
            String monthlyRainfallAverage = String.format("%.2f", sumRainfall);
            averages.add("The average rainfall amount for " + month.get(i) + " is " + monthlyRainfallAverage + " inches.");
            sumRainfall = 0;
        }
        return averages;
    }


public static void main(String[] args) {
    try {
        RainData rd = new RainData("src/rainfall_data.txt");
        PrintWriter pw = new PrintWriter( new FileOutputStream( "src/rainfall_analysis.txt" ) );
        String result = String.format("%.2f", rd.calculateOverallAverage());
        pw.println("The overall average rainfall amount is " + result + " inches.");

        ArrayList<String> monthlyAverage = rd.calculateMonthlyAverage();
        for(int i = 0; i < monthlyAverage.size(); i++){
            pw.println(monthlyAverage.get(i));
        }
        pw.close();
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
}
}

