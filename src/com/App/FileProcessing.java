package com.App;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class FileProcessing {
    private String FileName;
    private String FilePath;
    private List<String> trashList;

    FileProcessing(String FilePath, String FileName) {
        this.FileName = FileName;
        this.FilePath = FilePath;
        this.trashList = new ArrayList<String>();
    }
    public void CheckTrashList(){
          for(int i =0; i < trashList.size(); i++){
              System.out.println(trashList.get(i));
          }
    }
    private List<List<String>> ReadCSVFile() {
        List<List<String>> Attd = new ArrayList<List<String>>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(FilePath + FileName), "utf-8"));

            String line = "";
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                tmpList = Arrays.asList(array);
                if (!tmpList.isEmpty()) {
                    Attd.add(tmpList);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    CheckTrashList();
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Attd;
    }

    private Student ProcessingDuplicated(List stdList, Student s1) {
        Student s = s1.GetCopy();
        int freq = Collections.frequency(stdList, s.GetName());
        if (freq > 1) {
            System.out.println(s.GetCode()+"isDup");
            s.SetIsDuplicated(true);
        } else {
            s.SetIsDuplicated(false);
        }
        return s;
    }

    public Lecture ReadStudentFile() {
        List<String> stdList = new ArrayList<String>();
        List<List<String>> Attd = new ArrayList<List<String>>();
        Attd = ReadCSVFile();
        Lecture l = new Lecture();
        List<Student> std = new ArrayList<Student>();
        for (int i = 0; i < Attd.size(); i++) {
            Student s = new Student();
            s.SetCode((Integer) Integer.parseInt(Attd.get(i).get(0)));
            s.SetName(Attd.get(i).get(1));
            stdList.add(Attd.get(i).get(1));
            std.add(s);
        }
        for (int j = 0; j < Attd.size(); j++) {
            Student s = std.get(j).GetCopy();
            s = ProcessingDuplicated(stdList,std.get(j));
            std.get(j).SetIsDuplicated(s.GetIsDuplicated());
        }
        String LectureName = FileName.replace(".csv", "");
        l.SetLectureName(LectureName);

        l.SetStudents(std);
        return l;
    }

    private Integer ParseTime(String s) {
        Integer TimeStemp = 0;
        String array[] = s.split(" ")[1].split(":");
        if (Integer.parseInt(array[0]) % 10 == Integer.parseInt(array[0])) {
            TimeStemp = Integer.parseInt(array[0]) * 100 + 1200;
        } else {
            TimeStemp += Integer.parseInt(array[0]) * 100;
        }
        TimeStemp += Integer.parseInt(array[1]);
        return TimeStemp;
    }

    private Student ProcessTotalAtdTime(Student s, List<String> Attd) {
        if (s.GetTotalAtdTime() == null) {
            s.SetTotalAtdTime(Integer.parseInt(Attd.get(4)));
        } else {
            Integer time = s.GetTotalAtdTime() + Integer.parseInt(Attd.get(4));
            s.SetTotalAtdTime(time);
        }
        return s;
    }

    private Student ProcessSetEndTime(Student s, List<String> Attd) {
        if (s.GetEndTime() == null) {
            s.SetEndTime(ParseTime(Attd.get(3)));
        } else if (s.GetEndTime() < ParseTime(Attd.get(3))) {
            s.SetEndTime(ParseTime(Attd.get(3)));
        }
        return s;
    }

    //private Student ProcessSetStartTime
    private Student ProcessSetStartTime(Student s, List<String> Attd) {
        if (s.GetStartTime() == null) {
            s.SetStartTime(ParseTime(Attd.get(2)));
        } else if (s.GetStartTime() > ParseTime(Attd.get(2))) {
            s.SetStartTime(ParseTime(Attd.get(2)));
        }
        return s;
    }

    private Integer ParseStartTime(Integer i) {
        if (i % 100 != 0) {
            i = i + 100;
            i = i / 100;
            i = i * 100;
        }
        return i;
    }

    private Lecture ProcessLectureStartTime(Lecture l, List<String> Attd) {
        Integer i = ParseStartTime(ParseTime(Attd.get(2)));
        if (l.GetLectureSTime() == null) {
            l.SetLectureSTime(i);
        } else if (l.GetLectureSTime() > ParseTime(Attd.get(2))) {
            l.SetLectureSTime(i);
        }
        return l;
    }

    private void SetStudentInfo(Student s, List<String> SList, Lecture l, int i) {
        s.SetEmail(SList.get(1));
        s = ProcessTotalAtdTime(s, SList);
        s = ProcessSetEndTime(s, SList);
        s = ProcessSetStartTime(s, SList);
        l.SetStudent(s, i);
    }
    public Lecture fSetAttdLate(Lecture l) {
        List<Student> s = l.GetStudents();
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
        return l;
    }
    public String dateParse(String dirName){
        String [] s = dirName.replace(".csv","").split("_");
        return s[2];
    }
    public void SendErrorStudent(String dirName, List<List<String>> list){
        String Name = dirName.replace(".csv","");
        try (PrintWriter writer = new PrintWriter(new File(Name+"_Error"+".csv"))) {
            StringBuilder sb = new StringBuilder();
            sb.append("이름(원래 이름)");
            sb.append(",");
            sb.append("사용자 이메일");
            sb.append(",");
            sb.append("참가 시간");
            sb.append(",");
            sb.append("나간 시간");
            sb.append(",");
            sb.append("기간(분)");
            sb.append(",");
            sb.append("게스트");
            sb.append("\n");
            for(int i = 0; i < list.size(); i++){
                int j;
                for(j = 0; j < list.get(i).size() -1; j++){
                    sb.append(list.get(i).get(j));
                    sb.append(",");
                }
                sb.append(list.get(i).get(j));
                sb.append("\n");
            }
            writer.write(sb.toString());
            writer.close();
        }catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
    }

    public Lecture ReadAttendanceFile(String dirName ,Lecture ll) {
        Lecture l = ll.GetCopy();
        List<List<String>> Attd = new ArrayList<List<String>>();
        Attd = ReadCSVFile();
        l.SetDate(dateParse(dirName));
        List<Student> std = new ArrayList<Student>();
        List<List<String>> errorList = new ArrayList<>();
        for (int i =0; i < Attd.size(); i++) {
            List<Student> s = l.GetStudents();
            List<String> SList = Attd.get(i);
            for (int j =0; j < s.size(); j++) {
                Student t = s.get(j).GetCopy();
                if(SList.get(0).contains(t.GetName()) && !t.GetIsDuplicated()){
                    SetStudentInfo(t, SList, l, j);
                    break;
                }else if(SList.get(0).contains(Integer.toString(t.GetCode()))){
                    SetStudentInfo(t, SList, l, j);
                    break;
                }else if (SList.get(5).contains("아니요")){
                    l = ProcessLectureStartTime(l, SList);
                    break;
                }
                if(j == s.size() - 1){
                    System.out.println("this is wrong user"+ Attd.get(i));
                    errorList.add(Attd.get(i));
                    SendErrorStudent(dirName, errorList);
                    break;
                }
            }
        }
        String LectureName = FileName.replace(".csv", "");
        l.SetLectureName(LectureName);
        l = fSetAttdLate(l);
        return l;
    }
}
