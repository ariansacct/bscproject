/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.ac.ut.snl.mrcd;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 *
 * @author arian
 */
public class MyFileInputFormat extends FileInputFormat<Text, ShortestPathTuple>{

    @Override
    public RecordReader<Text, ShortestPathTuple> createRecordReader(InputSplit is, TaskAttemptContext tac) throws IOException, InterruptedException {
        tac.setStatus(is.toString());
        return new MyRecordReader(tac.getConfiguration(), (FileSplit) is);
    }
    
}
