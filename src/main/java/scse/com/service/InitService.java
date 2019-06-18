package scse.com.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import scse.com.domain.Course;
import scse.com.domain.Deadline;
import scse.com.domain.Teacher;
import scse.com.domain.Time;
import scse.com.domain.Week;
import scse.com.domain.in.CourseGroupIn;
import scse.com.domain.in.CourseGroupTime;
import scse.com.mapper.CourseMapper;

@Service
public class InitService {
		@Autowired
		private CourseMapper courseDao;
		@Autowired
		private TimeService timeService;
		@Autowired
		private TeacherService teacherService;
		@Autowired
		private CourseGroupService courseGroupService;
		@Autowired
		private TakeService takeService;
	
		//初始化数据库，
		public void resetDb() {
			//删除take表和group表内容
			courseGroupService.deleteTable();
			takeService.deleteTable();
		}
		
		//批量上传选课信息
		public void uploadCourseGroup() {
			//获取限定条数的Course列20条
			ArrayList<Course> courses=courseDao.getCoursesLimit(546);
			//获取Teacher列
			ArrayList<Teacher> teachers=teacherService.getTeachers();
			//获取Time列
			ArrayList<Time> times=timeService.getTimeList();
			//获取Week列
			ArrayList<Week> weeks=timeService.getWeekList();
			//获取deadline列,大小课期限要相同，如果不同则为单双周的区别
			ArrayList<Deadline> deadlines=timeService.getDeadlineList();
			//人数为30-60随机
			int count=0;
			//开始向tb_select_course表循环添加选课表
			for(Course course:courses) {
				double needCredits=course.getNeedCredits();//课程学分
				int groupNum=getRandom(3)+2; //2-4组
				count=getRandom(31)+30; //40-60人
				for(int i=1;i<=groupNum;i++) {
					CourseGroupIn courseGroupIn=new CourseGroupIn();
					courseGroupIn.setCourseId(course.getCourseId());
					courseGroupIn.setCount(count);
					
					int deadlineBig=0; //期限列的下标0-9
					int deadlineSmall=0;
					if(needCredits==1) { //下标0-2(1-5，6-10，11-15)
						deadlineBig=getRandom(3);
						deadlineSmall=deadlineBig;
					}else if(needCredits==2) { //下标3-6(1-8，9-16，单周,双周,有小课)
						deadlineBig=getRandom(4)+2;
						deadlineSmall=deadlineBig;
						if(deadlineBig==5) { 	 //如果大课是单周，小课则为双周
							deadlineSmall=6;
						}
					}else if(needCredits==3) { //下标7-8(1-13，5-17,有小课)
						deadlineBig=getRandom(2)+7;
						deadlineSmall=deadlineBig;
					}else if(needCredits>3) { //下标9(1-17,有小课)
						deadlineBig=9;
						deadlineSmall=deadlineBig;
					}
					
					int timeBig=getRandom(times.size());
					int timeSmall=getRandom(times.size());
					
					int weekBig=getRandom(weeks.size());
					int weekSmall=getRandom(weeks.size());
					while(timeBig==timeSmall&&weekBig==weekSmall&&deadlineBig!=5) {
						weekSmall=getRandom(weeks.size());
					}
					if(weekBig>weekSmall&&deadlineBig!=5) { //必须大课周在前,分单双周除外
						int temp=weekSmall;
						weekSmall=weekBig;
						weekBig=temp;
					}else if(weekBig==weekSmall&&deadlineBig!=5){ //如果周相等，大课时间必须在前,分单双周除外
						while(timeBig==timeSmall) {
							timeSmall=getRandom(times.size());
						}
						if(timeBig>timeSmall) { //必须大课周在前
							int temp=timeSmall;
							timeSmall=timeBig;
							timeBig=temp;
						}
					}
				//开始检查数据库在同一课程组中是否有冲突时间
				CourseGroupTime courseGroupTime1=new CourseGroupTime(timeBig,weekBig,deadlineBig);
				
				if(courseGroupService.checkCourseRepeat(courseGroupTime1)) {
					i--;
					System.out.println("大课时间冲突");
					continue;
				}
				
				
				CourseGroupTime courseGroupTime2=new CourseGroupTime(timeBig,weekBig,deadlineBig);
				if(courseGroupService.checkCourseRepeat(courseGroupTime2)) {
					i--;
					System.out.println("小课时间冲突");
					continue;
				}
				//检查完成，没有冲突，可以插入
				courseGroupIn.setGroupName("("+i+"班)");
				System.out.println("检查完成，没有冲突，可以插入");
				courseGroupIn.setBigCourseTeacher(teachers.get(getRandom(teachers.size())).getTeacherNo());
				courseGroupIn.setBigCourseTime(times.get(timeBig).getTimeId());
				courseGroupIn.setBigCourseWeek(weeks.get(weekBig).getWeekId());
				courseGroupIn.setBigCourseDeadline(deadlines.get(deadlineBig).getDeadlineId());
				
				courseGroupIn.setSmallCourseTeacher(teachers.get(getRandom(teachers.size())).getTeacherNo());
				courseGroupIn.setSmallCourseTime(times.get(timeSmall).getTimeId());
				courseGroupIn.setSmallCourseWeek(weeks.get(weekSmall).getWeekId());
				courseGroupIn.setSmallCourseDeadline(deadlines.get(deadlineSmall).getDeadlineId());
				//System.out.println(selectCourse);
				courseGroupService.addCourseGroup(courseGroupIn);
				}
			}
			
			
		}
		public int getRandom(int max) {  //0到max-1之间
			return (int) (Math.random() * max);
		}
//		public void uploadSelectCourse() {
//			//先按组获取课程编号
//			ArrayList<String> courseIds=courseGroupService.getCourseGroupCId();
//			//由课程编号搜索相对应的组
//			for(String courseId:courseIds) {
//				ArrayList<Integer> groupIds=courseGroupService.getCourseGroupIdByCId(courseId);
//				SelectCourseIn selectCourseIn=new SelectCourseIn();
//				selectCourseIn.setGroupIdOne(groupIds.get(0));
//				selectCourseIn.setGroupIdTwo(groupIds.get(1));
//				if(groupIds.size()>=3) {
//					selectCourseIn.setGroupIdThree(groupIds.get(2));
//				}
//				if(groupIds.size()>=4) {
//					selectCourseIn.setGroupIdFour(groupIds.get(3));
//				}
//				System.out.println(selectCourseIn);
//				selectCourseService.addSelectCourse(selectCourseIn);
//			}
//			//插入到tb_select_course中
//		}
		//批量上传课程信息
		public  void uploadCourse() {
			FileInputStream in = null;
			try {
				//获取数据源
				in=new FileInputStream(ResourceUtils.getFile("classpath:excl/course.xls"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 //Excel文件
		    HSSFWorkbook wb = null;
			try {
				wb = new HSSFWorkbook(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		    //Excel工作表
		    HSSFSheet sheet = wb.getSheetAt(0);//获取第一张工作表

		    List<Course> courses=new ArrayList<Course>();
		    for(int i=1; i<=sheet.getLastRowNum(); i++) {
		    	Course course=new Course();
		        HSSFRow row = sheet.getRow(i);
		        course.setCourseId(row.getCell(0).getStringCellValue());; //课程id
		        course.setCourseName(row.getCell(1).getStringCellValue()); ; //课程名
		        course.setNeedCredits(Double.valueOf(row.getCell(2).getStringCellValue()));	//学分
		        course.setAssess(row.getCell(3).getStringCellValue());		//考核方式
		        course.setBeforeCourse(row.getCell(4).getStringCellValue()) ;	//先修课程
		        courses.add(course);
		    }
		    System.out.println("共有 " + courses.size() + " 条数据：");
		    long before=System.currentTimeMillis();
		    for(Course course : courses) {
		    	courseDao.insertCourse(course);
		    }
		    System.out.println("录入用时："+(System.currentTimeMillis()-before)+"ms");
		}
//		public static void main(String[] args) {
//			CoursesUpload c=new CoursesUpload();
//			c.upload();
//		}
		
}
