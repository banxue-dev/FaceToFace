package com.general.modules.api.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: 录音文件信息
 * @Author LuoJing
 * @Date 2019/10/2817:46
 */
@Entity
@Data
@Table(name = "record_file_info")
public class RecordFileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = RecordFileInfo.Update.class)
    private Long id;

    @Column(name = "channels_id", columnDefinition = "bigint(20) COMMENT '频道ID'")
    private Long channelsId;

    @Column(name = "file_name", columnDefinition = "varchar(50) COMMENT '文件名'")
    private String fileName;

    @CreationTimestamp
    @Column(name = "begin_time", columnDefinition = "datetime COMMENT '文件开始事件'")
    private Date beginTime;

    @CreationTimestamp
    @Column(name = "end_time", columnDefinition = "datetime COMMENT '文件结束事件'")
    private Date endTime;

    public @interface Update {
    }

    public void copy(RecordFileInfo source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
