/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.snl.mrcd;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author arian
 */

public class StageOne extends Configured implements Tool {

    static enum StageOneCounter {
        ALL_ACTIVE
    }

    public static class StageOneMapper extends Mapper<Text, Text, Text, ShortestPathTuple> {
//    public static class StageOneMapper extends Mapper<LongWritable, Text, Text, ShortestPathTuple> {
//    public static class StageOneMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        //  CHERA Mapper<LongWritable, Text, Text, IntWritable> DAR BALA, VA public void
        //map(LongWritable key, Text value, Context context) DAR PAEEN GHALAT BOOD??????!!
        @SuppressWarnings("empty-statement")
        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            String valueString = value.toString();
            String[] records = valueString.split(",");
            int tId = Integer.parseInt(records[0]);
            int sId = Integer.parseInt(records[1]);
            int d = Integer.parseInt(records[2]);
            char s = records[3].charAt(0);
            double w = Double.parseDouble(records[4]);
            String pathString = records[5].substring(1, records[5].length() - 1);
            List<Integer> pi = new ArrayList<Integer>();
            if (pathString.length() > 0) {
                String[] pathArray = pathString.split(";");
                for (String li : pathArray) {
                    pi.add(Integer.parseInt(li));
                }
            }

            List<Integer> al = new ArrayList<Integer>();
            String adjString = records[6].substring(1, records[6].length() - 1);
            if (adjString.length() > 0) {
                String[] adjArray = adjString.split(";");
                for (String li : adjArray) {
                    al.add(Integer.parseInt(li));
                }
            }

            ShortestPathTuple shortestPathTuple = new ShortestPathTuple(tId, sId,
                    d, s, w, pi, al);

//            Text newKey = new Text(tId + "," + sId);
//            //  YA BE JASH,
//            Text newKey;
//            if (tId < sId)
//                newKey = new Text(tId + "," + sId);
//            else
//                newKey = new Text(sId + "," + tId);
            if (shortestPathTuple.status == 'i') {
                context.write(key, shortestPathTuple);
            } else if (shortestPathTuple.status == 'a') {
                shortestPathTuple.status = 'i';
                //  YA BE JASH,
                Text newKey;
//                if (tId < sId) {
////                newKey = new Text(tId + "," + sId);
//                    //  HICHI HAM EMIT NEMIKONIM
//                } else {
//                    newKey = new Text(tId + "," + sId);
//                    context.write(newKey, shortestPathTuple);
//                }
                newKey = new Text(tId + "," + sId);
                    context.write(newKey, shortestPathTuple);
                
                shortestPathTuple.distance++;
                System.out.println("MMMMMMMMMMMMMMMMMMMMMMMMM : " + shortestPathTuple.toString());
                shortestPathTuple.pathInfo.add(shortestPathTuple.targetId);
                 for (Integer node : shortestPathTuple.adjList) {
                    ShortestPathTuple spt = new ShortestPathTuple(node, shortestPathTuple.sourceId,
                            shortestPathTuple.distance, 'a', shortestPathTuple.weight, shortestPathTuple.pathInfo, null);

//                    Text newKey2 = new Text(spt.getTargetId() + "," + spt.getSourceId());
                    //  YA BE JASH,
                    Text newKey2;
                    newKey2 = new Text(spt.getTargetId() + "," + spt.getSourceId());
                        context.write(newKey2, spt);
                }
            }
        }
    }

    public static class StageOneReducer extends Reducer<Text, ShortestPathTuple, Text, ShortestPathTuple> {

        @SuppressWarnings("empty-statement")
        public void reduce(Text key, Iterable<ShortestPathTuple> values, Context context) throws IOException, InterruptedException {
            
            Iterator<ShortestPathTuple> it = values.iterator();
            ArrayList<ShortestPathTuple> lists = new ArrayList<ShortestPathTuple>();
            //Pair<Integer, List<List<Integer>>> minimumPair = new Pair<Integer, List<List<Integer>>>(Integer.MAX_VALUE, null);
            double weightMin = Double.MAX_VALUE;
            int minDistance=Integer.MAX_VALUE;
            //int valCount = 0;
            List<Integer> adList = new ArrayList<Integer>();
            for (ShortestPathTuple value : values) {

                
                System.out.println("xxxxxxxxxxxxbbbbbbbb   "+key.toString() + ",,,,"+value.getStatus()+",,,"+value.getDistance());
                
                
                //if (value.getDistance() > minimumPair.getX()) ; else if (value.distance < minimumPair.getX()) {
                if (value.distance > minDistance) { 
                } else if (value.distance < minDistance) {
                    //minimumPair.setX(value.distance);
                    //List<List<Integer>> list = new ArrayList<List<Integer>>();
                    //list.add(value.getPathInfo());
                    //minimumPair.setY(list);
                    System.out.println("InsideIffffffffffffffffffff   "+key.toString() + ",,,,"+value.getStatus()+",,,"+value.getDistance());
                    minDistance=value.getDistance();
                    weightMin = 1;
                    lists = new ArrayList<ShortestPathTuple>();
                    lists.add(new ShortestPathTuple(value.targetId,value.sourceId,value.distance,value.status,value.weight,value.pathInfo,value.adjList));
                } else if (value.distance == minDistance) {
                    //minimumPair.getY().add(value.getPathInfo());
                    
                    System.out.println("InsideElseeeeeeeeeeeeeeeeee   "+key.toString() + ",,,,"+value.getStatus()+",,,"+value.getDistance());
                    weightMin += 1;
                    lists.add(new ShortestPathTuple(value.targetId,value.sourceId,value.distance,value.status,value.weight,value.pathInfo,value.adjList));
                }
                //valCount++;
            }

            String[] tokens = key.toString().split(",");
            int targetId = Integer.valueOf(tokens[0]);
            int sourceId = Integer.valueOf(tokens[1]);
            //if (! (targetId <= sourceId)){
            
            
            //}
//            for (ShortestPathTuple value : values) {    //  MAJBOORIM INO AVAZ KONIM, CHON DIGE ITERATORESH BARNEMIGARDE SARESH

//            System.out.println("HHHHHHAAAAAAAAAPPPPPPPPPP   " + valCount);
            for (ShortestPathTuple val : lists) {
                ShortestPathTuple current = val;
                System.out.println("ttttttttttttrrrrrrrrrrrrrrrrrrrrr   "+key.toString() + ",,,,"+val.getStatus()+",,,"+val.getDistance());
                List<Integer> myAdjList = new ArrayList<Integer>();

//                RandomAccessFile file = new RandomAccessFile("/home/arian/NetBeansProjects/bscthesis2/src/main/java/ir/ac/ut/snl/mrcd/input3-converted", "rw");
                RandomAccessFile file = new RandomAccessFile("/home/arian/myhadoop/NetBeansProjects/bscthesis2/input3-converted", "rw");
                int pos = (val.getTargetId() + 1) * 50;
                file.seek(pos);
                String aLine = file.readLine();
                file.close();
                String al = aLine.trim();
                String[] alTokens = al.split("\\s+");
                adList.clear();
                for (int i = 1; i < alTokens.length; i++) {
                    adList.add(Integer.parseInt(alTokens[i]));
                }
                

                ShortestPathTuple shortestPathTuple = new ShortestPathTuple(targetId, sourceId, minDistance,
                        current.getStatus(), weightMin, current.getPathInfo(), adList);

                //  IN MOMKENE GHALAT BASHE, BE KHATERE value.getAdjList()
//                context.write(key, shortestPathTuple);
//                System.out.println("CHETOOOOOOOOORRRRRRRRRRI" + key.toString() + " hhhhhhhhhhhhhaaaaaaaaaaaaaaap" + shortestPathTuple);
                if (shortestPathTuple.getStatus() == 'a') {
                    context.getCounter(StageOneCounter.ALL_ACTIVE).increment(1L);
                }

                context.write(key, shortestPathTuple);

            }
        }
    }

    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Job job;
        String input, output;

        int iterationCount = 0;
        long terminationValue = 1;

        boolean result = false;

        while (terminationValue > 0) {
            job = new Job();

            if (iterationCount == 0) {
                input = args[0];
            } else {
                input = args[1] + iterationCount;
            }

            output = args[1] + (iterationCount + 1);

            FileInputFormat.addInputPath(job, new Path(input));
            FileOutputFormat.setOutputPath(job, new Path(output));

            job.setJarByClass(StageOne.class);
            job.setJobName("Stage one");

            job.setInputFormatClass(KeyValueTextInputFormat.class);
            job.setMapperClass(StageOneMapper.class);
            job.setReducerClass(StageOneReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(ShortestPathTuple.class);

            result = job.waitForCompletion(true);

            Counters jobCounters = job.getCounters();
            terminationValue = jobCounters.findCounter(StageOneCounter.ALL_ACTIVE).getValue();
            iterationCount++;
        }

        return 0;
    }

    public static void main(String[] args) throws Exception {

        int res = ToolRunner.run(new Configuration(), new StageOne(), args);
        if (args.length != 2) {
            System.err.println("Usage: StageOne!!! <input path> <output path>");
        }
        System.exit(res);

    }

}
