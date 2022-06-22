package com.example.demo.bat;

import com.example.demo.domain.Dept;
import com.example.demo.domain.Dept2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

//-------------------------------------
// Program arguments : 실행 명령어
// --job.name=jpaPageJob1 date=20220622 v=3
// 실행시 v 정보 변경
// Dept => Dept2 로 이동

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPageJob2 {
    //
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;    //
    // jpa data
    private final EntityManagerFactory entityManagerFactory;


    private int chunkSize = 10; // 순차적으로 단위를 짤라서

    //
    @Bean
    public Job jpaPageJob2_batchBuild(){
        return jobBuilderFactory.get("jpaPageJob2")
                .start(jpaPageJob2_step1())
                .build();
    }

    // step size
    @Bean
    public Step jpaPageJob2_step1(){
        return stepBuilderFactory.get("jpa")
                .<Dept,Dept2>chunk(chunkSize) // input Entity , output Entity
                .reader(jpaPageJob2_dbItemReader())
                .processor(jpaPageJob2_processor())
                .writer(jpaPageJob2_dbItemWriter())
                .build();
    }

    
    //
    private ItemProcessor<Dept, Dept2> jpaPageJob2_processor() {  // read Dept writer Dept2

        return dept ->{
            return new Dept2(dept.getDeptNo(), "NEW_" + dept.getDName(), "NEW_" + dept.getLoc());
        };
    }

    // page reader Item
    @Bean
    public JpaPagingItemReader<Dept> jpaPageJob2_dbItemReader(){
        return new JpaPagingItemReaderBuilder<Dept>()
                .name("jpaPageJob2_dbItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("SELECT d FROM Dept d order by dept_no desc")  // 반드시 sort  // DEPT (x) // 객체이름 query 에서 주의
                .build();
    }

    //
    @Bean
    public JpaItemWriter<Dept2> jpaPageJob2_dbItemWriter(){
        JpaItemWriter<Dept2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }



}
