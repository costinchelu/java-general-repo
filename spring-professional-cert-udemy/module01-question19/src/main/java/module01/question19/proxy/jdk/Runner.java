package module01.question19.proxy.jdk;

import module01.question19.dao.PersonDao;
import module01.question19.dao.PersonDaoImpl;
import module01.question19.ds.Person;

import java.lang.reflect.Proxy;

public class Runner {
    public static void main(String[] args) {
        PersonDao personDao = (PersonDao) Proxy.newProxyInstance(
                PersonDao.class.getClassLoader(), PersonDaoImpl.class.getInterfaces(),
                new PersonDaoInvocationHandler(
                        new PersonDaoImpl()
                )
        );

        Person person = personDao.findById(5);
        personDao.save(person);
    }
}
