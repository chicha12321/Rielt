package com;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

import com.data.*;
import net.miginfocom.swing.MigLayout;

public class EdStreetDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// ���� ������
	  private DBManager manager;
	  private BigDecimal old_key; // ������� �������� �����
	  // ��� �������� ��������� ����
	  private final static String title_add = 
	    "���������� ����� �����";
	  private final static String title_ed = 
	     "�������������� �����";
	   //  ��������� ���������� ��� ������ ������ �����
		private Street type = null;
	    // ���� ������ ����������� ����� ������
		private boolean isNewRow = false;
	    // �������� ��� ����� ���
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // �������� (���� ��������������) ��� ����� ������
		private JTextField edKod;
		private JTextField edName;
		private JTextField edKod_gorod;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		//  ������
		private JButton btnOk;
		private JButton btnCancel;
	    //  ����������� ������
		public EdStreetDialog(Window parent, Street type,
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
		     old_key=type.getKod_street();
		     }
		  else
		     this.type = new Street();	// ����� ������
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
				if (!constructStreet())
					return;
				if (isNewRow) {
	// ����� ������ ��������� �������� ����� ���. ����
					if (manager.addStreet(type)) {
					// ��� ������ ������� Ok � �������� ����
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// ����� ������ ��������� �������� ��� ����.
					if (manager.updateStreet(type, old_key)) {
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
		 Gorod gr = (Gorod) e.getItem();
		 edKod_gorod.setText(gr.getKod_gorod().toString());
		 }
		 }
		 }
		});
		// ������� ������ ������ ����� �����
		edKod_gorod.addFocusListener(new FocusListener() {
		 @Override
		 public void focusLost(FocusEvent e) {
		 // ��� ������ ������ �������� ������� ������
			//�������� �� �������-��������� �������
			 if(!(edKod_gorod.getText().isEmpty())) {
		 @SuppressWarnings("rawtypes")
		DefaultComboBoxModel model =
		(DefaultComboBoxModel) cmbgor.getModel();
		 BigDecimal grKod =
		new BigDecimal(edKod_gorod.getText());
		 setCmbItem(model, grKod);
		 }
		 }
		 @Override
		 public void focusGained(FocusEvent e) {
		 }
		});
		} // ����� bindListeners
		  
		  
		  // ����� �������� ������������ ����������
		@SuppressWarnings("rawtypes")
		private void createGui() {
			// �������� ������
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// �������� ����� ��� �������������� ������
	edKod = new JTextField(10);
			edName = new JTextField(40);
			edKod_gorod = new JTextField(10);
			cmbgor = new JComboBox ();
			//  �������� ������
			btnOk = new JButton("���������");
			btnCancel = new JButton("������");
			// ���������� ��������� �� ������
			pnl.add(new JLabel("���"));
			pnl.add(edKod,"span");
			pnl.add(new JLabel("������������"));
			pnl.add(edName,"span");
			pnl.add(new JLabel("�����"));
			pnl.add(edKod_gorod, "split 2");
			pnl.add(cmbgor, "growx, wrap");
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
		@SuppressWarnings("unchecked")
		private void loadData() {
		  if (!isNewRow){
				edKod.setText(type.getKod_street().toString());
			edName.setText(type.getName_street());
			edKod_gorod.setText(type.getGorod().getKod_gorod() == null ?
					"" : type.getGorod().getKod_gorod().toString());
	
		  }
		// �������� ������
		// ��������� ������ � ������
	
		ArrayList<Gorod> lst = manager.loadGorodForCmb();
		if (lst != null) {
		 // �������� ������ ������ �� ���� ������
		 @SuppressWarnings({ "rawtypes" })
		DefaultComboBoxModel model =
		new DefaultComboBoxModel(lst.toArray());
		 // ��������� ������ ��� JComboBox
		 cmbgor.setModel(model);
		 // ����������� ���� �������� �����
		 BigDecimal grKod = (isNewRow? null :
		type.getGorod().getKod_gorod());
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
		 if (((Gorod) model.getElementAt(i)).
		getKod_gorod().equals(grKod)) {
		 cmbgor.setSelectedIndex(i);
		 break;
		 }
		} 
		//������������ ������� ��� ����. ����� �����������
		private boolean constructStreet()	{
		  try {
				type.setKod_street(edKod.getText().equals("") ? 
	null : new BigDecimal(edKod.getText()));
				type.setName_street(edName.getText());
				Object obj = cmbgor.getSelectedItem();
				 Gorod gr = (Gorod) obj;
				 type.setGorod(gr);
				 
// jk.setModel(model.getText().isEmpty() ? ("") : (model.getText()));
				return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "������ ������",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// ������� ������� ��� ����.
		public Street getStreet()
		{
			return type;
		}
} 
