package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.*;
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

public class EdZayavDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// ���� ������
	  private DBManager manager;
	  private BigDecimal old_key; // ������� �������� �����
	  // ��� �������� ��������� ����
	  private final static String title_add = 
	    "���������� ����� ������";
	  private final static String title_ed = 
	     "�������������� ������";
	   //  ��������� ���������� ��� ������ ������
		private Zayav type = null;
	    // ���� ������ ����������� ����� ������
		private boolean isNewRow = false;
	    // �������� ��� ����� ���
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // �������� (���� ��������������) ��� ����� ������
		private JTextField edCod_sotrud;
		private JTextField edNum_zayav;
		private JTextField edVid;
		private JTextField edPrefer;
		private JTextField edDate_zaver;
		private JTextField edKod_klient;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbSotr, cmbKlient;
		
		//  ������
		private JButton btnOk;
		private JButton btnCancel;
		//��� ����. �������
		 private JTable tblBook_uchyot;
		 
			private JButton btnClose;
			// ������ �������������� 
				private JButton btnEdit;
				// ������ ���������� 
				private JButton btnNew;
				// ������ �������� 
				private JButton btnDelete;
				//���� ������
			private TKTableModel tblModel;
			 private ArrayList<Book_uchyot> kls;
			 
	    //  ����������� ������
		public EdZayavDialog(Window parent,Zayav type,
	                    DBManager manager) {
	      this.manager = manager;
	  // ��������� ����� ��� ������ ���������� ����� ������
		  isNewRow = type == null ? true : false;
		  // ����������� ��������� ��� �������� ������./������.
		  setTitle(isNewRow ? title_add : title_ed);
		  // ����������� ������� ������������� ������  
		  if (!isNewRow) {
		     this.type = type; // ������������ ������
		     // ���������� �������� �������� �����
		     old_key=type.getId_zayav();
		     loadDataGruz(); //�������� � ������� 2
		     }
		  else {
		     this.type = new Zayav();	// ����� ������
		     kls=new ArrayList <Book_uchyot>();
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
		 kls=manager.loadBook_uchyot(type.getId_zayav());
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
			  if (!constructZayav()) return;
			  //����� ������ ���������� 31.03.2021
			  else {if (isNewRow)
			  			{
				  		if (manager.addZayav(type)==true)
				  			{
				  					setDialogResult(JDialogResult.OK);
				  					((AbstractButton) e.getSource()).setText("�������������");
				  					setButton();
				  			}
			  			}
			  else 
				  if (manager.updateZayav(type, old_key)==true)
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
							//������� ������ �������� ������
								cmbKlient.addItemListener(new ItemListener() {
									 @Override
									 public void itemStateChanged(ItemEvent e) {
									 // ��������� �������� ���� �������� �����
									 if (e.getStateChange() == ItemEvent.SELECTED) {
									 if (e.getItem() != null) {
									 Klient gr = (Klient) e.getItem();
									 edKod_klient.setText(gr.getKod_klient().toString());
									 }
									 }
									 }
									});
									// ������� ������ ������ ����� �����
								edKod_klient.addFocusListener(new FocusListener() {
									 @Override
									 public void focusLost(FocusEvent e) {
									 // ��� ������ ������ �������� ������� ������
										//�������� �� �������-��������� �������
										 if(!(edKod_klient.getText().isEmpty())) {
									 @SuppressWarnings("rawtypes")
									DefaultComboBoxModel model1 =
									(DefaultComboBoxModel) cmbKlient.getModel();
									 BigDecimal grKod =
									new BigDecimal(edKod_klient.getText());
									 setCmbItem2(model1, grKod);
									 }
									 }
									 @Override
									 public void focusGained(FocusEvent e) {
									 }
									});
			 }
			 
		  // ����� �������� ������������ ����������
		@SuppressWarnings("rawtypes")
		private void createGui() {
			// �������� ������
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// �������� ����� ��� �������������� ������
	edCod_sotrud = new JTextField(10);
	edNum_zayav = new JTextField(10);
	cmbSotr = new JComboBox();
	cmbKlient = new JComboBox();
			edVid = new JTextField(20);
			edPrefer = new JTextField(50);
			edKod_klient = new JTextField(10);
			edDate_zaver = new JFormattedTextField(
	                createFormatter("##-##-####"));
			edDate_zaver.setColumns(10);
			//  �������� ������
			btnOk = new JButton("���������");
			btnCancel = new JButton("������");
			// ���������� ��������� �� ������
			
			pnl.add(new JLabel("����� ������"));
			pnl.add(edNum_zayav,"span");
			pnl.add(new JLabel("���"));
			pnl.add(edVid,"span");
			pnl.add(new JLabel("������������"));
			pnl.add(edPrefer,"span");
			pnl.add(new JLabel("������"));
			pnl.add(edKod_klient,"split 2");
			pnl.add(cmbKlient, "growx, wrap");
			
			pnl.add(new JLabel("���� ������"));
			pnl.add(edDate_zaver,"span");
			pnl.add(new JLabel("���������"));
			pnl.add(edCod_sotrud, "split 2");
			pnl.add(cmbSotr, "growx, wrap");
			
			pnl.add(btnOk, "span, split 2, center, sg ");
			pnl.add(btnCancel, "sg 1");
			//  ���������� ������ � ���� ������
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(pnl, BorderLayout.NORTH);
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
						(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
						 tblBook_uchyot.getColumnModel().getColumn(0).setMaxWidth(100);
						 tblBook_uchyot.getColumnModel().getColumn(0).setMinWidth(100);
						 // �������� ������ ��� �������� �����
						 btnClose = new JButton("�������");
						 pnfl.add(getToolBar(),"growx,wrap");//��������  ������
						 //���������� �� ������: �����, ������� � �������� � ������
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
				edPrefer.setText(type.getPrefer().toString());
		//		edCod_sotrud.setText(type.getId_zayav().toString());
				edNum_zayav.setText(type.getNum_zayav().toString());
			edVid.setText(type.getVid());
			edDate_zaver.setText(type.getDate_zayav()==null? 
					   "":frmt.format(type.getDate_zayav()));
			edKod_klient.setText(type.getKlient().getKod_klient().toString());
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
			ArrayList<Klient> lst2 = manager.loadKlient();
			if (lst2 != null) {
			 // �������� ������ ������ �� ���� ������
			 @SuppressWarnings({ "rawtypes" })
			DefaultComboBoxModel model2 =
			new DefaultComboBoxModel(lst2.toArray());
			 // ��������� ������ ��� JComboBox
			 cmbKlient.setModel(model2);
			 // ����������� ���� �������� �����
			 BigDecimal grKod = (isNewRow? null :
			type.getKlient().getKod_klient());
			 // ����� ������ ��������� �������� ������
			 // ���������������� �������� �������� �����
			 setCmbItem2(model2, grKod);
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
		private void setCmbItem2(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
				 BigDecimal grKod) {
				cmbKlient.setSelectedItem(null);
				if (grKod != null)
				 // �������� ��������� ������ ��� ���������� ��������
				 // � �������� �����
				 for (int i = 0, c = model.getSize(); i < c; i++)
				 if (((Klient) model.getElementAt(i)).
				getKod_klient().equals(grKod)) {
				 cmbKlient.setSelectedIndex(i);
				 break;
				 }
				} 
		
		
		//������������ ������� ������ ����� �����������
		private boolean constructZayav()	{
		  try {
		//		type.setId_zayav(edCod_sotrud.getText().equals("") ? 
//	null : new BigDecimal(edCod_sotrud.getText()));
				type.setNum_zayav(edNum_zayav.getText().equals("") ? 
						null : new BigDecimal(edNum_zayav.getText()));
				
				if(!(edPrefer.getText().isEmpty()))
					type.setPrefer(edPrefer.getText());
				if(!(edVid.getText().isEmpty()))
					type.setVid(edVid.getText());
				
				type.setKod_klient(edKod_klient.getText().equals("") ? 
						null : new BigDecimal(edKod_klient.getText()));
				type.setDate_zayav(edDate_zaver.getText().substring(0,
					      1).trim().equals("") ? null : 
					      frmt.parse(edDate_zaver.getText()));
				Object obj = cmbSotr.getSelectedItem();
				Sotrud gr = (Sotrud) obj;
				type.setSotrud(gr);
				
				 Object obj2 = cmbKlient.getSelectedItem();
				 Klient gr2 = (Klient) obj2;
				 type.setKlient(gr2);
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "������ ������",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// ������� ������� ������
		public Zayav getZayav()
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
				EdBook_uchyotDialog dlg = new EdBook_uchyotDialog(this,
		    prod,manager, type.getId_zayav());
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
					EdBook_uchyotDialog dlg = new EdBook_uchyotDialog(this, 
						null,manager,type.getId_zayav());
					dlg.showDialog();
					Book_uchyot prod = dlg.getBook_uchyot();
					if (prod!=null&&prod.getId_book_uchyot()!=null) {
						// ���������� ��� � ��������� ������
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
		 return 7;
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
			 Book_uchyot pr = prods.get(rowIndex);
		 // ������ ������� ������������ ���� �������
		 switch (columnIndex) {
		 case 0:
		 return pr.getId_book_uchyot();
		
		 case 1:
			 return pr.getObj_nedvij().getKod_obj();
		 case 2:
			 return pr.getUsluga().getName_usl();
		 case 3:
			 return pr.getUch_date();
		 case 4:
			 return pr.getSotrud().getFio();
		 case 5:
			 return pr.getStoim();
		 case 6:
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
			 return "��� �������";
		 case 2:
			 return "������";
		 case 3:
			 return "���� ������";
		 case 4:
			 return "���������";
		 case 5:
			 return "���������";
		 case 6:
			 return "��������";
		 
		 
		 
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


