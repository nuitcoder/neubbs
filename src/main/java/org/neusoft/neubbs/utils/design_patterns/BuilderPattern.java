package org.neusoft.neubbs.utils.design_patterns;

import java.util.Date;


interface Builder<T> {
    public T build();
}
/**
 * 构造器模式
 */
public class BuilderPattern{
    private final Integer age;
    private final String name;
    private final Date createtime;

    public static class Builder implements org.neusoft.neubbs.utils.design_patterns.Builder{
        private Integer age = null;
        private String name = null;
        private Date createtime = null;

        public Builder(){}

        public Builder age(int age){
            this.age = age;
            return this;
        }
        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder createtime(Date createtime){
            this.createtime = createtime;
            return this;
        }

        @Override
        public Object build() {
           return new BuilderPattern(this);
        }
    }

    /**
     * Constructor
     */
    public BuilderPattern(Builder builder){
        age = builder.age;
        name = builder.name;
        createtime = builder.createtime;
    }


    @Override
    public String toString() {
        return "BuilderPattern{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", createtime=" + createtime +
                '}';
    }

    /**
     * main
     */
    public static void main(String [] args){
        BuilderPattern bp = (BuilderPattern)new BuilderPattern.Builder().age(10).name("学习").createtime(new Date()).build();
        System.out.println(bp.toString());
    }
}
