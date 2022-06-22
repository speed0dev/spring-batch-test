package com.example.demo.bat;


import com.example.demo.dto.OneDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TextJob1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static final int chuckSize = 5;

    // 외부에서 job 을 가져오는 형태로 고민해 볼것.
    @Bean
    public Job textJob1_batchJob1(){
        return jobBuilderFactory.get("textJob1_batchJob1")
                .start(textJob1_batchStep1())
                .build();
    }

    @Bean
    public Step textJob1_batchStep1(){
        return stepBuilderFactory.get("textJob1_batchStep1")
                .chunk(chuckSize)
                .reader(textJob1_FileReader())
                .writer(oneDto -> oneDto.stream().forEach(i ->{
                    log.debug("(writer)##" + i.toString());
                }))
                .build();
    }


    // reader
    @Bean
    public FlatFileItemReader<OneDto> textJob1_FileReader(){
        FlatFileItemReader<OneDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new ClassPathResource("sample/textJob1_input.txt"));
        flatFileItemReader.setLineMapper((line, lineNumber)->{
            log.debug(" @@[textJob1_FileReader] LineNumber:" + lineNumber);
            return new OneDto(lineNumber + ")" + line);
        });
        return flatFileItemReader;
    }


}
