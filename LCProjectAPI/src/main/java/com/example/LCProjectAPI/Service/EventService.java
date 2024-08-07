package com.example.LCProjectAPI.Service;

import com.example.LCProjectAPI.Models.Event;
import com.example.LCProjectAPI.Repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class EventService {

    // Directory to save uploaded files
    private final String uploadDir = "uploads/";

    @Autowired
    private EventRepository eventRepository;

    public String saveFile(MultipartFile file) throws IOException {
        // Ensure the directory exists
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = path.resolve(fileName);

        // Save the file
        file.transferTo(filePath.toFile());
        return fileName;
    }

    public void createEvent(String eventName, String description, String eventDate,
                            String eventTime, String eventLocation, Double eventPrice,
                            String eventCategory, String zipCode, String filePath) {
        // Implement event saving logic here
        Event event = new Event();
        event.setEventName(eventName);
        event.setDescription(description);
        event.setEventDate(LocalDate.parse(eventDate));
        event.setEventTime(LocalTime.parse(eventTime));
        event.setEventLocation(eventLocation);
        event.setEventPrice(eventPrice);
        event.setEventCategory(eventCategory);
        event.setZipCode(Integer.valueOf(zipCode));
        event.setFilePath(filePath);
        eventRepository.save(event);
    }
}
