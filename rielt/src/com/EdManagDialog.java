package com;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

import com.data.*;

import net.miginfocom.swing.MigLayout;

public class EdManagDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// ���� ������
	  private DBManager manager;
	  private BigDecimal old_key; // ������� �������� �����
	  // ��� �������� ��������� ����
	  private final static String title_add = 
	    "���������� ������ ����������";
	  private final static String title_ed = 
	     "�������������� ����������";
	   //  ��������� ���������� ��� ������ ��������
		private Sotrud type = null;
	    // ���� ������ ����������� ����� ������
		private boolean isNewRow = false;
	    // �������� ��� ����� ���
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // �������� (���� ��������������) ��� ����� ������
		private JTextField edCod, edC;
		private JTextField edName;
		//  ������
		private JButton btnOk;
		private JButton btnCancel;
	    //  ����������� ������
		public EdManagDialog(Window parent,Sotrud type,
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
		     old_key=type.getKod_sotrud();
		     }
		  else
		     this.type = new Sotrud();	// ����� ������
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
				if (!constructManag())
					return;
				if (isNewRow) {
	// ����� ������ ��������� �������� ������ ����������
					if (manager.addSotrud(type)) {
					// ��� ������ ������� Ok � �������� ����
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// ����� ������ ��������� �������� ������
					if (manager.updateSotrud(type, old_key)) {
					setDialogResult(JDialogResult.OK);
					close();
				}
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
	edC = new JTextField(10);
			edName = new JTextField(50);
			//  �������� ������
			btnOk = new JButton("���������");
			btnCancel = new JButton("������");
			// ���������� ��������� �� ������
			pnl.add(new JLabel("���"));
			pnl.add(edCod,"span");
			pnl.add(new JLabel("���"));
			pnl.add(edName,"span");
			pnl.add(new JLabel("����"));
			pnl.add(edC,"span");
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
				edCod.setText(type.getKod_sotrud().toString());
			edName.setText(type.getFio());
			edC.setText(type.getStag().toString());
			}
		}
		//������������ ������� �������� ����� �����������
		private boolean constructManag()	{
		  try {
				type.setKod_sotrud(edCod.getText().equals("") ? 
	null : new BigDecimal(edCod.getText()));
				type.setFio(edName.getText());
				type.setStag(edC.getText().equals("") ? 
						null : new BigDecimal(edC.getText()));


				return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "������ ������",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// ������� ������� ��������
		public Sotrud getManag()
		{
			return type;
		}
}
