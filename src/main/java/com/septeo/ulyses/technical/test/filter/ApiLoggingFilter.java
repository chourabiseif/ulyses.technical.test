package com.septeo.ulyses.technical.test.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;

@Component
public class ApiLoggingFilter extends OncePerRequestFilter {

    @Value("${logging.custom.path:api-logs.log}")
    private String logPath;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        int statusCode;

        StatusCaptureResponseWrapper wrappedResponse = new StatusCaptureResponseWrapper(response);

        try {
            filterChain.doFilter(request, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            statusCode = wrappedResponse.getStatus();

            String logEntry = String.format(
                    "[%s] %s %s -> %d (%d ms)",
                    Instant.now(),
                    request.getMethod(),
                    request.getRequestURI(),
                    statusCode,
                    duration
            );

            writeToFile(logEntry);
        }
    }

    private void writeToFile(String logEntry) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logPath, true))) {
            writer.println(logEntry);
        } catch (IOException e) {
            e.printStackTrace(); // or log to console
        }
    }
}
