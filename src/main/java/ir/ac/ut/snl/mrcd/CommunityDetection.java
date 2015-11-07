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
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author arian
 */
public class CommunityDetection {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, Exception {
        /*Graph graph = new Graph("input2.txt");
        PrintWriter printWriter = new PrintWriter("allspts.txt", "UTF-8");
        for (int i = 0; i < graph.getN(); i++) {
            ShortestPathTuple spt = new ShortestPathTuple(i, graph.getData()[i]);
            printWriter.write(spt.getTargetId()+","+spt.getSourceId()+'\t'+spt.getTargetId()+","+spt.getSourceId()+","+spt.getDistance()+
                    ","+spt.getStatus()+","+spt.getWeight()+",[],"+spt.writeAdjList());
            if (i != graph.getN()-1)
                printWriter.write('\n');
        }
        printWriter.close();*/
        
        
        
        int res;
        
        res = ToolRunner.run(new Configuration(), new StageOne(), args);
        if (args.length != 2) {
            System.err.println("Usage: StageOne!!! <input path> <output path>");
        }
        
        res = ToolRunner.run(new Configuration(), new StageTwo(), args);
        if (args.length != 2) {
            System.err.println("Usage: StageTwo!!! <input path> <output path>");
        }
        System.exit(res);
    }
}
