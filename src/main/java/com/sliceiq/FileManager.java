package com.sliceiq;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    JSONParser parser = new JSONParser();
    int fileRows = 0, suppressed=0;
    public FileManager(){
        // constructor
    }
    public void printStats(){
        System.out.println( "Rows added:  " + fileRows);
        System.out.println("Records Suppressed:  " + suppressed);
    }

    public void loadFile(String filePath){


        try{
            JSONArray a = (JSONArray) parser.parse( new FileReader(filePath));
//          JSONObject o = (JSONObject) parser.parse( new FileReader(filePath));
//            String src = (String) ((JSONObject) o.get("_source")).get("NPI");
//            System.out.println( src );

            for(Object o : a){
                JSONObject practice = (JSONObject) o ;
                System.out.println( ((JSONObject) practice.get("_source.NPI")).get("NPI"));
            }
        } catch (Exception e){
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public void charAt(String path, int index){
        try {
            FileReader fr = new FileReader(path);
            int i;
            int count=0;
            while((i = fr.read()) !=-1){

                System.out.println((char)i +" "+ count +"\n");
                count++;
                if(count > index + 1){
                    break;
                }
            }
        }
        catch (Exception e){
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    public static Map<String, String>getMapFromCSV(final String filePath) throws IOException{
        Stream<String>lines = Files.lines(Paths.get(filePath));

        Map<String, String> resultMap = lines.map(line -> line.split(",")).collect(Collectors.toMap( line -> line[0].replace(",",""), line-> line[0].replace(",","")));

        lines.close();
        return resultMap;
    }

    public  void suppressFilteredData(final String filePath, Map supressionList) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        BufferedWriter bw = new BufferedWriter((new FileWriter("/Users/calvinmartin/IdeaProjects/klara-app/src/main/resources/output.csv")));
        String line = br.readLine();
        bw.write(line);

        int counter = 0;

        while(line !=null && !line.isEmpty()){
            String[] practices = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

            int length = practices.length;

            // Headings
            if(counter++ == 0){
                bw.write(line);
                bw.newLine();
            }else {
                String supp = practices[58].replace("\"","").replace(",","");

                if(!supressionList.containsKey(supp)){
                    bw.write(line);
                    bw.newLine();
                    this.fileRows++;
                }
                else{
                    this.suppressed++;
                }
            }
            line = br.readLine();
        }
        this.printStats();
    }
}
