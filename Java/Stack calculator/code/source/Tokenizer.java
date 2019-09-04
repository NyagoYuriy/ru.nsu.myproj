package ru.my.source;

import java.io.*;

public class Tokenizer {
    private String filePath;
    private BufferedReader reader;


    public Tokenizer(String path) throws FileNotFoundException {
        filePath = path;
        if (filePath.isEmpty())
            reader = new BufferedReader(new InputStreamReader(System.in));
        else
            reader = new BufferedReader(new FileReader(filePath));
    }


    public String nextLine() throws IOException {
        String line = reader.readLine();
        return line == null ? "" : line;
    }
}
