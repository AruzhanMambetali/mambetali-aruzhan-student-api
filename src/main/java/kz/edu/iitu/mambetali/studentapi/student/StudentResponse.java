package kz.edu.iitu.mambetali.studentapi.student;

public record StudentResponse(Long id, String fullName, String email, String program, int year) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(), student.getFullName(), student.getEmail(),
                student.getProgram(), student.getYear());
    }
}
