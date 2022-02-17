package com.App;

import java.io.*;
import java.util.*;

public class AttendanceProcessing {
    private HashMap<String, List<Lecture>> LectureDB = new HashMap<String, List<Lecture>>();
    public List<Lecture> GetLecture(String Lecture){
        System.out.println(Lecture.substring(0, Lecture.length()-5));
        String ClassName = Lecture.substring(0, Lecture.length()-5);
        List<Lecture> l = this.LectureDB.get(ClassName);
        return l;
    }

    public void SetStartTime(String className,String time){
        String ClassName = className.substring(0, className.length()-5);
        for(int i = 1; i < this.LectureDB.get(ClassName).size(); i++){
            if(this.LectureDB.get(ClassName).get(i).equals(className)){
                Lecture l = this.LectureDB.get(ClassName).get(i).GetCopy();
                l.SetLectureSTime(Integer.parseInt(time));
                //this.LectureDB.get(ClassName).get(i).SetLectureSTime();
            }
        }
    }

    public Boolean AppendLecture(String dirPath, String dirName) {
        List<Lecture> l = new ArrayList<Lecture>();
        FileProcessing f = new FileProcessing(dirPath, dirName);
        dirName = dirName.replace(".csv", "");
        if (LectureDB.containsKey(dirName)){ // 이미 존재하는 수업 파일을 입력했을 경우 메시지
            System.out.println("이미 존재하는 파일입니다");
            return Boolean.FALSE;
        }
        Lecture tl;
        tl = f.ReadStudentFile();
        LectureDB.put(dirName, l);
        LectureDB.get(dirName).add(tl);
        return Boolean.TRUE;
    }
    public List<Student> SetStudentAttd(Lecture l){
        Lecture lc = l.GetCopy();
        List<Student> s = lc.GetStudents();
        for (int i = 0; i < s.size(); i++) {
            if (s.get(i).GetStartTime() != null) {
                if (s.get(i).GetStartTime() > l.GetLectureSTime() + 10 || s.get(i).GetName() == null) { // 수업시작 후 10분 초과
                    s.get(i).SetAttdLate(0);
                } else if (s.get(i).GetStartTime() <= l.GetLectureSTime() + 10 && s.get(i).GetStartTime() > l.GetLectureSTime()) {
                    s.get(i).SetAttdLate(100 * 3 / 4);
                } else {
                    s.get(i).SetAttdLate(100);
                }
            }
        }
        return s;
    }
    public Boolean AppendStudent(String dirPath, String dirName) {
        String FileName = dirName.substring(0, dirName.length()-9);

        if (LectureDB.containsKey(FileName)){
            FileProcessing f = new FileProcessing(dirPath, dirName);
            LectureDB.get(FileName).add(f.ReadAttendanceFile(dirName,LectureDB.get(FileName).get(0)));
            //ReturnTotrashCSV("trash"+FileName,f.GettrashList());
        }else{ // 해당하는 수업이 없을 때 오류 처리
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    public void ProcessingLectureRate(String lecture){
        List<Lecture> l = LectureDB.get(lecture);
        float a = 0;
        int Lecture;
        if(l == null)
            return;
        List<Student> standard = l.get(0).GetStudents();
        for(int Student = 0; Student < standard.size(); Student++){
            a = 0;
            Lecture = 1;
            for(Lecture = 1; Lecture < l.size(); Lecture++){
                a = a + (l.get(Lecture).GetStudent(Student).GetAttdLate());
            }
            l.get(Lecture-1).GetStudent(Student).SetTotalAttd(a);
        }
    }

    public String ConvertShape(int n){
        if(n == 75){
            return "/";
        }
        if (n == 100){
            return "O";
        }
        if (n == 0){
            return "X";
        }
        return "X";
    }

    public void ReturnToCSV() {
        float a = 0;
        float a1 = 0;
        int Lecture;
        Iterator<String> keys = LectureDB.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            try (PrintWriter writer = new PrintWriter(new File(key+".csv"))) {
                StringBuilder sb = new StringBuilder();
                sb.append("학번");
                sb.append(",");
                sb.append("이름");
                sb.append(",");
                List<Lecture> l = LectureDB.get(key);
                for (int i = 1; i < l.size(); i++) {
                    sb.append(l.get(i).GetDate());
                    sb.append(",");
                }
                sb.append("\n");
                List<Student> standard = l.get(0).GetStudents();
                for(int Student = 0; Student < standard.size(); Student++){
                    a = 0;
                    Lecture = 1;
                    sb.append(l.get(Lecture).GetStudents().get(Student).GetCode());
                    sb.append(",");
                    sb.append(l.get(Lecture).GetStudents().get(Student).GetName());
                    sb.append(",");
                    for(Lecture = 1; Lecture < l.size(); Lecture++){
                        a = a + (l.get(Lecture).GetStudent(Student).GetAttdLate());
                        sb.append(ConvertShape((int)l.get(Lecture).GetStudent(Student).GetAttdLate()));
                        sb.append(",");
                    }
                    sb.append(a/(l.size()-1));
                    sb.append("\n");
                }
                sb.append(",");
                sb.append(",");
                float sum = 0;
                for(int Lec = 1; Lec < l.size(); Lec++){
                    a1 = 0;
                    for(int Student = 0; Student < standard.size(); Student++){
                        a1 = a1 + (l.get(Lec).GetStudent(Student).GetAttdLate());
                    }
                    a1 = a1 / (standard.size());
                    sum += a1;
                    sb.append(a1);
                    sb.append(",");
                }
                sum = sum / (l.size() - 1);
                sb.append(sum);
                sb.append("\n");
                writer.write(sb.toString());
                writer.close();
            }catch(FileNotFoundException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

