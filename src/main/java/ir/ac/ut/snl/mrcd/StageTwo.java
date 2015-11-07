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
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 *
 * @author arian
 */
public class StageTwo extends Configured implements Tool {

    public static class StageTwoMapper extends Mapper<Text, Text, Text, DoubleWritable> {

//        public void map(Text key, ShortestPathTuple value, Context context) throws IOException, InterruptedException {
        public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            //  converting Text to ShortestPathTuple
            String valueString = value.toString();
            System.out.println("valuuuuuuuuueeeeeeeee: "+valueString);
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
            
            
            
            System.out.println("too map too map too map");
            double weight = shortestPathTuple.getWeight();
            List<Integer> pathInfo = shortestPathTuple.getPathInfo();
            for (int i = 1; i < pathInfo.size(); i++) {
                int tail = pathInfo.get(i);
                int head = pathInfo.get(i - 1);
                //Edge edge = new Edge(tail, head);
                String edgeString = tail+","+head;
                //context.write(edge, new DoubleWritable(1 / weight));
                context.write(new Text(edgeString), new DoubleWritable(1 / weight));
                System.out.println("nnnnnnnnnnnnaaaaaaaaaaaaaaa");
            }
            
            if (pathInfo.size() > 0) {
                int tail = pathInfo.get(pathInfo.size() - 1);
                int head = tId;
                String edgeString = tail + "," + head;
                context.write(new Text(edgeString), new DoubleWritable(1 / weight));
            }
        }
    }

//    public static class StageTwoReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    public static class StageTwoReducer extends Reducer<Text, DoubleWritable, DoubleWritable, Text> {

        public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
            System.out.println("too reduce too reduce too reduce");
            double sum = 0;
            for (DoubleWritable value : values) {
                sum = sum + value.get();
            }
//            context.write(key, new DoubleWritable(sum));
            context.write(new DoubleWritable(sum), key);
        }
    }

    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("too run too run too run");
        Job job = new Job();
        String input = args[0];
        String output = args[1];
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        job.setJarByClass(StageTwo.class);
        job.setJobName("Stage two");
//        job.setInputFormatClass(MyFileInputFormat.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setMapperClass(StageTwoMapper.class);
        job.setReducerClass(StageTwoReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);
        System.out.println("too run too run too run2222222222222");
        job.waitForCompletion(true);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new StageTwo(), args);
        if (args.length != 2) {
            System.err.println("Usage: StageTwo!!! <input path> <output path>");
        }
        System.exit(res);
    }

}
