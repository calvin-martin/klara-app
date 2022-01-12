package com.sliceiq;

import java.util.Map;

public class klaraApp {

        public static void main(String[] args) throws Exception{
            String path = "/Users/calvinmartin/IdeaProjects/klara-app/src/main/resources";
            String filePath = path + "/klara-filtered.csv";
            String suppressionList = path +"/suppressionList.csv";
            FileManager fm = new FileManager();
            Map<String,String>suppList;

            //fm.loadFile(filePath);
            //fm.charAt(filePath,1690);
            suppList = FileManager.getMapFromCSV(suppressionList);

            System.out.println("Suppression List Loaded.....");
            fm.suppressFilteredData(filePath, suppList);
        }

}
