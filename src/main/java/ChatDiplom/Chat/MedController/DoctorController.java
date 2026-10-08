package ChatDiplom.Chat.MedController;

import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.models.Event;
import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.EventRepository;
import ChatDiplom.Chat.repo.PatientRepository;
import ChatDiplom.Chat.service.DoctorService;
import ChatDiplom.Chat.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Controller
public class DoctorController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PatientRepository patientRepository;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }


    // Фильтрация пациентов в картотеки
    @GetMapping("/doctor/patients/search")
    public String searchPatients(@RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String middleName,
                                 @RequestParam(required = false) String birthDate,
                                 Model model) {
        // Обработка даты с проверкой на пустую строку
        LocalDate date = null;
        if (birthDate != null && !birthDate.trim().isEmpty()) {
            try {
                date = LocalDate.parse(birthDate);
            } catch (DateTimeParseException e) {
                System.out.println("Неверный формат даты: " + birthDate);
            }
        }

        // Получаем пациентов
        List<Patient> patients = patientService.findPatients(lastName, firstName, middleName, date);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//        for (Patient patient : patients) {
//            if (patient.getBirthDate() != null) {
                // Форматируем дату рождения и передаем как строку в модель
//                String formattedBirthDate = patient.getBirthDate().format(formatter);
//                patient.setBirthDate(formattedBirthDate);
//            }
//        }

        model.addAttribute("patients", patients);

        return "doctor/patient-records";  // Имя шаблона
    }

    //Переход в ЛК пациента
    @GetMapping("/doctor/patient-lk/{id}")
    public String patientDetails(@PathVariable("id") long id, Model model) {
        if (!patientRepository.existsById(id)) {
            return "doctor/patient-records";
        }
        Optional<Patient> patient = patientRepository.findById(id);
        // Передаём объект, а не список:
        model.addAttribute("patient", patient.orElse(null));
        return "doctor/patient-lk";
    }


//    //Переход в протокол консультации пациента
//    @GetMapping("/doctor/protocol-cons")
//    public String protocolCons(@RequestParam("eventId") long id, Model model) {
//        if (!eventRepository.existsById(id)) {
//            return "doctor/patient-records";
//        }
//
//        Optional<Event> event = eventRepository.findById(id);
//        Event eventData = event.get();
//        Patient patient = eventData.getPatient();
//        // Передаём объект, а не список:
//        model.addAttribute("event",event.orElse(null));
//        model.addAttribute("patient", patient);
//        return "doctor/protocol-cons";
//    }
//
//
//    //Передача id пациента для создания нового случая обслуживания
//    @GetMapping("/doctor/protocol-cons-new")
//    public String protocolConsNew(@RequestParam("patientId") long patientId, Model model) {
//        Optional<Patient> patientOpt = patientRepository.findById(patientId);
//
//        if (patientOpt.isEmpty()) {
//            return "redirect:/doctor/patient-records"; // Если пациента нет, редирект в картотеку
//        }
//
//        Patient patient = patientOpt.get();
//        model.addAttribute("patient", patient);
//
//        return "redirect:/doctor/protocol-cons?patientId=" + patientId + "&isNew=true"; // Передаём isNew через GET
//    }

    //Переход в протокол консультации пациента
    @GetMapping("/doctor/protocol-cons")
    public String protocolCons(@RequestParam(value = "eventId", required = false) Long eventId,
                               @RequestParam(value = "patientId", required = false) Long patientId,
                               @RequestParam(value = "isNew", required = false, defaultValue = "false") boolean isNew,
                               Model model) {
        // Если создаём новый случай (isNew=true), загружаем пациента по patientId
        if (isNew) {
            if (patientId == null) {
                return "redirect:/doctor/patient-records"; // Перенаправление, если patientId отсутствует
            }

            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isEmpty()) {
                return "redirect:/doctor/patient-records";
            }

            model.addAttribute("patient", patientOpt.get());
            model.addAttribute("isNew", true);
            return "doctor/protocol-cons"; // Отображаем страницу в режиме создания
        }

        // Если просматриваем существующий случай (eventId передан)
        if (eventId == null) {
            return "redirect:/doctor/patient-records"; // Если eventId отсутствует, редирект
        }

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            return "redirect:/doctor/patient-records"; // Если событие не найдено
        }
        Event event = eventOpt.get();
        model.addAttribute("patient", event.getPatient());
        model.addAttribute("isNew", false); // Передаём, что это не создание

        return "doctor/protocol-cons";
    }


    @GetMapping("/doctor")
    public String doctorHome(Model model, Authentication authentication){
        Doctor doctor = (Doctor) authentication.getPrincipal();
        model.addAttribute("doctor", doctor);
        return "doctor/doctor-lk";
    }

    @GetMapping("/doctor/patient-care-for-doctor")
    public String patient_care_for_doctor(Model model){
        return "doctor/patient-care-for-doctor";
    };










//    //Картотека
//    @GetMapping("/doctor/patients")
//    public String patients(Model model){
//        Iterable<Patient> patients = patientRepository.findAll();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//        for (Patient entity : patients) {
//            if (entity.getBirthDate() != null) {
//                entity.setFormattedBirthDate(entity.getBirthDate().format(formatter));
//            } else {
//                entity.setFormattedBirthDate("Дата отсутствует");
//            }
//        }
//        model.addAttribute("patients",patients);
//        return "doctor/patient-records";
//    }

}