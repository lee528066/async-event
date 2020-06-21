package com.lee.async.event.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liwei
 * @since 2020-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("event")
public class EventDO implements Serializable {


    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件名称
     */
    private String name;

    /**
     * 唯一key
     */
    private String uniqueKey;

    /**
     * 执行类的命名空间
     */
    private String beanClass;

    private String billContent;

    /**
     * 执行方法
     */
    private String methodName;

    /**
     * 创建时间
     */
    private Date createTime;

    private Date modifyTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 失败原因
     */
    private String errorReason;

    /**
     * 失败次数
     */
    private Integer failCount;

    /**
     * 参数值
     */
    private String paramPairs;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String UNIQUE_KEY = "unique_key";

    public static final String BEAN_CLASS = "bean_class";

    public static final String BILL_CONTENT = "bill_content";

    public static final String METHOD_NAME = "method_name";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String STATUS = "status";

    public static final String ERROR_REASON = "error_reason";

    public static final String FAIL_COUNT = "fail_count";

    public static final String PARAM_PAIRS = "param_pairs";

}
