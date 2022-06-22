package com.example.demo.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

@SpringBootTest
public class TestDeptRepository {

    @Autowired
    DeptRepository deptRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @Commit
    void createData(){
        System.out.println(">> log test");

        for(int i=1; i<101;i++){
            deptRepository.save(new Dept(i, "dName_" + String.valueOf(i), "loc_" + i));
        }
    }

    // data Insert
    @Test
    @Commit
    void makeMemberDatas(){
        System.out.println("[create data]");
        for(int i=0;i<101;i++){
            memberRepository.save(new Member("ID_"+i, "userNm_"+i, "pass_"+i, "role_"+ i, "Y", "20220622"));
        }
    }
}
