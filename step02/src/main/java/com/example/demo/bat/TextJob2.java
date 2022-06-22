package com.example.demo.bat;


import com.example.demo.custom.CustomPassThroughLineAggregator;
import com.example.demo.dto.OneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chuckSize = 5;

    // 외부에서 job 을 가져오는 형태로 고민해 볼것.
    @Bean
    public Job textJob2_batchJob1(){
        return jobBuilderFactory.get("textJob2_batchJob1")
                .start(textJob2_batchStep1())
                .build();
    }

    @Bean
    public Step textJob2_batchStep1(){
        return stepBuilderFactory.get("textJob2_batchStep1")
                .chunk(chuckSize)
                .reader(textJob2_FileReader())
                //.processor()
                .writer(textJob2_FileWriter())
                .build();
    }

//    @Bean
//    ItemProcessor<OneDto> textJob2_itemProcessor(){
//        ItemProcessor<OneDto> p = new ItemProcessor() {
//            @Override
//            public Object process(Object item) throws Exception {
//                return null;
//            }
//        };
//        return p;
//    }

    // reader
    @Bean
    public FlatFileItemReader<OneDto> textJob2_FileReader(){
        FlatFileItemReader<OneDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/textJob2_input.txt"));
        flatFileItemReader.setLineMapper((line, lineNumber)->{
            log.debug(" @@[textJob2_FileReader] LineNumber:" + lineNumber);
            return new OneDto(lineNumber + ")" + line);
        });
        return flatFileItemReader;
    }

    @Bean
    public FlatFileItemWriter textJob2_FileWriter(){
        return new FlatFileItemWriterBuilder<OneDto>()
                .name("textJob2_FileWriter")
                .resource(new FileSystemResource("sample/textJob2_output.txt"))  // 외부파일 쓰기
                .lineAggregator(new CustomPassThroughLineAggregator<>())
                .build();

    }


}
