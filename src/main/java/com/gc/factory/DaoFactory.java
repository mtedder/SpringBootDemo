package com.gc.factory;

import org.springframework.beans.factory.annotation.Autowired;

import com.gc.dao.Dao;
import com.gc.dao.HibernateDao;
import com.gc.dao.JDBCDao;

/**
 * Created by maurice on 6/27/17.
 */
public class DaoFactory {
    public static final int JDBC = 1;
    private static final int HIBERNATE = 2;

	

    /*
    * Return a concrete instance of the requested dao type
    */
    public Dao getInstance(int daoType) {
        Dao dao = null;//this is bad, change later

        switch (daoType){
            case JDBC:
                //dao = new JDBCDao();
                break;
            case HIBERNATE:
                //dao = new HibernateDao();
                break;
            default:
                //TODO null for now
                break;
        }
        return dao;
    }
}
