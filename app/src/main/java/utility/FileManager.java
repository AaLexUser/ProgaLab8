package utility;

import java.io.*;
import java.util.*;

import com.opencsv.*;
import data.Coordinates;
import data.LocationFrom;
import data.LocationTo;
import data.Route;
import exception.NullEnvException;

/**
 * Класс управляет записью/чтением из файлов
 */
public class FileManager  {
    /**
     * Переменная окружения
     */
    private static String env;

    /**
     * Установить переменную окружения
     * @param env переменная окружения
     */
    public static void setEnv(String env){
        FileManager.env = env;
    }

    public static String getEnv(){
        return env;
    }

    /**
     * Сохранить коллекцию в файл
     */
    public static void saveCollection() throws IOException {
        BufferedWriter bWriter = null;
        CSVWriter writer = null;
        try {
            if (System.getenv(env) == null) throw new NullEnvException();
            OutputStream os = new FileOutputStream(System.getenv(env));
            bWriter = new BufferedWriter(new OutputStreamWriter(os));
            writer= new CSVWriter(bWriter);
            writer.writeAll(CollectionManager.getStringRouteCollection());
        } catch (NullEnvException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            writer.close();
            System.out.println("Коллекция успешно сохранена в " + env + ".");
            CollectionManager.saveTimeCollection();
        }
    }

    /**
     * Загрузить коллекцию из файла
     */
    public static void readCollection(){
        BufferedReader reader = null;
        String[] line = null;
        try {
            if (System.getenv(env) == null) throw new NullEnvException();
            reader = new BufferedReader(new FileReader(System.getenv(env)));
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(',')
                    .withIgnoreQuotations(true)
                    .build();
            CSVReader csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();
            while((line = csvReader.readNext()) != null){
                for (int i = 0; i < line.length; i++){
                    line[i] = line[i].trim().toLowerCase();
                }
                CollectionManager.getRouteCollection().add(new Route(line[0],line[1],
                        new Coordinates(Double.parseDouble(line[2]),Double.parseDouble(line[3])),line[4],
                        new LocationFrom(Integer.parseInt(line[5]),Float.parseFloat(line[6]),Double.parseDouble(line[7])),
                        new LocationTo(Float.parseFloat(line[8]), Long.parseLong(line[9]),line[10]), Long.parseLong(line[11])));
            }
        }
        catch(NullEnvException e){
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                reader.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Выполнить скрипт из файла
     * @param fileName
     */
    public static void readScript(String  fileName){
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            File file = new File(fileName);
            InputStream fileInput = new FileInputStream(file);
            Scanner userScanner = new Scanner(fileInput);
            ConsoleManager.setUserScanner(userScanner);
            while ((line = reader.readLine()) != null) {
                ConsoleManager.interactiveMode();
            }
            userScanner = new Scanner(System.in);
            ConsoleManager.setUserScanner(userScanner);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            try{
                reader.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
}