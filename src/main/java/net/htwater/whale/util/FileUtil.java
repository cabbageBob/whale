package net.htwater.whale.util;

import java.io.*;
/**
 * @author shitianlong
 * @since 2018-9-1
 */
public class FileUtil {
    /**
     * save json into .jon file
     * @param json
     * @param filePath the place to save file
     */
    public static void dumpJSONToFile(String json,String filePath){
        File file=new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter fileWriter=new FileWriter(filePath,false);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * delete single file
     * @param filePath
     * @return success:true;failure:false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * read file
     * @param filePath
     * @return string builder
     */
    public static String readFile(String filePath){
        File file = new File(filePath);
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(file));
            String content = "";
            while(content != null){
                content = bf.readLine();
                if(content == null){
                    break;
                }
                sb.append(content.trim());
            }
            bf.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
