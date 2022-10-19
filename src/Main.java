import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final String DATASET_TO_PROCESS = "CDN_A_bw.csv";
    private static final String PATH_TO_DATA_SET_TO_PROCESS = System.getProperty("user.dir") + "/data/";
    private static final int COLUMN_TIMESTAMPS = 0;
    private static final int COLUMN_SERVER_NAME = 1;
    private static final int COLUMN_VALUE = 2;

    public static void main (String[] args) {

        //Input file, containing one line for each value of each server at a certain timestamp
        Scanner datasetFile = null;

        //Map used to store the values per server, at each timestamp
        Map<String, Map<String, Double>> perServer = new HashMap<>();

        //Map used to store the aggregated values of all the server at a certain timestamp
        Map<String, Double> perTimestamp = new HashMap<>();

        try {
            //Open the file where we'll be processing the data
            datasetFile = new Scanner(new File(PATH_TO_DATA_SET_TO_PROCESS + DATASET_TO_PROCESS));

            //Array used to store the current line as three separate elements
            String[] fieldsOfCurrentLine;

            //String used to remember the timestamp we're currently working on (to aggregate all of its values)
            String currentlyTreatedTimestamp = "";

            //Double used to aggregate the values for a certain timestamp
            Double bandwidthForCurrentTimestamp = 0.0;

            //Map used to store the timestamps and values for the said server
            Map mapForThisServer;

            //Process the first line manually
            if (datasetFile.hasNext()) {

                fieldsOfCurrentLine = datasetFile.nextLine().split(",");

                //Processing the current data relative to the timestamps
                currentlyTreatedTimestamp = fieldsOfCurrentLine[COLUMN_TIMESTAMPS];
                bandwidthForCurrentTimestamp += Double.parseDouble(fieldsOfCurrentLine[COLUMN_VALUE]);

                //Processing the current data relative to the servers
                mapForThisServer = perServer.getOrDefault(fieldsOfCurrentLine[COLUMN_SERVER_NAME], new HashMap<>());
                mapForThisServer.put(fieldsOfCurrentLine[COLUMN_TIMESTAMPS], Double.parseDouble(fieldsOfCurrentLine[COLUMN_VALUE]));

                perServer.put(fieldsOfCurrentLine[COLUMN_SERVER_NAME], mapForThisServer);
            }

            //Process the rest of the lines, if any
            while (datasetFile.hasNext()) {
                //Cut the current line into three fields : timestamp, servername, value
                fieldsOfCurrentLine = datasetFile.nextLine().split(",");

                //Processing the current data relative to the servers
                mapForThisServer = perServer.getOrDefault(fieldsOfCurrentLine[COLUMN_SERVER_NAME], new HashMap<>());
                mapForThisServer.put(fieldsOfCurrentLine[COLUMN_TIMESTAMPS], Double.parseDouble(fieldsOfCurrentLine[COLUMN_VALUE]));
                perServer.put(fieldsOfCurrentLine[COLUMN_SERVER_NAME], mapForThisServer);

                //Processing the current data relative to the timestamps
                if (!currentlyTreatedTimestamp.matches(fieldsOfCurrentLine[COLUMN_TIMESTAMPS])) {
                    //If the timestamp has changed, we save the values we've aggregated so far for the previous timestamp
                    //And move on to the next timestamp
                    perTimestamp.put(currentlyTreatedTimestamp, bandwidthForCurrentTimestamp);
                    currentlyTreatedTimestamp = fieldsOfCurrentLine[COLUMN_TIMESTAMPS];
                    bandwidthForCurrentTimestamp += Double.parseDouble(fieldsOfCurrentLine[COLUMN_VALUE]);

                }
                else {
                    //Otherwise we keep aggregating for the current timestamp
                    bandwidthForCurrentTimestamp += Double.parseDouble(fieldsOfCurrentLine[COLUMN_VALUE]);
                }
            }
            //Write a file with the aggregated data per timestamp
            Main.writeFile("dataset_per_timestamps", perTimestamp);

            //Write one file for each server, each file containing a value per timestamp
            for(String currentServerName : perServer.keySet()) {
                Main.writeFile("dataset_for_" + currentServerName, perServer.get(currentServerName));
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            datasetFile.close();
        }

    }

    public static void writeFile(String filename, Map<String, Double> content) {
        try {
            //amap.keySet() returns the list of keys in a random order, so we need some fiddling to get it properly sorted
            List<String> sortedList = new ArrayList<>(content.keySet());
            Collections.sort(sortedList);

            //String used to store the string we want to write in the file
            String stringToWrite = "";

            //Prepare a file output stream, with the filename we defined
            FileOutputStream outputStream = new FileOutputStream(PATH_TO_DATA_SET_TO_PROCESS + filename + ".csv");

            //For each of the keys, we concatenate the timestamp, a separator, and the value
            //We write the string into the file (and print so that we can see what's going on)
            for (String key : sortedList) {
                stringToWrite = key + "," + content.get(key) + "\n";
                outputStream.write(stringToWrite.getBytes());
                System.out.println("Value for " + key + " : " + content.get(key));
            }

            //When done we close the file output stream
            outputStream.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
