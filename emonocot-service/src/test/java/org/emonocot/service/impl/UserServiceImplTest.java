package org.emonocot.service.impl;

import static org.junit.Assert.assertFalse;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.emonocot.model.user.User;
import org.emonocot.persistence.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class UserServiceImplTest {
    
    UserServiceImpl userService;
    
    UserDao userDao;
    
    @Before
    public final void setUp() {
        userService = new UserServiceImpl();
        userService.setPasswordEncoder(new Md5PasswordEncoder());
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("getUsername");
        userService.setSaltSource(saltSource);
        userDao = EasyMock.createMock(UserDao.class);
        userService.setUserDao(userDao);
    }

    /**
     *
     */
    @Test
    public final void testEncodePassword() {
        String password = "t35t";
        Capture<User> capture = new Capture<User>();
        User user = new User();
        user.setUsername("test@example.com");
        user.setPassword(password);
        EasyMock.expect(
                userDao.save(EasyMock.and(EasyMock.eq(user),
                        EasyMock.capture(capture)))).andReturn(user);
        EasyMock.replay(userDao);
        userService.createUser(user);
        EasyMock.verify(userDao);

        assertFalse(capture.getValue().getPassword().equals(password));
    }

}
