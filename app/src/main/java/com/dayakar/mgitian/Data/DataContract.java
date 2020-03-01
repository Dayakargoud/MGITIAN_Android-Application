package com.dayakar.mgitian.Data;

import android.provider.BaseColumns;

public class DataContract {


    public DataContract() { }

    //class for week table
    public final class WeekEntry implements BaseColumns{

        public static final String TABLE_WEEK = "week";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_DAY = "day";
        public static final String PERIOD_1 = "period1";
        public static final String PERIOD_2 = "period2";
        public static final String PERIOD_3 = "period3";
        public static final String PERIOD_4 = "period4";
        public static final String PERIOD_5 = "period5";
        public static final String PERIOD_6 = "period6";
        public static final String TIMINGS = "timings";

        public static final String P1ROOM = "p1r";
        public static final String P2ROOM = "p2r";
        public static final String P3ROOM = "p3r";
        public static final String P4ROOM = "p4r";
        public static final String P5ROOM = "p5r";
        public static final String P6ROOM = "p6r";

        public static final String P1Lecturer = "p1l";
        public static final String P2Lecturer = "p2l";
        public static final String P3Lecturer = "p3l";
        public static final String P4Lecturer = "p4l";
        public static final String P5Lecturer = "p5l";
        public static final String P6Lecturer = "p6l";






    }
    public final class SyllabusEntry implements BaseColumns{

        public static final String TABLE_SYLLABUS = "syllabus";
        public static final String COLUMN_ID = "id";
        public static final String SUBJECT= "subject";

        public static final String UNIT_1= "unit1";
        public static final String UNIT_2= "unit2";

        public static final String UNIT_3= "unit3";

        public static final String UNIT_4= "unit4";
        public static final String UNIT_5= "unit5";
        public static final String REFERENCE= "reference";
        public static final String SUB_CODE= "sub_code";







    }
    public final class FacultyEntry implements BaseColumns{
        public static final String TABLE_Faculty = "faculty";
        public static final String COLUMN_ID = "id";
        public static final String NAME= "name";

        public static final String DESIGNATION= "designation";
        public static final String QUALIFICATION= "qualification";
        public static final String CABIN="cabin";
    }
}
