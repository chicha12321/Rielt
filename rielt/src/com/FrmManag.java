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

public class FrmManag extends JDialog {
	private static final long serialVersionUID = 1L;
	// ���� ������
	private DBManager manager;
	 private JTable tblManag;
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
	 private ArrayList<Sotrud> auss;
	 // ����������� ������
	 // �������� � �������� ����������
	public FrmManag(DBManager manager){
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
	 setSize (700, 500);
	 // ��������� ����
	 setTitle("����������");
	 setLocationRelativeTo(this);
	 }
	 // ��������� ������
	private void loadData() {
	 // ��������� ������ ����� ��������
	 auss=manager.loadManag();
	 }
	 // ����� �������� ����������������� ����������
	private void createGUI() {
	 // �������� ������
	 JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4",
	//	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
	 "[grow, fill]", "[]5[grow, fill]10[]"));
	 // �������� ������� �������
	 tblManag = new JTable();
	 // �������� ������� ��������� ������ �� ����
	 // ��������������� ������
	 tblManag.setModel(tblModel = new TKTableModel(auss));
	 // �������� ������� ���������� ��� ��������� ������
	 RowSorter<TKTableModel> sorter = new
	 TableRowSorter<TKTableModel>(tblModel);
	 // ���������� ������� ���������� �������
	 tblManag.setRowSorter(sorter);
	 // ������ ��������� �������� ���� �������
	 // ��������� ������� ���� ������� ������
	 tblManag.setRowSelectionAllowed(true);
	 // ������ ���������� ����� ��������
	 tblManag.setIntercellSpacing(new Dimension(0, 1)); 
	 // ������ ���� �����
	 tblManag.setGridColor(new Color(170, 170, 255).darker());
	 // �������������� ����������� ������ ��������� �������
	 tblManag.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	 // ����������� ��������� ������ 1 ������
	 tblManag.getSelectionModel().setSelectionMode(
	 ListSelectionModel.SINGLE_SELECTION);
	 // �������� ������� ��������� � ������� � ��� �������
	 JScrollPane scrlPane = new JScrollPane(tblManag);
	 scrlPane.getViewport().setBackground(Color.white);
	 scrlPane.setBorder(BorderFactory.createCompoundBorder
	(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
	 tblManag.getColumnModel().getColumn(0).setMaxWidth(100);
	 tblManag.getColumnModel().getColumn(0).setMinWidth(100);
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
						addManag();
					}
				});
				//  ��� ������ ��������������
				btnEdit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						editManag();
					}
				});
				//  ��� ������ ��������
				btnDelete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					deleteManag();
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
		URL url = FrmManag.class.getResource("/images/add.png");
		// �������� ������ � ������������
		btnNew = new JButton(new ImageIcon(url));
		// �� ������ �� ��������������� �����
		btnNew.setFocusable(false);
		//  ���������� ����������� ��������� ��� ������
		btnNew.setToolTipText("�������� ������ ���������");
		// ���������� ������ ���������
		url = FrmManag.class.getResource("/images/delete.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("������� ���������");
		// ���������� ������ ���������������
		url = FrmManag.class.getResource("/images/edit.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("�������� ������ ���������");
		//  ���������� ������ �� ������
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);
		// ������� ������ � �������� ����������
		return res;
	   }
	 
	//�������������� �������� ������.
		private void editManag() {			
			int index = tblManag.getSelectedRow();
			if (index == -1)
				return;
			// �������������� ������� ������� � ������ ������
			int modelRow = tblManag.convertRowIndexToModel(index);
			// �������� ������ �� ������ �� �������
			Sotrud prod = auss.get(modelRow);
			@SuppressWarnings("unused")
			BigDecimal key = prod.getKod_sotrud();
			
			// �������� ������� ���� ��������������
			EdManagDialog dlg = new EdManagDialog(this,
	    prod,manager);
			// ����� ���� � �������� ���� ��������
			if (dlg.showDialog() == JDialogResult.OK) {
	               // ����� ������ ���������� ������ ������ ��������� ������
				tblModel.updateRow(modelRow);
				System.out.println("���������� OK");
				}
			
			
	
		}
		// �������� ������ ���������
			private void addManag() {
				// �������� ������� ���� ��������������
				EdManagDialog dlg = new EdManagDialog(this, 
					null,manager);
				// ����� ���� � �������� ���� ��������
				if (dlg.showDialog() == JDialogResult.OK) {
					// �������� ������ ������� �� ��������� ������
					Sotrud prod = dlg.getManag();
					//  ���������� ��� � ��������� ������
					tblModel.addRow(prod);}
		
			}
			// �������� ������� ���������
			private void deleteManag() {
				// ���������� ������ ������� ������.
				int index = tblManag.getSelectedRow();
				// ���� ��� ���������� ������, �� �����
				if (index == -1)
					return;
				// ����� ������� �� ��������. ��� ������ - �����
				if (JOptionPane.showConfirmDialog(this, 
		  "������� ����������?", "�������������", 
		  JOptionPane.YES_NO_OPTION,
				  JOptionPane.QUESTION_MESSAGE) != 
				  JOptionPane.YES_OPTION)
				  return;
				// �������������� ������� ������������� � ������ ������
				int modelRow = tblManag.convertRowIndexToModel(index);
				// �������� ������� ��� ���������� ������
				Sotrud prod = auss.get(modelRow);
				try {
			// ����������� ���� (���������� �����) ���������� ������
				  BigDecimal kod = prod.getKod_sotrud();
				  // ����� ������ ��������� ��� �������� ������
				  if (manager.deleteManag(kod)) {
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
	private ArrayList<Sotrud> prods;
	 // ����������� ������� ������
	 public TKTableModel(ArrayList<Sotrud> prods) {
	 this.prods = prods;
	 }
	 // ���������� ������� � �������
	 @Override
	 public int getColumnCount() {
	 return 3;
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
		 Sotrud pr = prods.get(rowIndex);
	 // ������ ������� ������������ ���� �������
	 switch (columnIndex) {
	 case 0:
	 return pr.getKod_sotrud();
	 case 1:
	 return pr.getFio();
	 case 2:
		 return pr.getStag();
	 
	
	 default:
	 return null;
	 }
	 }
	 // ����������� �������� �������
	 @Override
	 public String getColumnName(int column) {
	 switch (column) {
	 case 0:
	 return "���";
	 case 1:
	 return "���";
	 case 2:
		 return "����";
	
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
	return java.lang.String.class;
	 else
	 return getValueAt(0, c).getClass();
	 }


//������� ���������� ������
			public void addRow(Sotrud prod) {
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


