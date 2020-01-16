package com.cdy.demo.framework.jpa.demo1;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 实体
 *
 * @author HP
 */
@Data
@Entity(name = "TEST")
@ToString
public class Test {
    @Id
    private Integer id;
    @Column
    private String name;

    public Test() {
    }
}
