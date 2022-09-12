package com;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

import com.gui.LookAndFillUtil;
public class PgAppNedv {
  private DBManager manager=null;
  public PgAppNedv() {
  }
  // ����� ������ ���������� 
  public void run(){
    // ����� ���� �����������
    if (showLoginWnd()) {
     // ��� �������� ���������� ����������� ��������� ����
        showMainWnd();}
    else {
     // ����� - �������� ����������
      closeApplication();
    }
  }
  //  ����� ���� �����������
  private boolean showLoginWnd() {
    try {
       manager = new DBManager();
    } catch (SQLException e) {
      e.printStackTrace();
     }
     // �������� ����
     LoginDialog dlg = new LoginDialog(manager);
     //  ����� ����
     JDialogResult res = dlg.showDialog();
     // ������� true ��� ��������� ����������
     return (res == JDialogResult.OK);
  }
  //  ����� ��������� ���� ����������
  private void showMainWnd()	{
    // ����� ���� ��������
    //  � ��������� ������
    WaitingDialog wd = new WaitingDialog(
                 "����������� ���������...");
    wd.setVisible(true);
    //  ���������� ��������� ���������� ������ ��������� ����
    MainWindow mainWindow = null;
    try {
      // ����� ������ �������� ��������� ����
      mainWindow = createMainWindow(manager);
      // ���������� ��������� �� �������� ����
      mainWindow.addWindowListener(new WindowAdapter()
       {
        @Override
        public void windowClosed(WindowEvent e) 	{
          closeApplication();
        }
      });
      //  ������������� ���� 
      mainWindow.initialize();
      //  �������� � �������� ����-��������
      wd.setVisible(false);
      wd.dispose();
      //  ����������� ��������� ����
      mainWindow.setVisible(true);
    } catch (Exception ex) {
     //  ��������� ������ ��������� ����������:
     //  ����� ���������
     //  � �������� � �������� ����-��������
      String s = ex.getMessage();
      JOptionPane.showMessageDialog(null, s,
                  "������",
                  JOptionPane.ERROR_MESSAGE);
      wd.setVisible(false);
      wd.dispose();
    }
  }
  // �������� ��������� ���� 
  private MainWindow createMainWindow(DBManager manager){
     return new MainWindow(manager);
  }
  // �������� ����������
  private void closeApplication() {
    System.exit(0);
  }
  // ����� ����� � ���������� � ����� main
  public static void main(String[] args) {
		  try {
		  LookAndFillUtil.setGoodies(null);
		  } 
		  catch (IllegalAccessException | InstantiationException |
		  ClassNotFoundException| UnsupportedLookAndFeelException e){
		  e.printStackTrace();
		  }
    PgAppNedv app=new PgAppNedv();
    //  ����� ��� ������ ������
    app.run();  
  }   
}
