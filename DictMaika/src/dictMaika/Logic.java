package dictMaika;

import java.time.LocalDate;

public class Logic {

    public boolean databaseWord(String wordBG, String wordDE) {

        //logic
        return Database.insertWord(wordBG, wordDE);
    }

    public Object[] getWordsForDay(LocalDate date) {

        //logic
        return Database.getWordsForDay(date);
    }

    public Object[] getWords() {

        return Database.getWords();
    }

    public boolean updateRow(String bg, String de, String calendar, String tempBg, String tempDe, String tempCalendar) {
        return Database.updateRow(bg, de, calendar, tempBg, tempDe, tempCalendar);
    }

    public boolean deleteRow(String bg, String de) {
        return Database.deleteRow(bg, de);
    }

    public Integer[] getUniqueDates() {
        return Database.getUniqueNumbers();
    }
}
