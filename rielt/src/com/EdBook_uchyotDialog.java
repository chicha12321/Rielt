package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import com.data.*;

import net.miginfocom.swing.MigLayout;

public class EdBook_uchyotDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// ���� ������
	  private DBManager manager;
	  private BigDecimal fk_key;
	  private BigDecimal old_key; // ������� �������� �����
	  // ��� �������� ��������� ����
	  private final static String title_add = 
	    "���������� ����� ������";
	  private final static String title_ed = 
	     "�������������� ������";
	   //  ��������� ���������� ��� ������ ����� �����
		private Book_uchyot type = null;
	    // ���� ������ ����������� ����� ������
		private boolean isNewRow = false;
	    // �������� ��� ����� ���
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // �������� (���� ��������������) ��� ����� ������
		private JTextField edCod;
		
		private JTextField edCod_sotrud;
		private JTextField edOpl_stoim;
		private JTextField edDate_zaver;
		private JTextField edStoim, edKod_obj, edKod_usl;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbSotr, cmbUsl;
		//  ������
		private JButton btnOk;
		private JButton btnCancel;
		//��� ����. �������
		 private JTable tblBook_oplat;
		 
			private JButton btnClose;
			// ������ �������������� 
				private JButton btnEdit;
				// ������ ���������� 
				private JButton btnNew;
				// ������ �������� 
				private JButton btnDelete;
				//���� ������
			private TKTableModel tblModel;
			 private ArrayList<Book_oplat> kls;
	    //  ����������� ������
		public EdBook_uchyotDialog(Window parent,Book_uchyot type,
	                    DBManager manager, BigDecimal fk_key) {
	      this.manager = manager;
	  // ��������� ����� ��� ������ ���������� ����� ������
		  isNewRow = type == null ? true : false;
		  // ����������� ��������� ��� �������� ������./������.
		  setTitle(isNewRow ? title_add : title_ed);
		     this.fk_key=fk_key;
		  // ����������� ������� ������������� ������  
		  if (!isNewRow) {
		     this.type = type; // ������������ ������
		     // ���������� �������� �������� �����
		     old_key=type.getId_book_uchyot();
		
		     loadDataGruz();
		     }
		  else {
		     this.type = new Book_uchyot();	// ����� ������
		     kls=new ArrayList <Book_oplat>();
		  }
		  // �������� ������������ ���������� ����
		  createGui();
		  // ���������� ������������ ��� �������� �������
		  bindListeners();
		  //  ��������� ������
		  loadData();
		  pack();
		  // ������� ������ ������������ �������� ����
		  setResizable(false);
		  setButton(); // ��������� ����������� ������ �����
		  setLocationRelativeTo(parent);
		}
		// ����� ��������� ������ �����
		private void setButton() {
		if (btnOk.getText().equals("���������")) {
		btnCancel.setText("������");
		btnNew.setEnabled(false);
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		} else {
		btnCancel.setText("�����");
		btnNew.setEnabled(true);
		btnEdit.setEnabled(true);
		btnDelete.setEnabled(true);
		}
		}
		 // ��������� ������ � ������� ����
		private void loadDataGruz() {
		 // ��������� ������ ����� ��������
		 kls=manager.loadBook_oplat(type.getId_book_uchyot());
	//		sost_recipes=manager.loadSost_recipe();
		 }
		//����� ���������� ������������ �������� �������
		  private void bindListeners() {
		   //  ���������� ������� ������ 
		   setKeyListener(this, new KeyAdapter(){
		    @Override
	        // ��������� ������� ������� ESC � �������� ���� 
		    public void keyPressed(KeyEvent e){
		      if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
				setDialogResult(JDialogResult.Cancel);
				close();
				e.consume();
			}
			else
				super.keyPressed(e);
			}
		  });
	      // ��������� ������� ������ �������� ����  
		  addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		  });
	      //  ���������  ������ �������
		  btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
			  // ������� Cancel � �������� ����
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		  });
		  //  ���������  ������ �����������
		  btnOk.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
			  if (((AbstractButton) e.getSource()).getText().equals("���������")) {
			  // �������� ������, ������� Ok � �������� ����
			  if (!constructBook_uchyot()) return;
			  //����� ������ ���������� 31.03.2021
			  else {if (isNewRow)
			  			{
				  		if (manager.addBook_uchyot(type)==true)
				  			{
				  					setDialogResult(JDialogResult.OK);
				  					((AbstractButton) e.getSource()).setText("�������������");
				  					setButton();
				  			}
			  			}
			  else 
				  if (manager.updateBook_uchyot(type, old_key)==true)
				  {
					  setDialogResult(JDialogResult.OK);
				  ((AbstractButton) e.getSource()).setText("�������������");
				  setButton();
				  }	} 
			  }
			  else {
			  ((AbstractButton) e.getSource()).setText("���������");
			  setButton();
			  }
			  }
			  });
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
						addBook_oplat();
					}
				});
				//  ��� ������ ��������������
				btnEdit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						editBook_oplat();
					}
				});
				//  ��� ������ ��������
				btnDelete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					deleteBook_oplat();
					}
				});
				// ������� ������ �������� ������
				cmbgor.addItemListener(new ItemListener() {
				 @Override
				 public void itemStateChanged(ItemEvent e) {
				 // ��������� �������� ���� �������� �����
				 if (e.getStateChange() == ItemEvent.SELECTED) {
				 if (e.getItem() != null) {
				 Obj_nedvij gr = (Obj_nedvij) e.getItem();
				 edKod_obj.setText(gr.getKod_obj().toString());
				 }
				 }
				 }
				});
				// ������� ������ ������ ����� �����
				edKod_obj.addFocusListener(new FocusListener() {
				 @Override
				 public void focusLost(FocusEvent e) {
				 // ��� ������ ������ �������� ������� ������
					//�������� �� �������-��������� �������
					 if(!(edKod_obj.getText().isEmpty())) {
				 @SuppressWarnings("rawtypes")
				DefaultComboBoxModel model =
				(DefaultComboBoxModel) cmbgor.getModel();
				 BigDecimal grKod =
				new BigDecimal(edKod_obj.getText());
				 setCmbItem(model, grKod);
				 }
				 }
				 @Override
				 public void focusGained(FocusEvent e) {
				 }
				});
				// ������� ������ �������� ������
				  cmbSotr.addItemListener(new ItemListener() {
				  @Override
				  public void itemStateChanged(ItemEvent e) {
				  // ��������� �������� ���� �������� �����
				  if (e.getStateChange() == ItemEvent.SELECTED) {
				  if (e.getItem() != null) {
				  Sotrud c4 = (Sotrud) e.getItem();
				  edCod_sotrud.setText(c4.getKod_sotrud().toString());
				  }
				  }
				  }
				  });
				  // ������� ������ ������ ����� �����
				  edCod_sotrud.addFocusListener(new FocusListener() {
				  @Override
				  public void focusLost(FocusEvent e) {
				  // ��� ������ ������ �������� ������� ������
						//�������� �� �������-��������� �������
						 if(!(edCod_sotrud.getText().isEmpty())) {
				  @SuppressWarnings("rawtypes")
				DefaultComboBoxModel model5 =
				  (DefaultComboBoxModel) cmbSotr.getModel();
				  BigDecimal grKod =
				  new BigDecimal(edCod_sotrud.getText());
				  setCmbItem6(model5, grKod);
				  }
				  }
				  
				  
				  @Override
				  public void focusGained(FocusEvent e) {
				  }
				  });
				// ������� ������ �������� ������
				  cmbUsl.addItemListener(new ItemListener() {
				  @Override
				  public void itemStateChanged(ItemEvent e) {
				  // ��������� �������� ���� �������� �����
				  if (e.getStateChange() == ItemEvent.SELECTED) {
				  if (e.getItem() != null) {
				  Usluga c2 = (Usluga) e.getItem();
				  edKod_usl.setText(c2.getKod_usl().toString());
				  }
				  }
				  }
				  });
				  // ������� ������ ������ ����� �����
				  edKod_usl.addFocusListener(new FocusListener() {
				  @Override
				  public void focusLost(FocusEvent e) {
				  // ��� ������ ������ �������� ������� ������
						//�������� �� �������-��������� �������
						 if(!(edKod_usl.getText().isEmpty())) {
				  @SuppressWarnings("rawtypes")
				DefaultComboBoxModel model5 =
				  (DefaultComboBoxModel) cmbUsl.getModel();
				  BigDecimal grKod =
				  new BigDecimal(edKod_usl.getText());
				  setCmbItem2(model5, grKod);
				  }
				  }
				  
				  
				  @Override
				  public void focusGained(FocusEvent e) {
				  }
				  });
				} // ����� 
		
		  // ����� �������� ������������ ����������
		private void createGui() {
			// �������� ������
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// �������� ����� ��� �������������� ������
	edCod = new JTextField(10);
	
	cmbgor =new JComboBox();
	cmbSotr = new JComboBox();
	cmbUsl = new JComboBox();
			edCod_sotrud = new JTextField(10);
			edKod_obj = new JTextField(10);
			
			edKod_usl = new JTextField(10);
			edOpl_stoim = new JTextField(50);
			edStoim = new JTextField(40);
			edDate_zaver = new JFormattedTextField(
	                createFormatter("##-##-####"));
			edDate_zaver.setColumns(10);
			//  �������� ������
			btnOk = new JButton("���������");
			btnCancel = new JButton("������");
			// ���������� ��������� �� ������
			//pnl.add(new JLabel("�� ����� �����"));
		//	pnl.add(edCod,"span");
			//pnl.add(new JLabel("�� ������"));
		//	pnl.add(edId_zayav,"span");
			pnl.add(new JLabel("������"));
			pnl.add(edKod_obj, "split 2");
			pnl.add(cmbgor, "growx, wrap");
			pnl.add(new JLabel("������"));
		
			pnl.add(edKod_usl, "split 2");
			pnl.add(cmbUsl, "growx, wrap");
			pnl.add(new JLabel("���� ������"));
			pnl.add(edDate_zaver,"span");
		
			pnl.add(new JLabel("���������"));
			pnl.add(edCod_sotrud, "split 2");
			pnl.add(cmbSotr, "growx, wrap");
			pnl.add(new JLabel("���������"));
			pnl.add(edStoim,"span");
			pnl.add(new JLabel("���������� ���������"));
			pnl.add(edOpl_stoim,"span");
			pnl.add(btnOk, "span, split 2, center, sg ");
			pnl.add(btnCancel, "sg 1");
			//  ���������� ������ � ���� ������
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(pnl, BorderLayout.NORTH);
			
			 // �������� ������
			 JPanel pnfl = new JPanel(new MigLayout("insets 3, gapy 4",
			//	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
			 "[grow, fill]", "[]5[grow, fill]10[]"));
			 // �������� ������� �������
			 tblBook_oplat = new JTable();
			 // �������� ������� ��������� ������ �� ����
			 // ��������������� ������
			 tblBook_oplat.setModel(tblModel = new TKTableModel(kls));
			 // �������� ������� ���������� ��� ��������� ������
			 RowSorter<TKTableModel> sorter = new
			 TableRowSorter<TKTableModel>(tblModel);
			 // ���������� ������� ���������� �������
			 tblBook_oplat.setRowSorter(sorter);
			 // ������ ��������� �������� ���� �������
			 // ��������� ������� ���� ������� ������
			 tblBook_oplat.setRowSelectionAllowed(true);
			 // ������ ���������� ����� ��������
			 tblBook_oplat.setIntercellSpacing(new Dimension(0, 1)); 
			 // ������ ���� �����
			 tblBook_oplat.setGridColor(new Color(170, 170, 255).darker());
			 // �������������� ����������� ������ ��������� �������
			 tblBook_oplat.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			 // ����������� ��������� ������ 1 ������
			 tblBook_oplat.getSelectionModel().setSelectionMode(
			 ListSelectionModel.SINGLE_SELECTION);
			 // �������� ������� ��������� � ������� � ��� �������
			 JScrollPane scrlPane = new JScrollPane(tblBook_oplat);
			 scrlPane.getViewport().setBackground(Color.white);
			 scrlPane.setBorder(BorderFactory.createCompoundBorder
			(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
			 tblBook_oplat.getColumnModel().getColumn(0).setMaxWidth(100);
			 tblBook_oplat.getColumnModel().getColumn(0).setMinWidth(100);
			 // �������� ������ ��� �������� �����
			 btnClose = new JButton("�������");
			 //���������� �� ������: �����, ������� � �������� � ������
			 pnfl.add(scrlPane, "grow, span");
			 pnfl.add(btnClose, "growx 0, right");
			 // ���������� ������ � ����
			 
			 pnfl.add(getToolBar(),"growx,wrap");//��������  ������
//			 pnl.add(new JLabel("���������� ����� �������:"), "growx,span");
			 	pnfl.add(scrlPane, "grow, span");
			 	pnfl.add(btnClose, "growx 0, right");
			 	 getContentPane().add(pnfl, BorderLayout.CENTER);
		}
		// ����� ������������ ����� ����� ����
		protected MaskFormatter createFormatter(String s) {
		    MaskFormatter formatter = null;
		    try {
		        formatter = new MaskFormatter(s);
		    } catch (java.text.ParseException exc) {
		        System.err.println("formatter is bad: " 
	              + exc.getMessage());
		        System.exit(-1);
		    }
		    return formatter;
		}
		//  ����� ���������� ��������� ���������� 
	//� ����������� ����
		private void setKeyListener(Component c, KeyListener kl)
		{
		  c.addKeyListener(kl);
		  if (c instanceof Container)
		    for (Component comp:((Container)c).getComponents())
			 setKeyListener(comp, kl);
		}
	    // ����� ������������� ����� ����� (��� ��������������)
		@SuppressWarnings("unchecked")
		private void loadData() {
		  if (!isNewRow){
				edOpl_stoim.setText(type.getOpl_stoim().toString());
				edCod.setText(type.getId_book_uchyot().toString());
		//		edId_zayav.setText(type.getId_zayav().toString());
			edCod_sotrud.setText(type.getSotrud().getKod_sotrud().toString());
			edKod_obj.setText(type.getObj_nedvij().getKod_obj().toString());
			edKod_usl.setText(type.getUsluga().getKod_usl().toString());
			edDate_zaver.setText(type.getUch_date()==null? 
					   "":frmt.format(type.getUch_date()));
			edStoim.setText(type.getStoim().toString());
			}
		  else 
		  {
			  edOpl_stoim.setText("0");
			 
		  }
		  ArrayList<Sotrud> lst6 = manager.loadManagForCmb();
			if (lst6 != null) {
			// �������� ������ ������ �� ���� ������
			@SuppressWarnings({ "rawtypes" })
			DefaultComboBoxModel model5 =
			new DefaultComboBoxModel(lst6.toArray());
			// ��������� ������ ��� JComboBox
			cmbSotr.setModel(model5);
			BigDecimal grKod;
			// ����������� ���� �������� �����
			 //����� ������ ����������� ���� �������� ����� 10.02.2021
			 grKod = (isNewRow? null :
				 type.getSotrud()==null? null :
					 type.getSotrud().getKod_sotrud());
			// ����� ������ ��������� �������� ������
			// ���������������� �������� �������� �����
			setCmbItem6(model5, grKod);
			}
		// �������� ������
			// ��������� ������ � ������
		
			ArrayList<Obj_nedvij> lst = manager.loadObj_nedvij();
			if (lst != null) {
			 // �������� ������ ������ �� ���� ������
			 @SuppressWarnings({ "rawtypes", "unchecked" })
			DefaultComboBoxModel model =
			new DefaultComboBoxModel(lst.toArray());
			 // ��������� ������ ��� JComboBox
			 cmbgor.setModel(model);
			 // ����������� ���� �������� �����
			 BigDecimal grKod = (isNewRow? null :
			type.getObj_nedvij().getKod_obj());
			 // ����� ������ ��������� �������� ������
			 // ���������������� �������� �������� �����
			 setCmbItem(model, grKod);
			}
			ArrayList<Usluga> lst2 = manager.loadUslugaForCmb();
			if (lst2 != null) {
			 // �������� ������ ������ �� ���� ������
			 @SuppressWarnings({ "rawtypes", "unchecked" })
			DefaultComboBoxModel model =
			new DefaultComboBoxModel(lst2.toArray());
			 // ��������� ������ ��� JComboBox
			 cmbUsl.setModel(model);
			 // ����������� ���� �������� �����
			 BigDecimal grKod = (isNewRow? null :
			type.getUsluga().getKod_usl());
			 // ����� ������ ��������� �������� ������
			 // ���������������� �������� �������� �����
			 setCmbItem2(model, grKod);
			}
			} 
			//�������� ����� ��������� �������� ���� ������
			// ��������� �������� ������
			private void setCmbItem(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
			 BigDecimal grKod) {
			cmbgor.setSelectedItem(null);
			if (grKod != null)
			 // �������� ��������� ������ ��� ���������� ��������
			 // � �������� �����
			 for (int i = 0, c = model.getSize(); i < c; i++)
			 if (((Obj_nedvij) model.getElementAt(i)).
			getKod_obj().equals(grKod)) {
			 cmbgor.setSelectedIndex(i);
			 break;
			 }
			} 
			// ��������� �������� ������
			private void setCmbItem6(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
			BigDecimal grKod) {
			cmbSotr.setSelectedItem(null);
			if (grKod != null)
			// �������� ��������� ������ ��� ���������� ��������
			// � �������� �����
			for (int i = 0, c = model.getSize(); i < c; i++)
			if (((Sotrud) model.getElementAt(i)).
			getKod_sotrud().equals(grKod)) {
				cmbSotr.setSelectedIndex(i);
				break;
				}
			}
			// ��������� �������� ������
						private void setCmbItem2(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
						BigDecimal grKod) {
						cmbUsl.setSelectedItem(null);
						if (grKod != null)
						// �������� ��������� ������ ��� ���������� ��������
						// � �������� �����
						for (int i = 0, c = model.getSize(); i < c; i++)
						if (((Usluga) model.getElementAt(i)).
						getKod_usl().equals(grKod)) {
							cmbUsl.setSelectedIndex(i);
							break;
							}
						}
		//������������ ������� ����� ����� ����� �����������
		private boolean constructBook_uchyot()	{
		  try {
			//System.out.print(fk_key);
				type.setId_zayav(fk_key);
				type.setOpl_stoim(edOpl_stoim.getText().equals("") ? 
						null : new BigDecimal(edOpl_stoim.getText()));
				
				type.setKod_usl(edKod_usl.getText().equals("") ? 
						null : new BigDecimal(edKod_usl.getText()));
				type.setKod_obj(edKod_obj.getText().equals("") ? 
						null : new BigDecimal(edKod_obj.getText()));
			type.setStoim(edStoim.getText().equals("") ? 
						null : new BigDecimal(edStoim.getText()));
				type.setUch_date(edDate_zaver.getText().substring(0,
					      1).trim().equals("") ? null : 
					      frmt.parse(edDate_zaver.getText()));
				Object obj = cmbgor.getSelectedItem();
				 Obj_nedvij gr = (Obj_nedvij) obj;
				 type.setKod_obj(gr.getKod_obj());
					Object obj2 = cmbSotr.getSelectedItem();
					Sotrud gr2 = (Sotrud) obj2;
					type.setSotrud(gr2);
					Object obj3 = cmbUsl.getSelectedItem();
					Usluga gr3 = (Usluga) obj3;
					type.setUsluga(gr3);
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "������ ������",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// ������� ������� ����� �����
		public Book_uchyot getBook_uchyot()
		{
			return type;
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
		 
		//�������������� �������� �������
			private void editBook_oplat() {			
				int index = tblBook_oplat.getSelectedRow();
				if (index == -1)
					return;
				// �������������� ������� ������� � ������ ������
				int modelRow = tblBook_oplat.convertRowIndexToModel(index);
				// �������� ������ �� ������ �� �������
				Book_oplat prod = kls.get(modelRow);
				@SuppressWarnings("unused")
				BigDecimal key = prod.getId_book_oplat();
				
				// �������� ������� ���� ��������������
				EdBook_oplatDialog dlg = new EdBook_oplatDialog(this,
		    prod,manager,type.getId_book_uchyot());
				// ����� ���� � �������� ���� ��������
				if (dlg.showDialog() == JDialogResult.OK) {
		               // ����� ������ ���������� ������ ������ ��������� ������
					manager.refreshVR(type);
					isNewRow = false;
					loadData();
					tblModel.updateRow(modelRow);
					System.out.println("���������� OK");
					}
				
		
			}
			// �������� ����� ������
				private void addBook_oplat() {

					// �������� ������� ���� ��������������
					EdBook_oplatDialog dlg = new EdBook_oplatDialog(this, 
						null,manager,type.getId_book_uchyot());
					// ����� ���� � �������� ���� ��������
					if (dlg.showDialog() == JDialogResult.OK) {
						// �������� ������ ������� �� ��������� ������
						Book_oplat prod = dlg.getBook_oplat();
						manager.refreshVR(type);
						loadData();
						//  ���������� ��� � ��������� ������
						tblModel.addRow(prod);}
			
				}
				// �������� ������� ������
				private void deleteBook_oplat() {
					// ���������� ������ ������� ������.
					int index = tblBook_oplat.getSelectedRow();
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
					int modelRow = tblBook_oplat.convertRowIndexToModel(index);
					// �������� ������� ��� ���������� ������
					Book_oplat prod = kls.get(modelRow);
					try {
				// ����������� ���� (���������� �����) ���������� ������
					  BigDecimal kod = prod.getId_book_oplat();
					  // ����� ������ ��������� ��� �������� ������
					  if (manager.deleteBook_oplat(kod)) {
					  // ����� ������ �������� ������ �� ��������� ������
						  manager.refreshVR(type);
							isNewRow = false;
							loadData();
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
		private ArrayList<Book_oplat> prods;
		 // ����������� ������� ������
		 public TKTableModel(ArrayList<Book_oplat> prods) {
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
			 Book_oplat pr = prods.get(rowIndex);
		 // ������ ������� ������������ ���� �������
		 switch (columnIndex) {
		 case 0:
		 return pr.getId_book_oplat();
		
		 case 1:
			 return pr.getDate_opl();
		 case 2:
			 return pr.getSum_opl();
		
		 default:
		 return null;
		 }
		 }
		 // ����������� �������� �������
		 @Override
		 public String getColumnName(int column) {
		 switch (column) {
		 case 0:
		 return "��";
		
		 case 1:
			 return "���� ������";
		 case 2:
			 return "����� ������";
		 
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
				public void addRow(Book_oplat prod) {
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

