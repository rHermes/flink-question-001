package org.example.prow;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.example.prow.wikimedia.Event;

public class App {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);

        final String fileNameInput = "file:///Users/david/stuff/2020-07.enwiki.2016-04.tsv.bz2";
        final DataStream<String> linesIn = env.readTextFile(fileNameInput).setParallelism(1);

        final SingleOutputStreamOperator<Event> jj = linesIn.map(value -> new Event(value));

        BoundedOutOfOrdernessTimestampExtractor<Event> bounded = new BoundedOutOfOrdernessTimestampExtractor<Event>(Time.hours(1)) {
            @Override
            public long extractTimestamp(Event element) {
                return element.eventTimestamp.toEpochSecond() * 1000;
            }
        };

        final DataStream<Event> props = jj.assignTimestampsAndWatermarks(bounded);

        final KeyedStream<Event, String> praps = props.keyBy(e -> e.eventEntity.toString());

        praps.window(TumblingEventTimeWindows.of(Time.hours(1))).sum("something").print("JAJ!");

        env.execute("FlinkWikipediaHistoryTopEditors");
    }
}
