package com.example.LMS.controller;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.LMS.dto.response.ChartData;
import com.example.LMS.repository.BookRepository;
import com.example.LMS.repository.ComplaintRepository;
import com.example.LMS.repository.MemberRepository;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/dashboard/charts")
@Tag(name = "Charts", description = "Chart data APIs")
public class ChartController {

    @Autowired private BookRepository bookRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private ComplaintRepository complaintRepository;

    @GetMapping("/books")
    public ResponseEntity<ChartData> getBooksChartData(@RequestParam(defaultValue = "monthly") String period) {
        ChartData chartData = new ChartData();
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();
        
        if ("monthly".equals(period)) {
            // Last 6 months data
            for (int i = 5; i >= 0; i--) {
                YearMonth month = YearMonth.now().minusMonths(i);
                labels.add(month.getMonth().toString().substring(0, 3));
                
                LocalDateTime start = month.atDay(1).atStartOfDay();
                LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);
                Long count = bookRepository.countByCreatedDateBetween(start, end);
                data.add(count);
            }
        } else if ("daily".equals(period)) {
            // Last 7 days
            for (int i = 6; i >= 0; i--) {
                LocalDateTime date = LocalDateTime.now().minusDays(i);
                labels.add(date.getDayOfWeek().toString().substring(0, 3));
                
                LocalDateTime start = date.toLocalDate().atStartOfDay();
                LocalDateTime end = date.toLocalDate().atTime(LocalTime.MAX);
                Long count = bookRepository.countByCreatedDateBetween(start, end);
                data.add(count);
            }
        } else if ("weekly".equals(period)) {
            // Last 4 weeks
            for (int i = 3; i >= 0; i--) {
                LocalDateTime startOfWeek = LocalDateTime.now().minusWeeks(i).with(DayOfWeek.MONDAY);
                LocalDateTime endOfWeek = startOfWeek.plusDays(6);
                
                labels.add("Week " + (4 - i));
                
                LocalDateTime start = startOfWeek.toLocalDate().atStartOfDay();
                LocalDateTime end = endOfWeek.toLocalDate().atTime(LocalTime.MAX);
                Long count = bookRepository.countByCreatedDateBetween(start, end);
                data.add(count);
            }
        }
        
        chartData.setLabels(labels);
        
        ChartData.Dataset dataset = new ChartData.Dataset();
        dataset.setLabel("Books Added");
        dataset.setData(data.stream().map(Number.class::cast).collect(Collectors.toList()));
        dataset.setBackgroundColor(Arrays.asList("#36a2eb", "#ff6384", "#ffce56", "#4bc0c0", "#9966ff", "#ff9f40"));
        
        chartData.setDatasets(Arrays.asList(dataset));
        
        return ResponseEntity.ok(chartData);
    }


    @GetMapping("/members")
    public ResponseEntity<ChartData> getMembersChartData(@RequestParam(defaultValue = "monthly") String period) {
        ChartData chartData = new ChartData();
        
        if ("monthly".equals(period)) {
            List<String> labels = new ArrayList<>();
            List<Long> data = new ArrayList<>();
            
            for (int i = 5; i >= 0; i--) {
                YearMonth month = YearMonth.now().minusMonths(i);
                labels.add(month.getMonth().toString().substring(0, 3));
                
                LocalDateTime start = month.atDay(1).atStartOfDay();
                LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);
                Long count = memberRepository.countByRegistrationDateBetween(start, end);
                data.add(count);
            }
            
            chartData.setLabels(labels);
            
            ChartData.Dataset dataset = new ChartData.Dataset();
            dataset.setLabel("New Members");
            dataset.setData(data.stream().map(Number.class::cast).collect(Collectors.toList()));
            dataset.setBackgroundColor(Arrays.asList("#ff6384", "#36a2eb", "#ffce56", "#4bc0c0", "#9966ff", "#ff9f40"));
            
            chartData.setDatasets(Arrays.asList(dataset));
        }
        
        return ResponseEntity.ok(chartData);
    }

    @GetMapping("/complaints")
    public ResponseEntity<ChartData> getComplaintsChartData(@RequestParam(defaultValue = "monthly") String period) {
        ChartData chartData = new ChartData();
        
        if ("monthly".equals(period)) {
            List<String> labels = new ArrayList<>();
            List<Long> data = new ArrayList<>();
            
            for (int i = 5; i >= 0; i--) {
                YearMonth month = YearMonth.now().minusMonths(i);
                labels.add(month.getMonth().toString().substring(0, 3));
                
                LocalDateTime start = month.atDay(1).atStartOfDay();
                LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);
                Long count = complaintRepository.countBySubmissionDateBetween(start, end);
                data.add(count);
            }
            
            chartData.setLabels(labels);
            
            ChartData.Dataset dataset = new ChartData.Dataset();
            dataset.setLabel("Complaints Filed");
            dataset.setData(data.stream().map(Number.class::cast).collect(Collectors.toList()));
            dataset.setBackgroundColor(Arrays.asList("#ffce56", "#ff6384", "#36a2eb", "#4bc0c0", "#9966ff", "#ff9f40"));
            
            chartData.setDatasets(Arrays.asList(dataset));
        }
        
        return ResponseEntity.ok(chartData);
    }
}
