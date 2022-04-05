package com.example.manage.controller.monitor;

import com.example.manage.service.monitor.MonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xushuai
 * @date 2022年04月04日 20:12
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统-服务监控管理")
@RequestMapping("/api/monitor")
public class MonitorController {
    @Autowired
    private  MonitorService serverService;

    @GetMapping
    @ApiOperation("查询服务监控")
    @PreAuthorize("@el.check('monitor:list')")
    public ResponseEntity<Object> queryMonitor(){
        return new ResponseEntity<>(serverService.getServers(), HttpStatus.OK);
    }
}
