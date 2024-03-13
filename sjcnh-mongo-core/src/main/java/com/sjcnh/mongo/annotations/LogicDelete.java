package com.sjcnh.mongo.annotations;

/**
 * @author w
 * @description:
 * @title: LogicDelete
 * @projectName sjcnh-mongo-core
 * @date 2023/10/24
 * @company sjcnh-ctu
 */
public @interface LogicDelete {
    /**
     * 不删除的默认值
     *
     * @return int 0
     * @author W
     * @date: 2023/10/24
     */
    int value() default 0;

    /**
     * 删除的标志
     *
     * @return int 1
     * @author W
     * @date: 2023/10/24
     */
    int delVal() default 1;

}
