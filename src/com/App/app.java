package com.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.table.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class app extends JFrame implements ActionListener{

    AttendanceProcessing f1 = new AttendanceProcessing();
    private JTable table;				//리스트
    private JTextField inputDate;	//테스트 입력 Field
    private JButton SUBMIT;       //시간 입력 버튼
    private JButton LOAD;
    private JButton VIEW;
    private JButton SAVE;		//삭제 버튼
    private JComboBox Lecture;
    private DefaultTableModel model1;//JList에 보이는 실제 데이터
    private JScrollPane scrolled;

    public app(String title) {
        super(title);
        init();
    }

    public void init() {
        //list=new JList(model);
        Lecture=new JComboBox();
        VIEW=new JButton("보기");
        inputDate=new JTextField(4);
        SUBMIT=new JButton("설정");
        LOAD=new JButton("열기");
        SAVE=new JButton("저장");

        //list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	//하나만 선택 될 수 있도록


        this.setLayout(new BorderLayout());


        JPanel topPanel=new JPanel(new FlowLayout(10,10,FlowLayout.LEFT));
        topPanel.add(Lecture);
        topPanel.add(VIEW);
        topPanel.add(inputDate);
        topPanel.add(SUBMIT);
        topPanel.add(LOAD);
        topPanel.add(SAVE);		//위쪽 패널 [textfield]  [add] [del]
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));	//상, 좌, 하, 우 공백(Padding)

        this.add(topPanel,"North");

        LOAD.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("정상실행1");
                Frame f = new Frame("파일 열기");
                FileDialog fd = new FileDialog(f, "파일 열기 대화상자", FileDialog.LOAD);
                fd.setVisible(true);
                String filename = fd.getFile(); //파일 얻어온다
                String fileDir = fd.getDirectory(); //디렉토리를 얻어온다
                String[] s = filename.replace(".csv","").split("_");
                if(s.length == 2){
                    System.out.println("2");
                    String Name = filename.replace(".csv", "");
                    f1.AppendLecture(fileDir, filename);
                    //선택한 파일 이름이 filename에 들어가고 fileDir에 경로가 들어
                }else if(s.length == 3){
                    System.out.println("3");
                    String Name = filename.substring(0, filename.length()-4);
                    Lecture.addItem(Name);
                    f1.AppendStudent(fileDir, filename);
                }else{
                    return;
                }
            }
        });

        SAVE.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                f1.ReturnToCSV();
            }
        });

        SUBMIT.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(f1.GetLecture(Lecture.getSelectedItem().toString()) == null){
                    System.out.println("ddeeedddd");
                    return;
                }
                f1.GetLecture(Lecture.getSelectedItem().toString()).get(1).SetLectureSTime(Integer.parseInt(inputDate.getText()));
                f1.GetLecture(Lecture.getSelectedItem().toString()).get(1).SetStudents(f1.SetStudentAttd(f1.GetLecture(Lecture.getSelectedItem().toString()).get(1)));
                f1.ProcessingLectureRate(inputDate.getText().substring(0,inputDate.getText().length()-3));
            }
        });

        VIEW.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                List<com.App.Lecture> l = null;
                String s = (String)Lecture.getSelectedItem();
                l = f1.GetLecture(s);
                List<Student> s1 = null;
                if(l == null){

                    return;
                }
                if(model1 != null){
                    model1.setNumRows(0);}
                else{
                Vector vector = new Vector<String>();
                vector.addElement("이름");
                vector.addElement("학번");
                vector.addElement("출석");
                model1 = new DefaultTableModel(vector, 0);
                table = new JTable(model1);
                scrolled = new JScrollPane(table);}
                for (int i = 1; i < l.size(); i++) {
                    System.out.println(l.get(i).GetLectureName()+","+Lecture.getSelectedItem().toString());
                    if (l.get(i).GetLectureName().equals(Lecture.getSelectedItem().toString())) {
                        s1 = l.get(i).GetStudents();
                        for (int j = 0; j < s1.size(); j++) {
                            Vector v = new Vector<String>();
                            v.addElement(s1.get(j).GetName());
                            v.addElement(Integer.toString(s1.get(j).GetCode()));
                            v.addElement(f1.ConvertShape((int) s1.get(j).GetAttdLate()));
                            model1.addRow(v);
                            System.out.println("hh"+f1.ConvertShape((int) s1.get(j).GetAttdLate()));
                        }
                        break;
                    }
                }
                add(scrolled,"Center");
                topPanel.revalidate();
                topPanel.repaint();
                scrolled.revalidate();
                scrolled.repaint();
            }
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(620,800);
        this.setLocationRelativeTo(null);	//창 가운데 위치
        this.setVisible(true);

    }

    public void addItem() {
        String inputText=inputDate.getText();
        String fo4 = String.format("%tr", inputText);
        if(inputText==null||inputText.length()==0||inputText.length()!=4) return;
        //model.addElement(inputText);
        inputDate.setText("");		//내용 지우기
        inputDate.requestFocus();	//다음 입력을 편하게 받기 위해서 TextField에 포커스 요청
    }


    public static void main(String [] args){
        app a = new app("Attendance");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}