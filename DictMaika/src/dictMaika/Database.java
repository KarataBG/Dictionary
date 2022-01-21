package dictMaika;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Database {

    private static final LocalDate startDate = LocalDate.of(2021, 8, 31);
    private static final int chrono = (int) ChronoUnit.DAYS.between(startDate, LocalDate.now());
//    public static String path = null;
    static String url = "jdbc:sqlite:dictionary.db";

    public static boolean insertWord(String wordBG, String wordDE) {
        try {
            {
                try {
                    Connection connection = DriverManager.getConnection(url);

                    String sql = "Select * FROM words Where bg = ?";

                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, wordBG);

                    ResultSet result = statement.executeQuery();

                    result.next();

                    if (result.getRow() > 0) {
                        statement.close();
                        connection.close();
                        result.close();
                        return false;
                    }
                    statement.close();
                    connection.close();
                    result.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            Connection connection = DriverManager.getConnection(url);

            String sql = "Insert INTO words (bg, de, calendar) VALUES (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, wordBG);
            statement.setString(2, wordDE);
            statement.setString(3, String.valueOf(chrono));

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static Object[] getWordsForDay(LocalDate date) {

        try {
            Connection connection = DriverManager.getConnection(url);

            String sql = "Select * FROM words Where calendar = " + (int) ChronoUnit.DAYS.between(startDate, date);

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

//            StringBuilder wordsLines = new StringBuilder();
            ArrayList<String> words = new ArrayList<>();

            while (result.next()) {
//                wordsLines.append(result.getString(2)).append(".,.").append(result.getString(3)).append(".,.");
                words.add(result.getString(2));
                words.add(result.getString(3));
            }

//            System.out.println(wordsLines.toString());

//            if (wordsLines.length() - 1 >= 0) {
//                wordsLines.deleteCharAt(wordsLines.length() - 1);
//                wordsLines.deleteCharAt(wordsLines.length() - 1);
//                wordsLines.deleteCharAt(wordsLines.length() - 1);
//            }

            result.close();
            statement.close();
            connection.close();

//            return wordsLines.toString().split(".,.");
            return words.toArray();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new String[]{""};
    }

    public static Object[] getWords() {

        try {

            Connection connection = DriverManager.getConnection(url);

            String sql = "Select * FROM words";

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

//            StringBuilder wordsLines = new StringBuilder();
            ArrayList<String> words = new ArrayList<>();

            while (result.next()) {
//                wordsLines.append(result.getString(2)).append(".,.").append(result.getString(3)).append(".,.");
                words.add(result.getString(2));
                words.add(result.getString(3));
                words.add(result.getString(4));
            }

//            if (wordsLines.length() - 1 >= 0) {
//                wordsLines.deleteCharAt(wordsLines.length() - 1);
//                wordsLines.deleteCharAt(wordsLines.length() - 1);
//                wordsLines.deleteCharAt(wordsLines.length() - 1);
//            }

            result.close();
            statement.close();
            connection.close();

//            return wordsLines.toString().split(".,.");
            return words.toArray();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static boolean updateRow(String bg, String de, String calendar, String tempBg, String tempDe, String tempCalendar) {
        try {

            Connection connection = DriverManager.getConnection(url);

//            String sql2 = "Update words SET bg = " + bg + ", de = " + de + " Where id = 8" /* + "And de = " + tempDe*/;

            String sql = "UPDATE words SET bg = ?, de = ?, calendar = ? WHERE bg = ? AND de = ? AND calendar = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, bg);
            statement.setString(2, de);
            statement.setString(3, calendar);
            statement.setString(4, tempBg);
            statement.setString(5, tempDe);
            statement.setString(6, tempCalendar);

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static boolean deleteRow(String bg, String de) {
        try {
            Connection connection = DriverManager.getConnection(url);

            String sql = "Delete From words Where bg = ? And de = ?";

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, bg);
            statement.setString(2, de);

            statement.executeUpdate();

            statement.close();
            connection.close();

            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static Integer[] getUniqueNumbers() {

        try {

            Connection connection = DriverManager.getConnection(url);

            String sql = "SELECT DISTINCT" + " calendar " + "FROM" + " words";

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            ArrayList<Integer> uniqueNumbers = new ArrayList<>();

            while (result.next()) {
                uniqueNumbers.add(result.getInt(1));
            }

            result.close();
            statement.close();
            connection.close();

            return uniqueNumbers.toArray(new Integer[0]);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
