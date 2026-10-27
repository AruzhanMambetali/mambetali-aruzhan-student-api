package kz.edu.iitu.mambetali.studentapi.student;

import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponse create(StudentRequest request) {
        var student = new Student(request.fullName(), normalizedEmail(request.email()), request.program(), request.year());
        return StudentResponse.from(repository.save(student));
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findAll() {
        return repository.findAll(Sort.by("id")).stream().map(StudentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public StudentResponse findById(Long id) {
        return StudentResponse.from(repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id)));
    }

    public StudentResponse update(Long id, StudentRequest request) {
        var student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        student.update(request.fullName(), normalizedEmail(request.email()), request.program(), request.year());
        return StudentResponse.from(student);
    }

    public void delete(Long id) {
        var student = repository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        repository.delete(student);
    }

    private String normalizedEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
