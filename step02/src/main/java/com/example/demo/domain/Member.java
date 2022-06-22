package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TB_MEMBER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @Column(name="MEMBER_ID", length=25)
    String id;

    @Column(name="USER_NM", length = 30)
    String userName;

    String password;

    String roles;
    @Column(name="USE_YN", length = 1)
    String useYn;

    @Column(name="REG_DTM", length = 20)
    String regDtm;
}
