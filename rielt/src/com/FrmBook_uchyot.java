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

public class FrmBook_uchyot extends JDialog {
    private static final long serialVersionUID = 1L;
    // ���� ������
    private DBManager manager;
    private JTable tblBook_uchyot;
    private JButton btnClose;
    // ������ ��������������
    private JButton btnEdit;
    // ������ ����������
    private JButton btnNew;
    // ������ ��������
    private JButton btnDelete;
    private TKTableModel tblModel;
    @SuppressWarnings("unused")
    private TableRowSorter<TKTableModel> tblSorter;
    private ArrayList<Book_uchyot> kls;

    // ����������� ������
    // �������� � �������� ����������
    public FrmBook_uchyot(DBManager manager) {
        super();
        this.manager = manager;
        // ��������� ���������� ������ ������ ����
        setModal(true);
        //��� �������� ���� ����������� ������������ �� �������
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // ��������� ������
        loadData();
        // ���������� ������������ ���������� ����
        createGUI();
        // ���������� ������������ ��� �������� �������
        bindListeners();
        //pack();
        setSize(1050, 500);
        // ��������� ����
        setTitle("����� �����");
        setLocationRelativeTo(this);
    }

    // ��������� ������
    private void loadData() {
        // ��������� ������ ����� ��������
        kls = manager.loadBook_uchyot(BigDecimal.valueOf(-1));
    }

    // ����� �������� ����������������� ����������
    private void createGUI() {
        // �������� ������
        JPanel pnfl = new JPanel(new MigLayout("insets 3, gapy 4",
                //	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
                "[grow, fill]", "[]5[grow, fill]10[]"));
        // �������� ������� �������
        tblBook_uchyot = new JTable();
        // �������� ������� ��������� ������ �� ����
        // ��������������� ������
        tblBook_uchyot.setModel(tblModel = new TKTableModel(kls));
        // �������� ������� ���������� ��� ��������� ������
        RowSorter<TKTableModel> sorter = new
                TableRowSorter<TKTableModel>(tblModel);
        // ���������� ������� ���������� �������
        tblBook_uchyot.setRowSorter(sorter);
        // ������ ��������� �������� ���� �������
        // ��������� ������� ���� ������� ������
        tblBook_uchyot.setRowSelectionAllowed(true);
        // ������ ���������� ����� ��������
        tblBook_uchyot.setIntercellSpacing(new Dimension(0, 1));
        // ������ ���� �����
        tblBook_uchyot.setGridColor(new Color(170, 170, 255).darker());
        // �������������� ����������� ������ ��������� �������
        tblBook_uchyot.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        // ����������� ��������� ������ 1 ������
        tblBook_uchyot.getSelectionModel().setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        // �������� ������� ��������� � ������� � ��� �������
        JScrollPane scrlPane = new JScrollPane(tblBook_uchyot);
        scrlPane.getViewport().setBackground(Color.white);
        scrlPane.setBorder(BorderFactory.createCompoundBorder
                (new EmptyBorder(3, 0, 3, 0), scrlPane.getBorder()));
        tblBook_uchyot.getColumnModel().getColumn(0).setMaxWidth(100);
        tblBook_uchyot.getColumnModel().getColumn(0).setMinWidth(100);
        // �������� ������ ��� �������� �����
        btnClose = new JButton("�������");
        //���������� �� ������: �����, ������� � �������� � ������
        pnfl.add(scrlPane, "grow, span");
        pnfl.add(btnClose, "growx 0, right");
        // ���������� ������ � ����
        getContentPane().setLayout(
                new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]",
                        "[grow, fill]"));
        getContentPane().add(pnfl, "grow");
        pnfl.add(getToolBar(), "growx,wrap");//��������  ������
//	 pnl.add(new JLabel("���������� ����� �������:"), "growx,span");
        pnfl.add(scrlPane, "grow, span");
        pnfl.add(btnClose, "growx 0, right");
    }

    // ����� ���������� ������������
    private void bindListeners() {
        // ��� ������ �������
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��������� ����
                dispose();
            }
        });
        //  ��� ������ ����������
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook_uchyot();
            }
        });
        //  ��� ������ ��������������
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBook_uchyot();
            }
        });
        //  ��� ������ ��������
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBook_uchyot();
            }
        });
    }

    private JToolBar getToolBar() {
        // �������� ������
        JToolBar res = new JToolBar();
        // ������������ ��������� ������
        res.setFloatable(false);
        // ���������� ������ ����������
        // ����������� �������������� ����������� ��� ������
        URL url = FrmBook_uchyot.class.getResource("/images/add.png");
        // �������� ������ � ������������
        btnNew = new JButton(new ImageIcon(url));
        // �� ������ �� ��������������� �����
        btnNew.setFocusable(false);
        //  ���������� ����������� ��������� ��� ������
        btnNew.setToolTipText("�������� ����� ������");
        // ���������� ������ ���������
        url = FrmBook_uchyot.class.getResource("/images/delete.png");
        btnDelete = new JButton(new ImageIcon(url));
        btnDelete.setFocusable(false);
        btnDelete.setToolTipText("������� ������");
        // ���������� ������ ���������������
        url = FrmBook_uchyot.class.getResource("/images/edit.png");
        btnEdit = new JButton(new ImageIcon(url));
        btnEdit.setFocusable(false);
        btnEdit.setToolTipText("�������� ������ ������");
        //  ���������� ������ �� ������
        res.add(btnNew);
        res.add(btnEdit);
        res.add(btnDelete);
        // ������� ������ � �������� ����������
        return res;
    }

    //�������������� ������� ������
    private void editBook_uchyot() {
        int index = tblBook_uchyot.getSelectedRow();
        if (index == -1)
            return;
        // �������������� ������� ������� � ������ ������
        int modelRow = tblBook_uchyot.convertRowIndexToModel(index);
        // �������� ������ �� ������ �� �������
        Book_uchyot prod = kls.get(modelRow);
        @SuppressWarnings("unused")
        BigDecimal key = prod.getId_book_uchyot();

        // �������� ������� ���� ��������������
        EdBook_uchyotDialog2 dlg = new EdBook_uchyotDialog2(this,
                prod, manager);
        // ����� ���� � �������� ���� ��������
        if (dlg.showDialog() == JDialogResult.OK) {
            // ����� ������ ���������� ������ ������ ��������� ������
            tblModel.updateRow(modelRow);
            System.out.println("���������� OK");
        }


    }

    // �������� ����� ������
    private void addBook_uchyot() {

        // �������� ������� ���� ��������������
        EdBook_uchyotDialog2 dlg = new EdBook_uchyotDialog2(this,
                null, manager);
        // ����� ���� � �������� ���� ��������
        if (dlg.showDialog() == JDialogResult.OK) {
            // �������� ������ ������� �� ��������� ������
            Book_uchyot prod = dlg.getBook_uchyot();
            //  ���������� ��� � ��������� ������
            tblModel.addRow(prod);
        }

    }

    // �������� ������� ������
    private void deleteBook_uchyot() {
        // ���������� ������ ������� ������.
        int index = tblBook_uchyot.getSelectedRow();
        // ���� ��� ���������� ������, �� �����
        if (index == -1)
            return;
        // ����� ������� �� ��������. ��� ������ - �����
        if (JOptionPane.showConfirmDialog(this,
                "������� ������?", "�������������",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) !=
                JOptionPane.YES_OPTION)
            return;
        // �������������� ������� ������������� � ������ ������
        int modelRow = tblBook_uchyot.convertRowIndexToModel(index);
        // �������� ������� ��� ���������� ������
        Book_uchyot prod = kls.get(modelRow);
        try {
            // ����������� ���� (���������� �����) ���������� ������
            BigDecimal kod = prod.getId_book_uchyot();
            // ����� ������ ��������� ��� �������� ������
            if (manager.deleteBook_uchyot(kod)) {
                // ����� ������ �������� ������ �� ��������� ������
                tblModel.deleteRow(modelRow);
                System.out.println("�������� OK");
            } else
                JOptionPane.showMessageDialog(this,
                        "������ �������� ������", "������",
                        JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "������ ��������",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    // ������ ������ ��� �������
    private class TKTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private ArrayList<Book_uchyot> prods;

        // ����������� ������� ������
        public TKTableModel(ArrayList<Book_uchyot> prods) {
            this.prods = prods;
        }

        // ���������� ������� � �������
        @Override
        public int getColumnCount() {
            return 8;
        }

        // ���������� ����� � ������� = ������� ������
        @Override
        public int getRowCount() {
            return (prods == null ? 0 : prods.size());
        }

        // ����������� ����������� �����
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            // �������� ������ �� ������ �� �������� �������
            Book_uchyot pr = prods.get(rowIndex);
            // ������ ������� ������������ ���� �������
            switch (columnIndex) {
                case 0:
                    return pr.getId_book_uchyot();
                case 1:
                    return pr.getId_zayav();
                case 2:
                    return pr.getKod_obj();
                case 3:
                    return pr.getUsluga().getName_usl();
                case 4:
                    return pr.getUch_date();
                case 5:
                    return pr.getSotrud().getFio();
                case 6:
                    return pr.getStoim();
                case 7:
                    return pr.getOpl_stoim();

                default:
                    return null;
            }
        }

        // ����������� �������� �������
        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "�� �����";
                case 1:
                    return "�� ������";
                case 2:
                    return "��� �������";
                case 3:
                    return "������";
                case 4:
                    return "���� ������";
                case 5:
                    return "���������";
                case 6:
                    return "���������";
                case 7:
                    return "��������";


                default:
                    return null;
            }
        }

        // ���� ����� ������������ ��� ����������� �����������
        // ����� ������� � ����������� �� ���� ������
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Class getColumnClass(int c) {
            if (c == 0) // ������ �� null � ������� 4
                return java.lang.Number.class;
            else if (c == 1) // ������ �� null � ������� 5
//return Date.class;
                return java.lang.Number.class;
            else
                return java.lang.String.class;
        }


        //������� ���������� ������
        public void addRow(Book_uchyot prod) {
            //  ���������� ��������� ����������� ������
            int len = prods.size();
            // ���������� � ����� ������ � ������ ������
            prods.add(prod);
            // ���������� ����������� ������ � ������ �������
            fireTableRowsInserted(len, len);
        }

        // ������� ��������������
        public void updateRow(int index) {
            // ���������� ����������� ���������� ������
            fireTableRowsUpdated(index, index);
        }

        // ������� ��������
        public void deleteRow(int index) {
            //  ���� ��������� ������ �� ����� �������
            if (index != prods.size() - 1)
                fireTableRowsUpdated(index + 1, prods.size() - 1);
            // �������� ������ �� ������ ������
            prods.remove(index);
            // ���������� ����������� ����� ��������
            fireTableRowsDeleted(index, index);
        }
    }
}
