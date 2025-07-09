
package com.fyp.aps;
import java.util.*;

import org.springframework.web.bind.annotation.*;

import io.micrometer.core.instrument.MeterRegistry;

@RestController
@RequestMapping("/api")
public class SystemMonitorController {

    private final MeterRegistry meterRegistry;

    public SystemMonitorController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @GetMapping("/systemmonitor")
    public Map<String, Object> getSystemMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        metrics.put("cpuUsage", getCpuUsage());
        metrics.put("memoryUsage", getMemoryUsage());

        return metrics;
    }

    private double getCpuUsage() {
        return meterRegistry.find("system.cpu.usage").gauge().value() * 100;
    }

    private String getMemoryUsage() {
    Double used = getGaugeValue("jvm.memory.used");
    Double max = getGaugeValue("jvm.memory.max");

    if (used == null) used = 0.0;
    if (max == null || max <= 0) return String.format("%.2f MB / Unknown", used / (1024 * 1024));

    return String.format("%.2f MB / %.2f MB", used / (1024 * 1024), max / (1024 * 1024));
    }

    private Double getGaugeValue(String metricName) {
        var gauge = meterRegistry.find(metricName).gauge();
        return (gauge != null) ? gauge.value() : null;
    }

}
