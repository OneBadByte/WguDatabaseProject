package com.blackdartq.WguDatabaseProject.FileUtil;

import java.io.*;

public class FileUtil {

   public static String readFromFile(String fileName) {
       BufferedReader reader;
       String output = "";

       {
           try {
               reader = new BufferedReader(new FileReader(fileName));
               String temp = "";
               while ((temp = reader.readLine()) != null){
                   output = output + temp + "\n";
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
      return output;
   }

   public static void appendWriteToFile(String fileName, String text){
       String oldData = readFromFile(fileName);
       System.out.println(oldData);
       oldData = oldData + text;
       try {
           BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
           writer.write(oldData);
           writer.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

}

