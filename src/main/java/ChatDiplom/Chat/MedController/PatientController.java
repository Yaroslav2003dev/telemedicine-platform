package ChatDiplom.Chat.MedController;

import ChatDiplom.Chat.models.Doctor;
import ChatDiplom.Chat.models.Event;
import ChatDiplom.Chat.models.Patient;
import ChatDiplom.Chat.repo.DoctorRepository;
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

import java.util.List;
import java.util.Optional;

@Controller
public class PatientController {

    @Autowired
    private EventRepository eventRepository;
    private final PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    private final DoctorService doctorService;

    @Autowired
    public PatientController(PatientService patientService, DoctorService doctorService) {
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

//    Поиск врача
    @GetMapping("/patient/doctors/search")
    public String searchDoctors(@RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String middleName,
                                @RequestParam(required = false) String speciality,
                                Model model) {
        // Получаем отфильтрованных врачей
        List<Doctor> doctors = doctorService.findDoctors(lastName, firstName, middleName, speciality);
        if (doctors == null || doctors.isEmpty()) {
            System.out.println("Врачи не найдены!");
        } else {
            System.out.println("Count doctors: " + doctors.size());
        }
        // Добавляем в модель для отображения в шаблоне
        model.addAttribute("doctors", doctors);

        return "patient/doctor-records";  // Имя Thymeleaf-шаблона, где будут отображены врачи
    }

    //Переход в протокол консультации пациента
    @GetMapping("/patient/protocol-cons")
    public String protocolCons(@RequestParam(value = "eventId", required = false) Long eventId,
                               @RequestParam(value = "patientId", required = false) Long patientId,
                               @RequestParam(value = "isNew", required = false, defaultValue = "false") boolean isNew,
                               Model model) {

        // Если просматриваем существующий случай (eventId передан)
        if (eventId == null) {
            return "redirect:/patient"; // Если eventId отсутствует, редирект
        }

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            return "redirect:/patient"; // Если событие не найдено
        }
        Event event = eventOpt.get();
        model.addAttribute("patient", event.getPatient());
        model.addAttribute("isNew", false); // Передаём, что это не создание

        return "patient/protocol-cons";
    }

//Лк пациента
    @GetMapping("/patient")
    public String patientHome(Model model, Authentication authentication) {
        Patient patient = (Patient) authentication.getPrincipal();
        model.addAttribute("patient", patient);
        return "patient/patient-lk";
    }

    @GetMapping("/patient/patient-care-for-patient")
    public String patientService()
    {
        return "patient/patient-care-for-patient";
    }

    @GetMapping("/patient/doctors/{id}")
    public String doctorDetails(@PathVariable(value= "id") long id, Model model) {
        if (!doctorRepository.existsById(id)) {
            return "patient/doctor-records";
        }
        model.addAttribute("doctor", doctorService.getDoctorById(id));
//        НАДО ПОМЕНЯТЬ!!!!!!!
        return "patient/doctor-lk";
    }





//    @GetMapping("/patient/doctors")
//    public String searchDoctors(Model model) {
//
//        return "patient/doctor-records";  // Имя Thymeleaf-шаблона, где будут отображены врачи
//    }

}
