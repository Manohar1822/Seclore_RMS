package com.seclore.main.repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.seclore.main.domain.BookingDetails;
import com.seclore.main.domain.BookingSlots;

@Repository
public class BookingSlotsRepository implements BookingSlotsRepositoryInterface {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String ADD_BOOKING_SLOT = "DECLARE @date DATE = ?; " + "DECLARE @startTime Time =? ;"
			+ "DECLARE @endTime Time =? ;" + "DECLARE @roomId INT=?; " + "DECLARE @bookingId INT=?; "
			+ "DECLARE @isSlotActive BIT=1"
			+ "insert into booking_slots(booking_id,slot_id,room_id,is_slot_active) ((select @bookingId, slot_id, @roomId, @isSlotActive from slot_master "
			+ "where ( ((start_time>@startTime) and (end_time<@endTime) and (end_time!='00:00:00')) "
			+ "or ( start_time<=@startTime and end_time>@startTime) "
			+ "or ( start_time<@endTime and (end_time>=@endTime or end_time='00:00:00')) ) " + "and date=@date" + "))";

	private static final String DELETE_BOOKING_SLOT = "DECLARE @bookingId INT=? " + "DECLARE @date DATE = ? "
			+ "DECLARE @startTime Time =? " + "DECLARE @endTime Time =? " + "update booking_slots "
			+ "set is_slot_active=0 " + "where booking_id=@bookingId and slot_id in "
			+ "(select slot_id from slot_master where (start_time>@startTime and end_time<@endTime) and date=@date)";

//	private static final String GET_ALL_BOOKING_SLOTS_BY_BOOKING_ID="select * from booking_slots where booking_id=?";
//	
	private static final String DELETE_BOOKING_SLOT_BY_BOOKING_ID = "update booking_slots " + "set is_slot_active=0 "
			+ "where booking_id=?";

//	private static final String GET_ALL_BOOKING_SLOTS_BY_TIME="DECLARE @date DATE = ?; "
//			+ "DECLARE @startTime Time =? ;"
//			+ "DECLARE @endTime Time =? ;"
//			+ "DECLARE @bookingId INT=?; "
//			+"select * from booking_slots "
//			+ "where booking_id=@bookingId and slot_id in "
//			+ "(select slot_id from slot_master where (start_time>@startTime and end_time<@endTime) and date=@date)";

	@Override
	public int addBookingSlots(BookingDetails bookingDetails, LocalTime startLocalTime, LocalTime endLocalTime,
			LocalDate localDate) {
		// TODO Auto-generated method stub
		System.out.println("in addBookingSlots");
		Date date = Date.valueOf(localDate);
		Time startTime = Time.valueOf(startLocalTime);
		Time endTime = Time.valueOf(endLocalTime);
		int bookingId = bookingDetails.getBookingId();
		int roomId = bookingDetails.getRoom().getRoomId();
		Object[] args = { date, startTime, endTime, roomId, bookingId };
		int count = jdbcTemplate.update(ADD_BOOKING_SLOT, args);

		return count;

	}

	@Override
	public boolean deleteBookingSlots(BookingDetails bookingDetails, LocalTime startLocalTime, LocalTime endLocalTime,
			LocalDate localDate) {
		// TODO Auto-generated method stub
		Date date = Date.valueOf(localDate);
		Time startTime = Time.valueOf(startLocalTime);
		Time endTime = Time.valueOf(endLocalTime);
		int bookingId = bookingDetails.getBookingId();

		Object[] args = { bookingId, date, startTime, endTime };
		int count = jdbcTemplate.update(DELETE_BOOKING_SLOT, args);

		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteBookingSlotsByBookingId(int bookingId) {
		// TODO Auto-generated method stub
		Object[] args = { bookingId };
		int count = jdbcTemplate.update(DELETE_BOOKING_SLOT_BY_BOOKING_ID, args);
		if (count > 0) {
			return true;
		}
		return false;
	}

//	@Override
//	public List<BookingSlots> getAllBookingSlotsByBookingId(int bookingId) {
//		// TODO Auto-generated method stub
//		try {
//		Object[] args= {bookingId};
//		List<BookingSlots> allBookingSlots=jdbcTemplate.query(GET_ALL_BOOKING_SLOTS_BY_BOOKING_ID, new BookingSlotsRowMapper(), args);
//		return allBookingSlots;
//		}catch(Exception e) {
//			return null;
//		}
//	}

//	@Override
//	public List<BookingSlots> getAllBookingSlotsByTime(BookingDetails bookingDetails, LocalTime startLocalTime, LocalTime endLocalTime,
//			LocalDate localDate) {
//		// TODO Auto-generated method stub
//		Date date=Date.valueOf(localDate);
//		Time startTime=Time.valueOf(startLocalTime);
//		Time endTime=Time.valueOf(endLocalTime);
//		int bookingId=bookingDetails.getBookingId();
//		
//		try {
//			Object[] args= {date, startTime, endTime, bookingId};
//			List<BookingSlots> allBookingSlots=jdbcTemplate.query(GET_ALL_BOOKING_SLOTS_BY_TIME, new BookingSlotsRowMapper(), args);
//			return allBookingSlots;
//		}catch(Exception e) {
//			return null;
//		}
//	}

}
