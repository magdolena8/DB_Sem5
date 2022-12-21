package com.begdev.lab_11;

import android.provider.BaseColumns;

public class DBContract {
    public DBContract() {
    }

    public static String DB_NAME = "lab_11.db";

    public static abstract class DBGroupsTable implements BaseColumns {
        public static final String TABLE_NAME = "GROUPS";  //or GROUP
        public static final String COLUMN_NAME_IDGROUP = "_id";
        public static final String COLUMN_NAME_FACULTY = "FACULTY";
        public static final String COLUMN_NAME_COURSE = "COURSE";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_HEAD = "HEAD";
    }

    public static abstract class DBProgressTable implements BaseColumns {
        public static final String TABLE_NAME = "PROGRESS";
        public static final String COLUMN_NAME_IDSUBJECT = "IDSUBJECT";
        public static final String COLUMN_NAME_IDSTUDENT = "IDSTUDENT";
        public static final String COLUMN_NAME_EXAMDATE = "EXAMDATE";
        public static final String COLUMN_NAME_MARK = "MARK";
        public static final String COLUMN_NAME_TEACHER = "TEACHER";
    }

    public static abstract class DBStudentsTable implements BaseColumns {
        public static final String TABLE_NAME = "STUDENT";
        public static final String COLUMN_NAME_IDSTUDENT = "_id";
        public static final String COLUMN_NAME_IDGROUP = "IDGROUP";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_BIRTHDATE = "BIRTHDATE";
        public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
    }

    public static abstract class DBSubjectTable implements BaseColumns {
        public static final String TABLE_NAME = "SUBJECT";
        public static final String COLUMN_NAME_IDSUBJECT = "_id";
        public static final String COLUMN_NAME_SUBJECT = "SUBJECT";
    }

    public static abstract class DBFacultyTable implements BaseColumns {
        public static final String TABLE_NAME = "FACULTY";
        public static final String COLUMN_NAME_IDFACULTY = "_id";
        public static final String COLUMN_NAME_FACULTY = "FACULTY";
        public static final String COLUMN_NAME_DEAN = "DEAN";
//        public static final String TABLE_OFFICETIMETABLE = "OFFICETIMETABLE";
    }

    public static String SQL_CREATE_GROUPS_TABLE = "CREATE TABLE IF NOT EXISTS " + DBGroupsTable.TABLE_NAME + " (" +
            DBGroupsTable.COLUMN_NAME_IDGROUP + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //PK
            DBGroupsTable.COLUMN_NAME_FACULTY + " INTEGER, " +      //FK
            DBGroupsTable.COLUMN_NAME_COURSE + " INTEGER NOT NULL, " +
            DBGroupsTable.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
            DBGroupsTable.COLUMN_NAME_HEAD + " INTEGER,  " +      //FK
            "FOREIGN KEY(" + DBGroupsTable.COLUMN_NAME_HEAD + ") REFERENCES " + DBStudentsTable.TABLE_NAME + "(" + DBStudentsTable.COLUMN_NAME_IDSTUDENT + ") " +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE, " +
            "FOREIGN KEY(" + DBGroupsTable.COLUMN_NAME_FACULTY + ") REFERENCES " + DBFacultyTable.TABLE_NAME + "(" + DBFacultyTable.COLUMN_NAME_IDFACULTY + ") " +
            "ON DELETE CASCADE " +
            "ON UPDATE CASCADE)";

    public static String SQL_CREATE_STUDENTS_TABLE = "CREATE TABLE IF NOT EXISTS " + DBStudentsTable.TABLE_NAME + " (" +
            DBStudentsTable.COLUMN_NAME_IDSTUDENT + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DBStudentsTable.COLUMN_NAME_IDGROUP + " INTEGER, " +   //FK
            DBStudentsTable.COLUMN_NAME_NAME + " TEXT CHECK (" + DBStudentsTable.COLUMN_NAME_NAME + " != \"\"), " +
            DBStudentsTable.COLUMN_NAME_BIRTHDATE + " INTEGER NOT NULL, " +
            DBStudentsTable.COLUMN_NAME_ADDRESS + " TEXT CHECK (" + DBStudentsTable.COLUMN_NAME_ADDRESS + " != \"\"), " +
            "FOREIGN KEY (" + DBStudentsTable.COLUMN_NAME_IDGROUP + ") REFERENCES " + DBGroupsTable.TABLE_NAME + "(" + DBGroupsTable.COLUMN_NAME_IDGROUP + ") " +
            "ON UPDATE CASCADE " +
            "ON DELETE CASCADE)";

    public static String SQL_CREATE_SUBJECT_TABLE = "CREATE TABLE IF NOT EXISTS " + DBSubjectTable.TABLE_NAME + " (" +
            DBSubjectTable.COLUMN_NAME_IDSUBJECT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //PK
            DBSubjectTable.COLUMN_NAME_SUBJECT + " TEXT UNIQUE NOT NULL ) ";

    public static String SQL_CREATE_PROGRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + DBProgressTable.TABLE_NAME + " (" +
            DBProgressTable.COLUMN_NAME_IDSUBJECT + " INTEGER NOT NULL, " +
            DBProgressTable.COLUMN_NAME_IDSTUDENT + " INTEGER NOT NULL, " +
            DBProgressTable.COLUMN_NAME_EXAMDATE + "  INTEGER NOT NULL, " +
            DBProgressTable.COLUMN_NAME_MARK + " INTEGER CHECK(" + DBProgressTable.COLUMN_NAME_MARK + " > 0 AND " + DBProgressTable.COLUMN_NAME_MARK + " <= 10 ), " +
            DBProgressTable.COLUMN_NAME_TEACHER + " TEXT NOT NULL CHECK (" + DBProgressTable.COLUMN_NAME_TEACHER + " != \"\"), " +
            "FOREIGN KEY (" + DBProgressTable.COLUMN_NAME_IDSUBJECT + ") REFERENCES " + DBSubjectTable.TABLE_NAME + "(" + DBSubjectTable.COLUMN_NAME_IDSUBJECT + ") " +
            "ON UPDATE CASCADE " +
            "ON DELETE CASCADE," +
            "FOREIGN KEY (" + DBProgressTable.COLUMN_NAME_IDSTUDENT + ") REFERENCES " + DBStudentsTable.TABLE_NAME + "(" + DBStudentsTable.COLUMN_NAME_IDSTUDENT + ") " +
            "ON UPDATE CASCADE " +
            "ON DELETE CASCADE" + ")";

    public static String SQL_CREATE_FACULTY_TABLE = "CREATE TABLE IF NOT EXISTS " + DBFacultyTable.TABLE_NAME + " (" +
            DBFacultyTable.COLUMN_NAME_IDFACULTY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //PK
            DBFacultyTable.COLUMN_NAME_FACULTY + " TEXT UNIQUE NOT NULL CHECK (" + DBGroupsTable.COLUMN_NAME_FACULTY + " != \"\"), " +
            DBFacultyTable.COLUMN_NAME_DEAN + " TEXT UNIQUE CHECK (" + DBGroupsTable.COLUMN_NAME_FACULTY + " != \"\")) ";


    public static String createViewTopFacultyQuery = "create temp view if not exists topFaculty as select\n" +
            "\tstudent._id as idStudent,\n" +
            "\tstudent.name as nameStudent, \n" +
            "\tgroups._id as idGroup, \n" +
            "\tFACULTY._id as idFaculty,\n" +
            "\tFACULTY.FACULTY as nameFaculty,\n" +
            "\tgroups.name as nameGroup,\n" +
            "\tprogress.mark, \n" +
            "\tsubject.subject,\n" +
            "\tPROGRESS.EXAMDATE\n" +
            "\t\tfrom groups \n" +
            "\t\t\tinner join student on groups._id = student.idgroup\n" +
            "\t\t\tinner join faculty on groups.FACULTY = FACULTY._id\n" +
            "\t\t\tinner join progress on progress.idstudent = student._id\n" +
            "\t\t\tinner join subject on subject._id = progress.idsubject;\n";

    public static String createViewGroupsMarksQuery = "create temp view if not exists groupsMarks as select\n" +
            "\tstudent._id as idStudent,\n" +
            "\tstudent.name as nameStudent, \n" +
            "\tgroups._id as idGroup, \n" +
            "\tgroups.name as nameGroup,\n" +
            "\tprogress.mark,\n" +
            "\tsubject._id as idSubject, \n" +
            "\tsubject.subject,\n" +
            "\tPROGRESS.EXAMDATE\n" +
            "\t\tfrom groups \n" +
            "\t\t\tinner join student on groups._id = student.idgroup \n" +
            "\t\t\tinner join progress on progress.idstudent = student._id\n" +
            "\t\t\tinner join subject on subject._id = progress.idsubject;\n";

    public static String createViewWorstStudentsQuery = "create view if not exists worstStudents as select\n" +
            "\tstudent._id as idStudent,\n" +
            "\tstudent.name as nameStudent, \n" +
            "\tgroups._id as idGroup, \n" +
            "\tFACULTY._id as idFaculty,\n" +
            "\tFACULTY.FACULTY as nameFaculty,\n" +
            "\tgroups.name as nameGroup,\n" +
            "\tprogress.mark, \n" +
            "\tsubject.subject,\n" +
            "\tPROGRESS.EXAMDATE as examDate\n" +
            "\t\tfrom groups \n" +
            "\t\t\tinner join student on groups._id = student.idgroup\n" +
            "\t\t\tinner join faculty on groups.FACULTY = FACULTY._id\n" +
            "\t\t\tinner join progress on progress.idstudent = student._id\n" +
            "\t\t\tinner join subject on subject._id = progress.idsubject;";
    public static String createViewSubjectQuery = "CREATE VIEW if not exists SUBJECTVIEW AS SELECT * FROM SUBJECT;\n";
    
    public static String createExamdateIndex = "CREATE INDEX IF NOT EXISTS INDEX_PROGRESS_EXAMDATE ON "+ DBProgressTable.TABLE_NAME+" ("+DBProgressTable.COLUMN_NAME_EXAMDATE+")";
    public static String createMarkIndex = "CREATE INDEX IF NOT EXISTS INDEX_PROGRESS_MARK ON "+ DBProgressTable.TABLE_NAME+" ("+DBProgressTable.COLUMN_NAME_MARK+")";
    public static String createForeignKeyIndex = "CREATE INDEX IF NOT EXISTS INDEX_PROGRESS_MARK ON "+ DBProgressTable.TABLE_NAME+" ("+DBProgressTable.COLUMN_NAME_IDSTUDENT+")";


    public static String createTriggerDeleteStudent = "create trigger if not exists tr_student_delete\n" +
            "before delete on STUDENT\n" +
            "when (3 > (select count(*) from STUDENT WHERE STUDENT.IDGROUP = old.IDGROUP))\n" +
            "\tbegin\n" +
            "\t\tselect RAISE(FAIL, 'В группе меньше 3 стедентов. Отмена');\n" +
            "\tend;";
    public static String createTriggerInsertStudent = "create trigger if not exists tr_student_insert\n" +
            "before insert on STUDENT\n" +
            "when (6 < (select count(*) from STUDENT WHERE STUDENT.IDGROUP = NEW.IDGROUP))\n" +
            "BEGIN\n" +
            "\tselect RAISE(FAIL, 'В группе больше 6 студентов. Отмена');\n" +
            "END;";
    public static String createTriggerUpdateSubjectView = "create trigger if not exists tr_subjectview_update\n" +
            "instead of update on SUBJECTVIEW\n" +
            "begin\n" +
            "\tupdate SUBJECT set SUBJECT = new.SUBJECT where SUBJECT._id = new._id;\n" +
            "end;";

    public static String SQL_FILL_SUBJECT = "INSERT INTO SUBJECT (SUBJECT) VALUES " +
            "(\"СТПМС\"),\n" +
            "(\"БД\"),\n" +
            "(\"Криптография\"),\n" +
            "(\"АЛОЦВМ\"),\n" +
            "(\"ОИТ\")\n";

    public static String SQL_FILL_FACULTY = "INSERT INTO FACULTY(FACULTY, DEAN) VALUES" +
            " (\"ФИТ\", \"Шиман\")," +
            " (\"ПИМ\", \"Иванов\")," +
            " (\"ИЭФ\", \"Сидоров\")," +
            " (\"ХТиТ\", \"Петров\")\n";

    public static String SQL_FILL_GROUPS = "INSERT INTO GROUPS(FACULTY, COURSE, NAME) VALUES " +
            "(1, 1, \"Группа-1\"), (1, 1, \"Группа-2\"),\n" +
            "(1, 2, \"Группа-1\"),(1, 2, \"Группа-2\"),\n" +
            "(1, 3, \"Группа-1\"), (1, 3, \"Группа-2\"),\n" +
            "(1, 4, \"Группа-1\"),(1, 4, \"Группа-2\"),\n" +
            "(2, 1, \"Группа-1\"),(2, 1, \"Группа-2\"),\n" +
            "(2, 2, \"Группа-1\"),(2, 2, \"Группа-2\"),\n" +
            "(2, 3, \"Группа-1\"),(2, 3, \"Группа-2\"),\n" +
            "(2, 4, \"Группа-1\"),(2, 4, \"Группа-2\"),\n" +
            "(3, 1, \"Группа-1\"),(3, 1, \"Группа-2\"),\n" +
            "(3, 2, \"Группа-1\"),(3, 2, \"Группа-2\"),\n" +
            "(3, 3, \"Группа-1\"),(3, 3, \"Группа-2\"),\n" +
            "(3, 4, \"Группа-1\"),(3, 4, \"Группа-2\"),\n" +
            "(4, 1, \"Группа-1\"),(4, 1, \"Группа-2\"),\n" +
            "(4, 2, \"Группа-1\"),(4, 2, \"Группа-2\"),\n" +
            "(4, 3, \"Группа-1\"),(4, 3, \"Группа-2\"),\n" +
            "(4, 4, \"Группа-1\"),(4, 4, \"Группа-2\")\n";

    public static String SQL_FILL_STUDENTS = "INSERT INTO STUDENT\n" +
            "  (IDGROUP,NAME,BIRTHDATE,ADDRESS)\n" +
            "VALUES\n" +
            "  (26, \"Logan\", \"09-30-17\", \"Saratov\"),\n" +
            "  (26, \"Burt\", \"12-22-05\", \"Kurgan\"),\n" +
            "  (18, \"Bowman\", \"01-24-12\", \"Oryol\"),\n" +
            "  (1, \"Doyle\", \"04-08-19\", \"Tula\"),\n" +
            "  (7, \"Buchanan\", \"10-31-15\", \"Lipetsk\"),\n" +
            "  (7, \"Murphy\", \"11-04-20\", \"Omsk\"),\n" +
            "  (9, \"Fernandez\", \"12-11-02\", \"Saratov\"),\n" +
            "  (21, \"Bush\", \"03-20-02\", \"Kostroma\"),\n" +
            "  (13, \"Harper\", \"12-23-10\", \"Yekaterinburg\"),\n" +
            "  (13, \"Santos\", \"10-14-01\", \"Belgorod\"),\n" +
            "  (18, \"Riddle\", \"03-18-17\", \"Kursk\"),\n" +
            "  (32, \"Mclaughlin\", \"07-27-17\", \"Miass\"),\n" +
            "  (11, \"Larsen\", \"08-20-01\", \"Novosibirsk\"),\n" +
            "  (18, \"Sullivan\", \"11-23-17\", \"Pskov\"),\n" +
            "  (3, \"Leonard\", \"03-02-20\", \"Volgograd\"),\n" +
            "  (4, \"Haynes\", \"02-15-03\", \"Smolensk\"),\n" +
            "  (17, \"Shelton\", \"01-11-13\", \"Oryol\"),\n" +
            "  (3, \"Joseph\", \"06-11-04\", \"Kaliningrad\"),\n" +
            "  (7, \"Buckner\", \"12-03-20\", \"Balashikha\"),\n" +
            "  (20, \"Brown\", \"09-22-16\", \"Rostov\"),\n" +
            "  (21, \"William\", \"03-14-09\", \"Saint Petersburg\"),\n" +
            "  (10, \"Carlson\", \"01-10-04\", \"Voronezh\"),\n" +
            "  (14, \"Ramos\", \"09-30-03\", \"Vladimir\"),\n" +
            "  (26, \"Blevins\", \"04-14-07\", \"Astrakhan\"),\n" +
            "  (7, \"Joyce\", \"09-27-10\", \"Sevsk\"),\n" +
            "  (12, \"Macias\", \"03-07-23\", \"Tula\"),\n" +
            "  (24, \"Hopkins\", \"04-14-20\", \"Novosibirsk\"),\n" +
            "  (23, \"Duran\", \"10-12-17\", \"Znamensk\"),\n" +
            "  (21, \"Wright\", \"05-13-17\", \"Omsk\"),\n" +
            "  (30, \"Valdez\", \"10-30-05\", \"Magadan\"),\n" +
            "  (30, \"Giles\", \"07-27-14\", \"Nizhny\"),\n" +
            "  (27, \"Livingston\", \"09-22-19\", \"Omsk\"),\n" +
            "  (12, \"Mullins\", \"08-07-23\", \"Saratov\"),\n" +
            "  (11, \"Hurley\", \"10-14-23\", \"Ivangorod\"),\n" +
            "  (21, \"Rose\", \"01-29-09\", \"Kemerovo\"),\n" +
            "  (23, \"Riddle\", \"09-21-04\", \"Ryazan\"),\n" +
            "  (7, \"Kramer\", \"04-23-05\", \"Ryazan\"),\n" +
            "  (28, \"Oliver\", \"08-20-12\", \"Tomsk\"),\n" +
            "  (22, \"Allison\", \"12-27-03\", \"Novosibirsk\"),\n" +
            "  (24, \"Harmon\", \"11-23-22\", \"Irkutsk\"),\n" +
            "  (24, \"Doyle\", \"03-20-20\", \"Kaluga\"),\n" +
            "  (19, \"Hester\", \"11-26-15\", \"Magnitogorsk\"),\n" +
            "  (9, \"Cline\", \"02-20-15\", \"Tomsk\"),\n" +
            "  (10, \"Noel\", \"12-30-08\", \"Kaliningrad\"),\n" +
            "  (18, \"Dodson\", \"06-21-14\", \"Novosibirsk\"),\n" +
            "  (20, \"Chase\", \"07-16-16\", \"Rostov\"),\n" +
            "  (17, \"Pennington\", \"12-17-06\", \"Tambov\"),\n" +
            "  (16, \"Cantu\", \"08-06-11\", \"Kostroma\"),\n" +
            "  (18, \"Robles\", \"04-21-13\", \"Tambov\"),\n" +
            "  (15, \"Valencia\", \"07-03-10\", \"Ivanovo\"),\n" +
            "  (4, \"Richmond\", \"09-01-08\", \"Nizhny\"),\n" +
            "  (21, \"Valenzuela\", \"09-20-14\", \"Seltso\"),\n" +
            "  (28, \"Gill\", \"07-11-07\", \"Samara\"),\n" +
            "  (25, \"Butler\", \"11-11-14\", \"Bakal\"),\n" +
            "  (8, \"Jimenez\", \"05-28-16\", \"Voronezh\"),\n" +
            "  (30, \"Craft\", \"02-07-20\", \"Kirov\"),\n" +
            "  (22, \"Fitzgerald\", \"09-22-19\", \"Sevastopol\"),\n" +
            "  (7, \"Mcgowan\", \"06-07-05\", \"Tula\"),\n" +
            "  (20, \"Fischer\", \"04-25-08\", \"Brin-Navolok\"),\n" +
            "  (5, \"Duncan\", \"04-22-05\", \"Nizhny\"),\n" +
            "  (16, \"Foster\", \"10-10-23\", \"Saratov\"),\n" +
            "  (21, \"Strickland\", \"03-30-23\", \"Kurgan\"),\n" +
            "  (22, \"Beard\", \"01-15-18\", \"Skovorodino\"),\n" +
            "  (29, \"Avila\", \"10-07-08\", \"Sevastopol\"),\n" +
            "  (25, \"Marsh\", \"10-22-15\", \"Volgograd\"),\n" +
            "  (31, \"Pennington\", \"04-22-22\", \"Gatchina\"),\n" +
            "  (24, \"Everett\", \"09-15-04\", \"Kharabali\"),\n" +
            "  (31, \"Simon\", \"12-18-20\", \"Ryazan\"),\n" +
            "  (12, \"Stafford\", \"04-16-15\", \"Novosibirsk\"),\n" +
            "  (11, \"Salas\", \"08-24-21\", \"Kemerovo\"),\n" +
            "  (21, \"Bolton\", \"02-25-10\", \"Tambov\"),\n" +
            "  (7, \"Alvarez\", \"06-28-01\", \"Saratov\"),\n" +
            "  (20, \"Cortez\", \"09-16-18\", \"Tyumen\"),\n" +
            "  (21, \"Marshall\", \"06-15-16\", \"Kurgan\"),\n" +
            "  (31, \"Kane\", \"02-22-22\", \"Orenburg\"),\n" +
            "  (24, \"Knight\", \"07-04-07\", \"Murmansk\"),\n" +
            "  (15, \"Blair\", \"05-01-08\", \"Kaliningrad\"),\n" +
            "  (22, \"Wallace\", \"07-07-21\", \"Novgorod\"),\n" +
            "  (12, \"Keller\", \"09-06-11\", \"Yekaterinburg\"),\n" +
            "  (23, \"Munoz\", \"11-04-03\", \"Rostov\"),\n" +
            "  (27, \"Rodgers\", \"10-13-02\", \"Ryazan\"),\n" +
            "  (15, \"Crosby\", \"12-23-10\", \"Tula\"),\n" +
            "  (9, \"Mcdaniel\", \"05-07-09\", \"Kursk\"),\n" +
            "  (17, \"Peterson\", \"02-09-13\", \"Kaliningrad\"),\n" +
            "  (10, \"Cameron\", \"07-17-22\", \"Kamyzyak\"),\n" +
            "  (15, \"Best\", \"03-26-15\", \"Saratov\"),\n" +
            "  (22, \"Mcgee\", \"12-24-15\", \"Kaluga\"),\n" +
            "  (15, \"Trevino\", \"05-26-11\", \"Volgograd\"),\n" +
            "  (29, \"Branch\", \"07-29-15\", \"Novosibirsk\"),\n" +
            "  (31, \"Larsen\", \"03-24-12\", \"Cherepovets\"),\n" +
            "  (4, \"Mueller\", \"12-11-08\", \"Tver\"),\n" +
            "  (14, \"Dunn\", \"08-18-15\", \"Belogorsk\"),\n" +
            "  (11, \"Chase\", \"07-24-04\", \"Tomsk\"),\n" +
            "  (26, \"Lyons\", \"10-28-02\", \"Voronezh\"),\n" +
            "  (2, \"Melton\", \"05-29-12\", \"Murmansk\"),\n" +
            "  (29, \"Greene\", \"02-03-11\", \"Stroitel\"),\n" +
            "  (7, \"Winters\", \"04-01-22\", \"Tomsk\"),\n" +
            "  (20, \"Burton\", \"07-26-03\", \"Tver\"),\n" +
            "  (27, \"Sheppard\", \"12-13-07\", \"Kemerovo\"),\n" +
            "  (21, \"Dorsey\", \"02-23-03\", \"Sevastopol\"),\n" +
            "  (11, \"Long\", \"11-08-09\", \"Novosibirsk\"),\n" +
            "  (6, \"Hahn\", \"05-18-02\", \"Voronezh\"),\n" +
            "  (19, \"Mcconnell\", \"09-28-11\", \"Nizhny\"),\n" +
            "  (21, \"Bates\", \"06-03-09\", \"Yaroslavl\"),\n" +
            "  (2, \"Poole\", \"12-11-16\", \"Rostov\"),\n" +
            "  (27, \"Campos\", \"10-04-04\", \"Rostov\"),\n" +
            "  (15, \"Merrill\", \"07-20-11\", \"Kostroma\"),\n" +
            "  (14, \"Hester\", \"09-19-12\", \"Rakitnoye\"),\n" +
            "  (17, \"Patrick\", \"02-14-07\", \"Severodvinsk\"),\n" +
            "  (25, \"Compton\", \"06-22-06\", \"Cherepovets\"),\n" +
            "  (30, \"Mccullough\", \"02-10-03\", \"Volgograd\"),\n" +
            "  (5, \"Anthony\", \"01-22-07\", \"Narimanov\"),\n" +
            "  (23, \"Cherry\", \"05-13-18\", \"Novosibirsk\"),\n" +
            "  (18, \"Giles\", \"11-13-06\", \"Blagoveshchensk\"),\n" +
            "  (27, \"Combs\", \"09-09-13\", \"Kostroma\"),\n" +
            "  (19, \"Montgomery\", \"10-16-16\", \"Kaluga\"),\n" +
            "  (17, \"Waters\", \"06-29-04\", \"Lipetsk\"),\n" +
            "  (12, \"Camacho\", \"02-12-08\", \"Samara\"),\n" +
            "  (25, \"Lawrence\", \"08-24-23\", \"Kaluga\"),\n" +
            "  (31, \"Cole\", \"09-22-04\", \"Ulyanovsk\"),\n" +
            "  (9, \"Campbell\", \"03-16-04\", \"Kostroma\"),\n" +
            "  (31, \"Little\", \"09-27-23\", \"Tyumen\"),\n" +
            "  (4, \"Weber\", \"06-11-21\", \"Orenburg\"),\n" +
            "  (9, \"Hess\", \"03-07-01\", \"Akhtubinsk\"),\n" +
            "  (24, \"Justice\", \"02-22-08\", \"Novosibirsk\"),\n" +
            "  (3, \"Romero\", \"09-24-17\", \"Sevastopol\"),\n" +
            "  (5, \"Merritt\", \"01-14-18\", \"Samara\"),\n" +
            "  (14, \"Hart\", \"08-02-07\", \"Tyumen\"),\n" +
            "  (10, \"Shields\", \"10-01-13\", \"Krasnozavodsk\"),\n" +
            "  (20, \"Snyder\", \"12-25-12\", \"Smolensk\"),\n" +
            "  (13, \"Edwards\", \"12-31-06\", \"Oryol\"),\n" +
            "  (13, \"Roach\", \"07-05-07\", \"Rostov\"),\n" +
            "  (24, \"Roy\", \"10-22-14\", \"Yaroslavl\"),\n" +
            "  (31, \"Spence\", \"06-26-05\", \"Kaliningrad\"),\n" +
            "  (20, \"Frazier\", \"06-12-19\", \"Volgograd\"),\n" +
            "  (7, \"Hyde\", \"07-13-01\", \"Solvychegodsk\"),\n" +
            "  (10, \"Schmidt\", \"07-24-01\", \"Penza\"),\n" +
            "  (17, \"Velazquez\", \"03-18-01\", \"Rakitnoye\"),\n" +
            "  (11, \"Bridges\", \"03-03-14\", \"Moscow\"),\n" +
            "  (3, \"Phelps\", \"05-01-01\", \"Kaliningrad\"),\n" +
            "  (2, \"Hunt\", \"12-03-04\", \"Yaroslavl\"),\n" +
            "  (26, \"Mccarthy\", \"04-02-09\", \"Nizhny\"),\n" +
            "  (24, \"Reilly\", \"09-30-09\", \"Penza\"),\n" +
            "  (23, \"Sanchez\", \"10-11-02\", \"Omsk\"),\n" +
            "  (9, \"Pennington\", \"06-03-13\", \"Oryol\"),\n" +
            "  (12, \"Payne\", \"01-21-20\", \"Tyumen\"),\n" +
            "  (20, \"Bishop\", \"10-19-11\", \"Ivanovo\"),\n" +
            "  (16, \"Justice\", \"04-17-19\", \"Vladimir\"),\n" +
            "  (21, \"Summers\", \"09-15-22\", \"Kostroma\"),\n" +
            "  (16, \"Gonzalez\", \"11-03-03\", \"Sakhalin\"),\n" +
            "  (29, \"Reese\", \"09-07-12\", \"Tyumen\"),\n" +
            "  (16, \"Fleming\", \"04-15-14\", \"Ulyanovsk\"),\n" +
            "  (9, \"Roberts\", \"12-05-12\", \"Kursk\"),\n" +
            "  (23, \"Sheppard\", \"03-07-13\", \"Kemerovo\"),\n" +
            "  (23, \"Hinton\", \"07-31-13\", \"Magadan\"),\n" +
            "  (6, \"Roman\", \"10-25-17\", \"Magadan\"),\n" +
            "  (3, \"Eaton\", \"11-03-14\", \"Kaluga\"),\n" +
            "  (21, \"Fry\", \"04-25-19\", \"Tyumen\"),\n" +
            "  (4, \"Thornton\", \"04-28-20\", \"Tula\"),\n" +
            "  (32, \"Weiss\", \"04-28-16\", \"Samara\"),\n" +
            "  (13, \"Kinney\", \"08-31-07\", \"Kaliningrad\"),\n" +
            "  (28, \"Gay\", \"10-29-08\", \"Vologda\"),\n" +
            "  (11, \"Mitchell\", \"07-27-07\", \"Kharabali\"),\n" +
            "  (22, \"Petty\", \"12-20-16\", \"Novgorod\"),\n" +
            "  (28, \"Vasquez\", \"10-14-13\", \"Tula\"),\n" +
            "  (6, \"Kelly\", \"01-10-09\", \"Mezen\"),\n" +
            "  (4, \"Shepherd\", \"03-04-16\", \"Sakhalin\"),\n" +
            "  (7, \"Cochran\", \"10-26-20\", \"Gatchina\"),\n" +
            "  (30, \"Lawson\", \"03-14-12\", \"Kurgan\"),\n" +
            "  (11, \"James\", \"10-05-01\", \"Sakhalin\"),\n" +
            "  (11, \"Freeman\", \"10-19-14\", \"Kemerovo\"),\n" +
            "  (24, \"Goff\", \"08-17-12\", \"Sakhalin\"),\n" +
            "  (28, \"Church\", \"03-22-07\", \"Tver\"),\n" +
            "  (2, \"Pratt\", \"07-25-01\", \"Voronezh\"),\n" +
            "  (27, \"Anthony\", \"11-27-05\", \"Kemerovo\"),\n" +
            "  (6, \"Neal\", \"10-17-14\", \"Ulyanovsk\"),\n" +
            "  (25, \"Lucas\", \"04-01-20\", \"Tula\"),\n" +
            "  (19, \"Levy\", \"08-23-21\", \"Ryazan\"),\n" +
            "  (24, \"Pena\", \"09-11-21\", \"Smolensk\"),\n" +
            "  (17, \"Moody\", \"06-15-16\", \"Saint Petersburg\"),\n" +
            "  (27, \"Patterson\", \"03-06-06\", \"Tambov\"),\n" +
            "  (21, \"Mclaughlin\", \"04-22-11\", \"Tolyatti\"),\n" +
            "  (19, \"Goodwin\", \"06-04-03\", \"Solnechnogorsk\"),\n" +
            "  (5, \"Dillon\", \"08-24-19\", \"Oryol\"),\n" +
            "  (30, \"Mosley\", \"08-02-15\", \"Sakhalin\"),\n" +
            "  (25, \"Livingston\", \"12-23-03\", \"Novgorod\"),\n" +
            "  (27, \"Holcomb\", \"03-13-04\", \"Omsk\"),\n" +
            "  (9, \"Meyer\", \"04-17-12\", \"Lipetsk\"),\n" +
            "  (25, \"Ochoa\", \"11-11-21\", \"Magadan\"),\n" +
            "  (30, \"Meyers\", \"08-13-06\", \"Tomsk\"),\n" +
            "  (23, \"Price\", \"04-17-11\", \"Yekaterinburg\"),\n" +
            "  (22, \"Newman\", \"04-10-23\", \"Rostov\"),\n" +
            "  (11, \"Stokes\", \"01-16-04\", \"Sakhalin\"),\n" +
            "  (29, \"Todd\", \"12-01-19\", \"Sevastopol\"),\n" +
            "  (21, \"Wilkinson\", \"10-09-08\", \"Gatchina\"),\n" +
            "  (6, \"Pruitt\", \"09-28-07\", \"Irkutsk\"),\n" +
            "  (31, \"Conway\", \"05-31-18\", \"Nizhny\"),\n" +
            "  (21, \"Madden\", \"01-23-08\", \"Novosibirsk\"),\n" +
            "  (6, \"Berry\", \"03-21-19\", \"Oryol\"),\n" +
            "  (1, \"Buckner\", \"02-08-08\", \"Verkhny Ufaley\"),\n" +
            "  (17, \"Navarro\", \"03-19-06\", \"Saint Petersburg\"),\n" +
            "  (11, \"Santos\", \"01-19-18\", \"Penza\"),\n" +
            "  (12, \"Bartlett\", \"09-15-13\", \"Ulyanovsk\"),\n" +
            "  (3, \"Harrington\", \"09-15-08\", \"Kemerovo\"),\n" +
            "  (8, \"Eaton\", \"04-30-09\", \"Kaliningrad\"),\n" +
            "  (13, \"Gillespie\", \"07-20-16\", \"Kostroma\"),\n" +
            "  (14, \"Lowery\", \"06-24-02\", \"Kursk\"),\n" +
            "  (25, \"Logan\", \"09-02-06\", \"Yekaterinburg\"),\n" +
            "  (25, \"Hatfield\", \"08-31-02\", \"Ulyanovsk\"),\n" +
            "  (23, \"Adams\", \"02-10-09\", \"Kostroma\"),\n" +
            "  (14, \"Kramer\", \"05-23-15\", \"Gatchina\"),\n" +
            "  (22, \"Horton\", \"04-13-09\", \"Ulyanovsk\"),\n" +
            "  (20, \"Graham\", \"11-06-08\", \"Kostroma\"),\n" +
            "  (24, \"Mayer\", \"07-21-16\", \"Krasnaya Yaruga\"),\n" +
            "  (2, \"Mcintyre\", \"08-02-20\", \"Kirov\"),\n" +
            "  (15, \"Bell\", \"08-17-13\", \"Yekaterinburg\"),\n" +
            "  (12, \"Vincent\", \"06-13-15\", \"Kirov\"),\n" +
            "  (6, \"Koch\", \"01-21-15\", \"Kemerovo\"),\n" +
            "  (30, \"Berry\", \"01-05-16\", \"Murmansk\"),\n" +
            "  (28, \"Strong\", \"03-26-04\", \"Saratov\"),\n" +
            "  (19, \"Leonard\", \"10-04-13\", \"Kostroma\"),\n" +
            "  (28, \"Mitchell\", \"06-18-19\", \"Tula\"),\n" +
            "  (12, \"Wong\", \"11-15-14\", \"Valuyki\"),\n" +
            "  (3, \"Duffy\", \"10-13-04\", \"Ryazan\"),\n" +
            "  (14, \"Pruitt\", \"07-27-19\", \"Saint Petersburg\"),\n" +
            "  (12, \"Fleming\", \"12-16-03\", \"Tambov\"),\n" +
            "  (27, \"Hayes\", \"04-01-16\", \"Kirov\"),\n" +
            "  (15, \"Webster\", \"10-04-13\", \"Yaroslavl\"),\n" +
            "  (12, \"Robinson\", \"05-27-01\", \"Yekaterinburg\"),\n" +
            "  (13, \"Weber\", \"01-17-02\", \"Volgograd\"),\n" +
            "  (4, \"Klein\", \"07-14-04\", \"Kaliningrad\"),\n" +
            "  (22, \"Bradshaw\", \"03-05-03\", \"Magadan\"),\n" +
            "  (19, \"Casey\", \"06-13-01\", \"Volgograd\"),\n" +
            "  (8, \"Davis\", \"04-12-11\", \"Zlatoust\"),\n" +
            "  (30, \"Duffy\", \"09-08-01\", \"Belgorod\"),\n" +
            "  (19, \"Rojas\", \"06-06-07\", \"Irkutsk\"),\n" +
            "  (8, \"Bryant\", \"09-13-16\", \"Ivangorod\"),\n" +
            "  (3, \"Brady\", \"05-15-14\", \"Lipetsk\"),\n" +
            "  (16, \"Howe\", \"03-12-07\", \"Novodvinsk\"),\n" +
            "  (25, \"Garner\", \"01-03-07\", \"Tomsk\"),\n" +
            "  (5, \"Guerrero\", \"07-07-11\", \"Nizhny\"),\n" +
            "  (5, \"Ayers\", \"09-11-17\", \"Saratov\"),\n" +
            "  (18, \"Orr\", \"01-08-13\", \"Penza\"),\n" +
            "  (25, \"Simon\", \"08-16-13\", \"Kaluga\"),\n" +
            "  (10, \"Peck\", \"04-27-04\", \"Tomsk\"),\n" +
            "  (10, \"Farrell\", \"01-05-04\", \"Tver\"),\n" +
            "  (16, \"Russell\", \"02-27-01\", \"Saint Petersburg\"),\n" +
            "  (12, \"Acevedo\", \"03-04-06\", \"Moscow\"),\n" +
            "  (9, \"Perry\", \"03-19-21\", \"Yuryuzan\"),\n" +
            "  (15, \"Moody\", \"01-15-14\", \"Ust-Katav\"),\n" +
            "  (3, \"Keller\", \"04-01-08\", \"Penza\"),\n" +
            "  (23, \"Mckay\", \"09-07-10\", \"Tomsk\"),\n" +
            "  (22, \"Barrett\", \"01-11-23\", \"Lipetsk\"),\n" +
            "  (20, \"Alford\", \"09-13-12\", \"Znamensk\"),\n" +
            "  (3, \"Workman\", \"04-17-05\", \"Zlatoust\"),\n" +
            "  (29, \"Hurst\", \"09-30-15\", \"Tomsk\"),\n" +
            "  (17, \"Buckner\", \"12-09-22\", \"Tambov\"),\n" +
            "  (27, \"Everett\", \"12-27-05\", \"Tomsk\"),\n" +
            "  (24, \"Park\", \"01-08-01\", \"Rostov\"),\n" +
            "  (27, \"Dixon\", \"10-31-23\", \"Kursk\"),\n" +
            "  (10, \"Snyder\", \"09-11-23\", \"Rostov\"),\n" +
            "  (24, \"Jackson\", \"11-07-08\", \"Kaluga\"),\n" +
            "  (24, \"Yang\", \"02-12-10\", \"Gatchina\"),\n" +
            "  (25, \"Petersen\", \"08-04-09\", \"Ulyanovsk\"),\n" +
            "  (23, \"Lester\", \"06-27-23\", \"Lipetsk\"),\n" +
            "  (28, \"Sellers\", \"07-29-21\", \"Oryol\"),\n" +
            "  (22, \"Cooper\", \"09-18-10\", \"Belgorod\"),\n" +
            "  (20, \"Neal\", \"05-03-01\", \"Vialky\"),\n" +
            "  (8, \"Fry\", \"05-26-15\", \"Tambov\"),\n" +
            "  (14, \"Kent\", \"05-08-22\", \"Kemerovo\"),\n" +
            "  (17, \"Wilcox\", \"11-24-14\", \"Oryol\"),\n" +
            "  (3, \"Fitzgerald\", \"02-08-14\", \"Kirov\"),\n" +
            "  (8, \"Lowe\", \"03-31-10\", \"Oryol\"),\n" +
            "  (16, \"Haley\", \"05-27-07\", \"Pskov\"),\n" +
            "  (11, \"Sweet\", \"07-20-08\", \"Tambov\"),\n" +
            "  (10, \"Miles\", \"09-12-04\", \"Vologda\"),\n" +
            "  (19, \"Vinson\", \"09-27-10\", \"Kurgan\"),\n" +
            "  (21, \"Rocha\", \"04-27-17\", \"Biryuch\"),\n" +
            "  (1, \"Moore\", \"11-27-18\", \"Novosibirsk\"),\n" +
            "  (8, \"Flores\", \"04-16-06\", \"Ulyanovsk\"),\n" +
            "  (8, \"Maxwell\", \"12-09-20\", \"Akhtubinsk\"),\n" +
            "  (26, \"Savage\", \"06-16-03\", \"Saratov\"),\n" +
            "  (24, \"Le\", \"08-23-15\", \"Nizhny\"),\n" +
            "  (31, \"Mayer\", \"04-03-07\", \"Oryol\"),\n" +
            "  (20, \"Dickson\", \"03-30-19\", \"Orenburg\"),\n" +
            "  (26, \"Schneider\", \"11-23-02\", \"Yaroslavl\"),\n" +
            "  (22, \"Farmer\", \"02-03-14\", \"Belgorod\"),\n" +
            "  (17, \"Fitzpatrick\", \"08-20-07\", \"Voronezh\"),\n" +
            "  (3, \"Mcintyre\", \"11-13-10\", \"Gatchina\"),\n" +
            "  (2, \"Beach\", \"01-03-23\", \"Novgorod\"),\n" +
            "  (31, \"Wilkinson\", \"02-20-13\", \"Ryazan\"),\n" +
            "  (7, \"Moran\", \"09-14-21\", \"Yaroslavl\"),\n" +
            "  (8, \"Potter\", \"03-04-09\", \"Kemerovo\"),\n" +
            "  (14, \"Owens\", \"03-21-05\", \"Sevastopol\"),\n" +
            "  (20, \"Bell\", \"10-22-19\", \"Yaroslavl\"),\n" +
            "  (2, \"Tanner\", \"01-11-02\", \"Novosibirsk\"),\n" +
            "  (24, \"Howard\", \"07-05-06\", \"Volgograd\"),\n" +
            "  (14, \"Joyce\", \"03-05-20\", \"Novgorod\");\n";

    public static String SQL_FILL_PROGRESS = "INSERT INTO PROGRESS\n" +
            "  (IDSUBJECT,IDSTUDENT,EXAMDATE,MARK,TEACHER)\n" +
            "VALUES\n" +
            "  (4, 10, \"07-31-22\", 2, \"Wilder\"),\n" +
            "  (5, 67, \"12-27-22\", 2, \"Donaldson\"),\n" +
            "  (1, 149, \"03-05-23\", 4, \"Barry\"),\n" +
            "  (2, 16, \"08-21-22\", 4, \"Massey\"),\n" +
            "  (3, 236, \"09-09-23\", 4, \"Wolf\"),\n" +
            "  (4, 256, \"02-17-22\", 2, \"Pate\"),\n" +
            "  (5, 179, \"03-07-22\", 9, \"Chen\"),\n" +
            "  (1, 196, \"08-06-22\", 10, \"Phelps\"),\n" +
            "  (2, 266, \"11-06-23\", 9, \"Morin\"),\n" +
            "  (3, 17, \"03-01-22\", 7, \"Casey\"),\n" +
            "  (4, 112, \"11-13-22\", 6, \"Payne\"),\n" +
            "  (5, 162, \"04-27-23\", 4, \"Hardy\"),\n" +
            "  (1, 237, \"07-06-22\", 5, \"Potter\"),\n" +
            "  (2, 232, \"08-28-22\", 3, \"Buchanan\"),\n" +
            "  (3, 282, \"09-26-22\", 2, \"Saunders\"),\n" +
            "  (4, 56, \"09-06-22\", 8, \"Dixon\"),\n" +
            "  (5, 228, \"04-08-23\", 4, \"Hood\"),\n" +
            "  (1, 28, \"08-13-22\", 4, \"Spencer\"),\n" +
            "  (2, 269, \"04-10-22\", 9, \"Williamson\"),\n" +
            "  (3, 151, \"10-19-23\", 10, \"Mccall\"),\n" +
            "  (4, 167, \"10-09-22\", 10, \"Cruz\"),\n" +
            "  (5, 226, \"09-29-23\", 4, \"Gomez\"),\n" +
            "  (1, 105, \"02-06-22\", 8, \"Olson\"),\n" +
            "  (2, 217, \"09-26-22\", 4, \"Sellers\"),\n" +
            "  (3, 34, \"08-23-22\", 5, \"Flowers\"),\n" +
            "  (4, 206, \"01-26-22\", 9, \"Cash\"),\n" +
            "  (5, 203, \"04-30-23\", 9, \"Vargas\"),\n" +
            "  (1, 183, \"05-05-23\", 3, \"Cabrera\"),\n" +
            "  (2, 148, \"10-11-22\", 10, \"Kirk\"),\n" +
            "  (3, 254, \"01-01-22\", 8, \"Battle\"),\n" +
            "  (4, 66, \"12-31-22\", 4, \"Gilliam\"),\n" +
            "  (5, 263, \"09-15-22\", 9, \"Lyons\"),\n" +
//            "  (8, 245, \"02-22-23\", 2, \"Mayo\"),\n" +
//            "  (9, 263, \"06-28-23\", 3, \"Beard\"),\n" +
//            "  (10, 256, \"07-26-22\", 2, \"Smith\"),\n" +
//            "  (11, 220, \"08-21-23\", 3, \"Case\"),\n" +
            "  (1, 294, \"04-24-22\", 6, \"Vance\"),\n" +
            "  (2, 21, \"10-25-22\", 9, \"Kline\"),\n" +
            "  (3, 98, \"05-07-22\", 10, \"Lancaster\"),\n" +
            "  (4, 1, \"10-28-22\", 1, \"Daniel\"),\n" +
            "  (5, 240, \"12-20-21\", 4, \"Norton\"),\n" +
//            "  (6, 67, \"05-29-22\", 1, \"Welch\"),\n" +
//            "  (7, 192, \"02-07-22\", 10, \"Vincent\"),\n" +
//            "  (8, 277, \"07-18-22\", 7, \"Rojas\"),\n" +
//            "  (9, 238, \"03-27-23\", 1, \"Valencia\"),\n" +
//            "  (10, 248, \"03-21-22\", 6, \"Berger\"),\n" +
//            "  (11, 5, \"12-14-22\", 8, \"Weiss\"),\n" +
            "  (1, 267, \"04-23-22\", 8, \"Bowman\"),\n" +
            "  (2, 115, \"08-15-23\", 8, \"Bass\"),\n" +
            "  (3, 275, \"11-12-23\", 8, \"Bartlett\"),\n" +
            "  (4, 46, \"10-27-22\", 7, \"Pugh\"),\n" +
            "  (5, 265, \"06-29-22\", 5, \"Pugh\"),\n" +
//            "  (6, 199, \"05-31-23\", 9, \"Huffman\"),\n" +
//            "  (7, 221, \"08-08-22\", 8, \"Adams\"),\n" +
//            "  (8, 164, \"08-06-23\", 7, \"Fitzpatrick\"),\n" +
//            "  (9, 149, \"10-07-23\", 2, \"Summers\"),\n" +
//            "  (10, 44, \"04-23-23\", 6, \"Harding\"),\n" +
//            "  (11, 147, \"06-03-23\", 4, \"May\"),\n" +
            "  (1, 70, \"06-19-23\", 5, \"Drake\"),\n" +
            "  (2, 94, \"04-06-23\", 6, \"Gaines\"),\n" +
            "  (3, 16, \"05-23-22\", 6, \"Carlson\"),\n" +
            "  (4, 213, \"10-11-22\", 9, \"Mcknight\"),\n" +
            "  (5, 107, \"04-25-23\", 4, \"Mcpherson\"),\n" +
//            "  (6, 235, \"12-01-21\", 2, \"Gallagher\"),\n" +
//            "  (7, 170, \"11-21-21\", 9, \"Koch\"),\n" +
//            "  (8, 146, \"10-18-23\", 9, \"Guy\"),\n" +
//            "  (9, 169, \"12-11-21\", 5, \"Golden\"),\n" +
//            "  (10, 191, \"12-01-21\", 5, \"Bray\"),\n" +
//            "  (11, 132, \"02-15-23\", 7, \"Burks\"),\n" +
            "  (1, 272, \"12-10-21\", 5, \"Hoffman\"),\n" +
            "  (2, 182, \"08-06-22\", 10, \"Park\"),\n" +
            "  (3, 224, \"06-12-23\", 2, \"Mckenzie\"),\n" +
            "  (4, 87, \"12-25-22\", 3, \"Sheppard\"),\n" +
            "  (5, 112, \"06-11-23\", 8, \"Dudley\"),\n" +
//            "  (6, 10, \"08-30-22\", 6, \"Kirby\"),\n" +
//            "  (7, 167, \"07-01-23\", 7, \"Myers\"),\n" +
//            "  (8, 249, \"05-27-23\", 2, \"Mills\"),\n" +
//            "  (9, 73, \"12-21-21\", 10, \"Daugherty\"),\n" +
//            "  (10, 137, \"03-19-22\", 9, \"Simmons\"),\n" +
//            "  (11, 46, \"03-09-22\", 4, \"Wheeler\"),\n" +
            "  (1, 57, \"08-12-22\", 2, \"Merrill\"),\n" +
            "  (2, 108, \"05-12-23\", 2, \"Jacobson\"),\n" +
            "  (3, 230, \"04-01-23\", 2, \"Everett\"),\n" +
            "  (4, 41, \"01-11-22\", 4, \"Porter\"),\n" +
            "  (5, 168, \"05-01-23\", 9, \"Morales\"),\n" +
//            "  (6, 222, \"03-09-22\", 7, \"Salazar\"),\n" +
//            "  (7, 66, \"08-09-22\", 8, \"Gregory\"),\n" +
//            "  (8, 171, \"02-11-23\", 2, \"Reyes\"),\n" +
//            "  (9, 130, \"03-01-22\", 8, \"Robinson\"),\n" +
//            "  (10, 132, \"04-09-22\", 5, \"Love\"),\n" +
//            "  (11, 75, \"10-26-22\", 2, \"Murphy\"),\n" +
            "  (1, 53, \"01-15-23\", 4, \"Potter\"),\n" +
            "  (2, 8, \"05-26-23\", 9, \"Mcdowell\"),\n" +
            "  (3, 12, \"08-27-22\", 5, \"Duffy\"),\n" +
            "  (4, 173, \"09-06-23\", 3, \"Hayden\"),\n" +
            "  (5, 121, \"02-28-23\", 9, \"Carter\"),\n" +
//            "  (6, 97, \"07-04-22\", 7, \"Noel\"),\n" +
//            "  (7, 57, \"07-26-23\", 6, \"Harmon\"),\n" +
//            "  (8, 280, \"03-26-23\", 8, \"Paul\"),\n" +
//            "  (9, 232, \"08-04-23\", 5, \"Romero\"),\n" +
//            "  (10, 260, \"12-06-22\", 7, \"Petty\"),\n" +
//            "  (11, 43, \"11-29-22\", 5, \"Villarreal\"),\n" +
            "  (1, 61, \"12-17-21\", 6, \"Ramos\"),\n" +
            "  (2, 89, \"08-25-22\", 4, \"Spears\"),\n" +
            "  (3, 152, \"05-03-22\", 3, \"Barry\"),\n" +
            "  (4, 79, \"12-09-22\", 8, \"Townsend\"),\n" +
            "  (5, 10, \"09-02-23\", 2, \"Marquez\"),\n" +
//            "  (6, 289, \"06-15-23\", 10, \"Gutierrez\"),\n" +
//            "  (7, 91, \"01-06-23\", 6, \"Sears\"),\n" +
//            "  (8, 166, \"07-19-22\", 1, \"Lowe\"),\n" +
//            "  (9, 166, \"06-29-23\", 8, \"Campbell\"),\n" +
//            "  (10, 147, \"03-11-23\", 3, \"Bentley\"),\n" +
//            "  (11, 101, \"08-31-23\", 7, \"Wolf\"),\n" +
            "  (1, 150, \"09-02-22\", 8, \"Marquez\"),\n" +
            "  (2, 78, \"11-25-21\", 5, \"Ochoa\"),\n" +
            "  (3, 251, \"07-06-22\", 6, \"Huffman\"),\n" +
            "  (4, 154, \"11-04-22\", 3, \"Walker\"),\n" +
            "  (5, 186, \"04-19-22\", 7, \"Rose\"),\n" +
//            "  (6, 163, \"03-02-23\", 7, \"Mosley\"),\n" +
//            "  (7, 264, \"02-28-23\", 9, \"Pugh\"),\n" +
//            "  (8, 207, \"02-04-23\", 4, \"Bentley\"),\n" +
//            "  (9, 39, \"01-13-23\", 5, \"Simmons\"),\n" +
//            "  (10, 260, \"03-10-23\", 2, \"Conley\"),\n" +
//            "  (11, 160, \"04-27-23\", 9, \"Strong\"),\n" +
            "  (1, 208, \"03-09-23\", 7, \"Raymond\"),\n" +
            "  (2, 56, \"12-03-22\", 3, \"Boyle\"),\n" +
            "  (3, 165, \"11-24-21\", 5, \"Chavez\"),\n" +
            "  (4, 215, \"03-15-23\", 5, \"Ortiz\"),\n" +
            "  (5, 79, \"05-13-22\", 9, \"Cobb\"),\n" +
//            "  (6, 263, \"07-10-23\", 8, \"Ramirez\"),\n" +
//            "  (7, 283, \"10-08-22\", 2, \"Ward\"),\n" +
//            "  (8, 114, \"08-13-23\", 6, \"Foley\"),\n" +
//            "  (9, 51, \"04-17-23\", 2, \"Dunn\"),\n" +
//            "  (10, 107, \"08-22-23\", 8, \"Bruce\"),\n" +
//            "  (11, 78, \"07-21-23\", 5, \"Chase\"),\n" +
            "  (1, 231, \"11-03-22\", 4, \"Frost\"),\n" +
            "  (2, 170, \"04-14-23\", 3, \"Hill\"),\n" +
            "  (3, 237, \"01-06-23\", 2, \"Garza\"),\n" +
            "  (4, 68, \"03-07-23\", 2, \"Richard\"),\n" +
            "  (5, 273, \"07-08-22\", 9, \"Vinson\"),\n" +
//            "  (6, 152, \"12-25-21\", 3, \"Moore\"),\n" +
//            "  (7, 193, \"03-16-22\", 7, \"Clemons\"),\n" +
//            "  (8, 162, \"08-13-23\", 8, \"Gonzales\"),\n" +
//            "  (9, 83, \"12-11-21\", 8, \"Sanford\"),\n" +
//            "  (10, 76, \"01-07-23\", 2, \"Whitfield\"),\n" +
//            "  (11, 213, \"10-21-22\", 7, \"Walls\"),\n" +
            "  (1, 138, \"11-30-22\", 5, \"Dorsey\"),\n" +
            "  (2, 134, \"10-06-23\", 6, \"Schneider\"),\n" +
            "  (3, 124, \"09-06-22\", 4, \"Santiago\"),\n" +
            "  (4, 287, \"07-29-23\", 8, \"Lindsay\"),\n" +
            "  (5, 287, \"05-18-22\", 6, \"Crane\"),\n" +
//            "  (6, 121, \"01-09-23\", 3, \"Irwin\"),\n" +
//            "  (7, 246, \"05-05-23\", 8, \"Wall\"),\n" +
//            "  (8, 87, \"07-16-22\", 8, \"Park\"),\n" +
//            "  (9, 83, \"12-11-22\", 10, \"Bennett\"),\n" +
//            "  (10, 214, \"08-13-23\", 6, \"Bullock\"),\n" +
//            "  (11, 262, \"11-30-21\", 9, \"Schultz\"),\n" +
            "  (1, 254, \"06-19-23\", 10, \"Carver\"),\n" +
            "  (2, 166, \"11-25-22\", 1, \"Dominguez\"),\n" +
            "  (3, 219, \"01-20-22\", 3, \"Navarro\"),\n" +
            "  (4, 9, \"06-02-23\", 1, \"Brown\"),\n" +
            "  (5, 97, \"01-23-23\", 4, \"Pugh\"),\n" +
//            "  (6, 42, \"06-19-23\", 4, \"Mooney\"),\n" +
//            "  (7, 20, \"03-31-23\", 3, \"Garza\"),\n" +
//            "  (8, 252, \"08-24-23\", 2, \"Cortez\"),\n" +
//            "  (9, 54, \"10-21-23\", 4, \"Cantrell\"),\n" +
//            "  (10, 97, \"03-03-23\", 9, \"Hampton\"),\n" +
//            "  (11, 280, \"07-25-23\", 10, \"Stark\"),\n" +
            "  (1, 183, \"09-23-23\", 8, \"Estes\"),\n" +
            "  (2, 122, \"11-09-23\", 10, \"Rogers\"),\n" +
            "  (3, 246, \"04-16-23\", 6, \"Bender\"),\n" +
            "  (4, 251, \"10-08-23\", 5, \"Atkinson\"),\n" +
            "  (5, 204, \"11-15-23\", 4, \"Noel\"),\n" +
//            "  (6, 179, \"07-14-23\", 5, \"Clarke\"),\n" +
//            "  (7, 84, \"12-02-21\", 6, \"Contreras\"),\n" +
//            "  (8, 261, \"07-19-22\", 4, \"Schroeder\"),\n" +
//            "  (9, 116, \"07-26-22\", 4, \"Hanson\"),\n" +
//            "  (10, 168, \"11-19-21\", 10, \"Cohen\"),\n" +
//            "  (11, 114, \"12-09-22\", 8, \"Elliott\"),\n" +
            "  (1, 177, \"11-23-21\", 9, \"Patterson\"),\n" +
            "  (2, 99, \"05-21-22\", 3, \"Kirkland\"),\n" +
            "  (3, 48, \"12-11-21\", 5, \"Deleon\"),\n" +
            "  (4, 249, \"06-06-22\", 7, \"Harrington\"),\n" +
            "  (5, 288, \"10-02-23\", 5, \"Franks\"),\n" +
//            "  (6, 122, \"04-24-23\", 2, \"Parsons\"),\n" +
//            "  (7, 83, \"08-18-23\", 5, \"Richard\"),\n" +
//            "  (8, 1, \"05-08-22\", 4, \"Bruce\"),\n" +
//            "  (9, 176, \"09-26-23\", 9, \"Russo\"),\n" +
//            "  (10, 113, \"08-23-23\", 2, \"Giles\"),\n" +
//            "  (11, 127, \"11-03-22\", 5, \"Morton\"),\n" +
            "  (1, 80, \"01-13-22\", 7, \"Crane\"),\n" +
            "  (2, 293, \"10-11-22\", 4, \"Kaufman\"),\n" +
            "  (3, 32, \"06-01-22\", 3, \"Rogers\"),\n" +
            "  (4, 146, \"08-07-22\", 10, \"Ross\"),\n" +
            "  (5, 110, \"11-19-21\", 2, \"Harrell\"),\n" +
//            "  (6, 233, \"09-25-23\", 6, \"Pennington\"),\n" +
//            "  (7, 184, \"03-27-22\", 8, \"Ferrell\"),\n" +
//            "  (8, 244, \"02-04-22\", 5, \"Dotson\"),\n" +
//            "  (9, 176, \"05-14-23\", 7, \"Klein\"),\n" +
//            "  (10, 135, \"04-11-23\", 10, \"Poole\"),\n" +
//            "  (11, 26, \"12-20-21\", 2, \"Hogan\"),\n" +
            "  (1, 255, \"05-10-22\", 1, \"Cardenas\"),\n" +
            "  (2, 251, \"12-25-21\", 5, \"Craig\"),\n" +
            "  (3, 195, \"08-18-22\", 4, \"Sears\"),\n" +
            "  (4, 198, \"06-09-23\", 5, \"Clemons\"),\n" +
            "  (5, 217, \"03-21-23\", 3, \"Gaines\"),\n" +
//            "  (6, 149, \"05-30-22\", 2, \"Bond\"),\n" +
//            "  (7, 214, \"06-25-23\", 9, \"Alford\"),\n" +
//            "  (8, 265, \"11-13-23\", 5, \"Cash\"),\n" +
//            "  (9, 99, \"01-15-22\", 3, \"Chambers\"),\n" +
//            "  (10, 176, \"12-04-22\", 6, \"Bates\"),\n" +
//            "  (11, 236, \"08-17-22\", 2, \"Osborn\"),\n" +
            "  (1, 126, \"05-28-22\", 7, \"Heath\"),\n" +
            "  (2, 129, \"07-03-23\", 8, \"Hensley\"),\n" +
            "  (3, 250, \"12-11-22\", 2, \"Livingston\"),\n" +
            "  (4, 226, \"05-23-22\", 3, \"Chavez\"),\n" +
            "  (5, 159, \"01-24-23\", 1, \"Orr\"),\n" +
//            "  (6, 265, \"04-09-23\", 6, \"Rosa\"),\n" +
//            "  (7, 212, \"10-04-22\", 2, \"Hinton\"),\n" +
//            "  (8, 65, \"02-23-22\", 4, \"Tanner\"),\n" +
//            "  (9, 166, \"09-18-23\", 7, \"Leon\"),\n" +
//            "  (10, 261, \"05-25-23\", 9, \"Warner\"),\n" +
//            "  (11, 21, \"07-02-22\", 9, \"Acevedo\"),\n" +
            "  (1, 167, \"11-21-22\", 6, \"Hoover\"),\n" +
            "  (2, 264, \"02-23-23\", 8, \"Ingram\"),\n" +
            "  (3, 164, \"08-03-23\", 2, \"Beach\"),\n" +
            "  (4, 223, \"04-13-22\", 6, \"Woodard\"),\n" +
            "  (5, 48, \"04-13-22\", 6, \"Cantu\"),\n" +
//            "  (6, 241, \"09-05-23\", 10, \"Kemp\"),\n" +
//            "  (7, 167, \"08-02-22\", 9, \"Cherry\"),\n" +
//            "  (8, 224, \"01-21-23\", 7, \"Kirby\"),\n" +
//            "  (9, 177, \"01-22-22\", 2, \"Fletcher\"),\n" +
//            "  (10, 135, \"03-31-23\", 9, \"Haney\"),\n" +
//            "  (11, 46, \"06-02-23\", 7, \"Mathis\"),\n" +
            "  (1, 130, \"07-11-22\", 8, \"Goff\"),\n" +
            "  (2, 147, \"04-26-23\", 7, \"Reeves\"),\n" +
            "  (3, 71, \"09-22-23\", 8, \"Dodson\"),\n" +
            "  (4, 276, \"11-11-23\", 6, \"Huffman\"),\n" +
            "  (5, 61, \"01-27-23\", 6, \"Bishop\"),\n" +
//            "  (6, 106, \"03-12-23\", 8, \"Gilliam\"),\n" +
//            "  (7, 69, \"11-17-23\", 1, \"Wooten\"),\n" +
//            "  (8, 6, \"08-15-22\", 4, \"Case\"),\n" +
//            "  (9, 167, \"10-30-22\", 6, \"Wall\"),\n" +
//            "  (10, 96, \"09-06-22\", 8, \"Rodriquez\"),\n" +
//            "  (11, 286, \"09-03-22\", 7, \"Knox\"),\n" +
            "  (1, 164, \"06-27-23\", 2, \"Owens\"),\n" +
            "  (2, 296, \"09-25-22\", 3, \"Garza\"),\n" +
            "  (3, 260, \"07-06-23\", 8, \"Floyd\"),\n" +
            "  (4, 149, \"12-05-21\", 7, \"Frederick\"),\n" +
            "  (5, 59, \"07-18-23\", 7, \"Elliott\"),\n" +
//            "  (6, 295, \"11-20-22\", 1, \"Beach\"),\n" +
//            "  (7, 113, \"07-09-22\", 8, \"Franklin\"),\n" +
//            "  (8, 119, \"07-31-22\", 6, \"Duncan\"),\n" +
//            "  (9, 17, \"11-12-23\", 10, \"Ashley\"),\n" +
//            "  (10, 45, \"08-27-23\", 9, \"Mayo\"),\n" +
//            "  (11, 84, \"01-23-23\", 10, \"Grimes\"),\n" +
            "  (1, 154, \"10-02-23\", 9, \"Briggs\"),\n" +
            "  (2, 46, \"02-15-23\", 5, \"Tyson\"),\n" +
            "  (3, 232, \"03-26-23\", 8, \"Lancaster\"),\n" +
            "  (4, 50, \"01-09-23\", 8, \"Bolton\"),\n" +
            "  (5, 106, \"12-21-22\", 2, \"Acevedo\"),\n" +
//            "  (6, 288, \"02-27-23\", 3, \"Livingston\"),\n" +
//            "  (7, 4, \"08-17-23\", 5, \"Gibbs\"),\n" +
//            "  (8, 257, \"08-12-23\", 6, \"Gillespie\"),\n" +
//            "  (9, 108, \"10-07-23\", 10, \"Tyler\"),\n" +
//            "  (10, 216, \"09-09-22\", 8, \"Frank\"),\n" +
//            "  (11, 58, \"01-26-23\", 3, \"Mendoza\"),\n" +
            "  (1, 109, \"05-19-23\", 9, \"Craft\"),\n" +
            "  (2, 215, \"04-29-23\", 4, \"Anderson\"),\n" +
            "  (3, 17, \"05-01-22\", 2, \"Barlow\"),\n" +
            "  (4, 192, \"02-01-23\", 8, \"Mercer\"),\n" +
            "  (5, 261, \"11-08-22\", 8, \"Webster\"),\n" +
//            "  (6, 58, \"05-21-23\", 4, \"Mcknight\"),\n" +
//            "  (7, 88, \"12-15-22\", 5, \"Hess\"),\n" +
//            "  (8, 143, \"01-28-23\", 6, \"Boyer\"),\n" +
//            "  (9, 111, \"09-20-22\", 2, \"Ramos\"),\n" +
//            "  (10, 221, \"01-08-23\", 9, \"Mejia\"),\n" +
//            "  (11, 267, \"11-25-22\", 2, \"Winters\"),\n" +
            "  (1, 93, \"02-19-22\", 6, \"Hammond\"),\n" +
            "  (2, 285, \"11-24-21\", 7, \"Harding\"),\n" +
            "  (3, 189, \"02-27-22\", 5, \"Keith\"),\n" +
            "  (4, 247, \"08-23-22\", 4, \"Velez\"),\n" +
            "  (5, 92, \"03-22-23\", 8, \"Vasquez\"),\n" +
//            "  (6, 178, \"07-31-23\", 4, \"Price\"),\n" +
//            "  (7, 160, \"03-23-22\", 3, \"Clemons\"),\n" +
//            "  (8, 232, \"02-09-22\", 8, \"Bates\"),\n" +
//            "  (9, 104, \"10-29-23\", 3, \"Gates\"),\n" +
//            "  (10, 171, \"06-05-22\", 6, \"Perez\"),\n" +
//            "  (11, 155, \"09-18-22\", 3, \"Mccullough\"),\n" +
            "  (1, 209, \"09-02-23\", 9, \"Turner\"),\n" +
            "  (2, 195, \"11-06-23\", 4, \"Walter\"),\n" +
            "  (3, 221, \"01-06-22\", 7, \"Mckee\"),\n" +
            "  (4, 105, \"06-09-23\", 9, \"Hurley\"),\n" +
            "  (5, 107, \"10-21-23\", 9, \"Durham\"),\n" +
//            "  (6, 201, \"06-21-23\", 2, \"Herrera\"),\n" +
//            "  (7, 293, \"04-02-22\", 1, \"Mcknight\"),\n" +
//            "  (8, 198, \"09-03-23\", 4, \"Mclaughlin\"),\n" +
//            "  (9, 227, \"10-10-23\", 2, \"Hester\"),\n" +
//            "  (10, 94, \"05-31-23\", 4, \"Blackburn\"),\n" +
//            "  (11, 124, \"09-20-23\", 4, \"Valentine\"),\n" +
            "  (1, 109, \"05-01-23\", 6, \"Conley\"),\n" +
            "  (2, 71, \"11-25-21\", 9, \"Finley\"),\n" +
            "  (3, 22, \"06-27-23\", 10, \"Wilkinson\"),\n" +
            "  (4, 216, \"08-20-23\", 10, \"Clements\"),\n" +
            "  (5, 262, \"08-14-22\", 6, \"Dotson\"),\n" +
//            "  (6, 100, \"04-21-22\", 3, \"Savage\"),\n" +
//            "  (7, 257, \"03-23-22\", 8, \"Mathis\"),\n" +
//            "  (8, 70, \"02-18-22\", 6, \"Neal\"),\n" +
//            "  (9, 292, \"12-25-22\", 6, \"Taylor\"),\n" +
//            "  (10, 205, \"10-04-22\", 9, \"O'connor\"),\n" +
//            "  (11, 285, \"10-14-23\", 2, \"Love\"),\n" +
            "  (1, 139, \"08-08-22\", 10, \"Castaneda\"),\n" +
            "  (2, 186, \"12-07-21\", 9, \"Ward\"),\n" +
            "  (3, 43, \"02-28-23\", 7, \"Shepard\"),\n" +
            "  (4, 185, \"04-17-23\", 7, \"Lyons\"),\n" +
            "  (5, 174, \"09-24-22\", 5, \"Brewer\"),\n" +
//            "  (6, 87, \"03-25-22\", 1, \"Roman\"),\n" +
//            "  (7, 130, \"08-21-22\", 3, \"Carey\"),\n" +
//            "  (8, 63, \"12-24-21\", 8, \"Riggs\"),\n" +
//            "  (9, 26, \"11-06-22\", 10, \"Clark\"),\n" +
//            "  (10, 27, \"06-25-23\", 7, \"Fletcher\"),\n" +
//            "  (11, 83, \"12-19-22\", 9, \"Campos\"),\n" +
            "  (1, 130, \"02-02-22\", 7, \"Zimmerman\"),\n" +
            "  (2, 191, \"05-09-22\", 10, \"Hurley\"),\n" +
            "  (3, 208, \"03-10-23\", 6, \"Heath\"),\n" +
            "  (4, 188, \"06-19-23\", 9, \"Holland\"),\n" +
            "  (5, 283, \"03-02-23\", 8, \"Sexton\"),\n" +
//            "  (6, 8, \"02-05-23\", 2, \"Carney\"),\n" +
//            "  (7, 144, \"02-15-23\", 3, \"Alston\"),\n" +
//            "  (8, 220, \"10-11-22\", 7, \"Hatfield\"),\n" +
//            "  (9, 199, \"12-08-21\", 5, \"Watson\"),\n" +
//            "  (10, 128, \"12-20-21\", 4, \"Goodwin\"),\n" +
//            "  (11, 62, \"09-17-22\", 9, \"Hodge\"),\n" +
            "  (1, 231, \"01-11-22\", 6, \"Gutierrez\"),\n" +
            "  (2, 272, \"04-01-22\", 5, \"Hunter\"),\n" +
            "  (3, 33, \"01-23-23\", 7, \"Vega\"),\n" +
            "  (4, 39, \"11-05-23\", 2, \"Mueller\"),\n" +
            "  (5, 44, \"07-02-22\", 8, \"Abbott\"),\n" +
//            "  (6, 10, \"12-31-22\", 10, \"Small\"),\n" +
//            "  (7, 293, \"07-22-23\", 7, \"Castaneda\"),\n" +
//            "  (8, 91, \"11-02-22\", 6, \"Colon\"),\n" +
//            "  (9, 194, \"11-30-21\", 6, \"Emerson\"),\n" +
//            "  (10, 194, \"09-28-22\", 6, \"Marquez\"),\n" +
//            "  (11, 70, \"12-16-21\", 9, \"Wood\"),\n" +
            "  (1, 179, \"11-03-22\", 9, \"Rice\"),\n" +
            "  (2, 252, \"05-05-22\", 10, \"Wheeler\"),\n" +
            "  (3, 212, \"12-15-22\", 10, \"Barr\"),\n" +
            "  (4, 12, \"12-31-22\", 4, \"Brady\"),\n" +
            "  (5, 180, \"12-19-21\", 10, \"Mcdowell\"),\n" +
//            "  (6, 71, \"01-22-23\", 4, \"Collier\"),\n" +
//            "  (7, 39, \"02-02-23\", 2, \"Ryan\"),\n" +
//            "  (8, 173, \"06-29-23\", 7, \"Daniels\"),\n" +
//            "  (9, 98, \"08-13-22\", 5, \"Peters\"),\n" +
//            "  (10, 172, \"04-06-22\", 10, \"Jensen\"),\n" +
//            "  (11, 9, \"04-24-23\", 3, \"Beck\"),\n" +
            "  (1, 218, \"04-06-22\", 8, \"Gillespie\"),\n" +
            "  (2, 240, \"12-07-22\", 3, \"Decker\"),\n" +
            "  (3, 257, \"10-29-23\", 7, \"Black\"),\n" +
            "  (4, 104, \"03-22-22\", 8, \"Bowers\"),\n" +
            "  (5, 227, \"06-01-23\", 9, \"Mcfadden\"),\n" +
//            "  (6, 123, \"03-31-23\", 4, \"Murphy\"),\n" +
//            "  (7, 207, \"10-14-22\", 9, \"Welch\"),\n" +
//            "  (8, 96, \"09-04-23\", 10, \"Yang\"),\n" +
//            "  (9, 291, \"10-04-22\", 4, \"Humphrey\"),\n" +
//            "  (10, 176, \"05-21-23\", 9, \"Callahan\"),\n" +
//            "  (11, 5, \"06-17-23\", 9, \"Roy\"),\n" +
            "  (1, 62, \"10-02-23\", 2, \"Glover\"),\n" +
            "  (2, 25, \"02-25-23\", 8, \"Daniels\"),\n" +
            "  (3, 190, \"11-11-23\", 3, \"Little\"),\n" +
            "  (4, 59, \"07-18-23\", 2, \"Owens\"),\n" +
            "  (5, 127, \"09-10-23\", 7, \"Ortega\"),\n" +
//            "  (6, 161, \"09-22-23\", 1, \"Cain\"),\n" +
//            "  (7, 255, \"02-27-23\", 3, \"Stafford\"),\n" +
//            "  (8, 298, \"01-25-23\", 5, \"Hudson\"),\n" +
//            "  (9, 97, \"07-19-22\", 4, \"Dean\"),\n" +
//            "  (10, 237, \"05-15-22\", 2, \"Carpenter\"),\n" +
//            "  (11, 258, \"10-03-22\", 7, \"Torres\"),\n" +
            "  (1, 221, \"09-20-22\", 4, \"Merrill\"),\n" +
            "  (2, 31, \"01-06-23\", 5, \"Wyatt\"),\n" +
            "  (3, 161, \"01-01-23\", 8, \"Barton\"),\n" +
            "  (4, 243, \"10-10-23\", 2, \"Mathis\"),\n" +
            "  (5, 234, \"10-15-22\", 8, \"Knight\"),\n" +
//            "  (6, 22, \"05-14-23\", 8, \"Floyd\"),\n" +
//            "  (7, 257, \"01-08-22\", 4, \"Massey\"),\n" +
//            "  (8, 198, \"12-27-22\", 6, \"Potter\"),\n" +
//            "  (9, 72, \"03-29-22\", 3, \"O'connor\"),\n" +
//            "  (10, 296, \"05-09-22\", 5, \"Patrick\"),\n" +
//            "  (11, 259, \"08-20-22\", 1, \"Mckee\"),\n" +
            "  (1, 41, \"01-08-23\", 5, \"Lawson\"),\n" +
            "  (2, 30, \"04-16-22\", 7, \"Contreras\"),\n" +
            "  (3, 154, \"05-25-22\", 4, \"Parrish\"),\n" +
            "  (4, 107, \"07-22-23\", 4, \"Beach\"),\n" +
            "  (5, 178, \"08-20-22\", 7, \"Campbell\"),\n" +
//            "  (6, 188, \"03-28-22\", 3, \"Giles\"),\n" +
//            "  (7, 211, \"09-09-23\", 7, \"Dorsey\"),\n" +
//            "  (8, 128, \"09-08-22\", 6, \"Fischer\"),\n" +
//            "  (9, 250, \"01-23-23\", 1, \"Hardin\"),\n" +
//            "  (10, 27, \"11-22-22\", 8, \"Marquez\"),\n" +
//            "  (11, 218, \"11-08-22\", 10, \"Norton\"),\n" +
            "  (1, 99, \"08-15-22\", 2, \"Luna\"),\n" +
            "  (2, 104, \"08-01-22\", 9, \"Cote\"),\n" +
            "  (3, 134, \"08-28-23\", 5, \"Burks\"),\n" +
            "  (4, 120, \"09-02-22\", 9, \"Fields\"),\n" +
            "  (5, 74, \"01-10-23\", 3, \"Monroe\"),\n" +
//            "  (6, 123, \"02-06-23\", 1, \"Burgess\"),\n" +
//            "  (7, 72, \"12-23-22\", 8, \"Lane\"),\n" +
//            "  (8, 112, \"03-11-23\", 3, \"Rosales\"),\n" +
//            "  (9, 102, \"10-18-22\", 9, \"Walton\"),\n" +
//            "  (10, 36, \"03-17-23\", 10, \"Stephens\"),\n" +
//            "  (11, 236, \"05-15-23\", 2, \"Dominguez\"),\n" +
            "  (1, 35, \"11-20-21\", 5, \"Black\"),\n" +
            "  (2, 142, \"02-21-22\", 5, \"Rodgers\"),\n" +
            "  (3, 197, \"08-09-22\", 6, \"Washington\"),\n" +
            "  (4, 39, \"01-11-23\", 5, \"Farmer\"),\n" +
            "  (5, 131, \"09-07-22\", 5, \"Howe\"),\n" +
//            "  (6, 51, \"11-25-22\", 1, \"Ortiz\"),\n" +
//            "  (7, 88, \"11-25-21\", 1, \"Meadows\"),\n" +
//            "  (8, 32, \"12-01-22\", 5, \"Jordan\"),\n" +
//            "  (9, 244, \"06-12-22\", 10, \"Best\"),\n" +
//            "  (10, 117, \"05-21-23\", 8, \"Brewer\"),\n" +
//            "  (11, 266, \"09-27-23\", 1, \"Hendricks\"),\n" +
            "  (1, 219, \"07-13-23\", 5, \"Navarro\"),\n" +
            "  (2, 274, \"10-27-22\", 6, \"Hendrix\"),\n" +
            "  (3, 188, \"03-27-23\", 2, \"Holden\"),\n" +
            "  (4, 30, \"02-20-23\", 9, \"Blair\"),\n" +
            "  (5, 101, \"01-14-22\", 3, \"Leach\"),\n" +
//            "  (6, 285, \"03-26-22\", 9, \"Sharp\"),\n" +
//            "  (7, 280, \"06-04-22\", 10, \"Hood\"),\n" +
//            "  (8, 57, \"05-04-23\", 5, \"Benjamin\"),\n" +
//            "  (9, 245, \"05-15-22\", 9, \"Sherman\"),\n" +
//            "  (10, 7, \"05-21-23\", 2, \"Benson\"),\n" +
//            "  (11, 188, \"05-17-23\", 2, \"Beach\"),\n" +
            "  (1, 268, \"09-27-23\", 2, \"Bridges\"),\n" +
            "  (2, 296, \"11-17-22\", 4, \"Mayer\"),\n" +
            "  (3, 128, \"10-02-23\", 3, \"Jenkins\"),\n" +
            "  (4, 27, \"07-05-22\", 8, \"Mendez\"),\n" +
            "  (5, 236, \"10-29-22\", 7, \"Franks\"),\n" +
//            "  (6, 59, \"07-15-23\", 5, \"Boone\"),\n" +
//            "  (7, 213, \"02-20-23\", 7, \"Hester\"),\n" +
//            "  (8, 288, \"03-28-22\", 7, \"Hopkins\"),\n" +
//            "  (9, 76, \"06-07-23\", 7, \"Pruitt\"),\n" +
//            "  (10, 293, \"09-18-23\", 5, \"Norman\"),\n" +
//            "  (11, 117, \"11-22-21\", 9, \"Ramos\"),\n" +
            "  (1, 134, \"06-26-22\", 8, \"Jefferson\"),\n" +
            "  (2, 141, \"06-19-22\", 2, \"Arnold\"),\n" +
            "  (3, 88, \"08-07-23\", 3, \"Tyson\"),\n" +
            "  (4, 70, \"09-12-23\", 10, \"Eaton\"),\n" +
            "  (5, 243, \"07-17-23\", 1, \"Elliott\"),\n" +
//            "  (6, 124, \"09-14-23\", 4, \"Faulkner\"),\n" +
//            "  (7, 58, \"08-01-23\", 2, \"Mckenzie\"),\n" +
//            "  (8, 13, \"05-05-22\", 5, \"Frye\"),\n" +
//            "  (9, 186, \"02-16-22\", 2, \"Lucas\"),\n" +
//            "  (10, 164, \"04-29-23\", 10, \"Cote\"),\n" +
//            "  (11, 239, \"07-11-22\", 1, \"Christensen\"),\n" +
            "  (1, 145, \"01-05-23\", 6, \"Buckley\"),\n" +
            "  (2, 232, \"09-26-23\", 3, \"Ellison\"),\n" +
            "  (3, 16, \"01-24-23\", 6, \"Pratt\"),\n" +
            "  (4, 35, \"08-08-23\", 9, \"Horne\"),\n" +
            "  (5, 24, \"01-27-23\", 7, \"Lindsay\")," +
            "(1, 98, \"08-10-22\", 4, \"Dillard\"),\n" +
            "  (2, 281, \"07-29-22\", 6, \"Swanson\"),\n" +
            "  (3, 76, \"08-13-23\", 7, \"Clarke\"),\n" +
            "  (4, 145, \"10-20-23\", 5, \"Underwood\"),\n" +
            "  (5, 249, \"11-29-21\", 5, \"Wooten\"),\n" +
            "  (1, 37, \"12-18-21\", 2, \"Irwin\"),\n" +
            "  (2, 53, \"12-29-22\", 9, \"Erickson\"),\n" +
            "  (3, 262, \"08-09-22\", 2, \"Wagner\"),\n" +
            "  (4, 64, \"03-16-22\", 8, \"Snow\"),\n" +
            "  (5, 237, \"08-30-23\", 3, \"Noble\"),\n" +
            "  (1, 33, \"01-14-23\", 7, \"Sparks\"),\n" +
            "  (2, 107, \"07-14-22\", 9, \"Holcomb\"),\n" +
            "  (3, 139, \"03-30-22\", 6, \"Collier\"),\n" +
            "  (4, 25, \"10-16-23\", 3, \"Rutledge\"),\n" +
            "  (5, 193, \"12-28-22\", 4, \"Perez\"),\n" +
            "  (1, 219, \"03-10-23\", 4, \"Jefferson\"),\n" +
            "  (2, 109, \"01-30-22\", 4, \"O'Neill\"),\n" +
            "  (3, 235, \"07-21-23\", 7, \"Spears\"),\n" +
            "  (4, 153, \"01-27-22\", 5, \"Wheeler\"),\n" +
            "  (5, 237, \"04-17-23\", 3, \"Cleveland\"),\n" +
            "  (1, 97, \"07-14-22\", 7, \"Harris\"),\n" +
            "  (2, 117, \"04-26-23\", 2, \"Mclean\"),\n" +
            "  (3, 225, \"08-07-22\", 8, \"Branch\"),\n" +
            "  (4, 70, \"02-18-23\", 5, \"Hahn\"),\n" +
            "  (5, 156, \"11-20-21\", 9, \"Cash\"),\n" +
            "  (1, 153, \"10-08-22\", 1, \"Vazquez\"),\n" +
            "  (2, 174, \"06-07-23\", 5, \"Chandler\"),\n" +
            "  (3, 126, \"06-26-23\", 10, \"Barton\"),\n" +
            "  (4, 273, \"02-05-23\", 9, \"Tran\"),\n" +
            "  (5, 213, \"04-01-22\", 4, \"Henry\"),\n" +
            "  (1, 131, \"09-08-22\", 9, \"Fitzpatrick\"),\n" +
            "  (2, 282, \"10-16-22\", 9, \"Copeland\"),\n" +
            "  (3, 242, \"07-23-23\", 9, \"Oneal\"),\n" +
            "  (4, 298, \"11-24-22\", 6, \"Trevino\"),\n" +
            "  (5, 288, \"08-10-22\", 3, \"Crane\"),\n" +
            "  (1, 78, \"07-09-23\", 6, \"Chavez\"),\n" +
            "  (2, 2, \"08-06-22\", 5, \"Castro\"),\n" +
            "  (3, 262, \"04-02-22\", 1, \"Atkinson\"),\n" +
            "  (4, 60, \"10-04-23\", 3, \"Kirkland\"),\n" +
//            "  (5, 299, \"03-25-22\", 7, \"Snow\"),\n" +
            "  (1, 292, \"06-07-23\", 8, \"Gould\"),\n" +
            "  (2, 49, \"06-06-23\", 3, \"Mcleod\"),\n" +
            "  (3, 35, \"07-26-22\", 9, \"Velazquez\"),\n" +
            "  (4, 133, \"02-26-22\", 4, \"Buchanan\"),\n" +
            "  (5, 289, \"10-13-22\", 5, \"Taylor\"),\n" +
            "  (1, 243, \"03-17-23\", 5, \"Velasquez\"),\n" +
            "  (2, 93, \"08-14-22\", 5, \"Bond\"),\n" +
            "  (3, 56, \"04-03-22\", 4, \"Orr\"),\n" +
            "  (4, 158, \"04-16-22\", 7, \"Hess\"),\n" +
            "  (5, 179, \"12-02-21\", 8, \"Reed\"),\n" +
            "  (1, 151, \"06-17-23\", 9, \"David\"),\n" +
            "  (2, 165, \"02-03-23\", 7, \"Shepherd\"),\n" +
            "  (3, 135, \"01-31-23\", 7, \"Dunlap\"),\n" +
            "  (4, 177, \"04-18-23\", 8, \"Alvarez\"),\n" +
            "  (5, 180, \"09-22-23\", 2, \"Middleton\"),\n" +
            "  (1, 204, \"02-08-23\", 7, \"Hunt\"),\n" +
            "  (2, 261, \"06-20-23\", 9, \"Weber\"),\n" +
            "  (3, 92, \"10-12-22\", 9, \"Lara\"),\n" +
            "  (4, 192, \"04-03-23\", 7, \"Carroll\"),\n" +
            "  (5, 274, \"02-11-22\", 6, \"Coffey\"),\n" +
            "  (1, 215, \"11-05-23\", 1, \"Nielsen\"),\n" +
            "  (2, 81, \"12-30-21\", 7, \"Cochran\"),\n" +
            "  (3, 191, \"08-05-23\", 3, \"Flowers\"),\n" +
            "  (4, 69, \"06-06-23\", 9, \"Cline\"),\n" +
            "  (5, 46, \"11-03-22\", 8, \"Gilmore\"),\n" +
            "  (1, 285, \"06-21-22\", 8, \"Bowen\"),\n" +
            "  (2, 297, \"07-20-22\", 9, \"Lynn\"),\n" +
            "  (3, 78, \"04-14-22\", 5, \"Rollins\"),\n" +
            "  (4, 114, \"09-09-23\", 7, \"Cleveland\"),\n" +
            "  (5, 50, \"07-22-22\", 5, \"Ingram\"),\n" +
            "  (1, 8, \"09-03-22\", 7, \"Middleton\"),\n" +
            "  (2, 59, \"07-01-23\", 8, \"Workman\"),\n" +
            "  (3, 11, \"08-20-22\", 7, \"Wolfe\"),\n" +
            "  (4, 223, \"08-20-23\", 1, \"Woodard\"),\n" +
            "  (5, 201, \"06-12-22\", 7, \"White\"),\n" +
            "  (1, 142, \"05-01-22\", 2, \"Wong\"),\n" +
            "  (2, 126, \"05-22-22\", 9, \"Johnston\"),\n" +
            "  (3, 36, \"04-26-22\", 6, \"Spencer\"),\n" +
            "  (4, 164, \"12-17-21\", 6, \"Ramsey\"),\n" +
            "  (5, 1, \"11-04-23\", 6, \"George\"),\n" +
            "  (1, 9, \"08-15-22\", 7, \"Garrett\"),\n" +
            "  (2, 38, \"05-13-22\", 10, \"Thompson\"),\n" +
            "  (3, 71, \"08-23-23\", 8, \"Alexander\"),\n" +
            "  (4, 138, \"12-25-21\", 7, \"Chase\"),\n" +
            "  (5, 271, \"07-01-22\", 5, \"Avery\"),\n" +
            "  (1, 105, \"01-25-22\", 8, \"Mcpherson\"),\n" +
            "  (2, 92, \"12-24-22\", 6, \"Bridges\"),\n" +
            "  (3, 241, \"10-30-22\", 8, \"Lane\"),\n" +
            "  (4, 192, \"10-21-22\", 4, \"Delacruz\"),\n" +
            "  (5, 152, \"02-27-23\", 1, \"Stewart\"),\n" +
            "  (1, 169, \"06-03-22\", 6, \"Bender\"),\n" +
            "  (2, 51, \"03-22-23\", 4, \"Knox\"),\n" +
            "  (3, 12, \"09-21-23\", 6, \"Hopper\"),\n" +
            "  (4, 236, \"04-21-22\", 1, \"Christian\"),\n" +
            "  (5, 82, \"02-19-22\", 8, \"Bray\"),\n" +
            "  (1, 38, \"02-12-23\", 3, \"Miles\"),\n" +
            "  (2, 158, \"08-20-23\", 5, \"Rios\"),\n" +
            "  (3, 128, \"09-06-23\", 7, \"Guerrero\"),\n" +
            "  (4, 97, \"06-09-23\", 4, \"Ross\"),\n" +
            "  (5, 232, \"05-15-22\", 8, \"Castillo\"),\n" +
            "  (1, 196, \"11-14-22\", 8, \"Bernard\"),\n" +
            "  (2, 269, \"07-05-22\", 4, \"Ball\"),\n" +
            "  (3, 293, \"06-23-23\", 6, \"Tyson\"),\n" +
            "  (4, 212, \"09-26-23\", 6, \"Shepard\"),\n" +
            "  (5, 149, \"04-30-22\", 6, \"Hood\"),\n" +
            "  (1, 242, \"12-21-22\", 3, \"Burks\"),\n" +
            "  (2, 57, \"02-08-22\", 7, \"Hanson\"),\n" +
            "  (3, 267, \"08-22-22\", 5, \"Foley\"),\n" +
            "  (4, 39, \"07-24-23\", 9, \"Navarro\"),\n" +
            "  (5, 177, \"07-22-22\", 6, \"Kaufman\"),\n" +
            "  (1, 241, \"09-10-22\", 3, \"Garner\"),\n" +
            "  (2, 160, \"04-19-22\", 5, \"Bond\"),\n" +
            "  (3, 148, \"12-02-21\", 7, \"Snow\"),\n" +
            "  (4, 42, \"02-20-22\", 2, \"Ramsey\"),\n" +
            "  (5, 72, \"01-09-22\", 4, \"Elliott\"),\n" +
            "  (1, 21, \"08-27-22\", 5, \"Noble\"),\n" +
            "  (2, 115, \"05-13-22\", 8, \"Reynolds\"),\n" +
            "  (3, 103, \"04-22-23\", 6, \"Hunter\"),\n" +
            "  (4, 156, \"04-30-23\", 2, \"Giles\"),\n" +
            "  (5, 26, \"07-26-22\", 4, \"Welch\"),\n" +
            "  (1, 291, \"10-10-22\", 2, \"Cooke\"),\n" +
            "  (2, 94, \"10-20-22\", 7, \"Hurley\"),\n" +
            "  (3, 82, \"05-31-22\", 6, \"Stafford\"),\n" +
            "  (4, 170, \"05-31-22\", 3, \"Joseph\"),\n" +
            "  (5, 24, \"12-28-21\", 5, \"Dillard\"),\n" +
            "  (1, 5, \"05-17-23\", 10, \"Torres\"),\n" +
            "  (2, 75, \"04-03-22\", 1, \"Watson\"),\n" +
            "  (3, 73, \"08-14-22\", 8, \"Daugherty\"),\n" +
            "  (4, 132, \"05-20-22\", 1, \"Patton\"),\n" +
            "  (5, 52, \"11-16-23\", 5, \"Montgomery\"),\n" +
            "  (1, 202, \"11-10-23\", 6, \"Dunn\"),\n" +
            "  (2, 253, \"07-10-23\", 4, \"Wiggins\"),\n" +
            "  (3, 65, \"07-06-23\", 6, \"Salas\"),\n" +
            "  (4, 31, \"09-09-23\", 4, \"Barton\"),\n" +
            "  (5, 164, \"03-11-22\", 2, \"Rosa\"),\n" +
            "  (1, 133, \"07-21-23\", 5, \"Horne\"),\n" +
            "  (2, 189, \"08-22-23\", 5, \"Whitaker\"),\n" +
            "  (3, 129, \"12-16-21\", 10, \"May\"),\n" +
            "  (4, 21, \"01-19-23\", 1, \"Park\"),\n" +
            "  (5, 178, \"12-25-21\", 3, \"Solis\"),\n" +
            "  (1, 32, \"06-20-23\", 9, \"Bates\"),\n" +
            "  (2, 109, \"07-22-22\", 5, \"Mendez\"),\n" +
            "  (3, 119, \"11-10-22\", 7, \"Rogers\"),\n" +
            "  (4, 236, \"07-31-22\", 10, \"Barton\"),\n" +
            "  (5, 17, \"06-10-22\", 1, \"Jefferson\"),\n" +
            "  (1, 230, \"10-20-23\", 3, \"Dodson\"),\n" +
            "  (2, 42, \"07-08-22\", 6, \"Solis\"),\n" +
            "  (3, 39, \"06-14-22\", 2, \"Ford\"),\n" +
            "  (4, 136, \"02-17-23\", 9, \"Rice\"),\n" +
            "  (5, 246, \"02-09-23\", 8, \"Walker\"),\n" +
            "  (1, 173, \"06-08-22\", 1, \"Skinner\"),\n" +
            "  (2, 243, \"01-06-22\", 9, \"Boone\"),\n" +
            "  (3, 278, \"04-28-22\", 7, \"Coleman\"),\n" +
            "  (4, 197, \"04-08-23\", 7, \"Juarez\"),\n" +
            "  (5, 209, \"10-08-22\", 5, \"Chen\"),\n" +
            "  (1, 73, \"07-16-22\", 5, \"Hudson\"),\n" +
            "  (2, 202, \"06-23-23\", 2, \"Weaver\"),\n" +
            "  (3, 173, \"08-05-22\", 4, \"Brady\"),\n" +
            "  (4, 100, \"12-29-22\", 2, \"Abbott\"),\n" +
            "  (5, 138, \"11-28-21\", 4, \"Fitzpatrick\"),\n" +
            "  (1, 287, \"04-27-23\", 6, \"Black\"),\n" +
            "  (2, 234, \"05-09-23\", 9, \"Schroeder\"),\n" +
            "  (3, 196, \"07-31-22\", 7, \"Bryant\"),\n" +
            "  (4, 174, \"07-30-23\", 10, \"Melton\"),\n" +
            "  (5, 255, \"06-23-23\", 5, \"Mason\"),\n" +
            "  (1, 224, \"12-30-22\", 5, \"Haynes\"),\n" +
            "  (2, 222, \"09-07-23\", 2, \"Elliott\"),\n" +
            "  (3, 128, \"09-10-23\", 4, \"Cantrell\"),\n" +
            "  (4, 156, \"12-18-22\", 9, \"Powers\"),\n" +
            "  (5, 9, \"11-03-22\", 8, \"Lott\"),\n" +
            "  (1, 235, \"06-16-22\", 9, \"Mccarthy\"),\n" +
            "  (2, 103, \"07-03-22\", 4, \"Merrill\"),\n" +
            "  (3, 173, \"07-31-23\", 1, \"Boyd\"),\n" +
            "  (4, 134, \"02-14-23\", 4, \"Ware\"),\n" +
            "  (5, 259, \"08-03-22\", 8, \"Burnett\"),\n" +
            "  (1, 191, \"02-27-23\", 1, \"Lancaster\"),\n" +
            "  (2, 228, \"04-20-23\", 6, \"Delgado\"),\n" +
            "  (3, 39, \"12-05-22\", 3, \"Johns\"),\n" +
            "  (4, 16, \"03-05-22\", 9, \"Nixon\"),\n" +
            "  (5, 134, \"09-14-23\", 8, \"Cantrell\"),\n" +
            "  (1, 245, \"01-14-22\", 8, \"Gilbert\"),\n" +
            "  (2, 235, \"06-10-22\", 1, \"Fleming\"),\n" +
            "  (3, 134, \"02-13-22\", 6, \"Jefferson\"),\n" +
            "  (4, 277, \"03-19-22\", 8, \"Hoover\"),\n" +
            "  (5, 273, \"05-01-22\", 7, \"Simmons\"),\n" +
            "  (1, 70, \"07-03-22\", 6, \"Goodman\"),\n" +
            "  (2, 80, \"03-21-23\", 5, \"Madden\"),\n" +
            "  (3, 55, \"01-10-22\", 4, \"Pennington\"),\n" +
            "  (4, 70, \"05-05-23\", 1, \"David\"),\n" +
            "  (5, 257, \"07-30-23\", 3, \"Mendez\"),\n" +
            "  (1, 32, \"12-01-21\", 2, \"Roach\"),\n" +
            "  (2, 275, \"05-05-23\", 8, \"Orr\"),\n" +
            "  (3, 143, \"02-28-23\", 9, \"Kirkland\"),\n" +
            "  (4, 66, \"01-24-23\", 8, \"Garner\"),\n" +
            "  (5, 75, \"07-05-23\", 6, \"Alford\"),\n" +
            "  (1, 136, \"08-13-22\", 7, \"Mason\"),\n" +
            "  (2, 158, \"08-08-22\", 7, \"Bradley\"),\n" +
            "  (3, 289, \"03-07-23\", 8, \"O'Neill\"),\n" +
            "  (4, 145, \"08-05-22\", 9, \"Cabrera\"),\n" +
            "  (5, 118, \"03-15-23\", 3, \"Sykes\"),\n" +
            "  (1, 174, \"11-17-23\", 4, \"Mcfarland\"),\n" +
            "  (2, 257, \"08-27-22\", 7, \"Stanton\"),\n" +
            "  (3, 250, \"07-20-22\", 3, \"Harvey\"),\n" +
            "  (4, 113, \"11-07-22\", 3, \"Mclean\"),\n" +
            "  (5, 191, \"06-04-23\", 6, \"Hartman\"),\n" +
            "  (1, 119, \"06-08-22\", 6, \"Holman\"),\n" +
            "  (2, 261, \"05-26-22\", 6, \"Marsh\"),\n" +
            "  (3, 227, \"04-28-23\", 7, \"Davidson\"),\n" +
            "  (4, 272, \"04-20-22\", 1, \"Clark\"),\n" +
            "  (5, 56, \"08-11-23\", 2, \"Rasmussen\"),\n" +
            "  (1, 87, \"10-03-22\", 5, \"Alvarez\"),\n" +
            "  (2, 254, \"06-25-23\", 9, \"Adkins\"),\n" +
            "  (3, 234, \"09-22-23\", 8, \"Good\"),\n" +
            "  (4, 201, \"10-04-22\", 9, \"Porter\"),\n" +
            "  (5, 115, \"11-23-22\", 9, \"Wolfe\"),\n" +
            "  (1, 16, \"05-10-22\", 1, \"Johns\"),\n" +
            "  (2, 231, \"05-08-23\", 4, \"Villarreal\"),\n" +
            "  (3, 228, \"01-17-23\", 6, \"Boyle\"),\n" +
            "  (4, 55, \"02-18-23\", 2, \"Mejia\"),\n" +
            "  (5, 97, \"01-18-23\", 8, \"Charles\"),\n" +
            "  (1, 33, \"12-05-22\", 9, \"Blake\"),\n" +
            "  (2, 64, \"12-12-22\", 7, \"Buck\"),\n" +
            "  (3, 160, \"07-05-23\", 1, \"Sloan\"),\n" +
            "  (4, 212, \"08-15-22\", 8, \"Hardin\"),\n" +
            "  (5, 219, \"01-19-22\", 2, \"Stewart\"),\n" +
            "  (1, 73, \"10-11-23\", 8, \"Sears\"),\n" +
            "  (2, 165, \"07-29-22\", 3, \"Downs\"),\n" +
            "  (3, 245, \"02-22-23\", 7, \"Bridges\"),\n" +
            "  (4, 214, \"12-17-22\", 8, \"Sears\"),\n" +
            "  (5, 40, \"07-09-23\", 3, \"King\"),\n" +
            "  (1, 284, \"05-24-22\", 3, \"Fulton\"),\n" +
            "  (2, 199, \"05-31-23\", 7, \"Haney\"),\n" +
            "  (3, 67, \"08-05-22\", 1, \"Trevino\"),\n" +
            "  (4, 33, \"03-29-22\", 10, \"Bartlett\"),\n" +
            "  (5, 171, \"05-15-23\", 7, \"Farley\"),\n" +
            "  (1, 17, \"06-21-22\", 10, \"Glass\"),\n" +
            "  (2, 77, \"07-22-23\", 7, \"Suarez\"),\n" +
            "  (3, 65, \"08-04-22\", 3, \"Sims\"),\n" +
            "  (4, 115, \"12-09-22\", 2, \"Kane\"),\n" +
            "  (5, 14, \"02-13-23\", 7, \"Page\"),\n" +
            "  (1, 264, \"07-26-23\", 9, \"Blair\"),\n" +
            "  (2, 284, \"06-05-23\", 1, \"Robinson\"),\n" +
            "  (3, 144, \"06-13-23\", 4, \"Hunter\"),\n" +
            "  (4, 64, \"04-16-22\", 2, \"Lott\"),\n" +
            "  (5, 98, \"03-21-23\", 4, \"Mathews\"),\n" +
            "  (1, 153, \"12-17-21\", 6, \"Rosa\"),\n" +
            "  (2, 51, \"10-08-23\", 2, \"Benjamin\"),\n" +
            "  (3, 50, \"08-15-22\", 7, \"Jacobs\"),\n" +
            "  (4, 2, \"05-24-23\", 8, \"Wooten\"),\n" +
            "  (5, 224, \"04-08-23\", 9, \"Jarvis\"),\n" +
            "  (1, 192, \"08-08-23\", 6, \"O'brien\"),\n" +
            "  (2, 200, \"08-23-23\", 6, \"Quinn\"),\n" +
            "  (3, 146, \"03-02-23\", 5, \"Michael\"),\n" +
            "  (4, 182, \"01-18-23\", 8, \"Joyce\"),\n" +
            "  (5, 134, \"01-04-23\", 10, \"Wynn\"),\n" +
            "  (1, 33, \"09-05-23\", 7, \"Weeks\"),\n" +
            "  (2, 80, \"03-03-22\", 1, \"Battle\"),\n" +
            "  (3, 24, \"02-18-22\", 9, \"Blair\"),\n" +
            "  (4, 103, \"06-09-22\", 5, \"Fitzpatrick\"),\n" +
            "  (5, 228, \"02-17-22\", 6, \"Medina\"),\n" +
            "  (1, 174, \"06-06-23\", 10, \"Sargent\"),\n" +
            "  (2, 107, \"08-17-22\", 8, \"Anthony\"),\n" +
            "  (3, 150, \"11-14-23\", 9, \"Haley\"),\n" +
            "  (4, 12, \"02-14-22\", 6, \"Frazier\"),\n" +
            "  (5, 80, \"11-22-21\", 8, \"Walsh\"),\n" +
            "  (1, 50, \"02-17-23\", 8, \"Hinton\"),\n" +
            "  (2, 9, \"02-18-22\", 4, \"Wiley\"),\n" +
            "  (3, 5, \"03-30-23\", 1, \"Wilkinson\"),\n" +
            "  (4, 221, \"11-18-21\", 9, \"Austin\"),\n" +
            "  (5, 187, \"09-08-23\", 6, \"Roach\"),\n" +
            "  (1, 233, \"12-03-22\", 5, \"Glover\"),\n" +
            "  (2, 203, \"01-14-23\", 10, \"Freeman\"),\n" +
            "  (3, 218, \"04-23-22\", 2, \"Olson\"),\n" +
            "  (4, 95, \"11-01-23\", 10, \"Walter\"),\n" +
            "  (5, 8, \"06-20-23\", 2, \"Riley\"),\n" +
            "  (1, 17, \"09-15-22\", 2, \"Green\"),\n" +
            "  (2, 89, \"04-27-23\", 9, \"Ayala\"),\n" +
            "  (3, 154, \"05-14-23\", 5, \"Lindsay\"),\n" +
            "  (4, 262, \"04-30-22\", 4, \"Patton\"),\n" +
            "  (5, 7, \"03-30-23\", 7, \"Alvarado\"),\n" +
            "  (1, 225, \"02-19-23\", 6, \"Clemons\"),\n" +
            "  (2, 139, \"08-26-23\", 3, \"Montgomery\"),\n" +
            "  (3, 200, \"12-14-22\", 6, \"Beck\"),\n" +
            "  (4, 4, \"06-07-22\", 4, \"Petersen\"),\n" +
            "  (5, 294, \"07-24-23\", 6, \"Lane\"),\n" +
            "  (1, 219, \"05-17-23\", 5, \"Potter\"),\n" +
            "  (2, 77, \"09-11-23\", 4, \"Roman\"),\n" +
            "  (3, 272, \"06-19-23\", 9, \"Russell\"),\n" +
            "  (4, 157, \"06-24-23\", 5, \"Dalton\"),\n" +
            "  (5, 159, \"10-23-23\", 4, \"Valencia\"),\n" +
            "  (1, 19, \"05-26-22\", 4, \"Rivera\"),\n" +
            "  (2, 4, \"10-22-23\", 8, \"Mcclure\"),\n" +
            "  (3, 22, \"12-04-22\", 4, \"Raymond\"),\n" +
            "  (4, 294, \"08-10-22\", 4, \"Sexton\"),\n" +
            "  (5, 191, \"06-12-22\", 7, \"Allison\"),\n" +
            "  (1, 47, \"03-21-22\", 7, \"Humphrey\"),\n" +
            "  (2, 227, \"05-30-23\", 6, \"Chang\"),\n" +
            "  (3, 130, \"11-10-22\", 2, \"Wilder\"),\n" +
            "  (4, 72, \"01-22-22\", 3, \"Sanchez\"),\n" +
            "  (5, 33, \"04-21-22\", 2, \"Hopkins\"),\n" +
            "  (1, 181, \"07-23-23\", 7, \"Leonard\"),\n" +
            "  (2, 278, \"01-28-22\", 2, \"Melendez\"),\n" +
            "  (3, 246, \"08-16-22\", 5, \"Mosley\"),\n" +
            "  (4, 293, \"02-17-22\", 5, \"Young\"),\n" +
            "  (5, 127, \"10-13-22\", 5, \"Lambert\"),\n" +
            "  (1, 100, \"09-04-22\", 3, \"Hartman\"),\n" +
            "  (2, 54, \"12-20-21\", 6, \"Zimmerman\"),\n" +
            "  (3, 149, \"07-23-22\", 8, \"Albert\"),\n" +
            "  (4, 23, \"04-26-23\", 1, \"Vang\"),\n" +
            "  (5, 140, \"01-26-23\", 3, \"Zamora\"),\n" +
            "  (1, 73, \"06-30-22\", 1, \"Richardson\"),\n" +
            "  (2, 257, \"01-02-23\", 1, \"Newton\"),\n" +
            "  (3, 79, \"07-13-23\", 9, \"Chen\"),\n" +
            "  (4, 212, \"10-14-23\", 2, \"Reynolds\"),\n" +
            "  (5, 65, \"11-15-23\", 2, \"Farmer\"),\n" +
            "  (1, 150, \"08-25-23\", 8, \"Moran\"),\n" +
            "  (2, 50, \"04-22-22\", 9, \"Hubbard\"),\n" +
            "  (3, 224, \"09-21-23\", 3, \"Stevenson\"),\n" +
            "  (4, 225, \"03-10-23\", 6, \"Brock\"),\n" +
            "  (5, 119, \"09-03-22\", 3, \"Fitzpatrick\"),\n" +
            "  (1, 155, \"01-25-23\", 5, \"Walton\"),\n" +
            "  (2, 216, \"08-08-23\", 2, \"Mcmillan\"),\n" +
            "  (3, 247, \"03-26-22\", 8, \"Mcneil\"),\n" +
            "  (4, 165, \"02-27-22\", 3, \"Petersen\"),\n" +
            "  (5, 148, \"03-28-23\", 6, \"Collins\"),\n" +
            "  (1, 75, \"02-13-23\", 2, \"Greer\"),\n" +
            "  (2, 242, \"07-28-22\", 4, \"Chambers\"),\n" +
            "  (3, 162, \"08-06-22\", 3, \"Martin\"),\n" +
            "  (4, 35, \"04-15-23\", 3, \"Hardy\"),\n" +
            "  (5, 4, \"11-03-22\", 2, \"Morton\"),\n" +
            "  (1, 132, \"01-02-22\", 6, \"Arnold\"),\n" +
            "  (2, 57, \"03-11-22\", 2, \"Goff\"),\n" +
            "  (3, 233, \"12-14-22\", 8, \"Garrison\"),\n" +
            "  (4, 214, \"04-28-22\", 6, \"Henson\"),\n" +
            "  (5, 274, \"03-19-22\", 6, \"Aguilar\"),\n" +
            "  (1, 162, \"12-04-22\", 2, \"Oliver\"),\n" +
            "  (2, 27, \"07-12-23\", 9, \"Shepard\"),\n" +
            "  (3, 264, \"10-15-22\", 5, \"Richardson\"),\n" +
            "  (4, 53, \"02-16-22\", 7, \"Howard\"),\n" +
            "  (5, 72, \"08-25-23\", 9, \"Ayers\"),\n" +
            "  (1, 90, \"02-15-22\", 6, \"Cardenas\"),\n" +
            "  (2, 72, \"03-16-22\", 3, \"Pearson\"),\n" +
            "  (3, 215, \"07-10-22\", 6, \"Carr\"),\n" +
            "  (4, 165, \"04-18-22\", 5, \"Stark\"),\n" +
            "  (5, 292, \"10-07-23\", 5, \"George\"),\n" +
            "  (1, 167, \"06-09-22\", 5, \"Richards\"),\n" +
            "  (2, 118, \"11-24-21\", 5, \"Whitehead\"),\n" +
            "  (3, 52, \"03-19-22\", 8, \"Bishop\"),\n" +
            "  (4, 284, \"09-22-22\", 6, \"Buckner\"),\n" +
            "  (5, 278, \"09-10-23\", 3, \"Lester\"),\n" +
            "  (1, 271, \"07-31-22\", 6, \"Garcia\"),\n" +
            "  (2, 45, \"07-26-23\", 4, \"Petty\"),\n" +
            "  (3, 76, \"05-23-22\", 6, \"Collier\"),\n" +
            "  (4, 127, \"01-20-23\", 2, \"Guzman\"),\n" +
            "  (5, 204, \"07-21-23\", 7, \"Nguyen\"),\n" +
            "  (1, 91, \"09-18-23\", 9, \"Roach\"),\n" +
            "  (2, 78, \"02-17-22\", 3, \"Murray\"),\n" +
            "  (3, 187, \"09-02-23\", 7, \"Warner\"),\n" +
            "  (4, 26, \"05-19-23\", 2, \"Maddox\"),\n" +
            "  (5, 259, \"05-26-23\", 8, \"Frost\"),\n" +
            "  (1, 233, \"12-10-22\", 7, \"Glenn\"),\n" +
            "  (2, 197, \"05-08-22\", 5, \"Ballard\"),\n" +
            "  (3, 154, \"12-19-21\", 2, \"Lang\"),\n" +
            "  (4, 83, \"11-25-21\", 7, \"Moreno\"),\n" +
            "  (5, 286, \"06-30-22\", 5, \"Mckenzie\"),\n" +
            "  (1, 271, \"04-16-22\", 9, \"Lott\"),\n" +
            "  (2, 113, \"02-27-22\", 4, \"Bridges\"),\n" +
            "  (3, 68, \"07-19-23\", 8, \"Armstrong\"),\n" +
            "  (4, 249, \"02-14-22\", 3, \"Huber\"),\n" +
            "  (5, 23, \"04-15-23\", 10, \"O'donnell\"),\n" +
            "  (1, 30, \"05-09-23\", 3, \"Irwin\"),\n" +
            "  (2, 116, \"10-06-23\", 6, \"Woodard\"),\n" +
            "  (3, 234, \"01-07-23\", 2, \"Briggs\"),\n" +
            "  (4, 257, \"12-03-22\", 9, \"Potter\"),\n" +
            "  (5, 17, \"04-18-23\", 3, \"Malone\"),\n" +
            "  (1, 57, \"09-26-23\", 4, \"Foster\"),\n" +
            "  (2, 212, \"12-09-21\", 1, \"Rasmussen\"),\n" +
            "  (3, 55, \"08-03-23\", 8, \"Rosales\"),\n" +
            "  (4, 184, \"04-30-22\", 5, \"Vazquez\"),\n" +
            "  (5, 18, \"02-19-22\", 8, \"Tate\"),\n" +
            "  (1, 171, \"09-23-23\", 4, \"Willis\"),\n" +
            "  (2, 4, \"03-26-23\", 6, \"Bennett\"),\n" +
            "  (3, 200, \"01-18-23\", 2, \"Frye\"),\n" +
            "  (4, 81, \"03-25-23\", 8, \"Newman\"),\n" +
            "  (5, 157, \"12-12-21\", 4, \"Lucas\"),\n" +
            "  (1, 254, \"10-10-22\", 4, \"Wilkinson\"),\n" +
            "  (2, 123, \"12-14-22\", 10, \"Mcknight\"),\n" +
            "  (3, 79, \"08-30-22\", 4, \"Pratt\"),\n" +
            "  (4, 92, \"06-17-22\", 9, \"Barron\"),\n" +
            "  (5, 196, \"12-09-22\", 7, \"Wilder\"),\n" +
            "  (1, 257, \"12-11-21\", 8, \"Whitehead\"),\n" +
            "  (2, 59, \"03-25-22\", 6, \"Franks\"),\n" +
            "  (3, 37, \"06-22-23\", 3, \"Arnold\"),\n" +
            "  (4, 280, \"08-15-22\", 3, \"Everett\"),\n" +
            "  (5, 41, \"10-21-23\", 5, \"Martinez\"),\n" +
            "  (1, 202, \"11-15-22\", 3, \"Duke\"),\n" +
            "  (2, 129, \"03-23-22\", 1, \"Holt\"),\n" +
            "  (3, 27, \"04-02-22\", 8, \"Valenzuela\"),\n" +
            "  (4, 193, \"06-16-22\", 2, \"Bernard\"),\n" +
            "  (5, 101, \"04-17-22\", 7, \"Franks\"),\n" +
            "  (1, 209, \"12-15-22\", 6, \"Randall\"),\n" +
            "  (2, 261, \"08-10-23\", 9, \"Rose\"),\n" +
            "  (3, 153, \"11-03-23\", 6, \"Navarro\"),\n" +
            "  (4, 156, \"01-07-22\", 4, \"Hinton\"),\n" +
//            "  (5, 299, \"03-23-22\", 5, \"Glover\"),\n" +
            "  (1, 282, \"07-29-22\", 5, \"Bullock\"),\n" +
            "  (2, 150, \"02-22-23\", 10, \"Bright\"),\n" +
            "  (3, 140, \"07-08-23\", 3, \"Jefferson\"),\n" +
            "  (4, 104, \"04-14-22\", 3, \"Pitts\"),\n" +
            "  (5, 228, \"06-25-23\", 9, \"Jarvis\"),\n" +
            "  (1, 250, \"03-09-23\", 6, \"Cummings\"),\n" +
            "  (2, 205, \"05-11-23\", 5, \"Vance\"),\n" +
            "  (3, 188, \"03-25-22\", 7, \"Jenkins\"),\n" +
            "  (4, 277, \"07-21-22\", 2, \"Foster\"),\n" +
            "  (5, 101, \"01-25-22\", 2, \"Mcintosh\"),\n" +
            "  (1, 74, \"02-19-23\", 7, \"Frazier\"),\n" +
            "  (2, 129, \"01-06-23\", 5, \"Nixon\"),\n" +
            "  (3, 26, \"02-21-22\", 9, \"Mclean\"),\n" +
            "  (4, 234, \"10-25-22\", 4, \"Cortez\"),\n" +
            "  (5, 151, \"07-04-23\", 6, \"Holman\"),\n" +
            "  (1, 178, \"06-05-22\", 6, \"Lamb\"),\n" +
            "  (2, 198, \"03-10-23\", 5, \"Dyer\"),\n" +
            "  (3, 138, \"05-19-22\", 6, \"Lynn\"),\n" +
            "  (4, 13, \"03-02-22\", 8, \"Mccoy\"),\n" +
            "  (5, 267, \"11-24-21\", 7, \"Barnett\"),\n" +
            "  (1, 85, \"08-06-23\", 4, \"Mann\"),\n" +
            "  (2, 193, \"01-08-23\", 4, \"Terry\"),\n" +
            "  (3, 185, \"12-07-22\", 3, \"Clay\"),\n" +
            "  (4, 85, \"10-03-22\", 7, \"Mcintyre\"),\n" +
            "  (5, 87, \"04-04-22\", 7, \"Wilson\"),\n" +
            "  (1, 249, \"02-17-23\", 1, \"Sears\"),\n" +
            "  (2, 186, \"04-28-22\", 9, \"Chen\"),\n" +
            "  (3, 262, \"01-20-22\", 3, \"Singleton\"),\n" +
            "  (4, 271, \"10-08-23\", 9, \"Kim\"),\n" +
            "  (5, 6, \"01-04-23\", 2, \"Hart\"),\n" +
            "  (1, 290, \"02-06-22\", 2, \"Mills\"),\n" +
            "  (2, 179, \"03-05-23\", 4, \"Harding\"),\n" +
            "  (3, 118, \"08-16-23\", 10, \"Black\"),\n" +
            "  (4, 111, \"07-29-23\", 5, \"Gregory\"),\n" +
            "  (5, 216, \"05-11-23\", 10, \"David\"),\n" +
            "  (1, 107, \"11-29-21\", 7, \"Cervantes\"),\n" +
            "  (2, 33, \"01-06-22\", 2, \"Dejesus\"),\n" +
            "  (3, 226, \"09-14-23\", 4, \"Parks\"),\n" +
            "  (4, 234, \"06-16-23\", 9, \"Velasquez\"),\n" +
            "  (5, 111, \"12-16-22\", 7, \"Knapp\"),\n" +
            "  (1, 108, \"02-11-22\", 6, \"Phillips\"),\n" +
            "  (2, 7, \"06-22-22\", 2, \"Hester\"),\n" +
            "  (3, 87, \"05-16-22\", 7, \"Middleton\"),\n" +
            "  (4, 205, \"11-18-22\", 6, \"Hawkins\"),\n" +
            "  (5, 173, \"03-27-23\", 6, \"Maxwell\"),\n" +
            "  (1, 27, \"06-13-22\", 10, \"Olson\"),\n" +
            "  (2, 256, \"12-31-22\", 2, \"Travis\"),\n" +
            "  (3, 259, \"03-02-23\", 5, \"Whitehead\"),\n" +
//            "  (4, 300, \"02-17-22\", 3, \"Clarke\"),\n" +
            "  (5, 31, \"08-05-23\", 7, \"Leach\"),\n" +
            "  (1, 163, \"04-25-22\", 7, \"Lott\"),\n" +
            "  (2, 217, \"04-30-22\", 9, \"Hanson\"),\n" +
            "  (3, 271, \"07-18-23\", 9, \"Adams\"),\n" +
            "  (4, 12, \"05-18-23\", 9, \"Joyner\"),\n" +
            "  (5, 32, \"09-09-23\", 3, \"Lawson\"),\n" +
            "  (1, 16, \"04-17-22\", 1, \"Graves\"),\n" +
            "  (2, 61, \"09-10-22\", 4, \"Porter\"),\n" +
            "  (3, 210, \"02-12-23\", 9, \"Lee\"),\n" +
            "  (4, 108, \"10-10-23\", 2, \"O'Neill\"),\n" +
            "  (5, 220, \"12-02-22\", 5, \"Barker\"),\n" +
            "  (1, 95, \"03-22-22\", 8, \"Moon\"),\n" +
            "  (2, 63, \"10-02-23\", 5, \"Ball\"),\n" +
            "  (3, 202, \"04-22-23\", 4, \"Moses\"),\n" +
            "  (4, 290, \"07-25-23\", 7, \"Foley\"),\n" +
            "  (5, 13, \"03-28-23\", 4, \"Vargas\"),\n" +
            "  (1, 178, \"01-24-23\", 9, \"Emerson\"),\n" +
            "  (2, 158, \"08-08-23\", 10, \"Rice\"),\n" +
            "  (3, 262, \"01-18-23\", 5, \"Newman\"),\n" +
            "  (4, 30, \"04-12-22\", 1, \"Greene\"),\n" +
            "  (5, 231, \"05-10-23\", 4, \"Mckinney\"),\n" +
            "  (1, 180, \"05-20-23\", 4, \"Atkins\"),\n" +
            "  (2, 223, \"04-28-22\", 8, \"Ashley\"),\n" +
            "  (3, 294, \"01-06-23\", 2, \"Hawkins\"),\n" +
            "  (4, 109, \"03-17-23\", 7, \"Cain\"),\n" +
            "  (5, 106, \"10-24-22\", 8, \"Mclaughlin\"),\n" +
            "  (1, 141, \"11-26-22\", 1, \"Bowers\"),\n" +
            "  (2, 135, \"05-08-22\", 6, \"Cunningham\"),\n" +
            "  (3, 297, \"09-08-22\", 2, \"Tucker\"),\n" +
            "  (4, 279, \"09-22-23\", 6, \"Tate\"),\n" +
            "  (5, 187, \"10-02-23\", 6, \"Stone\"),\n" +
            "  (1, 115, \"01-11-23\", 10, \"Salazar\"),\n" +
            "  (2, 172, \"07-12-22\", 7, \"Clayton\"),\n" +
            "  (3, 239, \"05-15-23\", 5, \"Ochoa\"),\n" +
            "  (4, 178, \"08-07-22\", 2, \"Hammond\"),\n" +
            "  (5, 158, \"10-16-22\", 3, \"Acevedo\"),\n" +
            "  (1, 275, \"11-27-21\", 6, \"Parsons\"),\n" +
            "  (2, 104, \"01-28-23\", 2, \"Vasquez\"),\n" +
            "  (3, 90, \"08-11-22\", 9, \"Collier\"),\n" +
            "  (4, 43, \"03-11-23\", 9, \"Colon\"),\n" +
            "  (5, 54, \"12-08-21\", 8, \"Ball\"),\n" +
            "  (1, 65, \"08-09-23\", 2, \"Rutledge\"),\n" +
            "  (2, 164, \"04-03-22\", 4, \"Bowers\"),\n" +
            "  (3, 72, \"03-02-23\", 2, \"Chandler\"),\n" +
            "  (4, 289, \"07-21-23\", 7, \"Martin\"),\n" +
            "  (5, 124, \"08-04-23\", 6, \"Bates\");\n";
}