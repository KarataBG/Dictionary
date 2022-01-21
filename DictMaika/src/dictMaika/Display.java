package dictMaika;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.stream.IntStream;

public class Display {

    private final Font font = new Font("Times New Roman", Font.BOLD, 36);
    private final Font fontMed = new Font("Times New Roman", Font.BOLD, 21);
    private final Font fontSmall = new Font("Times New Roman", Font.BOLD, 18);
    private final JPanel jPanel = new JPanel(new GridBagLayout());
    private final JPanel jPanelZap = new JPanel(new GridBagLayout());
    private final JPanel jPanelIzb = new JPanel(new GridBagLayout());
    private final JPanel jPanelUpr = new JPanel(new GridBagLayout());
    private final JPanel jPanelRed = new JPanel(new GridBagLayout());
    private final GridBagConstraints c = new GridBagConstraints();
    private final Logic logic = new Logic();
    //
    private final Random r = new Random();
    JTable table = new JTable() {
        @Override
        public boolean isCellEditable(int row, int column) {
            //all cells false
            return false;
        }

        public Component prepareRenderer(TableCellRenderer r, int row, int col) {
            Component c = super.prepareRenderer(r, row, col);

            if (row % 2 == 0) {
                c.setBackground(Color.WHITE);
            } else {
                c.setBackground(new Color(234, 234, 234));
            }

            if (isRowSelected(row)) {
                c.setBackground(new Color(24, 134, 254));
            }

            return c;
        }
    };
    //
    private int i = 0;
    private int j = r.nextInt(2);
    private int[] pasts;
    private JFrame frame;
    private LocalDate dateChooserDate;

    public Display(String title, int width, int height) {

        Frame(title, width, height);
        c.ipady = 30;
        createStartPanel();
        createZapisPanel();
        createIzbPanel();
        createRedPanel();

        frame.add(jPanel);
        frame.pack();
    }

    public static Object[][] splitArray(Object[] inputArray, int chunkSize) {

        return IntStream.iterate(0, i -> i + chunkSize)
                .limit((int) Math.ceil((double) inputArray.length / chunkSize))
                .mapToObj(j -> Arrays.copyOfRange(inputArray, j, Math.min(inputArray.length, j + chunkSize)))
                .toArray(Object[][]::new);

//        List<Object[]> list = new ArrayList<>();
//        long limit = (int) Math.ceil((double) inputArray.length / chunkSize);
//        for (int i1 = 0; ; i1 = i1 + chunkSize) {
//            if (limit-- == 0) break;
//            Object[] objects = Arrays.copyOfRange(inputArray, i1, Math.min(inputArray.length, i1 + chunkSize));
//            list.add(objects);
//        }
//        return list.toArray(new String[0][]);
    }

    public static void fitToContentWidth(final JTable table, final int column) {
        int width = 0;
        for (int row = 0; row < table.getRowCount(); ++row) {
            final Object cellValue = table.getValueAt(row, column);
            final TableCellRenderer renderer = table.getCellRenderer(row, column);
            final Component comp
                    = renderer.getTableCellRendererComponent(table, cellValue, false, false, row, column);
            width = Math.max(width, comp.getPreferredSize().width);
        }
        final TableColumn tc = table.getColumn(table.getColumnName(column));
        width += table.getIntercellSpacing().width * 2;
        tc.setPreferredWidth(width);
        tc.setMinWidth(width);
    }

    private void Frame(String title, int width, int height) {
        frame = new JFrame(title);

//        frame.setBounds(0, 0, width, height);
        frame.setMinimumSize(new Dimension(width, height));
        frame.setResizable(true);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(0,0);
    }

    private void createStartPanel() {

        JLabel zapis = new JLabel("Записване");
        zapis.setFont(font);
        JButton doZapis = new JButton("Запис");
        doZapis.setFont(font);

        JLabel uprazhnenie = new JLabel("Упражнение");
        uprazhnenie.setFont(font);
        JButton doUprazhnenie = new JButton("Упражнение");
        doUprazhnenie.setFont(font);

        JLabel redaktirane = new JLabel("Редактиране");
        redaktirane.setFont(font);
        JButton doRedaktirane = new JButton("Редактиране");
        doRedaktirane.setFont(font);

        doZapis.addActionListener(e1 -> {
            frame.remove(jPanel);
            frame.add(jPanelZap);
            frame.pack();
        });

        doUprazhnenie.addActionListener(e -> {
            frame.remove(jPanel);
            frame.add(jPanelIzb);
            frame.pack();
            frame.repaint();
        });

        doRedaktirane.addActionListener(e -> {
            frame.remove(jPanel);
            frame.add(jPanelRed);
            updateTable();
            frame.pack();
            frame.repaint();
        });

        c.gridx = 0;
        c.gridy = 0;

        jPanel.add(zapis, c);
        c.gridx = 1;
        jPanel.add(uprazhnenie, c);
        c.gridx = 2;
        jPanel.add(redaktirane, c);
        c.gridy = 1;
        c.gridx = 0;
        jPanel.add(doZapis, c);
        c.gridx = 1;
        jPanel.add(doUprazhnenie, c);
        c.gridx = 2;
        jPanel.add(doRedaktirane, c);

    }

    private void createZapisPanel() {

        JLabel bg = new JLabel("БГ");
        bg.setFont(font);
        JLabel de = new JLabel("De");
        de.setFont(font);
        JLabel confirm = new JLabel("");
        confirm.setFont(font);

        JTextField textFieldBG = new JTextField();
        textFieldBG.setFont(font);
        textFieldBG.setPreferredSize(new Dimension(400, 30));
        textFieldBG.setText("");

        JTextField textFieldDE = new JTextField();
        textFieldDE.setFont(font);
        textFieldDE.setPreferredSize(new Dimension(400, 30));
        textFieldDE.setText("");

        JButton zapisBtn = new JButton("Запис");
        zapisBtn.setFont(font);
        zapisBtn.addActionListener(e -> {
            if (!textFieldBG.getText().equals("") && !textFieldDE.getText().equals("")) {
                if (logic.databaseWord(textFieldBG.getText(), textFieldDE.getText()))
                    confirm.setText("Добавено");
                else
                    confirm.setText("Не добавено");

                textFieldBG.setText("");
                textFieldDE.setText("");
//                confirm.setText("Потвърждение");
            }
        });

        JButton backBtn = new JButton("Назад");
        backBtn.setFont(font);
        backBtn.addActionListener(e -> {
            textFieldBG.setText("");
            textFieldDE.setText("");
            confirm.setText("");
            frame.remove(jPanelZap);
            frame.add(jPanel);
            frame.pack();
        });

        c.gridx = 0;
        c.gridy = 0;

        jPanelZap.add(bg, c);
        c.gridx = 1;
        jPanelZap.add(de, c);
        c.gridy = 1;
        c.gridx = 0;
        jPanelZap.add(textFieldBG, c);
        c.gridx = 1;
        jPanelZap.add(textFieldDE, c);
        c.gridy = 2;
        jPanelZap.add(zapisBtn, c);
        c.gridx = 0;
        jPanelZap.add(backBtn, c);
        c.gridy = 3;
        jPanelZap.add(confirm, c);

    }

    private void createIzbPanel() {

//        JDateChooser dateChooser = new JDateChooser();
//        dateChooser.setPreferredSize(new Dimension(400, 80));
//        dateChooser.setFont(font);
//        dateChooser.getCalendarButton().setPreferredSize(new Dimension(100, 100));

//        logic.getUniqueDates();

        String[] yearChoices = {"2021", "2022", "2023", "2024", "2025"};
        final JComboBox<String> yearCB = new JComboBox<>(yearChoices);
        yearCB.setPreferredSize(new Dimension(200, 80));
        yearCB.setFont(font);

        String[] monthChoices = {"Яну", "Фев", "Мар", "Апр", "Май", "Юни", "Юли", "Авг", "Сеп", "Окт", "Ное", "Дек"};
        final JComboBox<String> monthCB = new JComboBox<>(monthChoices);
        monthCB.setPreferredSize(new Dimension(200, 80));
        monthCB.setFont(font);

        JComboBox<String> dayCB = new JComboBox<>();
        dayCB.setPreferredSize(new Dimension(200, 80));
        dayCB.setFont(font);


        JButton izbor = new JButton("Избор");
        izbor.setFont(font);
        izbor.addActionListener(e -> {
//            dateChooserDate = dateChooser.getDate().toInstant().
//                    atZone(ZoneId.systemDefault()).toLocalDate();

            int year = yearCB.getSelectedIndex() + 2021;
            int month = monthCB.getSelectedIndex() + 1;

            @SuppressWarnings("MagicConstant") Calendar calendarStart1 = new GregorianCalendar(year, month - 1, 1);

            System.out.println(month);

            LocalDate startTime = LocalDate.of(2021, Month.AUGUST, 31);
            LocalDate calendarStart = LocalDate.of(year, month, 1);
            LocalDate calendarEnd = LocalDate.of(year, month, calendarStart1.getActualMaximum(Calendar.DAY_OF_MONTH));

            int minimumDate = (int) ChronoUnit.DAYS.between(startTime, calendarStart) - 1;
            int maximumDate = (int) ChronoUnit.DAYS.between(startTime, calendarEnd) + 1;

            dayCB.setModel(new DefaultComboBoxModel<>());
            for (Integer i : logic.getUniqueDates()) {
                if (i > minimumDate) {
                    if (i > maximumDate)
                        break;
                    LocalDate dayAdder = startTime.plusDays(i);

                    dayCB.addItem(dayAdder.toString());
                }
            }

        });

        JButton next = new JButton("Next");
        next.setFont(font);
        next.addActionListener(e -> {

            if (dayCB.getSelectedItem() != null) {
                System.out.println(dayCB.getSelectedItem());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                dateChooserDate = LocalDate.parse((CharSequence) dayCB.getSelectedItem(), formatter);

                createUprPanel();

                frame.remove(jPanelIzb);
                frame.add(jPanelUpr);
                frame.pack();
            }
        });

        JButton nazad = new JButton("Назад");
        nazad.setFont(font);
        nazad.addActionListener(e1 -> {
            frame.remove(jPanelIzb);
            frame.add(jPanel);
            frame.pack();
        });

        c.gridx = 0;
        c.gridy = 0;

        jPanelIzb.add(nazad);
        jPanelIzb.add(yearCB);
        c.gridx = 1;
        jPanelIzb.add(monthCB);
        c.gridx = 2;
        jPanelIzb.add(izbor);
        jPanelIzb.add(dayCB);
        c.gridx = 0;
        c.gridy = 1;
        c.gridx = 1;
        jPanelIzb.add(next);
    }

    private void createUprPanel() {

        jPanelUpr.removeAll();

        Object[][] words = splitArray(logic.getWordsForDay(dateChooserDate), 2);

        pasts = new int[words.length];
        Arrays.fill(pasts, 0);
        pasts[0] = j;

        for (int k = 0; k < words.length * 5; k++) {
            int r1 = r.nextInt(words.length);
            int r2 = r.nextInt(words.length);
            Object[] temp = words[r1];
            words[r1] = words[r2];
            words[r2] = temp;
        }
        System.out.println(i);
        System.out.println(j);

        JLabel duma = new JLabel(String.valueOf(words[i][j]), SwingConstants.LEFT);
        duma.setFont(font);
        duma.setMinimumSize(new Dimension(800, 30));// ????????????????????

        JLabel otgowor = new JLabel("Отговор", SwingConstants.LEFT);
        otgowor.setFont(font);
        otgowor.setMinimumSize(new Dimension(800, 30));

        JButton sled = new JButton("Следващ");
        sled.addActionListener(e -> {
            incrementor(duma, words);
            otgowor.setText("Отговор");
        });
        JButton preden = new JButton("Преден");
        preden.addActionListener(e -> {
            decrementor(duma, words);
            otgowor.setText("Отговор");
        });
        JButton prowerka = new JButton("Проверка");
        prowerka.addActionListener(e -> {
            otgowor.setText((String) words[i][1 - pasts[i]]);
        });

        JButton nazad = new JButton("Назад");
        nazad.addActionListener(e1 -> {
            frame.remove(jPanelUpr);
            frame.add(jPanelIzb);
            frame.pack();
        });

        JPanel labelHolder = new JPanel(new GridBagLayout());

        c.gridx = 0;
        c.gridy = 0;
        labelHolder.add(duma, c);
        c.gridy = 1;
        labelHolder.add(otgowor, c);

        JPanel buttonHolder = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        buttonHolder.add(nazad, c);
        c.gridx = 1;
        buttonHolder.add(preden, c);
        c.gridx = 2;
        buttonHolder.add(sled, c);
        c.gridx = 3;
        buttonHolder.add(prowerka, c);

        c.gridx = 0;
        c.gridy = 0;
        jPanelUpr.add(labelHolder, c);
        c.gridy = 1;
        jPanelUpr.add(buttonHolder, c);
        frame.pack();
    }

    private void updateTable() {
        table.setModel(new DefaultTableModel(splitArray(logic.getWords(), 3), new String[]{"bg", "de", "calendar"}));
        fitToContentWidth(table, 0);
        fitToContentWidth(table, 1);
        fitToContentWidth(table, 2);
    }

    private void createRedPanel() {

        jPanelRed.removeAll();

        JPanel topSide = new JPanel(new GridBagLayout());
        JPanel bottomSide = new JPanel(new GridBagLayout());

        //LQWO
        table.setFont(fontMed);
        table.setRowHeight(40);

        updateTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 400));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(30, 30));

        //DQSNO
        JLabel changed = new JLabel("");
        changed.setPreferredSize(new Dimension(200, 30));
        changed.setFont(fontSmall);

        JLabel deleted = new JLabel("");
        deleted.setPreferredSize(new Dimension(200, 30));
        deleted.setFont(fontSmall);

        JLabel bgLabel = new JLabel("BG");
        JLabel deLabel = new JLabel("DE");
        JLabel calLabel = new JLabel("Calendar");

        JTextField bg = new JTextField();
        bg.setFont(fontMed);
        bg.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth() / 2, 30));
        bg.setText("");

        JTextField de = new JTextField();
        de.setFont(fontMed);
        de.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth() / 2, 30));
        de.setText("");

        JTextField calendar = new JTextField();
        calendar.setFont(fontMed);
        calendar.setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth() / 2, 30));
        calendar.setText("");

        String[] tempWords = new String[3];

        table.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
//                int col = table.getSelectedColumn();
                tempWords[0] = (String) table.getValueAt(row, 0);
                tempWords[1] = (String) table.getValueAt(row, 1);
                tempWords[2] = (String) table.getValueAt(row, 2);

                bg.setText(tempWords[0]);
                de.setText(tempWords[1]);
                calendar.setText(tempWords[2]);

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JButton change = new JButton("Промени");
        change.setFont(font);
        change.addActionListener(e -> {
            if (!bg.getText().equals("") && !de.getText().equals("")) {
                changed.setText(logic.updateRow(bg.getText(), de.getText(), calendar.getText(), tempWords[0], tempWords[1], tempWords[2]) ? "Променено" : "Грешка");
                updateTable();

                deleted.setText("");
                bg.setText("");
                de.setText("");
                calendar.setText("");

                tempWords[0] = "";
                tempWords[1] = "";
                tempWords[2] = "";
            }
        });

        JButton delete = new JButton("Изтрий");
        delete.setFont(font);
        delete.addActionListener(e -> {
            if (!bg.getText().equals("") && !de.getText().equals("")) {
                deleted.setText(logic.deleteRow(tempWords[0], tempWords[1]) ? "Изтрито" : "Грешка");
                updateTable();
                changed.setText("");
                bg.setText("");
                de.setText("");
                calendar.setText("");

                tempWords[0] = "";
                tempWords[1] = "";
                tempWords[2] = "";
            }
        });

        JButton nazad = new JButton("Назад");
        nazad.setFont(font);
        nazad.addActionListener(e -> {
            bg.setText("");
            de.setText("");
            calendar.setText("");

            tempWords[0] = "";
            tempWords[1] = "";
            tempWords[2] = "";

            changed.setText("");
            deleted.setText("");

            frame.remove(jPanelRed);
            frame.add(jPanel);
            frame.pack();
        });

        c.gridx = 0;
        c.gridy = 0;
        topSide.add(scrollPane, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        bottomSide.add(bg, c);
        c.gridwidth = 1;
        c.gridx = 2;
        bottomSide.add(bgLabel, c);


        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        bottomSide.add(de, c);
        c.gridwidth = 1;
        c.gridx = 2;
        bottomSide.add(deLabel, c);


        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        bottomSide.add(calendar, c);
        c.gridwidth = 1;
        c.gridx = 2;
        bottomSide.add(calLabel, c);

        c.gridx = 0;
        c.gridy = 4;
        bottomSide.add(delete, c);
        c.gridx = 1;
        bottomSide.add(nazad, c);
        c.gridx = 2;
        bottomSide.add(change, c);
        c.gridx = 0;
        c.gridy = 5;
        bottomSide.add(changed, c);
        c.gridx = 1;
        bottomSide.add(deleted, c);

        c.gridx = 0;
        c.gridy = 0;

        jPanelRed.add(topSide, c);
        c.gridy = 1;
        jPanelRed.add(bottomSide, c);

    }

    private void incrementor(JLabel duma, Object[][] words) {
        i++;
        j = r.nextInt(2);
        if (i == words.length) {
            i = 0;
        }

        duma.setText(String.valueOf(words[i][j]));
        pasts[i] = j;
    }

    private void decrementor(JLabel duma, Object[][] words) {
        i--;
        if (i < 0) {
            i = words.length - 1;
        }
        duma.setText(String.valueOf(words[i][pasts[i]]));
    }

}
