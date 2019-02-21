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
                   output = output + temp;
               }
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
      return output;
   }
}

