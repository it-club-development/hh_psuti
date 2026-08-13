package com.example.demo.auth.controller;

import com.example.demo.auth.security.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IpController {

    @Autowired
    private IpUtil ipUtil;

    @GetMapping("/my-ip")
    public ResponseEntity<?> getMyIp(HttpServletRequest request) {
        String ip = ipUtil.getClientIp(request);
        return ResponseEntity.ok(Map.of(
                "ip", ip,
                "timestamp", LocalDateTime.now()
        ));
    }
}
