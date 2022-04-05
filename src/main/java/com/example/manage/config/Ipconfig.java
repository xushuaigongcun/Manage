package com.example.manage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xushuai
 * @date 2022年04月04日 20:38
 */
@Component
public class Ipconfig {
    public static Boolean ipLocal;

    @Value("${ip.local-parsing}")
    public void setIpLocal(Boolean ipLocal) {
        this.ipLocal = ipLocal;
    }
}
