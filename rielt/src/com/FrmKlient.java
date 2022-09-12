package com;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import com.data.*;

public class FrmKlient extends JDialog {
    private static final long serialVersionUID = 1L;
    // Поля класса
    private DBManager manager;
    private JTable tblKlient;
    private JButton btnClose;
    // кнопка редактирования
    private JButton btnEdit;
    // кнопка добавления
    private JButton btnNew;
    // кнопка удаления
    private JButton btnDelete;
    private TKTableModel tblModel;
    @SuppressWarnings("unused")
    private TableRowSorter<TKTableModel> tblSorter;
    private ArrayList<Klient> kls;

    // Конструктор класса
    // параметр – менеджер соединения
    public FrmKlient(DBManager manager) {
        super();
        this.manager = manager;
        // Установка модального режима вывода окна
        setModal(true);
        //при закрытии окна освобождаем используемые им ресурсы
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Получение данных
        loadData();
        // Построение графического интерфейса окна
        createGUI();
        // Добавление обработчиков для основных событий
        bindListeners();
        //pack();
        setSize(700, 500);
        // Заголовок окна
        setTitle("Клиенты");
        setLocationRelativeTo(this);
    }

    // Получение данных
    private void loadData() {
        // Получение данных через менеджер
        kls = manager.loadKlient();
    }

    // Метод создания пользовательского интерфейса
    private void createGUI() {
        // Создание панели
        JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4",
                //	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
                "[grow, fill]", "[]5[grow, fill]10[]"));
        // Создание объекта таблицы
        tblKlient = new JTable();
        // Создание объекта табличной модели на базе
        // сформированного списка
        tblKlient.setModel(tblModel = new TKTableModel(kls));
        // Создание объекта сортировки для табличной модели
        RowSorter<TKTableModel> sorter = new
                TableRowSorter<TKTableModel>(tblModel);
        // Назначение объекта сортировки таблице
        tblKlient.setRowSorter(sorter);
        // Задаем параметры внешнего вида таблицы
        // Выделение полосой всей текущей строки
        tblKlient.setRowSelectionAllowed(true);
        // Задаем промежутки между ячейками
        tblKlient.setIntercellSpacing(new Dimension(0, 1));
        // Задаем цвет сетки
        tblKlient.setGridColor(new Color(170, 170, 255).darker());
        // Автоматическое определение ширины последней колонки
        tblKlient.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        // Возможность выделения только 1 строки
        tblKlient.getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        // Создание области прокрутки и вставка в нее таблицы
        JScrollPane scrlPane = new JScrollPane(tblKlient);
        scrlPane.getViewport().setBackground(Color.white);
        scrlPane.setBorder(BorderFactory.createCompoundBorder
                (new EmptyBorder(3, 0, 3, 0), scrlPane.getBorder()));
        tblKlient.getColumnModel().getColumn(0).setMaxWidth(100);
        tblKlient.getColumnModel().getColumn(0).setMinWidth(100);
        // Создание кнопки для закрытия формы
        btnClose = new JButton("Закрыть");
        //Добавление на панель: метки, области с таблицей и кнопки
        pnl.add(scrlPane, "grow, span");
        pnl.add(btnClose, "growx 0, right");
        // Добавление панели в окно
        getContentPane().setLayout(
                new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]",
                        "[grow, fill]"));
        getContentPane().add(pnl, "grow");
        pnl.add(getToolBar(), "growx,wrap");//Создание  панели
//	 pnl.add(new JLabel("Справочник видов техники:"), "growx,span");
        pnl.add(scrlPane, "grow, span");
        pnl.add(btnClose, "growx 0, right");
    }

    // Метод назначения обработчиков
    private void bindListeners() {
        // Для кнопки Закрыть
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Закрываем окно
                dispose();
            }
        });
        //  Для кнопки добавления
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addKlient();
            }
        });
        //  Для кнопки редактирования
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editKlient();
            }
        });
        //  Для кнопки удаления
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteKlient();
            }
        });
    }

    private JToolBar getToolBar() {
        // Создание панели
        JToolBar res = new JToolBar();
        // Неизменяемое положение панели
        res.setFloatable(false);
        // Добавление кнопки «Добавить»
        // Определение местоположения изображения для кнопки
        URL url = FrmKlient.class.getResource("/images/add.png");
        // Создание кнопки с изображением
        btnNew = new JButton(new ImageIcon(url));
        // На кнопку не устанавливается фокус
        btnNew.setFocusable(false);
        //  Добавление всплывающей подсказки для кнопки
        btnNew.setToolTipText("Добавить нового клиента");
        // Добавление кнопки «Удалить»
        url = FrmKlient.class.getResource("/images/delete.png");
        btnDelete = new JButton(new ImageIcon(url));
        btnDelete.setFocusable(false);
        btnDelete.setToolTipText("Удалить клиента");
        // Добавление кнопки «Редактировать»
        url = FrmKlient.class.getResource("/images/edit.png");
        btnEdit = new JButton(new ImageIcon(url));
        btnEdit.setFocusable(false);
        btnEdit.setToolTipText("Изменить данные клиента");
        //  Добавление кнопок на панель
        res.add(btnNew);
        res.add(btnEdit);
        res.add(btnDelete);
        // Возврат панели в качестве результата
        return res;
    }

    //Редактирование текущего клиента
    private void editKlient() {
        int index = tblKlient.getSelectedRow();
        if (index == -1)
            return;
        // Преобразование индекса таблицы в индекс модели
        int modelRow = tblKlient.convertRowIndexToModel(index);
        // Получаем объект из модели по индексу
        Klient prod = kls.get(modelRow);
        @SuppressWarnings("unused")
        BigDecimal key = prod.getKod_klient();

        // Создание объекта окна редактирования
        EdKlientDialog dlg = new EdKlientDialog(this,
                prod, manager);
        // Вызов окна и проверка кода возврата
        if (dlg.showDialog() == JDialogResult.OK) {
            // Вызов метода обновления строки данных табличной модели
            tblModel.updateRow(modelRow);
            System.out.println("Обновление OK");
        }


    }

    // Создание нового клиента
    private void addKlient() {

        // Создание объекта окна редактирования
        EdKlientDialog dlg = new EdKlientDialog(this,
                null, manager);
        // Вызов окна и проверка кода возврата
        if (dlg.showDialog() == JDialogResult.OK) {
            // Создание нового объекта по введенным данным
            Klient prod = dlg.getKlient();
            //  Добавление его в табличную модель
            tblModel.addRow(prod);
        }

    }

    // Удаление текущего клиента
    private void deleteKlient() {
        // Определяем индекс текущей строки.
        int index = tblKlient.getSelectedRow();
        // Если нет выделенной строки, то выход
        if (index == -1)
            return;
        // Вывод запроса на удаления. При отказе - выход
        if (JOptionPane.showConfirmDialog(this,
                "Удалить клиента?", "Подтверждение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) !=
                JOptionPane.YES_OPTION)
            return;
        // Преобразование индекса представления в индекс модели
        int modelRow = tblKlient.convertRowIndexToModel(index);
        // Создание объекта для выделенной строки
        Klient prod = kls.get(modelRow);
        try {
            // Определение кода (первичного ключа) выделенной строки
            BigDecimal kod = prod.getKod_klient();
            // Вызов метода менеджера для удаления строки
            if (manager.deleteKlient(kod)) {
                // Вызов метода удаления строки из табличной модели
                tblModel.deleteRow(modelRow);
                System.out.println("Удаление OK");
            } else
                JOptionPane.showMessageDialog(this,
                        "Ошибка удаления строки", "Ошибка",
                        JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Ошибка удаления",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    // Модель данных для таблицы
    private class TKTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private ArrayList<Klient> prods;

        // Конструктор объекта класса
        public TKTableModel(ArrayList<Klient> prods) {
            this.prods = prods;
        }

        // Количество колонок в таблице
        @Override
        public int getColumnCount() {
            return 4;
        }

        // Количество строк в таблице = размеру списка
        @Override
        public int getRowCount() {
            return (prods == null ? 0 : prods.size());
        }

        // Определение содержимого ячеек
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            // Выделяем объект из списка по текущему индексу
            Klient pr = prods.get(rowIndex);
            // Каждой колонке сопоставляем поле объекта
            switch (columnIndex) {
                case 0:
                    return pr.getKod_klient();
                case 1:
                    return pr.getFio();
                case 2:
                    return pr.getTel();
                case 3:
                    return pr.getMoney();

                default:
                    return null;
            }
        }

        // Определение названия колонок
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Код";
                case 1:
                    return "ФИО";
                case 2:
                    return "Телефон";
                case 3:
                    return "Бюджет";

                default:
                    return null;
            }
        }

        // Этот метод используется для определения отрисовщика
        // ячеек колонок в зависимости от типа данных
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Class getColumnClass(int c) {
            if (c == 0) // защита от null в колонке 4
                return java.lang.Number.class;
            else if (c == 1) // защита от null в колонке 5
//return Date.class;
                return java.lang.String.class;
            else
                return getValueAt(0, c).getClass();
        }


        //Событие добавления строки
        public void addRow(Klient prod) {
            //  Определяем положение добавляемой строки
            int len = prods.size();
            // Добавление в новой строки в список модели
            prods.add(prod);
            // Обновление отображения строки с новыми данными
            fireTableRowsInserted(len, len);
        }

        // Событие редактирования
        public void updateRow(int index) {
            // Обновление отображения измененной строки
            fireTableRowsUpdated(index, index);
        }

        // Событие удаления
        public void deleteRow(int index) {
            //  Если удаленная строка не конце таблицы
            if (index != prods.size() - 1)
                fireTableRowsUpdated(index + 1, prods.size() - 1);
            // Удаление строки из списка модели
            prods.remove(index);
            // Обновление отображения после удаления
            fireTableRowsDeleted(index, index);
        }


    }
}


