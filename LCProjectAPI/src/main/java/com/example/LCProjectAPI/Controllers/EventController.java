package com.example.LCProjectAPI.Controllers;

import com.example.LCProjectAPI.Models.Event;
import com.example.LCProjectAPI.Repositories.EventRepository;
import com.example.LCProjectAPI.Service.EventService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    @Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Get all events
    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    // Search events by name containing case-insensitive
    @GetMapping("/search")
    public List<Event> searchEventsByName(@RequestParam(name = "name") String name) {
        return eventRepository.findByEventNameContainingIgnoreCase(name);
    }

    // Get event by id
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent()) {
            return ResponseEntity.ok(eventOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Create a new event
//
    @PostMapping("/events")
    public ResponseEntity<Map<String, String>> createEvent(
            @RequestParam("eventName") String eventName,
            @RequestParam("description") String description,
            @RequestParam("eventDate") String eventDate,
            @RequestParam("eventTime") String eventTime,
            @RequestParam("eventLocation") String eventLocation,
            @RequestParam("eventPrice") Double eventPrice,
            @RequestParam("eventCategory") String eventCategory,
            @RequestParam("zipCode") String zipCode,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Map<String, String> responseBody = new HashMap<>();

        try {
            // Validate required fields
            if (StringUtils.isEmpty(eventName) || StringUtils.isEmpty(description) ||
                    StringUtils.isEmpty(eventDate) || StringUtils.isEmpty(eventTime) ||
                    StringUtils.isEmpty(eventLocation) || eventPrice == null ||
                    StringUtils.isEmpty(eventCategory) || StringUtils.isEmpty(zipCode)) {
                responseBody.put("message", "All fields are required.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
            }

            // Process file if present
            String filePath = null;
            EventService eventService = new EventService();

// Rest of the code...

            eventService.createEvent(eventName, description, eventDate, eventTime,
                    eventLocation, eventPrice, eventCategory, zipCode, filePath);
            if (file != null && !file.isEmpty()) {
                try {
                    filePath = eventService.saveFile(file);
                } catch (IOException e) {
                    responseBody.put("message", "Failed to upload file.");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
                }
            }

            // Save event data
            eventService.createEvent(eventName, description, eventDate, eventTime,
                    eventLocation, eventPrice, eventCategory, zipCode, filePath);

            responseBody.put("message", "Event created successfully!");
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            responseBody.put("message", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
        }
    }
}
