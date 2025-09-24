import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class RainData {
    public ArrayList<String> month = new ArrayList<>();
    public ArrayList<Integer> year = new ArrayList<>();
    public ArrayList<Float> rain = new ArrayList<>();

    RainData() throws FileNotFoundException {
        Scanner fileReader = new Scanner(new FileInputStream("src/rainfall_data.txt"));
        String state = fileReader.nextLine();
        while (fileReader.hasNext()) {
            month.add(fileReader.next());
            year.add(fileReader.nextInt());
            rain.add(fileReader.nextFloat());
        }
    fileReader.close();

    }

    public Float overallAverage() {
        float totAvg = 0;
        for ( int i = 0; i < rain.size(); i++ ) {
            totAvg += rain.get(i);
        }
        return totAvg /= rain.size();
    }

    public ArrayList<String> Averages() {
        ArrayList<String> averages = new ArrayList<>();
        float rainavg = 0;
        for(int i = 0; i < 12; i++){
            for (int j = 0; j < (year.size()/12); j++){
                rainavg += rain.get(i+(12*j));
            }
            rainavg /= (year.size()/12);
            String trunc = String.format("%.2f", rainavg);
            averages.add("The average rainfall amount for " + month.get(i) + " is " + trunc + " inches.");
            rainavg = 0;
        }
        return averages;
    }


void main(String[] args) {
    try {
        PrintWriter pw = new PrintWriter( new FileOutputStream( "src/rainfall_analysis.txt" ) );
        String result = String.format("%.2f", overallAverage());
        pw.println("The overall average rainfall amount is " + result + " inches.");

        for(int i = 0; i < Averages().size(); i++){
            pw.println(Averages().get(i));
        }
        pw.close();
    } catch (FileNotFoundException e) {
        throw new RuntimeException(e);
    }
}
}

