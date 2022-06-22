package com.example.demo.bat;


import com.example.demo.dto.CoinMarket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JsonJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chuckSize = 5;

    @Bean
    public Job jsonJob2_batchBuild(){
        return jobBuilderFactory.get("jsonJob2")
                .start(jsonJob2_batchStep1())
                .build();
    }

    //
    @Bean
    public Step jsonJob2_batchStep1(){
        return stepBuilderFactory.get("jsonJob2_batchStep1")
                .<CoinMarket,CoinMarket>chunk(chuckSize)
                .reader(jsonJob2_jsonReader())
                .processor(jsonJob2_process())
                .writer(jsonJob2_jsonWriter(new FileSystemResource("output/jsonJob2_output.json")))  // /output/jsonJob2_output.json (x)
                .build();
    }

    // if 일부 데이터 처리..
    // null 저장에서 제외
    private ItemProcessor<CoinMarket,CoinMarket> jsonJob2_process() {
        return coinMarket -> {
            if(coinMarket.getMarket().startsWith("KRW-")){
                return new CoinMarket(coinMarket.getMarket(), coinMarket.getKorean_name(), coinMarket.getEnglish_name())
;            }else{
                return null;
            }
        };
    }


    // reader
    @Bean
    public JsonItemReader<CoinMarket> jsonJob2_jsonReader(){
        return new JsonItemReaderBuilder<CoinMarket>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(CoinMarket.class))
                .resource(new ClassPathResource("sample/jsonJob2_input.json"))
                .name("jsonJob2_jsonReader")
                .build();
    }

    // writer
    @Bean
    public JsonFileItemWriter<CoinMarket> jsonJob2_jsonWriter(FileSystemResource resource){
        return new JsonFileItemWriterBuilder<CoinMarket>()
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(resource)
                .name("jsonJob2_jsonWriter")
                .build();
    }


}
