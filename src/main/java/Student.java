/**
 * @author Asswei
 */
public class Student {
    public Student() {
    }

    public Student(String name, String ID) {
        this.name = name;
        this.ID = ID;
    }

    public String name;
    public String ID;
    public int teacherGrade;
    public int atGrade;
    public int peerGrade;
    public int totalGrade;
    public void setGrade(Student stu,int score){
        stu.peerGrade = score;
    }
    public void getGrade(){
        int temp;
        //teacherGrade老师打分      atGrade助教打分    peerGrade同学打分
        temp = (int) (0.7 * this.teacherGrade + 0.2*this.atGrade+ 0.1*this.peerGrade);//学生成绩由老师打分、助教打分、同学打分共同组成
        if(temp>100) {
            this.totalGrade = 100;
        } else {
            this.totalGrade = temp;
        }
    }
}

