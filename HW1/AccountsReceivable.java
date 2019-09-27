package com.jetbrains;

 import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class AccountsReceivable {
    private LinkedHashMap<Customer, Record> accounts;
    private File master, record;

    public AccountsReceivable(File master, File record) {
        this.accounts = new LinkedHashMap<>();
        this.master = master;
        this.record = record;
        readMasterFile();
    }

    private void readMasterFile() {
        try {
            FileInputStream masterStream = new FileInputStream(master);
            BufferedReader mFileReader = new BufferedReader(
                    new InputStreamReader(masterStream));
            String currentLine;
            while ((currentLine = mFileReader.readLine()) != null) {
                parseCustomerRecords(parseCustomerInfo(currentLine));
            }
            mFileReader.close();
        } catch (Exception ex) {
            System.out.println("Master file may be corrupt or" +
                    " formatted incorrectly.\n");
        }
    }

    private Customer parseCustomerInfo(String line) {
        String contents[] = line.split("\t", 3);

        return new Customer(contents[1].replaceAll("\t", ""),
                Integer.parseInt(contents[0].replaceAll("[\t, \\s]", "")),
                Double.parseDouble(contents[2].replaceAll("[\t, \\$, \\s]", "")));
    }

    private void parseCustomerRecords(Customer customer) {
        Record record = new Record(customer);
        Pattern p = Pattern.compile("[\t, \\s]");

        try {
            FileInputStream recordStream =
                    new FileInputStream(this.record.toString());
            BufferedReader recordReader = new BufferedReader(
                    new InputStreamReader(recordStream));
            String customerLine, recordLine;

            while ((customerLine = recordReader.readLine()) != null) {
                if (customerLine.equals("")) continue;
                if (Character.isDigit(customerLine.charAt(0))) {
                    if (Integer.parseInt(customerLine) == customer.getId()) {
                        while ((recordLine = recordReader.readLine()) != "") {
                            String contents[];
                            if (recordLine == null || recordLine.equals("")) {
                                break;
                            } else if (recordLine.charAt(0) == 'O') {
                                contents = recordLine.split(",", 4);
                                record.addOrder(
                                        contents[1].replaceAll("\\t", ""),
                                        Integer.parseInt(contents[2].replaceAll(p.toString(), "")),
                                        Double.parseDouble(contents[3].replaceAll(p + "\\$", "")));
                            } else if (recordLine.charAt(0) == 'P') {
                                contents = recordLine.split(",", 2);
                                record.addPayment(
                                        Double.parseDouble(contents[1].replaceAll(p + "\\$", "")));
                            }
                        }
                        accounts.put(customer, record);
                        recordReader.close();
                        return;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.out.println("Records file may be corrupt or" +
                    " formatted incorrectly.");
        }
    }

    public LinkedHashMap<Customer, Record> getAccounts() {
        return accounts;
    }

    public int customerCount() {
        return accounts.size();
    }

    public void updateFiles(File master, File record) {
        this.master = master;
        this.record = record;
        readMasterFile();
        accounts = new LinkedHashMap<>();
    }
}
