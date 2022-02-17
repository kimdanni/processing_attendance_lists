package com.App;

public class Student {
    private String Name;
    private String Email;
    private Boolean isGuest;
    private Integer Code;
    private Integer StartTime;
    private Integer EndTime;
    private Integer TotalAtdTime;
    private Boolean IsDuplicated;
    private float TotalAttd;
    private float AttdLate;
    public void SetAttdLate(float AttdLate) {
        this.AttdLate = AttdLate;
    }

    public float GetAttdLate() {
        return this.AttdLate;
    }

    public void SetIsDuplicated(Boolean IsDuplicated) {
        this.IsDuplicated = IsDuplicated;
    }

    public Boolean GetIsDuplicated() {
        return this.IsDuplicated;
    }

    public void SetName(String Name) {
        this.Name = Name;
    }

    public String GetName() {
        return this.Name;
    }

    public void SetEmail(String Email) {
        this.Email = Email;
    }

    public String GetEmail() {
        return this.Email;
    }

    public void SetIsGuest(Boolean isGuest) {
        this.isGuest = isGuest;
    }

    public Boolean GetIsGuest() {
        return this.isGuest;
    }

    public Integer GetCode() {
        return this.Code;
    }

    public void SetCode(Integer Code) {
        this.Code = Code;
    }

    public void SetStartTime(Integer StartTime) {
        this.StartTime = StartTime;
    }

    public Integer GetStartTime() {
        return this.StartTime;
    }

    public void SetEndTime(Integer EndTime) {
        this.EndTime = EndTime;
    }

    public Integer GetEndTime() {
        return this.EndTime;
    }

    public void SetTotalAtdTime(Integer TotalAtdTime) {
        this.TotalAtdTime = TotalAtdTime;
    }

    public Integer GetTotalAtdTime() {
        return this.TotalAtdTime;
    }

    public void SetTotalAttd(float TotalAttd) {
        this.TotalAttd = TotalAttd;
    }

    public float GetTotalAttd() {
        return this.TotalAttd;
    }


    public Student GetCopy() {
        Student s = new Student();
        s.SetName(Name);
        s.SetEmail(Email);
        s.SetIsGuest(isGuest);
        s.SetCode(Code);
        s.SetStartTime(StartTime);
        s.SetEndTime(EndTime);
        s.SetTotalAtdTime(TotalAtdTime);
        s.SetIsDuplicated(IsDuplicated);
        s.SetTotalAttd(TotalAttd);
        return s;
    }
}