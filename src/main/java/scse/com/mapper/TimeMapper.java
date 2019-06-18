package scse.com.mapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import scse.com.domain.Classroom;
import scse.com.domain.Deadline;
import scse.com.domain.Time;
import scse.com.domain.Week;

@Mapper
public interface TimeMapper {
	
	@Select("select * from tb_time")
	public ArrayList<Time> getTimeList();
	
	@Select("select * from tb_week")
	public ArrayList<Week> getWeekList();

	@Select("select * from tb_deadline")
	public ArrayList<Deadline> getDeadlineList();

	@Select("select * from tb_classroom")
	public ArrayList<Classroom> getClassroomList();
	
	/************************************************************/
	@Select("select * from tb_time where time_id=#{timeId} limit 1")
	public Time getTimeById(int timeId);
	
	@Select("select * from tb_week where week_id=#{weekId} limit 1")
	public Week getWeekById(int weekId);

	@Select("select * from tb_deadline where deadline_id=#{deadlineId} limit 1")
	public Deadline getDeadlineById(int deadlineId);

	@Select("select * from tb_classroom where classroom_id=#{classroomId} limit 1")
	public Classroom getClassroomById(int classroomId);
}
