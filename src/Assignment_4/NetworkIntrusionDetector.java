package Ora5.NetworkIntrusionDetector;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NetworkIntrusionDetector {
    private final int threshold;
    private final long windowMillis;
    private final long cooldownMillis;
    private final ZoneId zone = ZoneId.of("Europe/Belgrade");
    private final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Map<String, Deque<Long>> failTimes = new HashMap<>();
    private final Map<String, LinkedHashSet<String>> ipServers = new HashMap<>();
    private final Map<String, Long> lastFail = new HashMap<>();
    private final Set<String> blacklisted = new HashSet<>();

    public NetworkIntrusionDetector(int threshold, int windowMinutes, int cooldownMinutes) {
        this.threshold = threshold;
        this.windowMillis = windowMinutes * 60_000L;
        this.cooldownMillis = Math.max(0, cooldownMinutes) * 60_000L;
    }

    public String process(String rawLogLine) {
        LogEntry e = parseLine(rawLogLine);
        if (e == null) return null;
        applyCooldownIfEligible(e.ip, e.epochMillis);
        if ("FAIL".equalsIgnoreCase(e.status)) {
            long now = e.epochMillis;
            Deque<Long> q = failTimes.computeIfAbsent(e.ip, k -> new ArrayDeque<>(4));
            q.addLast(now);
            long cutoff = now - windowMillis;
            while (!q.isEmpty() && q.peekFirst() < cutoff) q.pollFirst();
            ipServers.computeIfAbsent(e.ip, k -> new LinkedHashSet<>(4)).add(e.server);
            lastFail.put(e.ip, now);
            if (q.isEmpty()) cleanupIfIdle(e.ip);
            if (q.size() >= threshold && !blacklisted.contains(e.ip)) {
                blacklisted.add(e.ip);
                long spanMillis = Math.max(0, now - q.peekFirst());
                String ts = TS.format(Instant.ofEpochMilli(now).atZone(zone));
                String servers = String.join(", ", ipServers.getOrDefault(e.ip, new LinkedHashSet<>()));
                return "ALERT at " + ts + ":\n" +
                        "IP: " + e.ip + " - " + q.size() + " failed attempts in " + formatSpan(spanMillis) + "\n" +
                        "Servers affected: " + servers + "\n" +
                        "Status: BLACKLISTED";
            }
        } else if ("SUCCESS".equalsIgnoreCase(e.status)) {
            cleanupIfIdle(e.ip);
        }
        return null;
    }

    public boolean isBlacklisted(String ip) {
        applyCooldownIfEligible(ip, System.currentTimeMillis());
        return blacklisted.contains(ip);
    }

    private void applyCooldownIfEligible(String ip, long now) {
        if (cooldownMillis <= 0) return;
        if (!blacklisted.contains(ip)) return;
        Long lf = lastFail.get(ip);
        if (lf == null) return;
        if (now - lf >= cooldownMillis) {
            blacklisted.remove(ip);
            cleanupIfIdle(ip);
        }
    }

    private void cleanupIfIdle(String ip) {
        Deque<Long> q = failTimes.get(ip);
        if (q != null && q.isEmpty() && !blacklisted.contains(ip)) {
            failTimes.remove(ip);
            if (!ipServers.getOrDefault(ip, new LinkedHashSet<>()).isEmpty() && cooldownMillis > 0) return;
            ipServers.remove(ip);
            lastFail.remove(ip);
        }
    }

    private String formatSpan(long ms) {
        long minutes = ms / 60_000L;
        long seconds = (ms % 60_000L) / 1000L;
        if (minutes > 0 && seconds > 0) return minutes + " minutes " + seconds + " seconds";
        if (minutes > 0) return minutes + " minutes";
        return seconds + " seconds";
    }

    private LogEntry parseLine(String s) {
        try {
            String t = s.trim();
            if (t.startsWith("{")) t = t.substring(1);
            if (t.endsWith("}")) t = t.substring(0, t.length() - 1);
            Map<String, String> kv = new HashMap<>();
            for (String part : t.split("\",\\s*\"")) {
                String p = part.replaceAll("^\\s*\"|\"\\s*$", "");
                int idx = p.indexOf("\":");
                if (idx < 0) continue;
                String key = p.substring(0, idx).trim();
                String val = p.substring(idx + 2).trim();
                val = val.replaceAll("^\\s*\"|\"\\s*$", "");
                kv.put(key, val);
            }
            String ts = kv.get("timestamp");
            String ip = kv.get("ip");
            String server = kv.get("server");
            String status = kv.get("status");
            if (ts == null || ip == null || server == null || status == null) return null;
            long epoch = LocalDateTime.parse(ts, TS).atZone(zone).toInstant().toEpochMilli();
            return new LogEntry(epoch, ip, server, status);
        } catch (Exception ex) {
            return null;
        }
    }

    private static final class LogEntry {
        final long epochMillis;
        final String ip, server, status;
        LogEntry(long ms, String ip, String srv, String st) { this.epochMillis = ms; this.ip = ip; this.server = srv; this.status = st; }
    }

    public static void main(String[] args) {
        NetworkIntrusionDetector det = new NetworkIntrusionDetector(3, 10, 15);
        List<String> logs = List.of(
                "{\"timestamp\": \"2025-10-23 14:00:00\", \"ip\": \"192.168.1.100\", \"server\": \"WEB-01\", \"status\": \"FAIL\"}",
                "{\"timestamp\": \"2025-10-23 14:02:00\", \"ip\": \"192.168.1.100\", \"server\": \"WEB-02\", \"status\": \"FAIL\"}",
                "{\"timestamp\": \"2025-10-23 14:03:00\", \"ip\": \"10.0.0.50\", \"server\": \"WEB-01\", \"status\": \"SUCCESS\"}",
                "{\"timestamp\": \"2025-10-23 14:04:00\", \"ip\": \"192.168.1.100\", \"server\": \"WEB-03\", \"status\": \"FAIL\"}",
                "{\"timestamp\": \"2025-10-23 14:05:00\", \"ip\": \"192.168.1.100\", \"server\": \"WEB-01\", \"status\": \"FAIL\"}",
                "{\"timestamp\": \"2025-10-23 14:06:00\", \"ip\": \"10.0.0.75\", \"server\": \"WEB-02\", \"status\": \"FAIL\"}",
                "{\"timestamp\": \"2025-10-23 14:07:00\", \"ip\": \"10.0.0.75\", \"server\": \"WEB-02\", \"status\": \"FAIL\"}",
                "{\"timestamp\": \"2025-10-23 14:08:00\", \"ip\": \"10.0.0.75\", \"server\": \"WEB-03\", \"status\": \"FAIL\"}"
        );
        for (String line : logs) {
            String alert = det.process(line);
            if (alert != null) System.out.println(alert);
        }
        System.out.println("Is 192.168.1.100 blacklisted? " + det.isBlacklisted("192.168.1.100"));
        System.out.println("Is 10.0.0.50 blacklisted? " + det.isBlacklisted("10.0.0.50"));
    }
}
