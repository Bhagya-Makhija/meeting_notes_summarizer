package com.bhagya.meeting_notes_summarizer.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhagya.meeting_notes_summarizer.dto.SummaryResponse;
import com.bhagya.meeting_notes_summarizer.dto.TranscriptRequest;
import com.bhagya.meeting_notes_summarizer.service.SummarizationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class SummaryController {

    private final SummarizationService summarizationService;
    // POST /summarize
    @PostMapping("/summarize")
    public SummaryResponse summarize(
            @RequestBody TranscriptRequest request) 
    {
        
        return summarizationService.summarize(request);
    }
}
