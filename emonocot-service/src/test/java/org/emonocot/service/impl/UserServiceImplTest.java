/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.service.impl;

import static org.junit.Assert.assertFalse;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.emonocot.model.auth.User;
import org.emonocot.persistence.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 *
 * @author ben
 *
 */
public class UserServiceImplTest {

    /**
     *
     */
    private UserServiceImpl userService;

    /**
     *
     */
    private UserDao userDao;

    /**
     *
     */
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
