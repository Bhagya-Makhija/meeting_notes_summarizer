# meeting_notes_summarizer
AI-powered meeting notes summarizer that converts meeting transcripts into concise summaries and action items using Google Gemini API.

ENDPOINT
POST /api/summarize

REQUEST
{
  "transcript": "Today we discussed improving API performance..."
}

RESPONSE
{
  "summary": "...",
  "actionItems": [
    "...",
    "..."
  ]
}

Client Request
   ↓
Spring Boot Controller
   ↓
Service Layer
   ↓
Gemini/OpenAI API Call
   ↓
AI Response
   ↓
Parse Output
   ↓
Return JSON Response

Java
Spring Boot
WebClient
Gemini API