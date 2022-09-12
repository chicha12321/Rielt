package com;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import com.data.*;

import net.miginfocom.swing.MigLayout;

public class EdObj_nedvijDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// ���� ������
	  private DBManager manager;
	  private BigDecimal old_key; // ������� �������� �����
	  // ��� �������� ��������� ����
	  private final static String title_add = 
	    "���������� ������ ������� ������������";
	  private final static String title_ed = 
	     "�������������� ������� ������������";
	   //  ��������� ���������� ��� ������ ������ ����.
		private Obj_nedvij type = null;
	    // ���� ������ ����������� ����� ������
		private boolean isNewRow = false;
	    // �������� ��� ����� ���
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // �������� (���� ��������������) ��� ����� ������
		private JTextField edCod;
		private JTextField edKod_zdan;
		private JTextField edPrice;
		private JTextField edArea;

		private JTextField edFloor;
		private JComboBox edVid_ned;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		//  ������
		private JButton btnOk;
		private JButton btnCancel;
	    //  ����������� ������
		public EdObj_nedvijDialog(Window parent,Obj_nedvij type,
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
		     old_key=type.getKod_obj();
		     }
		  else
		     this.type = new Obj_nedvij();	// ����� ������
		  // �������� ������������ ���������� ����
		  createGui();
		  // ���������� ������������ ��� �������� �������
		  bindListeners();
		  //  ��������� ������
		  loadData();
		  pack();
		  // ������� ������ ������������ �������� ����
		  setResizable(false);
		  setLocationRelativeTo(parent);
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
		  btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
			  // �������� ������, ����� 
	  //   ��� ������������ ���������� �����
				if (!constructObj_nedvij())
					return;
				if (isNewRow) {
	// ����� ������ ��������� �������� ����� ������ ����.
					if (manager.addObj_nedvij(type)) {
					// ��� ������ ������� Ok � �������� ����
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// ����� ������ ��������� �������� ������
					if (manager.updateObj_nedvij(type, old_key)) {
					setDialogResult(JDialogResult.OK);
					close();
				}
				}
		  });
			// ������� ������ �������� ������
			cmbgor.addItemListener(new ItemListener() {
			 @Override
			 public void itemStateChanged(ItemEvent e) {
			 // ��������� �������� ���� �������� �����
			 if (e.getStateChange() == ItemEvent.SELECTED) {
			 if (e.getItem() != null) {
			 Zdan gr = (Zdan) e.getItem();
			 edKod_zdan.setText(gr.getKod_zdan().toString());
			 }
			 }
			 }
			});
			// ������� ������ ������ ����� �����
			edKod_zdan.addFocusListener(new FocusListener() {
			 @Override
			 public void focusLost(FocusEvent e) {
			 // ��� ������ ������ �������� ������� ������
				//�������� �� �������-��������� �������
				 if(!(edKod_zdan.getText().isEmpty())) {
			 @SuppressWarnings("rawtypes")
			DefaultComboBoxModel model =
			(DefaultComboBoxModel) cmbgor.getModel();
			 BigDecimal grKod =
			new BigDecimal(edKod_zdan.getText());
			 setCmbItem(model, grKod);
			 }
			 }
			 @Override
			 public void focusGained(FocusEvent e) {
			 }
			});
		}
		  // ����� �������� ������������ ����������
		private void createGui() {
			// �������� ������
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// �������� ����� ��� �������������� ������
	edCod = new JTextField(10);
	edKod_zdan = new JTextField(10);
			edPrice = new JTextField(20);
			edArea = new JTextField(20);
			edFloor = new JTextField(20);
			cmbgor = new JComboBox <> ();
			edVid_ned = new JComboBox <> (new String[]
			        {"","��������","�����������","������� ���","���������"}); 
			//  �������� ������
			btnOk = new JButton("���������");
			btnCancel = new JButton("������");
			// ���������� ��������� �� ������
			pnl.add(new JLabel("��� �������"));
			pnl.add(edCod,"span");
			
			pnl.add(new JLabel("������"));
			pnl.add(edKod_zdan, "split 2");
			pnl.add(cmbgor, "growx, wrap");
			pnl.add(new JLabel("����"));
			pnl.add(edPrice,"span");
			pnl.add(new JLabel("�������"));
			pnl.add(edArea,"span");
			pnl.add(new JLabel("����"));
			pnl.add(edFloor,"span");
			pnl.add(new JLabel("��� ������������"));
			pnl.add(edVid_ned,"span");
			pnl.add(btnOk, "span, split 2, center, sg ");
			pnl.add(btnCancel, "sg 1");
			//  ���������� ������ � ���� ������
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(pnl, BorderLayout.CENTER);
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
		private void loadData() {
		  if (!isNewRow){
				edArea.setText(type.getArea().toString());
				edCod.setText(type.getKod_obj().toString());
				edKod_zdan.setText(type.getZdan().getKod_zdan().toString());
			edPrice.setText(type.getPrice().toString());
			edVid_ned.setSelectedItem(type.getVid_ned());

			edFloor.setText(type.getFloor().toString());
			}
		// �������� ������
					// ��������� ������ � ������
				
					ArrayList<Zdan> lst = manager.loadZdan();
					if (lst != null) {
					 // �������� ������ ������ �� ���� ������
					 @SuppressWarnings({ "rawtypes", "unchecked" })
					DefaultComboBoxModel model =
					new DefaultComboBoxModel(lst.toArray());
					 // ��������� ������ ��� JComboBox
					 cmbgor.setModel(model);
					 // ����������� ���� �������� �����
					 BigDecimal grKod = (isNewRow? null :
					type.getZdan().getKod_zdan());
					 // ����� ������ ��������� �������� ������
					 // ���������������� �������� �������� �����
					 setCmbItem(model, grKod);
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
					 if (((Zdan) model.getElementAt(i)).
					getKod_zdan().equals(grKod)) {
					 cmbgor.setSelectedIndex(i);
					 break;
					 }
					} 
		//������������ ������� ������ ������. ����� �����������
		private boolean constructObj_nedvij()	{
		  try {
				type.setKod_obj(edCod.getText().equals("") ? 
	null : new BigDecimal(edCod.getText()));
				
				type.setArea(edArea.getText().equals("") ? 
						null : new BigDecimal(edArea.getText()));
				type.setPrice(edPrice.getText().equals("") ? 
						null : new BigDecimal(edPrice.getText()));
				if (edVid_ned.getSelectedItem().toString()!="") 
					type.setVid_ned(edVid_ned.getSelectedItem().toString());	
				
				
				type.setFloor(edFloor.getText().equals("") ? 
						null : new BigDecimal(edFloor.getText()));
				Object obj = cmbgor.getSelectedItem();
				 Zdan gr = (Zdan) obj;
				 type.setZdan(gr);
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "������ ������",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// ������� ������� ������ ����.
		public Obj_nedvij getObj_nedvij()
		{
			return type;
		}
}
