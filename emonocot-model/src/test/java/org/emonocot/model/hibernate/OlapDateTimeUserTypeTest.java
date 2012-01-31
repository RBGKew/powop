package org.emonocot.model.hibernate;

import static org.junit.Assert.fail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.easymock.EasyMock;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class OlapDateTimeUserTypeTest {

    /**
     *
     */
    private OlapDateTimeUserType dateTimeUserType = new OlapDateTimeUserType();

    /**
     *
     */
    private PreparedStatement preparedStatement = null;

    /**
     *
     */
    @Before
    public final void setUp() {
        preparedStatement = EasyMock.createMock(PreparedStatement.class);
    }

    /**
     *
     */
    @Test
    public final void testNullDate() {
        try {
            preparedStatement.setNull(EasyMock.eq(0), EasyMock.eq(Types.INTEGER));
            EasyMock.replay(preparedStatement);
            dateTimeUserType.nullSafeSet(preparedStatement, null, 0);
        } catch (SQLException sqle) {
            fail("No exception expected here");
        }
        EasyMock.verify(preparedStatement);
    }

    /**
    *
    */
   @Test
   public final void testNonNullDate() {
       DateTime dateTime = new DateTime(2012,1,30, 0, 0, 0, 0);
       try {
           preparedStatement.setInt(EasyMock.eq(0), EasyMock.eq(182));
           EasyMock.replay(preparedStatement);
           dateTimeUserType.nullSafeSet(preparedStatement, dateTime, 0);
       } catch (SQLException sqle) {
           fail("No exception expected here");
       }
       EasyMock.verify(preparedStatement);
   }
}
