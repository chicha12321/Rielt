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

import com.data.Obj_nedvij;
import com.data.*;
public class FrmObj_nedvij extends JDialog {
	private static final long serialVersionUID = 1L;
	// ���� ������
	private DBManager manager;
	 private JTable tblObj_nedvij;
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
	 private ArrayList<Obj_nedvij> kls;
	 // ����������� ������
	 // �������� � �������� ����������
	public FrmObj_nedvij(DBManager manager){
	 super();
	 this.manager=manager;
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
	 setSize (1100, 500);
	 // ��������� ����
	 setTitle("������� ������������");
	 setLocationRelativeTo(this);
	 }
	 // ��������� ������
	private void loadData() {
	 // ��������� ������ ����� ��������
	 kls=manager.loadObj_nedvij();
	 }
	 // ����� �������� ����������������� ����������
	private void createGUI() {
	 // �������� ������
	 JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4",
	//	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
	 "[grow, fill]", "[]5[grow, fill]10[]"));
	 // �������� ������� �������
	 tblObj_nedvij = new JTable();
	 // �������� ������� ��������� ������ �� ����
	 // ��������������� ������
	 tblObj_nedvij.setModel(tblModel = new TKTableModel(kls));
	 // �������� ������� ���������� ��� ��������� ������
	 RowSorter<TKTableModel> sorter = new
	 TableRowSorter<TKTableModel>(tblModel);
	 // ���������� ������� ���������� �������
	 tblObj_nedvij.setRowSorter(sorter);
	 // ������ ��������� �������� ���� �������
	 // ��������� ������� ���� ������� ������
	 tblObj_nedvij.setRowSelectionAllowed(true);
	 // ������ ���������� ����� ��������
	 tblObj_nedvij.setIntercellSpacing(new Dimension(0, 1)); 
	 // ������ ���� �����
	 tblObj_nedvij.setGridColor(new Color(170, 170, 255).darker());
	 // �������������� ����������� ������ ��������� �������
	 tblObj_nedvij.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	 // ����������� ��������� ������ 1 ������
	 tblObj_nedvij.getSelectionModel().setSelectionMode(
	 ListSelectionModel.SINGLE_SELECTION);
	 // �������� ������� ��������� � ������� � ��� �������
	 JScrollPane scrlPane = new JScrollPane(tblObj_nedvij);
	 scrlPane.getViewport().setBackground(Color.white);
	 scrlPane.setBorder(BorderFactory.createCompoundBorder
	(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
	 tblObj_nedvij.getColumnModel().getColumn(0).setMaxWidth(100);
	 tblObj_nedvij.getColumnModel().getColumn(0).setMinWidth(100);
	 // �������� ������ ��� �������� �����
	 btnClose = new JButton("�������");
	 //���������� �� ������: �����, ������� � �������� � ������
	 pnl.add(scrlPane, "grow, span");
	 pnl.add(btnClose, "growx 0, right");
	 // ���������� ������ � ����
	 getContentPane().setLayout(
	 new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]",
	 "[grow, fill]"));
	 getContentPane().add(pnl, "grow");
	 pnl.add(getToolBar(),"growx,wrap");//��������  ������
//	 pnl.add(new JLabel("���������� ����� �������:"), "growx,span");
	 	pnl.add(scrlPane, "grow, span");
	 	pnl.add(btnClose, "growx 0, right");
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
						addObj_nedvij();
					}
				});
				//  ��� ������ ��������������
				btnEdit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						editObj_nedvij();
					}
				});
				//  ��� ������ ��������
				btnDelete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					deleteObj_nedvij();
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
		URL url = FrmObj_nedvij.class.getResource("/images/add.png");
		// �������� ������ � ������������
		btnNew = new JButton(new ImageIcon(url));
		// �� ������ �� ��������������� �����
		btnNew.setFocusable(false);
		//  ���������� ����������� ��������� ��� ������
		btnNew.setToolTipText("�������� ����� ������");
		// ���������� ������ ���������
		url = FrmObj_nedvij.class.getResource("/images/delete.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("������� ������");
		// ���������� ������ ���������������
		url = FrmObj_nedvij.class.getResource("/images/edit.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("�������� ������ �������");
		//  ���������� ������ �� ������
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);
		// ������� ������ � �������� ����������
		return res;
	   }
	 
	//�������������� ������� ������
		private void editObj_nedvij() {			
			int index = tblObj_nedvij.getSelectedRow();
			if (index == -1)
				return;
			// �������������� ������� ������� � ������ ������
			int modelRow = tblObj_nedvij.convertRowIndexToModel(index);
			// �������� ������ �� ������ �� �������
			Obj_nedvij prod = kls.get(modelRow);
			@SuppressWarnings("unused")
			BigDecimal key = prod.getKod_obj();
			
			// �������� ������� ���� ��������������
			EdObj_nedvijDialog dlg = new EdObj_nedvijDialog(this,
	    prod,manager);
			// ����� ���� � �������� ���� ��������
			if (dlg.showDialog() == JDialogResult.OK) {
	               // ����� ������ ���������� ������ ������ ��������� ������
				tblModel.updateRow(modelRow);
				System.out.println("���������� OK");
				}
			
	
		}
		// �������� ����� ������
			private void addObj_nedvij() {
				// �������� ������� ���� ��������������
				EdObj_nedvijDialog dlg = new EdObj_nedvijDialog(this, 
					null,manager);
				// ����� ���� � �������� ���� ��������
				if (dlg.showDialog() == JDialogResult.OK) {
					// �������� ������ ������� �� ��������� ������
					Obj_nedvij prod = dlg.getObj_nedvij();
					//  ���������� ��� � ��������� ������
					tblModel.addRow(prod);}
		
			}
			// �������� ������� ������
			private void deleteObj_nedvij() {
				// ���������� ������ ������� ������.
				int index = tblObj_nedvij.getSelectedRow();
				// ���� ��� ���������� ������, �� �����
				if (index == -1)
					return;
				// ����� ������� �� ��������. ��� ������ - �����
				if (JOptionPane.showConfirmDialog(this, 
		  "������� ������ ������������?", "�������������", 
		  JOptionPane.YES_NO_OPTION,
				  JOptionPane.QUESTION_MESSAGE) != 
				  JOptionPane.YES_OPTION)
				  return;
				// �������������� ������� ������������� � ������ ������
				int modelRow = tblObj_nedvij.convertRowIndexToModel(index);
				// �������� ������� ��� ���������� ������
				Obj_nedvij prod = kls.get(modelRow);
				try {
			// ����������� ���� (���������� �����) ���������� ������
				  BigDecimal kod = prod.getKod_obj();
				  // ����� ������ ��������� ��� �������� ������
				  if (manager.deleteObj_nedvij(kod)) {
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
	private ArrayList<Obj_nedvij> prods;
	 // ����������� ������� ������
	 public TKTableModel(ArrayList<Obj_nedvij> prods) {
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
	 return (prods==null?0:prods.size());
	 }
	 // ����������� ����������� �����
	 @Override 
	 public Object getValueAt(int rowIndex, int columnIndex) {
	 // �������� ������ �� ������ �� �������� �������
		 Obj_nedvij pr = prods.get(rowIndex);
	 // ������ ������� ������������ ���� �������
	 switch (columnIndex) {
	 case 0:
	 return pr.getKod_obj();
	 case 1:
	 return pr.getZdan().getKod_zdan();
	 case 2:
		 return pr.getZdan().getNomer_dom();
	 case 3:
		 return pr.getZdan().getStreet().getName_street();
	 case 4:
		 return pr.getArea();
	 case 5:
		 return pr.getPrice();
	 case 6:
		 return pr.getFloor();
	 case 7:
		 return pr.getVid_ned();
	 
	
	 default:
	 return null;
	 }
	 }
	 // ����������� �������� �������
	 @Override
	 public String getColumnName(int column) {
	 switch (column) {
	 case 0:
	 return "��� �������";
	 case 1:
	 return "��� ������";
	 case 2:
		 return "����� ����";
	 case 3:
		 return "�����";
	 case 4:
		 return "�������";
	 case 5:
		 return "����";
	 case 6:
		 return "����";
	 case 7:
		 return "��� ������������";
	
	 
	 default:
	 return null;
	 }
	 }
	 // ���� ����� ������������ ��� ����������� �����������
	 // ����� ������� � ����������� �� ���� ������
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
	 if (c==0) // ������ �� null � ������� 4
	 return java.lang.Number.class;
	 else if (c == 1) // ������ �� null � ������� 5
//return Date.class;
	return java.lang.Number.class;
	 else
	 return getValueAt(0, c).getClass();
	 }


//������� ���������� ������
			public void addRow(Obj_nedvij prod) {
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
