/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.snl.mrcd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;
import org.apache.hadoop.util.LineReader;

/**
 *
 * @author arian
 */
public class MyRecordReader extends RecordReader<Text, ShortestPathTuple> {

    private LineRecordReader lineReader;

    private LineRecordReader lineRecordReader;  //  ino cop zadam vase diagnose
    private byte separator = (byte) '\t';   //  ino cop zadam vase diagnose

    private Text innerValue;  //  ino cop zadam vase diagnose

    private Text key; //  ino cop zadam vase diagnose

    private Text value;   //  ino cop zadam vase diagnose

    private LongWritable lineKey;
    private Text lineValue;

    private Text keyRead;
    private ShortestPathTuple valueRead;

    public MyRecordReader() {

    }

    public MyRecordReader(Configuration conf, FileSplit split) {
        lineRecordReader = new LineRecordReader();
        lineReader = new LineRecordReader();
        lineKey = lineReader.getCurrentKey();
        lineValue = lineReader.getCurrentValue();
    }

    @Override
    public void initialize(InputSplit is, TaskAttemptContext tac) throws IOException, InterruptedException {
        lineReader.initialize(is, tac);
        lineRecordReader.initialize(is, tac);
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        System.out.println("INNNNNNNNNNNJJA");
        if(lineReader == null)
            System.out.println("in bhham NULLE");
        if (!lineReader.nextKeyValue()) {
            System.out.println("chera intoooooooooooooo NMEIA");
            return false;
        }
        System.out.println("bebINIM CHIE: "+lineValue.toString());
        String[] pieces = lineValue.toString().split("\\s+");
        String[] shortestPathValueElements = pieces[1].split(",");
        int tid, sid, d;
        double w;
        char s;
        List<Integer> pi = new ArrayList<Integer>();
        List<Integer> al = new ArrayList<Integer>();

        try {
            tid = Integer.parseInt(shortestPathValueElements[0]);
            sid = Integer.parseInt(shortestPathValueElements[1]);
            d = Integer.parseInt(shortestPathValueElements[2]);
            s = shortestPathValueElements[3].charAt(0);
            w = Double.parseDouble(shortestPathValueElements[4]);

            String pathString = shortestPathValueElements[5].substring(1, shortestPathValueElements[5].length() - 1);
            if (pathString.length() > 0) {
                String[] pathArray = pathString.split(";");
                for (String li : pathArray) {
                    pi.add(Integer.parseInt(li));
                }
            }

            String adjString = shortestPathValueElements[6].substring(1, shortestPathValueElements[6].length() - 1);
            if (adjString.length() > 0) {
                String[] adjArray = adjString.split(";");
                for (String li : adjArray) {
                    al.add(Integer.parseInt(li));
                }
            }
        } catch (Exception e) {
            throw new IOException("Vooroodi moshkel dare.");
        }
        
        //  now we know we'll succeed
        keyRead = new Text(pieces[0]);
        valueRead = new ShortestPathTuple(tid, sid, d, s, w, pi, al);
        return true;

//        System.out.println("injjjjjjjjjjaaaaaaa");
//        byte[] line = null;
//        int lineLen = -1;
//        if (lineRecordReader == null)
//            System.out.println("BABA IN NULLLLLE");
//        if (lineRecordReader.nextKeyValue()) {
//            System.out.println("injjjjjjjjjjaaaaaaa222222222222222222");
//            innerValue = lineRecordReader.getCurrentValue();
//            line = innerValue.getBytes();
//            lineLen = innerValue.getLength();
//            System.out.println("injjjjjjjjjjaaaaaa333333333333333333a");
//        } else {
//            System.out.println("injjjjj44444444444444444444444jjjjjaaaaaaa");
//            return false;
//        }
//        if (line == null) {
//            return false;
//        }
////    if (key == null) {
////      key = new Text();
////    }
////    if (value == null) {
////      value = new Text();
////    }
////    int pos = findSeparator(line, 0, lineLen, this.separator);
////    setKeyValue(key, value, line, lineLen, pos);
//        return true;

    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return keyRead;
    }

    @Override
    public ShortestPathTuple getCurrentValue() throws IOException, InterruptedException {
        return valueRead;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return lineReader.getProgress();
    }

    @Override
    public void close() throws IOException {
        lineReader.close();
    }

}
