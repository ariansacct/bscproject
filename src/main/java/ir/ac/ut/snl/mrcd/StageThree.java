/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.ut.snl.mrcd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
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
public class StageThree extends Configured implements Tool {

//    public static class StageThreeMapper extends Mapper<Text, DoubleWritable, Text, DoubleWritable> {
//
//        public void map(Text key, DoubleWritable value, Context context) {
//            //  "no operation is needed"
//        }
//    }
//    public static class StageThreeMapper extends Mapper<Text, Text, Text, DoubleWritable> {
    public static class StageThreeMapper extends Mapper<Text, Text, DoubleWritable, Text> {

//        public void map(Text key, Text value, Mapper.Context context) throws IOException, InterruptedException {
        public void map(Text betweenness, Text vertices, Context context) throws IOException, InterruptedException {
            double d = Double.parseDouble(betweenness.toString());
            System.out.println("sssssssaaaaaaaaaaalam");
            context.write(new DoubleWritable(d), vertices);
            System.out.println("BYYYYYYE");
        }
    }

//    public static class StageThreeReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    public static class StageThreeReducer extends Reducer<DoubleWritable, Text, DoubleWritable, Text> {

//        public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
//            for (DoubleWritable value : values)
//                context.write(key, value);
//        }
        public void reduce(DoubleWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
//            PrintWriter printWriter = new PrintWriter("/home/arian/NetBeansProjects/bscthesis/edgebetweenness", "UTF-8");
            for (Text value : values) {
                context.write(key, value);

            }
        }
    }

    public int run(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Job job = new Job();
        String input = args[0];
        String output = args[1];
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));
        job.setJarByClass(StageThree.class);
        job.setJobName("Stage three");
        job.setMapperClass(StageThreeMapper.class);
        job.setReducerClass(StageThreeReducer.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(DoubleWritable.class);
        job.setOutputKeyClass(DoubleWritable.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setSortComparatorClass(SortDoubleComparator.class);
        
        
        job.waitForCompletion(true);
        
        

        Scanner scanner = null;
        try {
            File file = new File("/home/arian/NetBeansProjects/bscthesis2/output/stagethree/part-r-00000");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            scanner = new Scanner(bufferedReader);
        } catch (Exception e) {
            System.out.println("NA NASHOD NASHOD NASHOD FILE BAZ NASHOD");
            e.printStackTrace();
        }

        PrintWriter printWriter = new PrintWriter("/home/arian/NetBeansProjects/bscthesis2/topkedgebetweenness", "UTF-8");

        int k = 4;
        for (int i = 0; i < k; i++) {
            printWriter.write(scanner.nextLine());
//            if (i != k - 1)
            printWriter.write('\n');
        }
        printWriter.close();
        scanner.close();

        Path inFile = new Path("/home/arian/NetBeansProjects/bscthesis2/topkedgebetweenness");
        Path outFile = new Path("/home/arian/myhadoop/NetBeansProjects/bscthesis2/topkedgebetweenness");
        FileSystem fs = FileSystem.get(new Configuration());
        FSDataInputStream in = fs.open(inFile);
        FSDataOutputStream out = fs.create(outFile);

        int bytesRead = 0;
        byte buffer[] = new byte[256];
        while ((bytesRead = in.read(buffer)) > 0) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();
        out.close();

        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new StageThree(), args);
        if (args.length != 2) {
            System.err.println("Usage: StageThree!!! <input path> <output path>");
        }
        System.exit(res);
    }
}
