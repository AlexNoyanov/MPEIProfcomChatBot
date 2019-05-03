package dbService.dao;

public class DAOContext {
    private StudentsDAO studentsDAO;

    private DAOContext() {
        studentsDAO = new StudentsDAOImpl();
    }

    private static DAOContext daoContext;

    static {
        daoContext = new DAOContext();
    }

    public static StudentsDAO getStudentsDAO() {
        return daoContext.studentsDAO;
    }
}
