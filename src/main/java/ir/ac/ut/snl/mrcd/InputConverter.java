/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.ac.ut.snl.mrcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author arian
 */
public class InputConverter {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        System.out.println(StringUtils.leftPad("foobar", 10, '*'));
//        RandomAccessFile file = new RandomAccessFile("randomaccesstest.txt", "rw");
//        String aLine = file.readLine();
//        System.out.println(aLine);
        InputConverter ic = new InputConverter();
        ic.convert("input3");
    }
    
    public void convert(String filename) throws FileNotFoundException, UnsupportedEncodingException {
        int paddingSize = 49;   //  50-1; 1 baraye '\n'
        
        File file = new File(filename);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        Scanner scanner = new Scanner(bufferedReader);
        
        PrintWriter printWriter = new PrintWriter(filename + "-converted", "UTF-8");
        
        int n = scanner.nextInt();
        scanner.nextLine();
        
        printWriter.write(StringUtils.leftPad(String.valueOf(n), paddingSize));
        printWriter.write('\n');
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            printWriter.write(StringUtils.leftPad(line, paddingSize));
            printWriter.write('\n');
        }
        
        scanner.close();
        printWriter.close();
    }
}
