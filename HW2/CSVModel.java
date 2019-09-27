package com.jetbrains;

import java.io.*;
import java.util.*;



public class CSVModel
{
    private int columns;
    private List<Object[]> csvContents;

    public CSVModel(File inputFile) {
        csvContents = new ArrayList<>();
        readFile(inputFile);
    }

    public CSVModel(File inputFile, int columns) {
        this.columns = columns;
        csvContents = new ArrayList<>();
        readFile(inputFile);
    }

    protected void readFile(File file) {
        try {
            FileInputStream fileStream = new FileInputStream(file);
            BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(fileStream));
            String line;
            while((line = fileReader.readLine()) != null) {
                csvContents.add((columns != 0 ?
                        line.split(",", columns) : line.split(",")));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage()+
                    "\nFile may be corrupt.");
        }
        clean();
    }

    // Removes unnecessary grammar
    private void clean() {
        for(Object[] content : csvContents) {
            for(int i = 0 ; i < content.length ; i++) {
                content[i] = content[i].toString()
                        .replaceAll("[\", \\s]", "");
            }
        }
    }

    // Returns the files contents.
    public List<Object[]> getModel() {
        return csvContents;
    }
}
