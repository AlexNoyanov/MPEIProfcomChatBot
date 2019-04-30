package dbService.dao;

import dbService.entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentsDAOImpl implements StudentsDAO {
    private Connection connection;

    public StudentsDAOImpl() {
        connection = ConnectionProvider.getConnection();
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private List<Student> getBySql(String sql) {
        List<Student> list = new ArrayList<>();
        try(Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                Student student = new Student(
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getInt(10)
                );
                student.setId(rs.getInt(1));
                list.add(student);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Student getById(int id) {
        List<Student> list = getBySql("select * from Students where studentId = " + id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Student getByChatId(String chatId) {
        List<Student> list = getBySql("select * from Students where chatId = " +
                "'" + chatId + "'");
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Student getByStudentCertificate(String studentCertificate) {
        List<Student> list = getBySql("select * from Students where studentCertificate = " +
                "'" + studentCertificate + "'");
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Student getByPhoneNumber(String phoneNumber) {
        List<Student> list = getBySql("select * from Students where phoneNumber = " +
                "'" + phoneNumber + "'");
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Student> getAll() {
        return getBySql("select * from Students");
    }

    @Override
    public void insert(Student entity) {
        try(PreparedStatement statement = connection.prepareStatement("insert into Students" +
                " (chatId, firstName, middleName, surName, studentCertificate, phoneNumber," +
                        " learningGroup, address, startYear) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getChatId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getMiddleName());
            statement.setString(4, entity.getSurName());
            statement.setString(5, entity.getStudentCertificate());
            statement.setString(6, entity.getPhoneNumber());
            statement.setString(7, entity.getLearningGroup());
            statement.setString(8, entity.getAddress());
            statement.setInt(9, entity.getStartYear());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next())
                entity.setId(generatedKeys.getInt(1));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Student entity) {
        try(PreparedStatement statement = connection.prepareStatement("update Students set" +
                        " chatId = ?, firstName = ?, middleName = ?, surName = ?, studentCertificate = ?," +
                        " phoneNumber = ?, learningGroup = ?, address = ?, startYear = ? where studentId = ?")) {
            statement.setString(1, entity.getChatId());
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getMiddleName());
            statement.setString(4, entity.getSurName());
            statement.setString(5, entity.getStudentCertificate());
            statement.setString(6, entity.getPhoneNumber());
            statement.setString(7, entity.getLearningGroup());
            statement.setString(8, entity.getAddress());
            statement.setInt(9, entity.getStartYear());
            statement.setInt(10, entity.getId());
            statement.execute();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if(generatedKeys.next())
                entity.setId(generatedKeys.getInt(1));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(int id) {
        try(Statement statement = connection.createStatement()) {
            statement.execute("delete from Students where studentId = " + id);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
