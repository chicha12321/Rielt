package com;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
// ���� ������
  private DBManager manager;
  JMenuBar mainMenu; // ��������� ����
  JMenuItem miExit; // ����� ���� �����
  JMenu mSprav; // ������� �����������
  JMenuItem miGorod; // ����� ���� �����
  JMenuItem miZdan; // ����� ���� ������
  JMenuItem miStreet; // ����� ���� �����
  JMenuItem miObj_nedvij; // ����� ���� ������ ������������
  JMenuItem miUsluga; // ����� ���� ������
  JMenuItem miManag; // ����� ���� 
  JMenuItem miBook_oplat; // ����� ���� �
  JMenuItem miBook_uchyot; // ����� ���� 
  JMenuItem miZayav; // ����� ���� ������
  JMenuItem miKlient; // ����� ���� 

 
  
  
  JMenu mKn; // ������� �����
  //  ����������� ������
  public MainWindow(DBManager manager) {
    this.manager = manager;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 // ��������� ������� �����
    manager.setPath(); 
    // ���� ����������
    testConn();
    //  �������� ������������ ���������� ����
    createGUI();
    //  ���������� ���� ����������
    bindListeners();
    //  ������ ����
    pack();
    //  ��������� ����
  
    setTitle("������������� ����� ����������� ����� � ��������� ������������.");
    // ����� ���� �� ������ ������ 
    setLocationRelativeTo(null);
  }
  
  private JMenuBar createMenu(){
	  // ������� ������ ��������� ����
	  mainMenu = new JMenuBar();
	  // ������� ������� � ������ ����
	  mSprav = new JMenu("�����������");
	  miExit = new JMenuItem("�����");
	  miGorod= new JMenuItem("�����");
	  miZdan= new JMenuItem("������");
	  miStreet= new JMenuItem("�����");
	  miBook_oplat= new JMenuItem("����� �����");
	  miManag= new JMenuItem("��������");
	  miBook_uchyot= new JMenuItem("����� �����");
	  miZayav= new JMenuItem("������");
	  miObj_nedvij= new JMenuItem("������ ������������");
	  miUsluga= new JMenuItem("������");
	  miKlient = new JMenuItem("������");
	  
	  
	  mKn = new JMenu("�����"); 
	  
	  // ��������� ����
	  mSprav.add(miGorod);
	  mSprav.add(miStreet);
	  mSprav.add(miZdan);
	  mKn.add(miZayav);
	  mKn.add(miBook_uchyot);
		 
	//  mKn.add(miBook_oplat);

	  mSprav.add(miObj_nedvij);
	  mSprav.add(miUsluga);
	  
	 
	  mSprav.addSeparator(); // �����-�����������
	  mSprav.add(miManag);
	  
	  mSprav.add(miKlient);
	 
	  mainMenu.add(mSprav);
	  mainMenu.add(mKn);

	  mainMenu.add(miExit);
	  return mainMenu;
	  }
  //  ���������� ��������� ���� ����������
  private void bindListeners() {
   // ���� ����� ��������� ������ ��� �������� ��������� ����
	// ����� �� ����������
	  miExit.addActionListener(new ActionListener() {
	  @Override
	  public void actionPerformed(ActionEvent e) {
	  System.exit(0);
	  }
	 }); 
	  miKlient.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowKlient();
		  }
		  }); 
	  miUsluga.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowUsluga();
		  }
		  }); 
	  miZayav.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowZayav();
		  }
		  }); 
	  miManag.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowManag();
		  }
		  }); 
	  miStreet.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowStreet();
		  }
		  }); 
	  miZdan.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowZdan();
		  }
		  }); 
	  miBook_uchyot.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowBook_uchyot();
		  }
		  }); 
	 
	  miGorod.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowGorod();
		  }
		  }); 
	  miObj_nedvij.addActionListener(new ActionListener() {
		  @Override
		  public void actionPerformed(ActionEvent e) {
		  ShowObj_nedvij();
		  }
		  }); 
	 


  }
  
  //���������� ��������
  protected void ShowKlient() {
	 FrmKlient frm = new FrmKlient(manager);
	  frm.setVisible(true);
	  } 
 
 //���������� ������
  protected void ShowZayav() {
	  FrmZayav frm = new FrmZayav(manager);
	  frm.setVisible(true);
	  } 
  
 
  //���������� �����
  protected void ShowStreet() {
	  FrmStreet frm = new FrmStreet(manager);
	  frm.setVisible(true);
	  } 
//���������� ����������
  protected void ShowManag() {
	  FrmManag frm = new FrmManag(manager);
	  frm.setVisible(true);
	  } 
  
 //���������� ������
  protected void ShowUsluga() {
	 FrmUsluga frm = new FrmUsluga(manager);
	  frm.setVisible(true);
	  } 
  //���������� ����� �����
  protected void ShowBook_uchyot() {
	  FrmBook_uchyot frm = new FrmBook_uchyot(manager);
	  frm.setVisible(true);
	  } 
  //���������� ������ ������������
  protected void ShowObj_nedvij() {
	  FrmObj_nedvij frm = new FrmObj_nedvij(manager);
	  frm.setVisible(true);
	  } 
//���������� �����
  protected void ShowGorod() {
	  FrmGorod frm = new FrmGorod(manager);
  frm.setVisible(true);
	  } 
//���������� ������
  protected void ShowZdan() {
	  FrmZdan frm = new FrmZdan(manager);
	  frm.setVisible(true);
	  } 

  
  // ����� ������������ ����������
  private void testConn() {
     // ����� ������ ��������� ��� ��������� ������ PG
     String ver = manager.getVersion();
     // ����� ���������� � �������
     System.out.println(ver);
  }
    //  �������� ������������ ���������� ����
  private void createGUI() {
 // ������� ������
 JPanel pnlImg = new JPanel(new FlowLayout());
 // ��������� �� ������ �����������
 // ����������� ��������� � ����� src/images
 //    � � �����  bin\images
    URL url = this.getClass().getResource("/images/112.jpg");
    BufferedImage wPic=null;
    try {
       wPic = ImageIO.read(url);
    } catch (IOException e) {
       e.printStackTrace();
    }
    // ����������� ��������� �� �����
    JLabel wIcon = new JLabel(new ImageIcon(wPic));
    // ����� ��������� �� ������
    pnlImg.add(wIcon);
    // ������ �������� ������������ ��������� ����
    // ����� ��������� ��� ������������ ����
    getContentPane().setLayout(new MigLayout("insets 0 2 0 2, gapy 0",
		"[grow, fill]", "[grow, fill]"));
   // ������ ��������� �� ������
getContentPane().add(pnlImg, "grow");
  //  getContentPane().add(pnlImg);
setJMenuBar(createMenu()); 
 }

	public void initialize() {
		Locale.setDefault(new Locale("ru"));
	}
}
