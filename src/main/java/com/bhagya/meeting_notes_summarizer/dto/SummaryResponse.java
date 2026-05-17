package com.bhagya.meeting_notes_summarizer.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryResponse {
    private String summary;
    private List<String> actionItems;
}
