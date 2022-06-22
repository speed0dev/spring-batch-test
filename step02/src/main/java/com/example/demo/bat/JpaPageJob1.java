package com.example.demo.bat;

import com.example.demo.domain.Dept;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

//-------------------------------------
// Program arguments : 실행 명령어
// --job.name=jpaPageJob1 date=20220622 v=3
// 실행시 v 정보 변경


@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob1 {
    //
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;    //
    // jpa data
    private final EntityManagerFactory entityManagerFactory;


    private int chunkSize = 10; // 순차적으로 단위를 짤라서

    //
    @Bean
    public Job jpaPageJob1_batchBuild(){
        return jobBuilderFactory.get("jpaPageJob1")
                .start(jpaPageJob1_step1())
                .build();
    }

    // step size
    @Bean
    public Step jpaPageJob1_step1(){
        return stepBuilderFactory.get("jpa")
                .<Dept,Dept>chunk(chunkSize) // input Entity , output Entity
                .reader(jpaPageJob1_dbItemReader())
                .writer(jpaPageJob1_printItemWriter())
                .build();
    }

    // page reader Item
    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob1_dbItemReader(){
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob1_dbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no desc")  // 반드시 sort  // DEPT (x) // 객체이름 query 에서 주의
                .build();
    }

    //
    @Bean
    public ItemWriter<Dept> jpaPageJob1_printItemWriter(){
        return list ->{
            for(Dept d : list){
                log.debug("@@@@ " + d.toString());
            }
        };
    }



}
