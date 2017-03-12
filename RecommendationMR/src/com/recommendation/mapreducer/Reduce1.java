package com.recommendation.mapreducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.recommendation.dto.TextPair;

import java.io.IOException;

/** 
 * Count number of common friends for each pair of user
 * Input in (list of (<user1, user2> as the TextPair, 0/1 as the IntWriteAble))
 * Output is user1\tuser2\t\NumberOfCommonFriends
 **/ 

public  class Reduce1 extends Reducer<TextPair, IntWritable, TextPair, IntWritable> {
    public void reduce(TextPair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0, prod = 1;
        for (IntWritable val : values) {
            sum += val.get();
            prod *= val.get();
        }
        if( prod!=0 ) {
            context.write(key, new IntWritable(sum));
        }
    }
}