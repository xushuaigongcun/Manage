package com.example.manage.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xushuai
 * @date 2022年04月04日 19:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MonitorVo implements Serializable {
    private String  os;
    private String cpuInfo;
    private String jvmJavaVersion;
    private String runTime;
    private String jvmHeapMax;

    private String  jvmHeapInit;
    private String jvmHeapUsed;
    private String jvmHeapCommitted;
    private String jvmNonHeapInit;
    private String jvmNonHeapMax;

    private String   jvmNonHeapUsed;
    private String jvmNonHeapCommitted;
    private String cpuUseRate;
    private String ramTotal;
    private String ramUsed;

    private String   diskTotal;
    private String diskUsed;

}
