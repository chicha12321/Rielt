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

public class EdZdanDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// ���� ������
	  private DBManager manager;
	  private BigDecimal old_key; // ������� �������� �����
	  // ��� �������� ��������� ����
	  private final static String title_add = 
	    "���������� ������ ������";
	  private final static String title_ed = 
	     "�������������� ������";
	   //  ��������� ���������� ��� ������ ������
		private Zdan type = null;
	    // ���� ������ ����������� ����� ������
		private boolean isNewRow = false;
	    // �������� ��� ����� ���
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // �������� (���� ��������������) ��� ����� ������
		private JTextField edCod;
		private JTextField edCod_gorod;
		private JTextField edNomer_dom;
		private JTextField edEtagej;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		//  ������
		private JButton btnOk;
		private JButton btnCancel;
	    //  ����������� ������
		public EdZdanDialog(Window parent,Zdan type,
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
		     old_key=type.getKod_zdan();
		     }
		  else
		     this.type = new Zdan();	// ����� ������
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
				if (!constructZdan())
					return;
				if (isNewRow) {
	// ����� ������ ��������� �������� ������ ������
					if (manager.addZdan(type)) {
					// ��� ������ ������� Ok � �������� ����
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// ����� ������ ��������� �������� ������
					if (manager.updateZdan(type, old_key)) {
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
			 Street gr = (Street) e.getItem();
			 edCod_gorod.setText(gr.getKod_street().toString());
			 }
			 }
			 }
			});
			// ������� ������ ������ ����� �����
			edCod_gorod.addFocusListener(new FocusListener() {
			 @Override
			 public void focusLost(FocusEvent e) {
			 // ��� ������ ������ �������� ������� ������
				//�������� �� �������-��������� �������
				 if(!(edCod_gorod.getText().isEmpty())) {
			 @SuppressWarnings("rawtypes")
			DefaultComboBoxModel model =
			(DefaultComboBoxModel) cmbgor.getModel();
			 BigDecimal grKod =
			new BigDecimal(edCod_gorod.getText());
			 setCmbItem(model, grKod);
			 }
			 }
			 @Override
			 public void focusGained(FocusEvent e) {
			 }
			});
			} // ����� bindListeners
		  // ����� �������� ������������ ����������
		private void createGui() {
			// �������� ������
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// �������� ����� ��� �������������� ������
	edCod = new JTextField(6);
	edCod_gorod = new JTextField(6);
	cmbgor = new JComboBox ();
			
			edNomer_dom = new JTextField(20);
			edEtagej = new JTextField(10);
			//  �������� ������
			btnOk = new JButton("���������");
			btnCancel = new JButton("������");
			// ���������� ��������� �� ������
			pnl.add(new JLabel("���"));
			pnl.add(edCod,"span");
			pnl.add(new JLabel("����� � �����"));
			pnl.add(edCod_gorod, "split 2");
			pnl.add(cmbgor, "growx, wrap");
			pnl.add(new JLabel("����� ����"));
			pnl.add(edNomer_dom,"span");
			pnl.add(new JLabel("������"));
			pnl.add(edEtagej,"span");
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
				edNomer_dom.setText(type.getNomer_dom().toString());
				edCod.setText(type.getKod_zdan().toString());
				edCod_gorod.setText(type.getStreet().getKod_street().toString());
		
			edEtagej.setText(type.getEtagej().toString());
			}
		// �������� ������
			// ��������� ������ � ������
		
			ArrayList<Street> lst = manager.loadStreet();
			if (lst != null) {
			 // �������� ������ ������ �� ���� ������
			 @SuppressWarnings({ "rawtypes", "unchecked" })
			DefaultComboBoxModel model =
			new DefaultComboBoxModel(lst.toArray());
			 // ��������� ������ ��� JComboBox
			 cmbgor.setModel(model);
			 // ����������� ���� �������� �����
			 BigDecimal grKod = (isNewRow? null :
			type.getStreet().getKod_street());
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
			 if (((Street) model.getElementAt(i)).
			getKod_street().equals(grKod)) {
			 cmbgor.setSelectedIndex(i);
			 break;
			 }
			} 
		//������������ ������� ������ ����� �����������
		private boolean constructZdan()	{
		  try {
				type.setKod_zdan(edCod.getText().equals("") ? 
	null : new BigDecimal(edCod.getText()));
				
				
				if(!(edNomer_dom.getText().isEmpty()))
					type.setNomer_dom(edNomer_dom.getText());
				Object obj = cmbgor.getSelectedItem();
				 Street gr = (Street) obj;
				 type.setStreet(gr);
				
				type.setEtagej(edEtagej.getText().equals("") ? 
						null : new BigDecimal(edEtagej.getText()));
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "������ ������",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// ������� ������� ������ (������ ���������)
		public Zdan getZdan()
		{
			return type;
		}
}

