///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package ir.ac.ut.snl.mrcd;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Scanner;
//
///**
// *
// * @author arian
// */
//public class Graph {
//    int n;
//    List<Integer>[] data;
//
//    public int getN() {
//        return n;
//    }
//
//    public List<Integer>[] getData() {
//        return data;
//    }
//    
////    public Graph(String filename) throws FileNotFoundException {
////        
////        File file = new File(filename);
////        FileReader fileReader = new FileReader(file);
////        BufferedReader bufferedReader = new BufferedReader(fileReader);
////        Scanner scanner = new Scanner(bufferedReader);
////        n = scanner.nextInt();
////        scanner.nextLine();         //clear the line
////        
////        data = new List[n];
////        for (int i = 0; i < n; i++)
////            data[i] = new ArrayList<Integer>();
////        
////        while (scanner.hasNextLine()) {
////            String line = scanner.nextLine();
////            String[] tokens = line.split("\\s+");
////            data[Integer.valueOf(tokens[0])].add(Integer.valueOf(tokens[1]));
////            data[Integer.valueOf(tokens[1])].add(Integer.valueOf(tokens[0]));
////        }
////        
////        System.out.println("Graph was built.");
////    }
//    
//    public Graph(String filename) throws FileNotFoundException {
//        
//        File file = new File(filename);
//        FileReader fileReader = new FileReader(file);
//        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        Scanner scanner = new Scanner(bufferedReader);
//        n = scanner.nextInt();
//        scanner.nextLine();         //clear the line
//        
//        data = new List[n];
//        for (int i = 0; i < n; i++)
//            data[i] = new ArrayList<Integer>();
//        
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            String[] tokens = line.split("\\s+");
//            data[Integer.valueOf(tokens[0])].add(Integer.valueOf(tokens[1]));
//            data[Integer.valueOf(tokens[1])].add(Integer.valueOf(tokens[0]));
//        }
//        
//        System.out.println("Graph was built.");
//    }
//}
