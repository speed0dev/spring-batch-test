package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.NoRepositoryBean;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OneDto {

    private String one;

    @Override
    public String toString(){
        return one;
    }
}
