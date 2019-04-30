package dbService.dao;

import dbService.entity.Student;

import java.util.List;

public interface StudentsDAO extends DAO<Student> {
    Student getByChatId(String chatId);
    Student getByStudentCertificate(String studentCertificate);
    Student getByPhoneNumber(String phoneNumber);
}
