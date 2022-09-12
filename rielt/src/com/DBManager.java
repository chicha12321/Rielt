package com;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import com.data.*;
import com.helper.HelperConverter;


public class DBManager {
    private Connection conn = null; 
    public DBManager() throws SQLException { 
      }
    public Connection getConnection(){
       return conn;
    }
    public void setConnection(Connection conn) {
       this.conn=conn;
    }
  //------------------------------����� SEQ
		// ����� ��������� ������������ �����
		// ������������ ��� ���� �������������������
		// ������� �������� - ��� ������������������
		public BigDecimal getId(String seqName) {
		BigDecimal id = null;
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "SELECT nextval(?)";
		try {
		// ������������ ������� � ��
		// ���������� ������� � ��������� ������ ������
		pst = con.prepareStatement(stm);
		// seqName - ��� ������������������
		pst.setString(1, seqName);
		ResultSet res = pst.executeQuery();
		while (res.next()) {
		id = res.getBigDecimal(1);
		}
		} catch (SQLException ex) {
		JOptionPane.showMessageDialog(null,
		ex.getMessage(),
		"������ ��������� ��������������",
		JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
		}
		return id;
		}

     // ����� ������������ ����������
    	public String getVersion() {
		String ver=null;
		Statement stmt;
		ResultSet rset;
		try {
			stmt = conn.createStatement();
			rset = stmt.executeQuery("SELECT VERSION()");
			while (rset.next())
			{
				ver =rset.getString(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ver;
	}
    	//��������� ����
    	public void setPath() {
    		 Statement stmt;
    		 try {
    		 stmt = conn.createStatement();
    		 // ��� ����� ���� �������� !!!!
    		 stmt.executeUpdate("SET SEARCH_PATH=rielt");
    		 } catch (SQLException ex) {
    		 ex.printStackTrace();
    		 }
    	}
    	private void RollBack() {
    	   	try {
    	   	  conn.rollback();
    	   } catch (SQLException e) {
    	   e.printStackTrace();
    	   	}
    	   	// ��������� ����� � ������
    	   	setPath();
    	   }
  //---------------------
    	//------------------�������� �����
        // ��������� ������� ������� �� ��
        // � ������� ��� � ���� ������ 
   public ArrayList<Gorod> loadGorod() {
   ArrayList<Gorod> grs = null;
   //��������� ������� ����������
   Connection con = this.getConnection();
   try {
   //������������ ������� � ��
   Statement stmt = con.createStatement();
   //���������� ������� � ��������� ������ ������
   ResultSet res = stmt.executeQuery(
   "SELECT kod_Gorod, name, popul "
   + "from Gorod order by kod_gorod");
   //�������� ������� � ������ �������
   grs = new ArrayList<Gorod>();
   //� ����� ��������� ������ ������ ��������� ������
   while (res.next()) {
   //������� ������ Gorod
   Gorod pr = new Gorod();
   //��������� ���� ������� ������� �� ������ ������
   pr.setKod_gorod(res.getBigDecimal(1));
   pr.setName(res.getString(2));
   pr.setPopul(res.getBigDecimal(3));

   //��������� ������ � ������
   grs.add(pr);
   }
   } catch (SQLException e) {
   JOptionPane.showMessageDialog(null, e.getMessage(),
   "������ ��������� ������", JOptionPane.ERROR_MESSAGE);
   }
   //������� ������
   return grs;
   }
 //------------------�������� �����
   // ��������� ������� ������� �� ��
   // � ������� ��� � ���� ������ 
public ArrayList<Gorod> loadGorodForCmb() {
ArrayList<Gorod> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT kod_Gorod, name, popul "
+ "from Gorod order by name");
//�������� ������� � ������ �������
grs = new ArrayList<Gorod>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Gorod
Gorod pr = new Gorod();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_gorod(res.getBigDecimal(1));
pr.setName(res.getString(2));
pr.setPopul(res.getBigDecimal(3));

//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}
   	//�������������� �����
   // ���������� ��������� �������������� ������
   //  ���������:
   //   dol � ������ �����Ļ �� ����� ��������������
   //   key � �������� ��������� ���������
   public boolean updateGorod(Gorod dol, BigDecimal key) {
       PreparedStatement pst = null;
   	 //  ��������� ������� ����������
       Connection con=this.getConnection();
       //  ������ � ������� ��������� (? � ���������)
       String stm ="UPDATE Gorod set "+"kod_Gorod=?,"
       + "name=?,"
     
     + "popul=?"
       + " WHERE kod_Gorod=?";
   	try {  
   	 //  �������� ������� ��������� � �����������
       pst = con.prepareStatement(stm);
   	 // ������ �������� ���������� ���������
   	 //  1- ����� ��������� 
     
   	 pst.setBigDecimal(1, dol.getKod_gorod());
   	 pst.setString(2, dol.getName());
   

   	 pst.setBigDecimal(3, dol.getPopul());
   	 pst.setBigDecimal(4, key);
   	 // ���������� ���������
   	 pst.executeUpdate();
   	 // ���������� ���������� � ���������� ���������
   	 con.commit();
   	} catch (SQLException ex) {
   // � ������ ������ � ������ ���������� 
   RollBack();
   	// � ����� ��������� �� ������
   JOptionPane.showMessageDialog(null, ex.getMessage(),
            "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
   ex.printStackTrace();
   return false;
   	} finally {
   try {
   	pst.close();
   } catch (SQLException e) {
   	       e.printStackTrace();
   return false;
   }	
   	}
   	return true;
   	}


   //���������� �����
   	// ���������� ��������� ���������� ������
   public  boolean addGorod(Gorod dol) {
   	        PreparedStatement pst = null;
    //  ��������� ������� ����������
    Connection con=this.getConnection();
   	        //  ������ � ������� ��������� (? � ���������)
    String stm ="INSERT INTO Gorod(kod_Gorod,"+
      "name, popul)"
   	      + "  VALUES(?,?,?)";
    try {  
   	//  �������� ������� ��������� � �����������
   	            pst = con.prepareStatement(stm);
   	// ������ �������� ���������� ���������
   	          
   	         pst.setBigDecimal(1, dol.getKod_gorod());
   	    	 pst.setString(2, dol.getName());
   	    

   	    	 pst.setBigDecimal(3, dol.getPopul());
   	// ���������� ���������
   	  pst.executeUpdate();
   	// ���������� ���������� � ���������� ���������
   	  con.commit();
   	  return true;
    } catch (SQLException ex) {
   	// � ������ ������ � ������ ���������� 
   	  RollBack();
   	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
   	               "������ ���������� ������", 
   	                JOptionPane.ERROR_MESSAGE);
   	  ex.printStackTrace();
   	  return false;
   } finally {
   try {
   	pst.close();
   } catch (SQLException e) {
   	e.printStackTrace();
   	  return false;
   }	
   	}
   }

   //�������� �����
   	// ���������� ��������� �������� ������
   public boolean deleteGorod(BigDecimal kod) {
   	       PreparedStatement pst = null;
    //  ��������� ������� ����������
   Connection con=this.getConnection();
   	       //  ������ � ������� ��������� (? � ���������)
   String stm ="DELETE FROM Gorod"
       + " WHERE kod_Gorod=?";
   try {  
    //  �������� ������� ��������� � �����������
   	        pst = con.prepareStatement(stm);
   	// ������ �������� ���������� ���������
    pst.setBigDecimal(1, kod);
   	// ���������� ���������
    pst.executeUpdate();
   	// ���������� ���������� � ���������� ���������
    con.commit();
    return true;
   } catch (SQLException ex) {
      ex.printStackTrace();
   	    	     return false;
   } finally {
   	try {
   pst.close();
   	} catch (SQLException e) {
        e.printStackTrace();
        return false;
   	}	
   	}
   }	
   
   
 //------------------�������� ������
// ��������� ������� ������ �� ��
// � ������� ��� � ���� ������ 
public ArrayList<Zayav> loadZayav() {
ArrayList<Zayav> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT id_zayav, Num_zayav, date_Zayav, vid, kod_klient, fio, prefer, kod_sotrud, fio2 "
+ "from Zayav_v order by id_zayav");
//�������� ������� � ������ ������
grs = new ArrayList<Zayav>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Zayav
Zayav pr = new Zayav();
//��������� ���� ������� ������� �� ������ ������
pr.setId_zayav(res.getBigDecimal(1));
pr.setNum_zayav(res.getBigDecimal(2));
pr.setDate_zayav(res.getDate(3));
pr.setVid(res.getString(4));
Klient kl = new Klient ();

kl.setKod_klient(res.getBigDecimal(5));
kl.setFio(res.getString(6));
pr.setKlient(kl);
pr.setPrefer(res.getString(7));
Sotrud kl2 = new Sotrud ();

kl2.setKod_sotrud(res.getBigDecimal(8));
kl2.setFio(res.getString(9));
pr.setSotrud(kl2);
//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

	//�������������� ������
//���������� ��������� �������������� ������
//���������:
//dol � ������ �������� �� ����� ��������������
//key � �������� ��������� ���������
public boolean updateZayav(Zayav dol, BigDecimal key) {
PreparedStatement pst = null;
	 //  ��������� ������� ����������
Connection con=this.getConnection();
//  ������ � ������� ��������� (? � ���������)
String stm ="UPDATE Zayav set "+"id_zayav=?,"
+ "Num_zayav=?,"
+ "date_zayav=?,"
+ "vid=?,"
+ "kod_klient=?,"
+ "prefer=?,"
+ "kod_sotrud=?"
+ " WHERE id_zayav=?";
	try {  
	 //  �������� ������� ��������� � �����������
pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getId_zayav());
	 pst.setBigDecimal(2, dol.getNum_zayav());
	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
	         dol.getDate_zayav()));
	 pst.setString(4, dol.getVid());
 	 pst.setBigDecimal(5, dol.getKlient().getKod_klient());

   	 pst.setString(6, dol.getPrefer());
 	 pst.setBigDecimal(7, dol.getSotrud().getKod_sotrud());
	 pst.setBigDecimal(8, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
//� ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
    "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ������
	// ���������� ��������� ���������� ������
public  boolean addZayav(Zayav dol) {
	        PreparedStatement pst = null;
//��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Zayav(id_zayav,"+
"Num_zayav, date_zayav, vid, kod_klient, prefer,kod_sotrud)"
	      + "  VALUES(?,?,?,?,?,?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	            dol.setId_zayav(getId("book_uchyot_seq"));
	            pst.setBigDecimal(1, dol.getId_zayav());
	       	 pst.setBigDecimal(2, dol.getNum_zayav());
	       	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
	       	         dol.getDate_zayav()));
	       	 pst.setString(4, dol.getVid());
	       	 pst.setBigDecimal(5, dol.getKlient().getKod_klient());

	       	 pst.setString(6, dol.getPrefer());
	     	 pst.setBigDecimal(7, dol.getSotrud().getKod_sotrud());
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ������
	// ���������� ��������� �������� ������
public boolean deleteZayav(BigDecimal kod) {
	       PreparedStatement pst = null;
//��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Zayav"
+ " WHERE id_zayav=?";
try {  
//�������� ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
e.printStackTrace();
return false;
	}	
	}
}	



 //------------------�������� ������
   // ��������� ������� ������ �� ��
   // � ������� ��� � ���� ������ 
public ArrayList<Zdan> loadZdan() {
ArrayList<Zdan> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT kod_Zdan, kod_street, name_street, kod_gorod, name, nomer_dom, etagej "
+ "from Zdan_v");
//�������� ������� � ������ ������
grs = new ArrayList<Zdan>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Zdan
Zdan pr = new Zdan();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_zdan(res.getBigDecimal(1));
Street s = new Street ();
s.setKod_street(res.getBigDecimal(2));
s.setName_street(res.getString(3));
Gorod g = new Gorod ();
g.setKod_gorod(res.getBigDecimal(4));
g.setName(res.getString(5));
s.setGorod(g);
pr.setStreet(s);

pr.setNomer_dom(res.getString(6));
pr.setEtagej(res.getBigDecimal(7));

//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

	//�������������� ������
// ���������� ��������� �������������� ������
//  ���������:
//   dol � ������ ������Ż �� ����� ��������������
//   key � �������� ��������� ���������
public boolean updateZdan(Zdan dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  ��������� ������� ����������
  Connection con=this.getConnection();
  //  ������ � ������� ��������� (? � ���������)
  String stm ="UPDATE Zdan set "+"kod_zdan=?,"
  + "kod_street=?,"
  + "nomer_doma=?,"
+ "etagej=?"
  + " WHERE kod_zdan=?";
	try {  
	 //  �������� ������� ��������� � �����������
  pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getKod_zdan());
	 pst.setBigDecimal(2, dol.getStreet().getKod_street());

	 pst.setString(3, dol.getNomer_dom());

	 pst.setBigDecimal(4, dol.getEtagej());
	 pst.setBigDecimal(5, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
// � ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ������
	// ���������� ��������� ���������� ������
public  boolean addZdan(Zdan dol) {
	        PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Zdan(kod_zdan,"+
 "kod_street, nomer_dom, etagej)"
	      + "  VALUES(?,?,?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	          
	       	 pst.setBigDecimal(1, dol.getKod_zdan());
	    	 pst.setBigDecimal(2, dol.getStreet().getKod_street());

	    	 pst.setString(3, dol.getNomer_dom());

	    	 pst.setBigDecimal(4, dol.getEtagej());
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ������
	// ���������� ��������� �������� ������
public boolean deleteZdan(BigDecimal kod) {
	       PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Zdan"
  + " WHERE kod_Zdan=?";
try {  
//  �������� ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
 ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
   e.printStackTrace();
   return false;
	}	
	}
}	

//------------------�������� ������
        // ��������� ������� �������� �� ��
        // � ������� ��� � ���� ������ 
   public ArrayList<Klient> loadKlient() {
   ArrayList<Klient> grs = null;
   //��������� ������� ����������
   Connection con = this.getConnection();
   try {
   //������������ ������� � ��
   Statement stmt = con.createStatement();
   //���������� ������� � ��������� ������ ������
   ResultSet res = stmt.executeQuery(
   "SELECT kod_klient, fio, tel, money "
   + "from Klient order by kod_klient");
   //�������� ������� � ������ ��������
   grs = new ArrayList<Klient>();
   //� ����� ��������� ������ ������ ��������� ������
   while (res.next()) {
   //������� ������ Klient
   Klient pr = new Klient();
   //��������� ���� ������� ������� �� ������ ������
   pr.setKod_klient(res.getBigDecimal(1));
   pr.setFio(res.getString(2));
   pr.setTel(res.getString(3));
   pr.setMoney(res.getBigDecimal(4));

   //��������� ������ � ������
   grs.add(pr);
   }
   } catch (SQLException e) {
   JOptionPane.showMessageDialog(null, e.getMessage(),
   "������ ��������� ������", JOptionPane.ERROR_MESSAGE);
   }
   //������� ������
   return grs;
   }
  
   	//�������������� ������
   // ���������� ��������� �������������� ������
   //  ���������:
   //   dol � ������ ������һ �� ����� ��������������
   //   key � �������� ��������� ���������
   public boolean updateKlient(Klient dol, BigDecimal key) {
       PreparedStatement pst = null;
   	 //  ��������� ������� ����������
       Connection con=this.getConnection();
       //  ������ � ������� ��������� (? � ���������)
       String stm ="UPDATE Klient set "+"kod_klient=?,"
       + "fio=?,"
     
       + "tel=?,"
     + "money=?"
       + " WHERE kod_klient=?";
   	try {  
   	 //  �������� ������� ��������� � �����������
       pst = con.prepareStatement(stm);
   	 // ������ �������� ���������� ���������
   	 //  1- ����� ��������� 
     
   	 pst.setBigDecimal(1, dol.getKod_klient());
   	 pst.setString(2, dol.getFio());
   	 pst.setString(3, dol.getTel());

   	 pst.setBigDecimal(4, dol.getMoney());
   	 pst.setBigDecimal(5, key);
   	 // ���������� ���������
   	 pst.executeUpdate();
   	 // ���������� ���������� � ���������� ���������
   	 con.commit();
   	} catch (SQLException ex) {
   // � ������ ������ � ������ ���������� 
   RollBack();
   	// � ����� ��������� �� ������
   JOptionPane.showMessageDialog(null, ex.getMessage(),
            "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
   ex.printStackTrace();
   return false;
   	} finally {
   try {
   	pst.close();
   } catch (SQLException e) {
   	       e.printStackTrace();
   return false;
   }	
   	}
   	return true;
   	}


   //���������� ������
   	// ���������� ��������� ���������� ������
   public  boolean addKlient(Klient dol) {
   	        PreparedStatement pst = null;
    //  ��������� ������� ����������
    Connection con=this.getConnection();
   	        //  ������ � ������� ��������� (? � ���������)
    String stm ="INSERT INTO Klient(kod_klient,"+
      "fio, tel, money)"
   	      + "  VALUES(?,?,?,?)";
    try {  
   	//  �������� ������� ��������� � �����������
   	            pst = con.prepareStatement(stm);
   	// ������ �������� ���������� ���������
   	          
   	         pst.setBigDecimal(1, dol.getKod_klient());
   	    	 pst.setString(2, dol.getFio());
   	    	 pst.setString(3, dol.getTel());

   	    	 pst.setBigDecimal(4, dol.getMoney());
   	// ���������� ���������
   	  pst.executeUpdate();
   	// ���������� ���������� � ���������� ���������
   	  con.commit();
   	  return true;
    } catch (SQLException ex) {
   	// � ������ ������ � ������ ���������� 
   	  RollBack();
   	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
   	               "������ ���������� ������", 
   	                JOptionPane.ERROR_MESSAGE);
   	  ex.printStackTrace();
   	  return false;
   } finally {
   try {
   	pst.close();
   } catch (SQLException e) {
   	e.printStackTrace();
   	  return false;
   }	
   	}
   }

   //�������� ������
   	// ���������� ��������� �������� ������
   public boolean deleteKlient(BigDecimal kod) {
   	       PreparedStatement pst = null;
    //  ��������� ������� ����������
   Connection con=this.getConnection();
   	       //  ������ � ������� ��������� (? � ���������)
   String stm ="DELETE FROM Klient"
       + " WHERE kod_klient=?";
   try {  
    //  �������� ������� ��������� � �����������
   	        pst = con.prepareStatement(stm);
   	// ������ �������� ���������� ���������
    pst.setBigDecimal(1, kod);
   	// ���������� ���������
    pst.executeUpdate();
   	// ���������� ���������� � ���������� ���������
    con.commit();
    return true;
   } catch (SQLException ex) {
      ex.printStackTrace();
   	    	     return false;
   } finally {
   	try {
   pst.close();
   	} catch (SQLException e) {
        e.printStackTrace();
        return false;
   	}	
   	}
   }	
   
 //------------------�������� ����� ������
   // ��������� ������� ������� ������ �� ��
   // � ������� ��� � ���� ������ 
public ArrayList<Book_oplat> loadBook_oplat() {
ArrayList<Book_oplat> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT id_Book_oplat, Id_book_uchyot, date_opl, Sum_opl "
+ "from Book_oplat");
grs = new ArrayList<Book_oplat>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Book_oplat
Book_oplat pr = new Book_oplat();
//��������� ���� ������� ������� �� ������ ������
pr.setId_book_oplat(res.getBigDecimal(1));
pr.setId_book_uchyot(res.getBigDecimal(2));
pr.setDate_opl(res.getDate(3));
pr.setSum_opl(res.getBigDecimal(4));

//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

//------------------�������� ����� ������
// ��������� ������� ������� ������ �� ��
// � ������� ��� � ���� ������ 
public ArrayList<Book_oplat> loadBook_oplat(BigDecimal ff) {
ArrayList<Book_oplat> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT id_Book_oplat, Id_book_uchyot, date_opl, Sum_opl "
+ "from Book_oplat where id_book_uchyot="+ff);
grs = new ArrayList<Book_oplat>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Book_oplat
Book_oplat pr = new Book_oplat();
//��������� ���� ������� ������� �� ������ ������
pr.setId_book_oplat(res.getBigDecimal(1));
pr.setId_book_uchyot(res.getBigDecimal(2));
pr.setDate_opl(res.getDate(3));
pr.setSum_opl(res.getBigDecimal(4));

//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}
	//�������������� ����� ������
// ���������� ��������� �������������� ������
//  ���������:
//   dol � ������ ������ �����ۻ �� ����� ��������������
//   key � �������� ��������� ���������
public boolean updateBook_oplat(Book_oplat dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  ��������� ������� ����������
  Connection con=this.getConnection();
  //  ������ � ������� ��������� (? � ���������)
  String stm ="UPDATE Book_oplat set "+"id_Book_oplat=?,"
  + "Id_book_uchyot=?,"
  + "date_opl=?,"
+ "Sum_opl=?"
  + " WHERE id_Book_oplat=?";
	try {  
	 //  �������� ������� ��������� � �����������
  pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getId_book_oplat());
	 pst.setBigDecimal(2, dol.getId_book_uchyot());
	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
		         dol.getDate_opl()));
			 

	 pst.setBigDecimal(4, dol.getSum_opl());
	 pst.setBigDecimal(5, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
// � ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ����� ������
	// ���������� ��������� ���������� ������
public  boolean addBook_oplat(Book_oplat dol) {
	        PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Book_oplat(id_Book_oplat,"+
 "Id_book_uchyot, date_opl, Sum_opl)"
	      + "  VALUES(?,?,?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	            dol.setId_book_oplat(getId("book_oplat_seq"));

	       	 pst.setBigDecimal(1, dol.getId_book_oplat());
	       	 pst.setBigDecimal(2, dol.getId_book_uchyot());
	       	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
	       		         dol.getDate_opl()));
	       			 

	       	 pst.setBigDecimal(4, dol.getSum_opl());
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ����� ������
	// ���������� ��������� �������� ������
public boolean deleteBook_oplat(BigDecimal kod) {
	       PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Book_oplat"
  + " WHERE id_Book_oplat=?";
try {  
//  ��������  ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
 ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
   e.printStackTrace();
   return false;
	}	
	}
}	


//------------------�������� ����� �ר��
//��������� ������� ������� ����� ����� �� ��
//� ������� ��� � ���� ������ 
public ArrayList<Book_uchyot> loadBook_uchyot(BigDecimal ff) {
ArrayList<Book_uchyot> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
ResultSet res;
//���������� ������� � ��������� ������ ������
if (ff.equals(BigDecimal.valueOf(-1))) {
res = stmt.executeQuery(
"SELECT id_Book_uchyot, Id_zayav, kod_obj, kod_zdan, " + 
"    nomer_dom, kod_street," + 
"    name_street, kod_gorod, name, "
+ "kod_usl, name_usl, uch_date, "
+ "kod_sotrud, fio, stoim, opl_stoim "
+ "from Book_uchyot_v");
}
else {
	 res = stmt.executeQuery(
			"SELECT id_Book_uchyot, Id_zayav, kod_obj, kod_zdan, " + 
			"    nomer_dom, kod_street," + 
			"    name_street, kod_gorod, name, "
			+ "kod_usl, name_usl, uch_date, "
			+ "kod_sotrud, fio, stoim, opl_stoim "
			+ "from Book_uchyot_v where id_zayav="+ff);
	
}
//�������� ������� � ������ ������� ����� �����
grs = new ArrayList<Book_uchyot>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Book_uchyot
Book_uchyot pr = new Book_uchyot();
//��������� ���� ������� ������� �� ������ ������
pr.setId_book_uchyot(res.getBigDecimal(1));
pr.setId_zayav(res.getBigDecimal(2));

Obj_nedvij onn = new Obj_nedvij();
onn.setKod_obj(res.getBigDecimal(3));

Zdan zd = new Zdan ();
zd.setKod_zdan(res.getBigDecimal(4));
zd.setNomer_dom(res.getString(5));

Street stt = new Street ();
stt.setKod_street(res.getBigDecimal(6));
stt.setName_street(res.getString(7));
Gorod g = new Gorod ();

g.setKod_gorod(res.getBigDecimal(8));
g.setName(res.getString(9));

stt.setGorod(g);
zd.setStreet(stt);
onn.setZdan(zd);
pr.setObj_nedvij(onn);

Usluga us = new Usluga ();
us.setKod_usl(res.getBigDecimal(10));
us.setName_usl(res.getString(11));
pr.setUsluga(us);
pr.setUch_date(res.getDate(12));
Sotrud kl2 =new Sotrud();
kl2.setKod_sotrud(res.getBigDecimal(13));
kl2.setFio(res.getString(14));
pr.setSotrud(kl2);
pr.setStoim(res.getBigDecimal(15));
pr.setOpl_stoim(res.getBigDecimal(16));


//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

	//�������������� ����� �ר��
//���������� ��������� �������������� ������
//���������:
//dol � ������ ������ �ר��� �� ����� ��������������
//key � �������� ��������� ���������
public boolean updateBook_uchyot(Book_uchyot dol, BigDecimal key) {
PreparedStatement pst = null;
	 //  ��������� ������� ����������
Connection con=this.getConnection();
//������ � ������� ��������� (? � ���������)
String stm ="UPDATE Book_uchyot set "+"id_Book_uchyot=?,"
+ "Id_zayav=?,"
+ "kod_obj=?,"
+ "kod_usl=?,"
+ "uch_date=?,"
+ "kod_sotrud=?,"
+ "stoim=?,"
+ "opl_stoim=?"
+ " WHERE id_Book_uchyot=?";
	try {  
	 //  �������� ������� ��������� � �����������
pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getId_book_uchyot());
	 pst.setBigDecimal(2, dol.getId_zayav());
	 pst.setBigDecimal(3, dol.getKod_obj());
	 pst.setBigDecimal(4, dol.getUsluga().getKod_usl());
	 pst.setDate(5,HelperConverter.convertFromJavaDateToSQLDate(
	         dol.getUch_date()));
	 pst.setBigDecimal(6, dol.getSotrud().getKod_sotrud());
	 pst.setBigDecimal(7, dol.getStoim());
	 pst.setBigDecimal(8, dol.getOpl_stoim());
	 pst.setBigDecimal(9, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
//� ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
 "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ����� �ר��
	// ���������� ��������� ���������� ������
public  boolean addBook_uchyot(Book_uchyot dol) {
	        PreparedStatement pst = null;
//��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Book_uchyot(id_Book_uchyot,"+
"Id_zayav, kod_obj, kod_usl, uch_Date, kod_sotrud, stoim, opl_Stoim)"
	      + "  VALUES(?,?,?,?,?,?,?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	            dol.setId_book_uchyot(getId("book_uchyot_seq"));
	            pst.setBigDecimal(1, dol.getId_book_uchyot());
	       	 pst.setBigDecimal(2, dol.getId_zayav());
	       	 pst.setBigDecimal(3, dol.getKod_obj());
	       	 pst.setBigDecimal(4, dol.getKod_usl());
	       	 pst.setDate(5,HelperConverter.convertFromJavaDateToSQLDate(
	       	         dol.getUch_date()));
	       	 pst.setBigDecimal(6, dol.getSotrud().getKod_sotrud());
	       	 pst.setBigDecimal(7, dol.getStoim());
	       	 pst.setBigDecimal(8, dol.getOpl_stoim());
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ����� �ר��
	// ���������� ��������� �������� ������
public boolean deleteBook_uchyot(BigDecimal kod) {
	       PreparedStatement pst = null;
//��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Book_uchyot"
+ " WHERE id_Book_uchyot=?";
try {  
//�������� ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
e.printStackTrace();
return false;
	}	
	}
}	

   	
 //------------------�������� ���������
   // ��������� ������� ����������� �� ��
   // � ������� ��� � ���� ������ 
public ArrayList<Sotrud> loadManag() {
ArrayList<Sotrud> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT Kod_sotrud, Fio, stage "
+ "from Sotrud");
//�������� ������� � ������ ����������
grs = new ArrayList<Sotrud>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Manag
	Sotrud pr = new Sotrud();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_sotrud(res.getBigDecimal(1));
pr.setFio(res.getString(2));
pr.setStag(res.getBigDecimal(3));


//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

//------------------�������� ���������
// ��������� ������� ����������� �� ��
// � ������� ��� � ���� ������ 
public ArrayList<Sotrud> loadManagForCmb() {
ArrayList<Sotrud> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT Kod_sotrud, Fio, stage "
+ "from Sotrud order by Fio");
//�������� ������� � ������ ����������
grs = new ArrayList<Sotrud>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Manag
	Sotrud pr = new Sotrud();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_sotrud(res.getBigDecimal(1));
pr.setFio(res.getString(2));
pr.setStag(res.getBigDecimal(3));


//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}
	//�������������� ���������
// ���������� ��������� �������������� ������
//  ���������:
//   dol � ������ ��������л �� ����� ��������������
//   key � �������� ��������� ���������
public boolean updateSotrud(Sotrud dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  ��������� ������� ����������
  Connection con=this.getConnection();
  //  ������ � ������� ��������� (? � ���������)
  String stm ="UPDATE sotrud set "+"Kod_sotrud=?,"
  
+ "Fio=?,"
+ "stag=?"
  + " WHERE Kod_sotrud=?";
	try {  
	 //  �������� ������� ��������� � �����������
  pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getKod_sotrud());
	 pst.setString(2, dol.getFio());
	 pst.setBigDecimal(3, dol.getStag());
	
	 pst.setBigDecimal(4, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
// � ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ���������
	// ���������� ��������� ���������� ������
public  boolean addSotrud(Sotrud dol) {
	        PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO sotrud(Kod_sotrud,"+
 "Fio, stag)"
	      + "  VALUES(?,?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	          
	            pst.setBigDecimal(1, dol.getKod_sotrud());
	       	 pst.setString(2, dol.getFio());
	       	 pst.setBigDecimal(3, dol.getStag());
	     	
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ��������
	// ���������� ��������� �������� ������
public boolean deleteManag(BigDecimal kod) {
	       PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Manag"
  + " WHERE Kod_sotrud=?";
try {  
//  �������� ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
 ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
   e.printStackTrace();
   return false;
	}	
	}
}	

//------------------�������� ������ ������������
// ��������� ������� �������� ������������ �� ��
// � ������� ��� � ���� ������ 
public ArrayList<Obj_nedvij> loadObj_nedvij() {
ArrayList<Obj_nedvij> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT kod_Obj, Kod_zdan, nomer_dom, kod_street, "
+ "name_street, kod_gorod, name, area, price, Floor, vid_ned "
+ "from Obj_nedvij_v");
//�������� ������� � ������ �������� ����.
grs = new ArrayList<Obj_nedvij>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Obj_nedvij
Obj_nedvij pr = new Obj_nedvij();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_obj(res.getBigDecimal(1));
Zdan zd = new Zdan ();
zd.setKod_zdan(res.getBigDecimal(2));
zd.setNomer_dom(res.getString(3));
Street s= new Street ();
s.setKod_street(res.getBigDecimal(4));
s.setName_street(res.getString(5));

Gorod g = new Gorod ();
g.setKod_gorod(res.getBigDecimal(6));
g.setName(res.getString(7));
s.setGorod(g);
zd.setStreet(s);
pr.setZdan(zd);
pr.setArea(res.getBigDecimal(8));
pr.setPrice(res.getBigDecimal(9));
pr.setFloor(res.getBigDecimal(10));
pr.setVid_ned(res.getString(11));

//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

	//�������������� ������ ������������
//���������� ��������� �������������� ������
//���������:
//dol � ������ ������� �����������Ȼ �� ����� ��������������
//key � �������� ��������� ���������
public boolean updateObj_nedvij(Obj_nedvij dol, BigDecimal key) {
PreparedStatement pst = null;
	 //  ��������� ������� ����������
Connection con=this.getConnection();
//  ������ � ������� ��������� (? � ���������)
String stm ="UPDATE Obj_nedvij set "+"kod_Obj=?,"
+ "Kod_zdan=?,"
+ "area=?,"
+ "price=?,"
+ "floor=?,"
+ "vid_ned=?"
+ " WHERE kod_Obj=?";
	try {  
	 //  �������� ������� ��������� � �����������
pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getKod_obj());
	 pst.setBigDecimal(2, dol.getKod_zdan());
	 pst.setBigDecimal(3, dol.getArea());
	 pst.setBigDecimal(4, dol.getPrice());

	 pst.setBigDecimal(5, dol.getFloor());
	 pst.setString(6, dol.getVid_ned());
	 pst.setBigDecimal(7, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
//� ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
    "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ������ ������������
	// ���������� ��������� ���������� ������
public  boolean addObj_nedvij(Obj_nedvij dol) {
	        PreparedStatement pst = null;
//��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Obj_nedvij(kod_Obj,"+
"Kod_zdan, area, price, floor, vid_ned)"
	      + "  VALUES(?,?,?,?,?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	          
	       	 pst.setBigDecimal(1, dol.getKod_obj());
	    	 pst.setBigDecimal(2, dol.getKod_zdan());
	    	 pst.setBigDecimal(3, dol.getArea());
	    	 pst.setBigDecimal(4, dol.getPrice());

	    	 pst.setBigDecimal(5, dol.getFloor());
	    	 pst.setString(6, dol.getVid_ned());
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ������ ������������
	// ���������� ��������� �������� ������
public boolean deleteObj_nedvij(BigDecimal kod) {
	       PreparedStatement pst = null;
//��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Obj_nedvij"
+ " WHERE kod_Obj=?";
try {  
//�������� ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
e.printStackTrace();
return false;
	}	
	}
}	

 //------------------�������� ������
   // ��������� ������� ����� �� ��
   // � ������� ��� � ���� ������ 
public ArrayList<Usluga> loadUsluga() {
ArrayList<Usluga> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT kod_usl, name_usl "
+ "from Usluga");
//�������� ������� � ������ �����
grs = new ArrayList<Usluga>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Usluga
Usluga pr = new Usluga();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_usl(res.getBigDecimal(1));
pr.setName_usl(res.getString(2));


//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}
//------------------�������� ������
// ��������� ������� ����� �� ��
// � ������� ��� � ���� ������ 
public ArrayList<Usluga> loadUslugaForCmb() {
ArrayList<Usluga> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT kod_usl, name_usl "
+ "from Usluga order by name_usl");
//�������� ������� � ������ �����
grs = new ArrayList<Usluga>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Usluga
Usluga pr = new Usluga();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_usl(res.getBigDecimal(1));
pr.setName_usl(res.getString(2));


//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

	//�������������� ������
// ���������� ��������� �������������� ������
//  ���������:
//   dol � ������ �������� �� ����� ��������������
//   key � �������� ��������� ���������
public boolean updateUsluga(Usluga dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  ��������� ������� ����������
  Connection con=this.getConnection();
  //  ������ � ������� ��������� (? � ���������)
  String stm ="UPDATE Usluga set "+"kod_usl=?,"
  
+ "name_usl=?"
  + " WHERE kod_usl=?";
	try {  
	 //  �������� ������� ��������� � �����������
  pst = con.prepareStatement(stm);
	 // ������ �������� ���������� ���������
	 //  1- ����� ��������� 

	 pst.setBigDecimal(1, dol.getKod_usl());
	 pst.setString(2, dol.getName_usl());
	
	 pst.setBigDecimal(3, key);
	 // ���������� ���������
	 pst.executeUpdate();
	 // ���������� ���������� � ���������� ���������
	 con.commit();
	} catch (SQLException ex) {
// � ������ ������ � ������ ���������� 
RollBack();
	// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
	} finally {
try {
	pst.close();
} catch (SQLException e) {
	       e.printStackTrace();
return false;
}	
	}
	return true;
	}


//���������� ������
	// ���������� ��������� ���������� ������
public  boolean addUsluga(Usluga dol) {
	        PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	        //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Usluga(kod_usl,"+
 "name_usl)"
	      + "  VALUES(?,?)";
try {  
	//  �������� ������� ��������� � �����������
	            pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
	          
	            pst.setBigDecimal(1, dol.getKod_usl());
	       	 pst.setString(2, dol.getName_usl());
	// ���������� ���������
	  pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// � ������ ������ � ������ ���������� 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "������ ���������� ������", 
	                JOptionPane.ERROR_MESSAGE);
	  ex.printStackTrace();
	  return false;
} finally {
try {
	pst.close();
} catch (SQLException e) {
	e.printStackTrace();
	  return false;
}	
	}
}

//�������� ������
	// ���������� ��������� �������� ������
public boolean deleteUsluga(BigDecimal kod) {
	       PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
	       //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Usluga"
  + " WHERE kod_usl=?";
try {  
//  �������� ������� ��������� � �����������
	        pst = con.prepareStatement(stm);
	// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
	// ���������� ���������
pst.executeUpdate();
	// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
 ex.printStackTrace();
	    	     return false;
} finally {
	try {
pst.close();
	} catch (SQLException e) {
   e.printStackTrace();
   return false;
	}	
	}
}	
//------------------�������� �����
// ��������� ������� ���� �� ��
// � ������� ��� � ���� ������ 
public ArrayList<Street> loadStreet() {
ArrayList<Street> grs = null;
//��������� ������� ����������
Connection con = this.getConnection();
try {
//������������ ������� � ��
Statement stmt = con.createStatement();
//���������� ������� � ��������� ������ ������
ResultSet res = stmt.executeQuery(
"SELECT kod_Street, name_street, kod_gorod, name "
+ "from Street_v order by kod_street");
//�������� ������� � ������ �������
grs = new ArrayList<Street>();
//� ����� ��������� ������ ������ ��������� ������
while (res.next()) {
//������� ������ Street
Street pr = new Street();
//��������� ���� ������� ������� �� ������ ������
pr.setKod_street(res.getBigDecimal(1));
pr.setName_street(res.getString(2));
Gorod g = new Gorod ();
g.setKod_gorod(res.getBigDecimal(3));
g.setName(res.getString(4));
pr.setGorod(g);

//��������� ������ � ������
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"������ ��������� ������", JOptionPane.ERROR_MESSAGE);
}
//������� ������
return grs;
}

//�������������� �����
// ���������� ��������� �������������� ������
//  ���������:
//   dol � ������ �����Ļ �� ����� ��������������
//   key � �������� ��������� ���������
public boolean updateStreet(Street dol, BigDecimal key) {
PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
//  ������ � ������� ��������� (? � ���������)
String stm ="UPDATE Street set "+"kod_Street=?,"
+ "name_street=?"

+ " WHERE kod_Street=?";
try {  
//  �������� ������� ��������� � �����������
pst = con.prepareStatement(stm);
// ������ �������� ���������� ���������
//  1- ����� ��������� 

pst.setBigDecimal(1, dol.getKod_street());
pst.setString(2, dol.getName_street());



pst.setBigDecimal(3, key);
// ���������� ���������
pst.executeUpdate();
// ���������� ���������� � ���������� ���������
con.commit();
} catch (SQLException ex) {
// � ������ ������ � ������ ���������� 
RollBack();
// � ����� ��������� �� ������
JOptionPane.showMessageDialog(null, ex.getMessage(),
    "������ ��������� ������",JOptionPane.ERROR_MESSAGE);
ex.printStackTrace();
return false;
} finally {
try {
pst.close();
} catch (SQLException e) {
      e.printStackTrace();
return false;
}	
}
return true;
}


//���������� �����
// ���������� ��������� ���������� ������
public  boolean addStreet(Street dol) {
       PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
       //  ������ � ������� ��������� (? � ���������)
String stm ="INSERT INTO Street(kod_Street,"+
"name_street)"
     + "  VALUES(?,?)";
try {  
//  �������� ������� ��������� � �����������
           pst = con.prepareStatement(stm);
// ������ �������� ���������� ���������
         
        pst.setBigDecimal(1, dol.getKod_street());
        pst.setString(2, dol.getName_street());

   
// ���������� ���������
 pst.executeUpdate();
// ���������� ���������� � ���������� ���������
 con.commit();
 return true;
} catch (SQLException ex) {
// � ������ ������ � ������ ���������� 
 RollBack();
 JOptionPane.showMessageDialog(null, ex.getMessage(), 
              "������ ���������� ������", 
               JOptionPane.ERROR_MESSAGE);
 ex.printStackTrace();
 return false;
} finally {
try {
pst.close();
} catch (SQLException e) {
e.printStackTrace();
 return false;
}	
}
}

//�������� �����
// ���������� ��������� �������� ������
public boolean deleteStreet(BigDecimal kod) {
      PreparedStatement pst = null;
//  ��������� ������� ����������
Connection con=this.getConnection();
      //  ������ � ������� ��������� (? � ���������)
String stm ="DELETE FROM Street"
+ " WHERE kod_Street=?";
try {  
//  �������� ������� ��������� � �����������
       pst = con.prepareStatement(stm);
// ������ �������� ���������� ���������
pst.setBigDecimal(1, kod);
// ���������� ���������
pst.executeUpdate();
// ���������� ���������� � ���������� ���������
con.commit();
return true;
} catch (SQLException ex) {
ex.printStackTrace();
   	     return false;
} finally {
try {
pst.close();
} catch (SQLException e) {
e.printStackTrace();
return false;
}	
}
}	

public void refreshVR(Book_uchyot jyrnal_prod) {
if (jyrnal_prod.getId_book_uchyot()==null)
 	return;
 	Connection conn = this.getConnection();
 	try {
 	// ������������ ������� � ��
 	Statement stmt = conn.createStatement();
 	// ���������� ������� � ��������� ������ ������
 	ResultSet res = stmt.executeQuery(
  	"SELECT opl_stoim FROM book_uchyot_v"+
 	" where id_Book_uchyot="+jyrnal_prod.getId_book_uchyot().toString());
 	// � ����� ��������� ������ ������ ��������� ������
 	while (res.next()) {
 jyrnal_prod.setOpl_stoim(res.getBigDecimal(1));
 	}
 	} catch (SQLException ex) {
	JOptionPane.showMessageDialog(null, ex.getMessage(),
  	"������ ��������� ������",
	    	JOptionPane.ERROR_MESSAGE);
	    	}
	}

		   
}