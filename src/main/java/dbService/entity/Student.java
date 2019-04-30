package dbService.entity;

import java.util.Objects;

public class Student extends DataBaseObject {
    private int studentId;
    private String chatId;
    private String firstName;
    private String middleName;
    private String surName;
    private String studentCertificate;
    private String phoneNumber;
    private String learningGroup;
    private String address;
    private int startYear;

    public Student() {}

    public Student(String chatId, String firstName, String middleName, String surName, String studentCertificate, String phoneNumber, String learningGroup, String address, int startYear) {
        this.chatId = chatId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.surName = surName;
        this.studentCertificate = studentCertificate;
        this.phoneNumber = phoneNumber;
        this.learningGroup = learningGroup;
        this.address = address;
        this.startYear = startYear;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getStudentCertificate() {
        return studentCertificate;
    }

    public void setStudentCertificate(String studentCertificate) {
        this.studentCertificate = studentCertificate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLearningGroup() {
        return learningGroup;
    }

    public void setLearningGroup(String learningGroup) {
        this.learningGroup = learningGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId == student.studentId &&
                startYear == student.startYear &&
                Objects.equals(chatId, student.chatId) &&
                Objects.equals(firstName, student.firstName) &&
                Objects.equals(middleName, student.middleName) &&
                Objects.equals(surName, student.surName) &&
                Objects.equals(studentCertificate, student.studentCertificate) &&
                Objects.equals(phoneNumber, student.phoneNumber) &&
                Objects.equals(learningGroup, student.learningGroup) &&
                Objects.equals(address, student.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, chatId, firstName, middleName, surName, studentCertificate, phoneNumber, learningGroup, address, startYear);
    }
}
