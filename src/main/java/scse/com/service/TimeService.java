package scse.com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scse.com.domain.Classroom;
import scse.com.domain.Deadline;
import scse.com.domain.Time;
import scse.com.domain.Week;
import scse.com.mapper.TimeMapper;

@Service
public class TimeService {
	
	@Autowired
	private TimeMapper timeMapper;
	
	//获取时间列
	public ArrayList<Time> getTimeList(){
		return timeMapper.getTimeList();
	};
	
	//获取周列
	public ArrayList<Week> getWeekList(){
		return timeMapper.getWeekList();
	};
	
	//获取期限列
	public ArrayList<Deadline> getDeadlineList(){
		return timeMapper.getDeadlineList();
	};
	
	//获取教室列
	public ArrayList<Classroom> getClassroomList(){
		return timeMapper.getClassroomList();
	};
}
