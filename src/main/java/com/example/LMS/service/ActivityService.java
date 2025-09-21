package com.example.LMS.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.LMS.dto.response.RecentActivityDto;
import com.example.LMS.model.Activity;
import com.example.LMS.model.Member;
import com.example.LMS.repository.ActivityRepository;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository activityRepository;

    public List<RecentActivityDto> getTopTenActivities() {
        List<Activity> activities = activityRepository.findTop10ByOrderByTimestampDesc();
        // Map Activity -> RecentActivityDto
        return activities.stream().map(activity -> {
            RecentActivityDto dto = new RecentActivityDto();
            dto.setId(activity.getId().toString());
            dto.setType(activity.getType());
            dto.setDescription(activity.getDescription());
            dto.setTimestamp(activity.getTimestamp());
            if (activity.getMember() != null) {
                dto.setMemberId(activity.getMember().getId().toString());
                dto.setMemberName(activity.getMember().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }
    public void logActivity(String type, String description, Member member) {
        Activity activity = new Activity();
        activity.setType(type);
        activity.setDescription(description);
        activity.setTimestamp(LocalDateTime.now());
        activity.setMember(member);
        activityRepository.save(activity);
    }
}
