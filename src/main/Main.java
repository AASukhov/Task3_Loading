package main;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {


    public static void main(String[] args) {

        StringBuilder builder = new StringBuilder();

        /*File res = new File("res");
        folderCreation("res", res, builder);

        File drawables = new File("res//drawables");
        folderCreation("res//drawables", drawables, builder);

        File vectors = new File("res//vectors");
        folderCreation("res//vectors", vectors, builder);

        File icons = new File("res//icons");
        folderCreation("res//icons", icons, builder);

        File src = new File("src");
        folderCreation("src", src, builder);

        File main = new File("src//main");
        folderCreation("src//main", main, builder);

        File fileMain = new File("src//main//Main.java");
        filesCreation("src//main//Main.java", fileMain, builder);

        File fileUtils = new File("src//main//Utils.java");
        filesCreation("src//main//Utils.java", fileUtils, builder);

        File test = new File("src//test");
        folderCreation("src//test", test, builder);

        File saveGames = new File("savegames");
        folderCreation("savegames", saveGames,builder);

        File temp = new File("temp");
        folderCreation("temp", temp, builder);

        File fileTemp = new File("temp//Temp.txt");
        filesCreation("temp//Temp.txt", fileTemp, builder);

        String result = builder.toString();
        try (FileWriter writer = new FileWriter("temp//Temp.txt", false)) {
            writer.write(result);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }*/

        GameProgress gameProgress1 = new GameProgress(100, 2, 1, 1.5);
        GameProgress gameProgress2 = new GameProgress(90, 5, 4, 10.5);
        GameProgress gameProgress3 = new GameProgress(83, 6, 9, 21.6);

        saveGame("savegames/save1.dat", gameProgress1);
        saveGame("savegames/save2.dat", gameProgress2);
        saveGame("savegames/save3.dat", gameProgress3);

        String [] saveFiles = new String[] {"savegames/save1.dat", "savegames/save2.dat", "savegames/save3.dat"};
        List <String> listOfFiles = Arrays.asList(saveFiles);
        zipFiles("savegames/archive.zip", listOfFiles);

        deleteFiles("savegames/save1.dat");
        deleteFiles("savegames/save2.dat");
        deleteFiles("savegames/save3.dat");


        openZip("savegames/archive.zip", "unpacked");

        System.out.println(openProgress("unpacked/save1.dat").toString());
        System.out.println(openProgress("unpacked/save2.dat").toString());
        System.out.println(openProgress("unpacked/save3.dat").toString());
    }

    //public static void folderCreation (String path, File file, StringBuilder builder) {
    //    if (file.mkdir()) {
    //        System.out.println("Каталог " + path + " создан"); // Создание файла
    //        builder.append("Каталог " + file + " создан\n");
    //    }
    //}

    public static void folderCreationWithoutBuilder (String path, File file) {
        if (file.mkdir()) {
            System.out.println("Каталог " + path + " создан"); // Создание папки
        }
    }

    /*public static void filesCreation (String path, File file, StringBuilder builder) {
        try {
            if (file.createNewFile())
                System.out.println("Файл "  + path + " был создан");
                builder.append("Файл " + file + " создан\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }*/


    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String archivePath, List <String> listOfFiles) {
        try {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archivePath));
            for (String file : listOfFiles) {
                File fileToZip = new File(file);
                FileInputStream fis = new FileInputStream(file);
                ZipEntry entry = new ZipEntry(fileToZip.getName());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
                }
            zout.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void deleteFiles (String path) {
        try {
            File file = new File(path);
            if (file.delete()) {
                System.out.println(path + " удален");
            } else {
                System.out.println("Файл " + path + " не удален");
            };
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void openZip(String archiveLocationPath, String archiveOpenPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(archiveLocationPath))) {
            ZipEntry entry;
            String name;
            File openArchive = new File(archiveOpenPath);
            folderCreationWithoutBuilder(archiveOpenPath, openArchive);
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(openArchive.getPath()+ "/" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } return gameProgress;
    }
}
