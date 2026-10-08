package ChatDiplom.Chat.MedController;

import ChatDiplom.Chat.models.Event;
import ChatDiplom.Chat.models.Patient;

import ChatDiplom.Chat.repo.EventRepository;
import ChatDiplom.Chat.repo.PatientRepository;
import ChatDiplom.Chat.service.EventService;
import ChatDiplom.Chat.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class RestEventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PatientRepository patientRepository;

    // Получить события для пациента по id
    @GetMapping("/events/patient/{patientId}")
    public List<Event> getEventsForPatient(@PathVariable Long patientId) {
        return eventRepository.findByPatientId(patientId);
    }

//    // Получить событие по id
//    @GetMapping("/events/{id}")
//    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
//        return eventRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    // Получить подробную информацию о событии и пациенте
    @GetMapping("/event/{id}")
    public ResponseEntity<Map<String, String>> getEventDetails(@PathVariable("id") Long id) {
        Optional<Event> eventOpt = eventRepository.findById(id);

        if (eventOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Event event = eventOpt.get();
        Patient patient = event.getPatient(); // Получаем пациента из события
        System.out.println("RestEventController"+ event.getPatient());

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Пациент не найден"));
        }

        // Создаем JSON-объект с нужными данными
        Map<String, String> response = new HashMap<>();
        response.put("lastName", patient.getLastname());
        response.put("firstName", patient.getFirstname());
        response.put("middleName", patient.getPatronymic());
        response.put("complaints", event.getComplaints());
        response.put("anamnesisDisease", event.getAnamnesis_Disease());
        response.put("anamnesisLife", event.getAnamnesis_Life());
        response.put("condition", event.getCondition());
        response.put("pulse", event.getPulse());
        response.put("recommendations", event.getRecommendations());
        response.put("conclusion", event.getConclusion());
        response.put("dateTime", event.getFormattedDateTime());

        return ResponseEntity.ok(response);
    }

    // Обновление данных события
    @PutMapping("/event/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") Long id, @RequestBody Map<String, String> updates) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = eventOpt.get();

        // Обновляем поля, если они переданы в запросе
        if (updates.containsKey("complaints")) {
            event.setComplaints(updates.get("complaints"));
        }
        if (updates.containsKey("anamnesisDisease")) {
            event.setAnamnesis_Disease(updates.get("anamnesisDisease"));
        }
        if (updates.containsKey("anamnesisLife")) {
            event.setAnamnesis_Life(updates.get("anamnesisLife"));
        }
        if (updates.containsKey("condition")) {
            event.setCondition(updates.get("condition"));
        }
        if (updates.containsKey("pulse")) {
            event.setPulse(updates.get("pulse"));
        }
        if (updates.containsKey("recommendations")) {
            event.setRecommendations(updates.get("recommendations"));
        }
        if (updates.containsKey("conclusion")) {
            event.setConclusion(updates.get("conclusion"));
        }

        // Сохраняем изменения
        Event updatedEvent = eventRepository.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    // Удаление события
    @DeleteMapping("/event/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Новый метод: Создание нового случая обслуживания
    @PostMapping("/event")
    public ResponseEntity<Event> createEvent(@RequestBody Map<String, Object> requestData) {
        // Проверяем наличие поля patientId
        if (!requestData.containsKey("patientId")) {
            return ResponseEntity.badRequest().build();
        }

        Object patientIdObj = requestData.get("patientId");
        Long patientId;
        try {
            if (patientIdObj instanceof Number) {
                patientId = ((Number) patientIdObj).longValue();
            } else if (patientIdObj instanceof String) {
                patientId = Long.parseLong((String) patientIdObj);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        // Проверяем, существует ли пациент
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Patient patient = patientOpt.get();

        // Создаем новый объект события
        Event newEvent = new Event();
        newEvent.setPatient(patient);
        newEvent.setComplaints((String) requestData.getOrDefault("complaints", ""));
        newEvent.setAnamnesis_Disease((String) requestData.getOrDefault("anamnesisDisease", ""));
        newEvent.setAnamnesis_Life((String) requestData.getOrDefault("anamnesisLife", ""));
        newEvent.setCondition((String) requestData.getOrDefault("condition", ""));
        newEvent.setPulse((String) requestData.getOrDefault("pulse", ""));
        newEvent.setRecommendations((String) requestData.getOrDefault("recommendations", ""));
        newEvent.setConclusion((String) requestData.getOrDefault("conclusion", ""));

        Event savedEvent = eventRepository.save(newEvent);
        return ResponseEntity.ok(savedEvent);
    }

}