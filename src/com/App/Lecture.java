package com.App;

import java.util.ArrayList;
import java.util.List;

public class Lecture{
    private String LectureName;
    private String Date;
    private Integer LectureSTime;
    private Integer LectureStdNum;
    private List<Student> std;
    public void SetDate(String Date){
        this.Date = Date;
    }
    public String GetDate(){
        return this.Date;
    }
    public void SetLectureName(String LectureName){
        this.LectureName = LectureName;
    }
    public String GetLectureName(){
        return this.LectureName;
    }
    public void SetLectureSTime(Integer LectureSTime){
        this.LectureSTime = LectureSTime;
    }
    public Integer GetLectureSTime(){
        return this.LectureSTime;
    }
    public void SetLectureStdNum(Integer LectureStdNum){
        this.LectureStdNum = LectureStdNum;
    }
    public Integer GetLectureStdNum(){
        return this.LectureSTime;
    }
    public void SetStudents(List<Student> std){
        this.std = std;
    }
    public List<Student> GetStudents(){
        return this.std;
    }
    public void SetStudent(Student std, int i){
        this.std.set(i, std);
    }
    public Student GetStudent(int i){
        return this.std.get(i);
    }

    public Lecture GetCopy() {
        Lecture lecture = new Lecture();
        lecture.SetLectureName(LectureName);
        lecture.SetLectureSTime(LectureSTime);
        lecture.SetLectureStdNum(LectureStdNum);
        lecture.SetStudents(new ArrayList<Student>());
        for (Student student : std)
            lecture.GetStudents().add(student.GetCopy());
        return lecture;
    }

}
