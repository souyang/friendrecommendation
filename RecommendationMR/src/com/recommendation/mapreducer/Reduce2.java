package com.recommendation.mapreducer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* Use the first ID as key to map each line to a key value pair */
import org.apache.hadoop.mapreduce.Reducer;
import com.recommendation.dto.TextIntPair;

/* for each user, find the 10 IDs that have most common friends with the user */
public  class Reduce2 extends Reducer<Text, TextIntPair, Text, Text> {
	private static final int TOPN = 10;
    public void reduce(Text key, Iterable<TextIntPair> values, Context context) throws IOException, InterruptedException {
        List<TextIntPair> topNCommonFriend  = new ArrayList<>();
        
        for (TextIntPair val:values){
        	//Deep copy each value, iterate through value, the same object is used.
        	//If directly add the value to list, all the elements will be the same
            Text first = new Text(val.getFirst());
            IntWritable second = new IntWritable(val.getSecond().get());
            //ensure the initial friend list is TOPN
            if (topNCommonFriend.size()<TOPN){
                topNCommonFriend.add(new TextIntPair(first, second));
                continue;
            }
            // for each new value, check against the original 10, set the value if
            for (int i=0; i<TOPN; i++){
                if(topNCommonFriend.get(i).getSecond().get()<second.get()){
                    topNCommonFriend.set(i, new TextIntPair(first, second)); 
                    break; // just set once if to maintain the top 10 in the loop
                }
            }
        }

        //change the list to '|' separated string
        StringBuilder stringBuilder = new StringBuilder();

        int j;
        for (j=0 ; j < Math.min(topNCommonFriend.size(),11) ;j++) {

            stringBuilder.append(topNCommonFriend.get(j).getFirst()) ;

            if (j < Math.min(topNCommonFriend.size(),11)-1){
                stringBuilder.append("|");}

        };

        context.write(key, new Text(stringBuilder.toString()));
    }
}
