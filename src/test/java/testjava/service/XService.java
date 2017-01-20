package testjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testjava.dao.XDao;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 20.01.17.
 */
@Service
public class XService {
    @Autowired
    XDao dao;

    public String getHelloMessageByService() {
        return dao.getHelloMessageByDao();
    }
}
