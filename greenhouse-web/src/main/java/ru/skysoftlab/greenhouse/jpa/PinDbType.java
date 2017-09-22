package ru.skysoftlab.greenhouse.jpa;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.usertype.UserType;
import org.joda.time.Duration;

import ru.skysoftlab.gpio.DigitalPin;

// TODO перекинуть в skylibs
public class PinDbType implements UserType, Serializable {

	private static final long serialVersionUID = 8008841901166096613L;

	private static final int[] SQL_TYPES = new int[] { Types.VARCHAR };

	@Override
	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	@Override
	public Class<?> returnedClass() {
		return DigitalPin.class;
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		if (x == y) {
			return true;
		}
		if (x == null || y == null) {
			return false;
		}
		Duration dtx = (Duration) x;
		Duration dty = (Duration) y;
		return dtx.equals(dty);
	}

	@Override
	public int hashCode(Object object) throws HibernateException {
		return object.hashCode();
	}

	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		Object pinName = StandardBasicTypes.STRING.nullSafeGet(resultSet, names, session, owner);
		if (pinName == null) {
			return null;
		}
		return new DigitalPin((String) pinName);
	}

	@Override
	public void nullSafeSet(PreparedStatement preparedStatement, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			StandardBasicTypes.STRING.nullSafeSet(preparedStatement, null, index, session);
		} else {
			DigitalPin pin = ((DigitalPin) value);
			StandardBasicTypes.STRING.nullSafeSet(preparedStatement, pin.getName(), index, session);
		}
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object value) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return original;
	}

}
