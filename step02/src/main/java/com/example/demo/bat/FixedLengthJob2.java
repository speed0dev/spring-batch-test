package com.example.demo.bat;


import com.example.demo.custom.CustomBeanWrapperFieldExtractor;
import com.example.demo.dto.TwoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class FixedLengthJob2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chuckSize = 5; //

    @Bean
    public Job fixedLengthJob2_batchJob1(){
        return jobBuilderFactory.get("fixedLengthJob2")
                .start(fixedLengthJob2_batchStep1())
                .build();
    }


    @Bean
    public Step fixedLengthJob2_batchStep1(){
        return stepBuilderFactory.get("fixedLengthJob2_batchStep1")
                .<TwoDto,TwoDto>chunk(chuckSize)
                .reader(fixeLengthJob2_FileReader())
                .writer(fixeLengthJob2_FileWriter(new FileSystemResource("output/fixedLengthJob2_output.txt")))
                .build();
    }


    // fixedLength
    @Bean
    public FlatFileItemReader<TwoDto> fixeLengthJob2_FileReader(){
        FlatFileItemReader<TwoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("/sample/fixedLengthJob2_input.txt")); // 5 자리수 씩 읽기
        flatFileItemReader.setLinesToSkip(1); // head skip
        DefaultLineMapper<TwoDto> dtoDefaultLineMapper = new DefaultLineMapper<>();

        FixedLengthTokenizer fixedLengthTokenizer = new FixedLengthTokenizer();
        fixedLengthTokenizer.setNames("one", "two");
        fixedLengthTokenizer.setColumns(new Range(1,5), new Range(6,10));


        BeanWrapperFieldSetMapper<TwoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(TwoDto.class);
        dtoDefaultLineMapper.setLineTokenizer(fixedLengthTokenizer);
        dtoDefaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        flatFileItemReader.setLineMapper(dtoDefaultLineMapper);

        return flatFileItemReader;
    }

    //
    @Bean
    public FlatFileItemWriter<TwoDto> fixeLengthJob2_FileWriter(Resource outputResource){
        // Custom 가능
        BeanWrapperFieldExtractor<TwoDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"one", "two"});
        fieldExtractor.afterPropertiesSet();

        FormatterLineAggregator<TwoDto> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFormat("%-5s###%5s");  // 왼쪽 오른쪽 정렬 쓰기
        lineAggregator.setFieldExtractor(fieldExtractor);


        return new FlatFileItemWriterBuilder<TwoDto>()
                .name("fixedLengthJob2_FileWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }

}
