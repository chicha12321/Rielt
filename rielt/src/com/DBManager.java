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
  //------------------------------МЕТОД SEQ
		// Метод получения суррогатного ключа
		// Используется для всех последовательностей
		// Входной параметр - имя последовательности
		public BigDecimal getId(String seqName) {
		BigDecimal id = null;
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "SELECT nextval(?)";
		try {
		// Формирование запроса к БД
		// Выполнение запроса и получение набора данных
		pst = con.prepareStatement(stm);
		// seqName - имя последовательности
		pst.setString(1, seqName);
		ResultSet res = pst.executeQuery();
		while (res.next()) {
		id = res.getBigDecimal(1);
		}
		} catch (SQLException ex) {
		JOptionPane.showMessageDialog(null,
		ex.getMessage(),
		"Ошибка получения идентификатора",
		JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
		}
		return id;
		}

     // Метод тестирования соединения
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
    	//установка пути
    	public void setPath() {
    		 Statement stmt;
    		 try {
    		 stmt = conn.createStatement();
    		 // имя схемы надо изменить !!!!
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
    	   	// Установка путей к схемам
    	   	setPath();
    	   }
  //---------------------
    	//------------------ЗАГРУЗКА ГОРОД
        // Получение перечня ГОРОДОВ из БД
        // и возврат его в виде списка 
   public ArrayList<Gorod> loadGorod() {
   ArrayList<Gorod> grs = null;
   //Получение объекта соединения
   Connection con = this.getConnection();
   try {
   //Формирование запроса к БД
   Statement stmt = con.createStatement();
   //Выполнение запроса и получение набора данных
   ResultSet res = stmt.executeQuery(
   "SELECT kod_Gorod, name, popul "
   + "from Gorod order by kod_gorod");
   //Создание объекта – список ГОРОДОВ
   grs = new ArrayList<Gorod>();
   //В цикле просмотра набора данных формируем список
   while (res.next()) {
   //Создаем объект Gorod
   Gorod pr = new Gorod();
   //Заполняем поля объекта данными из строки набора
   pr.setKod_gorod(res.getBigDecimal(1));
   pr.setName(res.getString(2));
   pr.setPopul(res.getBigDecimal(3));

   //Добавляем объект к списку
   grs.add(pr);
   }
   } catch (SQLException e) {
   JOptionPane.showMessageDialog(null, e.getMessage(),
   "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
   }
   //Возврат списка
   return grs;
   }
 //------------------ЗАГРУЗКА ГОРОД
   // Получение перечня ГОРОДОВ из БД
   // и возврат его в виде списка 
public ArrayList<Gorod> loadGorodForCmb() {
ArrayList<Gorod> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT kod_Gorod, name, popul "
+ "from Gorod order by name");
//Создание объекта – список ГОРОДОВ
grs = new ArrayList<Gorod>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Gorod
Gorod pr = new Gorod();
//Заполняем поля объекта данными из строки набора
pr.setKod_gorod(res.getBigDecimal(1));
pr.setName(res.getString(2));
pr.setPopul(res.getBigDecimal(3));

//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}
   	//РЕДАКТИРОВАНИЕ ГОРОД
   // Выполнение оператора редактирования строки
   //  Параметры:
   //   dol – объект «ГОРОД» из формы редактирования
   //   key – значение ключевого реквизита
   public boolean updateGorod(Gorod dol, BigDecimal key) {
       PreparedStatement pst = null;
   	 //  Получение объекта соединения
       Connection con=this.getConnection();
       //  Строка с текстом оператора (? – параметры)
       String stm ="UPDATE Gorod set "+"kod_Gorod=?,"
       + "name=?,"
     
     + "popul=?"
       + " WHERE kod_Gorod=?";
   	try {  
   	 //  Создание объекта «Оператор с параметрами»
       pst = con.prepareStatement(stm);
   	 // Задаем значения параметров оператора
   	 //  1- номер параметра 
     
   	 pst.setBigDecimal(1, dol.getKod_gorod());
   	 pst.setString(2, dol.getName());
   

   	 pst.setBigDecimal(3, dol.getPopul());
   	 pst.setBigDecimal(4, key);
   	 // Выполнение оператора
   	 pst.executeUpdate();
   	 // Завершение транзакции – сохранение изменений
   	 con.commit();
   	} catch (SQLException ex) {
   // В случае ошибки – отмена транзакции 
   RollBack();
   	// и вывод сообщения об ошибке
   JOptionPane.showMessageDialog(null, ex.getMessage(),
            "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


   //ДОБАВЛЕНИЕ ГОРОД
   	// Выполнение оператора добавления строки
   public  boolean addGorod(Gorod dol) {
   	        PreparedStatement pst = null;
    //  Получение объекта соединения
    Connection con=this.getConnection();
   	        //  Строка с текстом оператора (? – параметры)
    String stm ="INSERT INTO Gorod(kod_Gorod,"+
      "name, popul)"
   	      + "  VALUES(?,?,?)";
    try {  
   	//  Создание объекта «Оператор с параметрами»
   	            pst = con.prepareStatement(stm);
   	// Задаем значения параметров оператора
   	          
   	         pst.setBigDecimal(1, dol.getKod_gorod());
   	    	 pst.setString(2, dol.getName());
   	    

   	    	 pst.setBigDecimal(3, dol.getPopul());
   	// Выполнение оператора
   	  pst.executeUpdate();
   	// Завершение транзакции – сохранение изменений
   	  con.commit();
   	  return true;
    } catch (SQLException ex) {
   	// В случае ошибки – отмена транзакции 
   	  RollBack();
   	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
   	               "Ошибка добавления данных", 
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

   //УДАЛЕНИЕ ГОРОД
   	// Выполнение оператора удаления строки
   public boolean deleteGorod(BigDecimal kod) {
   	       PreparedStatement pst = null;
    //  Получение объекта соединения
   Connection con=this.getConnection();
   	       //  Строка с текстом оператора (? – параметры)
   String stm ="DELETE FROM Gorod"
       + " WHERE kod_Gorod=?";
   try {  
    //  Создание объекта «Оператор с параметрами»
   	        pst = con.prepareStatement(stm);
   	// Задаем значения параметров оператора
    pst.setBigDecimal(1, kod);
   	// Выполнение оператора
    pst.executeUpdate();
   	// Завершение транзакции – сохранение изменений
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
   
   
 //------------------ЗАГРУЗКА ЗАЯВКА
// Получение перечня ЗАЯВОК из БД
// и возврат его в виде списка 
public ArrayList<Zayav> loadZayav() {
ArrayList<Zayav> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT id_zayav, Num_zayav, date_Zayav, vid, kod_klient, fio, prefer, kod_sotrud, fio2 "
+ "from Zayav_v order by id_zayav");
//Создание объекта – список заявок
grs = new ArrayList<Zayav>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Zayav
Zayav pr = new Zayav();
//Заполняем поля объекта данными из строки набора
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
//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

	//РЕДАКТИРОВАНИЕ ЗАЯВКА
//Выполнение оператора редактирования строки
//Параметры:
//dol – объект «ЗАЯВКА» из формы редактирования
//key – значение ключевого реквизита
public boolean updateZayav(Zayav dol, BigDecimal key) {
PreparedStatement pst = null;
	 //  Получение объекта соединения
Connection con=this.getConnection();
//  Строка с текстом оператора (? – параметры)
String stm ="UPDATE Zayav set "+"id_zayav=?,"
+ "Num_zayav=?,"
+ "date_zayav=?,"
+ "vid=?,"
+ "kod_klient=?,"
+ "prefer=?,"
+ "kod_sotrud=?"
+ " WHERE id_zayav=?";
	try {  
	 //  Создание объекта «Оператор с параметрами»
pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

	 pst.setBigDecimal(1, dol.getId_zayav());
	 pst.setBigDecimal(2, dol.getNum_zayav());
	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
	         dol.getDate_zayav()));
	 pst.setString(4, dol.getVid());
 	 pst.setBigDecimal(5, dol.getKlient().getKod_klient());

   	 pst.setString(6, dol.getPrefer());
 	 pst.setBigDecimal(7, dol.getSotrud().getKod_sotrud());
	 pst.setBigDecimal(8, key);
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
//В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
    "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ ЗАЯВКА
	// Выполнение оператора добавления строки
public  boolean addZayav(Zayav dol) {
	        PreparedStatement pst = null;
//Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Zayav(id_zayav,"+
"Num_zayav, date_zayav, vid, kod_klient, prefer,kod_sotrud)"
	      + "  VALUES(?,?,?,?,?,?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
	            dol.setId_zayav(getId("book_uchyot_seq"));
	            pst.setBigDecimal(1, dol.getId_zayav());
	       	 pst.setBigDecimal(2, dol.getNum_zayav());
	       	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
	       	         dol.getDate_zayav()));
	       	 pst.setString(4, dol.getVid());
	       	 pst.setBigDecimal(5, dol.getKlient().getKod_klient());

	       	 pst.setString(6, dol.getPrefer());
	     	 pst.setBigDecimal(7, dol.getSotrud().getKod_sotrud());
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ ЗАЯВКА
	// Выполнение оператора удаления строки
public boolean deleteZayav(BigDecimal kod) {
	       PreparedStatement pst = null;
//Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Zayav"
+ " WHERE id_zayav=?";
try {  
//Создание объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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



 //------------------ЗАГРУЗКА ЗДАНИЕ
   // Получение перечня ЗДАНИЙ из БД
   // и возврат его в виде списка 
public ArrayList<Zdan> loadZdan() {
ArrayList<Zdan> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT kod_Zdan, kod_street, name_street, kod_gorod, name, nomer_dom, etagej "
+ "from Zdan_v");
//Создание объекта – список ЗДАНИЙ
grs = new ArrayList<Zdan>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Zdan
Zdan pr = new Zdan();
//Заполняем поля объекта данными из строки набора
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

//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

	//РЕДАКТИРОВАНИЕ ЗДАНИЕ
// Выполнение оператора редактирования строки
//  Параметры:
//   dol – объект «ЗДАНИЕ» из формы редактирования
//   key – значение ключевого реквизита
public boolean updateZdan(Zdan dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  Получение объекта соединения
  Connection con=this.getConnection();
  //  Строка с текстом оператора (? – параметры)
  String stm ="UPDATE Zdan set "+"kod_zdan=?,"
  + "kod_street=?,"
  + "nomer_doma=?,"
+ "etagej=?"
  + " WHERE kod_zdan=?";
	try {  
	 //  Создание объекта «Оператор с параметрами»
  pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

	 pst.setBigDecimal(1, dol.getKod_zdan());
	 pst.setBigDecimal(2, dol.getStreet().getKod_street());

	 pst.setString(3, dol.getNomer_dom());

	 pst.setBigDecimal(4, dol.getEtagej());
	 pst.setBigDecimal(5, key);
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
// В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ ЗДАНИЕ
	// Выполнение оператора добавления строки
public  boolean addZdan(Zdan dol) {
	        PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Zdan(kod_zdan,"+
 "kod_street, nomer_dom, etagej)"
	      + "  VALUES(?,?,?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
	          
	       	 pst.setBigDecimal(1, dol.getKod_zdan());
	    	 pst.setBigDecimal(2, dol.getStreet().getKod_street());

	    	 pst.setString(3, dol.getNomer_dom());

	    	 pst.setBigDecimal(4, dol.getEtagej());
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ ЗДАНИЕ
	// Выполнение оператора удаления строки
public boolean deleteZdan(BigDecimal kod) {
	       PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Zdan"
  + " WHERE kod_Zdan=?";
try {  
//  Создание объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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

//------------------ЗАГРУЗКА КЛИЕНТ
        // Получение перечня КЛИЕНТОВ из БД
        // и возврат его в виде списка 
   public ArrayList<Klient> loadKlient() {
   ArrayList<Klient> grs = null;
   //Получение объекта соединения
   Connection con = this.getConnection();
   try {
   //Формирование запроса к БД
   Statement stmt = con.createStatement();
   //Выполнение запроса и получение набора данных
   ResultSet res = stmt.executeQuery(
   "SELECT kod_klient, fio, tel, money "
   + "from Klient order by kod_klient");
   //Создание объекта – список КЛИЕНТОВ
   grs = new ArrayList<Klient>();
   //В цикле просмотра набора данных формируем список
   while (res.next()) {
   //Создаем объект Klient
   Klient pr = new Klient();
   //Заполняем поля объекта данными из строки набора
   pr.setKod_klient(res.getBigDecimal(1));
   pr.setFio(res.getString(2));
   pr.setTel(res.getString(3));
   pr.setMoney(res.getBigDecimal(4));

   //Добавляем объект к списку
   grs.add(pr);
   }
   } catch (SQLException e) {
   JOptionPane.showMessageDialog(null, e.getMessage(),
   "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
   }
   //Возврат списка
   return grs;
   }
  
   	//РЕДАКТИРОВАНИЕ КЛИЕНТ
   // Выполнение оператора редактирования строки
   //  Параметры:
   //   dol – объект «КЛИЕНТ» из формы редактирования
   //   key – значение ключевого реквизита
   public boolean updateKlient(Klient dol, BigDecimal key) {
       PreparedStatement pst = null;
   	 //  Получение объекта соединения
       Connection con=this.getConnection();
       //  Строка с текстом оператора (? – параметры)
       String stm ="UPDATE Klient set "+"kod_klient=?,"
       + "fio=?,"
     
       + "tel=?,"
     + "money=?"
       + " WHERE kod_klient=?";
   	try {  
   	 //  Создание объекта «Оператор с параметрами»
       pst = con.prepareStatement(stm);
   	 // Задаем значения параметров оператора
   	 //  1- номер параметра 
     
   	 pst.setBigDecimal(1, dol.getKod_klient());
   	 pst.setString(2, dol.getFio());
   	 pst.setString(3, dol.getTel());

   	 pst.setBigDecimal(4, dol.getMoney());
   	 pst.setBigDecimal(5, key);
   	 // Выполнение оператора
   	 pst.executeUpdate();
   	 // Завершение транзакции – сохранение изменений
   	 con.commit();
   	} catch (SQLException ex) {
   // В случае ошибки – отмена транзакции 
   RollBack();
   	// и вывод сообщения об ошибке
   JOptionPane.showMessageDialog(null, ex.getMessage(),
            "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


   //ДОБАВЛЕНИЕ КЛИЕНТ
   	// Выполнение оператора добавления строки
   public  boolean addKlient(Klient dol) {
   	        PreparedStatement pst = null;
    //  Получение объекта соединения
    Connection con=this.getConnection();
   	        //  Строка с текстом оператора (? – параметры)
    String stm ="INSERT INTO Klient(kod_klient,"+
      "fio, tel, money)"
   	      + "  VALUES(?,?,?,?)";
    try {  
   	//  Создание объекта «Оператор с параметрами»
   	            pst = con.prepareStatement(stm);
   	// Задаем значения параметров оператора
   	          
   	         pst.setBigDecimal(1, dol.getKod_klient());
   	    	 pst.setString(2, dol.getFio());
   	    	 pst.setString(3, dol.getTel());

   	    	 pst.setBigDecimal(4, dol.getMoney());
   	// Выполнение оператора
   	  pst.executeUpdate();
   	// Завершение транзакции – сохранение изменений
   	  con.commit();
   	  return true;
    } catch (SQLException ex) {
   	// В случае ошибки – отмена транзакции 
   	  RollBack();
   	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
   	               "Ошибка добавления данных", 
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

   //УДАЛЕНИЕ КЛИЕНТ
   	// Выполнение оператора удаления строки
   public boolean deleteKlient(BigDecimal kod) {
   	       PreparedStatement pst = null;
    //  Получение объекта соединения
   Connection con=this.getConnection();
   	       //  Строка с текстом оператора (? – параметры)
   String stm ="DELETE FROM Klient"
       + " WHERE kod_klient=?";
   try {  
    //  Создание объекта «Оператор с параметрами»
   	        pst = con.prepareStatement(stm);
   	// Задаем значения параметров оператора
    pst.setBigDecimal(1, kod);
   	// Выполнение оператора
    pst.executeUpdate();
   	// Завершение транзакции – сохранение изменений
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
   
 //------------------ЗАГРУЗКА КНИГА ОПЛАТЫ
   // Получение перечня записей оплаты из БД
   // и возврат его в виде списка 
public ArrayList<Book_oplat> loadBook_oplat() {
ArrayList<Book_oplat> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT id_Book_oplat, Id_book_uchyot, date_opl, Sum_opl "
+ "from Book_oplat");
grs = new ArrayList<Book_oplat>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Book_oplat
Book_oplat pr = new Book_oplat();
//Заполняем поля объекта данными из строки набора
pr.setId_book_oplat(res.getBigDecimal(1));
pr.setId_book_uchyot(res.getBigDecimal(2));
pr.setDate_opl(res.getDate(3));
pr.setSum_opl(res.getBigDecimal(4));

//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

//------------------ЗАГРУЗКА КНИГА ОПЛАТЫ
// Получение перечня записей оплаты из БД
// и возврат его в виде списка 
public ArrayList<Book_oplat> loadBook_oplat(BigDecimal ff) {
ArrayList<Book_oplat> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT id_Book_oplat, Id_book_uchyot, date_opl, Sum_opl "
+ "from Book_oplat where id_book_uchyot="+ff);
grs = new ArrayList<Book_oplat>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Book_oplat
Book_oplat pr = new Book_oplat();
//Заполняем поля объекта данными из строки набора
pr.setId_book_oplat(res.getBigDecimal(1));
pr.setId_book_uchyot(res.getBigDecimal(2));
pr.setDate_opl(res.getDate(3));
pr.setSum_opl(res.getBigDecimal(4));

//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}
	//РЕДАКТИРОВАНИЕ КНИГА ОПЛАТЫ
// Выполнение оператора редактирования строки
//  Параметры:
//   dol – объект «КНИГА ОПЛАТЫ» из формы редактирования
//   key – значение ключевого реквизита
public boolean updateBook_oplat(Book_oplat dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  Получение объекта соединения
  Connection con=this.getConnection();
  //  Строка с текстом оператора (? – параметры)
  String stm ="UPDATE Book_oplat set "+"id_Book_oplat=?,"
  + "Id_book_uchyot=?,"
  + "date_opl=?,"
+ "Sum_opl=?"
  + " WHERE id_Book_oplat=?";
	try {  
	 //  Создание объекта «Оператор с параметрами»
  pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

	 pst.setBigDecimal(1, dol.getId_book_oplat());
	 pst.setBigDecimal(2, dol.getId_book_uchyot());
	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
		         dol.getDate_opl()));
			 

	 pst.setBigDecimal(4, dol.getSum_opl());
	 pst.setBigDecimal(5, key);
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
// В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ КНИГА ОПЛАТЫ
	// Выполнение оператора добавления строки
public  boolean addBook_oplat(Book_oplat dol) {
	        PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Book_oplat(id_Book_oplat,"+
 "Id_book_uchyot, date_opl, Sum_opl)"
	      + "  VALUES(?,?,?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
	            dol.setId_book_oplat(getId("book_oplat_seq"));

	       	 pst.setBigDecimal(1, dol.getId_book_oplat());
	       	 pst.setBigDecimal(2, dol.getId_book_uchyot());
	       	 pst.setDate(3,HelperConverter.convertFromJavaDateToSQLDate(
	       		         dol.getDate_opl()));
	       			 

	       	 pst.setBigDecimal(4, dol.getSum_opl());
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ КНИГА ОПЛАТЫ
	// Выполнение оператора удаления строки
public boolean deleteBook_oplat(BigDecimal kod) {
	       PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Book_oplat"
  + " WHERE id_Book_oplat=?";
try {  
//  Создание  объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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


//------------------ЗАГРУЗКА КНИГА УЧЁТА
//Получение перечня записей книги учёта из БД
//и возврат его в виде списка 
public ArrayList<Book_uchyot> loadBook_uchyot(BigDecimal ff) {
ArrayList<Book_uchyot> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
ResultSet res;
//Выполнение запроса и получение набора данных
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
//Создание объекта – список записей книги учёта
grs = new ArrayList<Book_uchyot>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Book_uchyot
Book_uchyot pr = new Book_uchyot();
//Заполняем поля объекта данными из строки набора
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


//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

	//РЕДАКТИРОВАНИЕ КНИГА УЧЁТА
//Выполнение оператора редактирования строки
//Параметры:
//dol – объект «КНИГА УЧЁТА» из формы редактирования
//key – значение ключевого реквизита
public boolean updateBook_uchyot(Book_uchyot dol, BigDecimal key) {
PreparedStatement pst = null;
	 //  Получение объекта соединения
Connection con=this.getConnection();
//Строка с текстом оператора (? – параметры)
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
	 //  Создание объекта «Оператор с параметрами»
pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

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
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
//В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
 "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ КНИГА УЧЁТА
	// Выполнение оператора добавления строки
public  boolean addBook_uchyot(Book_uchyot dol) {
	        PreparedStatement pst = null;
//Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Book_uchyot(id_Book_uchyot,"+
"Id_zayav, kod_obj, kod_usl, uch_Date, kod_sotrud, stoim, opl_Stoim)"
	      + "  VALUES(?,?,?,?,?,?,?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
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
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ КНИГА УЧЁТА
	// Выполнение оператора удаления строки
public boolean deleteBook_uchyot(BigDecimal kod) {
	       PreparedStatement pst = null;
//Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Book_uchyot"
+ " WHERE id_Book_uchyot=?";
try {  
//Создание объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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

   	
 //------------------ЗАГРУЗКА СОТРУДНИК
   // Получение перечня сотрудников из БД
   // и возврат его в виде списка 
public ArrayList<Sotrud> loadManag() {
ArrayList<Sotrud> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT Kod_sotrud, Fio, stage "
+ "from Sotrud");
//Создание объекта – список менеджеров
grs = new ArrayList<Sotrud>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Manag
	Sotrud pr = new Sotrud();
//Заполняем поля объекта данными из строки набора
pr.setKod_sotrud(res.getBigDecimal(1));
pr.setFio(res.getString(2));
pr.setStag(res.getBigDecimal(3));


//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

//------------------ЗАГРУЗКА СОТРУДНИК
// Получение перечня сотрудников из БД
// и возврат его в виде списка 
public ArrayList<Sotrud> loadManagForCmb() {
ArrayList<Sotrud> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT Kod_sotrud, Fio, stage "
+ "from Sotrud order by Fio");
//Создание объекта – список менеджеров
grs = new ArrayList<Sotrud>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Manag
	Sotrud pr = new Sotrud();
//Заполняем поля объекта данными из строки набора
pr.setKod_sotrud(res.getBigDecimal(1));
pr.setFio(res.getString(2));
pr.setStag(res.getBigDecimal(3));


//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}
	//РЕДАКТИРОВАНИЕ сотрудник
// Выполнение оператора редактирования строки
//  Параметры:
//   dol – объект «МЕНЕДЖЕР» из формы редактирования
//   key – значение ключевого реквизита
public boolean updateSotrud(Sotrud dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  Получение объекта соединения
  Connection con=this.getConnection();
  //  Строка с текстом оператора (? – параметры)
  String stm ="UPDATE sotrud set "+"Kod_sotrud=?,"
  
+ "Fio=?,"
+ "stag=?"
  + " WHERE Kod_sotrud=?";
	try {  
	 //  Создание объекта «Оператор с параметрами»
  pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

	 pst.setBigDecimal(1, dol.getKod_sotrud());
	 pst.setString(2, dol.getFio());
	 pst.setBigDecimal(3, dol.getStag());
	
	 pst.setBigDecimal(4, key);
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
// В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ СОТРУДНИК
	// Выполнение оператора добавления строки
public  boolean addSotrud(Sotrud dol) {
	        PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO sotrud(Kod_sotrud,"+
 "Fio, stag)"
	      + "  VALUES(?,?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
	          
	            pst.setBigDecimal(1, dol.getKod_sotrud());
	       	 pst.setString(2, dol.getFio());
	       	 pst.setBigDecimal(3, dol.getStag());
	     	
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ МЕНЕДЖЕР
	// Выполнение оператора удаления строки
public boolean deleteManag(BigDecimal kod) {
	       PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Manag"
  + " WHERE Kod_sotrud=?";
try {  
//  Создание объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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

//------------------ЗАГРУЗКА ОБЪЕКТ НЕДВИЖИМОСТИ
// Получение перечня Объектов недвижимости из БД
// и возврат его в виде списка 
public ArrayList<Obj_nedvij> loadObj_nedvij() {
ArrayList<Obj_nedvij> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT kod_Obj, Kod_zdan, nomer_dom, kod_street, "
+ "name_street, kod_gorod, name, area, price, Floor, vid_ned "
+ "from Obj_nedvij_v");
//Создание объекта – список объектов недв.
grs = new ArrayList<Obj_nedvij>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Obj_nedvij
Obj_nedvij pr = new Obj_nedvij();
//Заполняем поля объекта данными из строки набора
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

//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

	//РЕДАКТИРОВАНИЕ ОБЪЕКТ НЕДВИЖИМОСТИ
//Выполнение оператора редактирования строки
//Параметры:
//dol – объект «ОБЪЕКТ НЕДВИЖИМОСТИ» из формы редактирования
//key – значение ключевого реквизита
public boolean updateObj_nedvij(Obj_nedvij dol, BigDecimal key) {
PreparedStatement pst = null;
	 //  Получение объекта соединения
Connection con=this.getConnection();
//  Строка с текстом оператора (? – параметры)
String stm ="UPDATE Obj_nedvij set "+"kod_Obj=?,"
+ "Kod_zdan=?,"
+ "area=?,"
+ "price=?,"
+ "floor=?,"
+ "vid_ned=?"
+ " WHERE kod_Obj=?";
	try {  
	 //  Создание объекта «Оператор с параметрами»
pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

	 pst.setBigDecimal(1, dol.getKod_obj());
	 pst.setBigDecimal(2, dol.getKod_zdan());
	 pst.setBigDecimal(3, dol.getArea());
	 pst.setBigDecimal(4, dol.getPrice());

	 pst.setBigDecimal(5, dol.getFloor());
	 pst.setString(6, dol.getVid_ned());
	 pst.setBigDecimal(7, key);
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
//В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
    "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ ОБЪЕКТ НЕДВИЖИМОСТИ
	// Выполнение оператора добавления строки
public  boolean addObj_nedvij(Obj_nedvij dol) {
	        PreparedStatement pst = null;
//Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Obj_nedvij(kod_Obj,"+
"Kod_zdan, area, price, floor, vid_ned)"
	      + "  VALUES(?,?,?,?,?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
	          
	       	 pst.setBigDecimal(1, dol.getKod_obj());
	    	 pst.setBigDecimal(2, dol.getKod_zdan());
	    	 pst.setBigDecimal(3, dol.getArea());
	    	 pst.setBigDecimal(4, dol.getPrice());

	    	 pst.setBigDecimal(5, dol.getFloor());
	    	 pst.setString(6, dol.getVid_ned());
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ ОБЪЕКТ НЕДВИЖИМОСТИ
	// Выполнение оператора удаления строки
public boolean deleteObj_nedvij(BigDecimal kod) {
	       PreparedStatement pst = null;
//Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Obj_nedvij"
+ " WHERE kod_Obj=?";
try {  
//Создание объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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

 //------------------ЗАГРУЗКА УСЛУГА
   // Получение перечня УСЛУГ из БД
   // и возврат его в виде списка 
public ArrayList<Usluga> loadUsluga() {
ArrayList<Usluga> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT kod_usl, name_usl "
+ "from Usluga");
//Создание объекта – список УСЛУГ
grs = new ArrayList<Usluga>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Usluga
Usluga pr = new Usluga();
//Заполняем поля объекта данными из строки набора
pr.setKod_usl(res.getBigDecimal(1));
pr.setName_usl(res.getString(2));


//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}
//------------------ЗАГРУЗКА УСЛУГА
// Получение перечня УСЛУГ из БД
// и возврат его в виде списка 
public ArrayList<Usluga> loadUslugaForCmb() {
ArrayList<Usluga> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT kod_usl, name_usl "
+ "from Usluga order by name_usl");
//Создание объекта – список УСЛУГ
grs = new ArrayList<Usluga>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Usluga
Usluga pr = new Usluga();
//Заполняем поля объекта данными из строки набора
pr.setKod_usl(res.getBigDecimal(1));
pr.setName_usl(res.getString(2));


//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

	//РЕДАКТИРОВАНИЕ УСЛУГА
// Выполнение оператора редактирования строки
//  Параметры:
//   dol – объект «УСЛУГА» из формы редактирования
//   key – значение ключевого реквизита
public boolean updateUsluga(Usluga dol, BigDecimal key) {
  PreparedStatement pst = null;
	 //  Получение объекта соединения
  Connection con=this.getConnection();
  //  Строка с текстом оператора (? – параметры)
  String stm ="UPDATE Usluga set "+"kod_usl=?,"
  
+ "name_usl=?"
  + " WHERE kod_usl=?";
	try {  
	 //  Создание объекта «Оператор с параметрами»
  pst = con.prepareStatement(stm);
	 // Задаем значения параметров оператора
	 //  1- номер параметра 

	 pst.setBigDecimal(1, dol.getKod_usl());
	 pst.setString(2, dol.getName_usl());
	
	 pst.setBigDecimal(3, key);
	 // Выполнение оператора
	 pst.executeUpdate();
	 // Завершение транзакции – сохранение изменений
	 con.commit();
	} catch (SQLException ex) {
// В случае ошибки – отмена транзакции 
RollBack();
	// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
       "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ УСЛУГА
	// Выполнение оператора добавления строки
public  boolean addUsluga(Usluga dol) {
	        PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	        //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Usluga(kod_usl,"+
 "name_usl)"
	      + "  VALUES(?,?)";
try {  
	//  Создание объекта «Оператор с параметрами»
	            pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
	          
	            pst.setBigDecimal(1, dol.getKod_usl());
	       	 pst.setString(2, dol.getName_usl());
	// Выполнение оператора
	  pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
	  con.commit();
	  return true;
} catch (SQLException ex) {
	// В случае ошибки – отмена транзакции 
	  RollBack();
	  JOptionPane.showMessageDialog(null, ex.getMessage(), 
	               "Ошибка добавления данных", 
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

//УДАЛЕНИЕ УСЛУГА
	// Выполнение оператора удаления строки
public boolean deleteUsluga(BigDecimal kod) {
	       PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
	       //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Usluga"
  + " WHERE kod_usl=?";
try {  
//  Создание объекта «Оператор с параметрами»
	        pst = con.prepareStatement(stm);
	// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
	// Выполнение оператора
pst.executeUpdate();
	// Завершение транзакции – сохранение изменений
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
//------------------ЗАГРУЗКА улица
// Получение перечня улиц из БД
// и возврат его в виде списка 
public ArrayList<Street> loadStreet() {
ArrayList<Street> grs = null;
//Получение объекта соединения
Connection con = this.getConnection();
try {
//Формирование запроса к БД
Statement stmt = con.createStatement();
//Выполнение запроса и получение набора данных
ResultSet res = stmt.executeQuery(
"SELECT kod_Street, name_street, kod_gorod, name "
+ "from Street_v order by kod_street");
//Создание объекта – список ГОРОДОВ
grs = new ArrayList<Street>();
//В цикле просмотра набора данных формируем список
while (res.next()) {
//Создаем объект Street
Street pr = new Street();
//Заполняем поля объекта данными из строки набора
pr.setKod_street(res.getBigDecimal(1));
pr.setName_street(res.getString(2));
Gorod g = new Gorod ();
g.setKod_gorod(res.getBigDecimal(3));
g.setName(res.getString(4));
pr.setGorod(g);

//Добавляем объект к списку
grs.add(pr);
}
} catch (SQLException e) {
JOptionPane.showMessageDialog(null, e.getMessage(),
"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
}
//Возврат списка
return grs;
}

//РЕДАКТИРОВАНИЕ ГОРОД
// Выполнение оператора редактирования строки
//  Параметры:
//   dol – объект «ГОРОД» из формы редактирования
//   key – значение ключевого реквизита
public boolean updateStreet(Street dol, BigDecimal key) {
PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
//  Строка с текстом оператора (? – параметры)
String stm ="UPDATE Street set "+"kod_Street=?,"
+ "name_street=?"

+ " WHERE kod_Street=?";
try {  
//  Создание объекта «Оператор с параметрами»
pst = con.prepareStatement(stm);
// Задаем значения параметров оператора
//  1- номер параметра 

pst.setBigDecimal(1, dol.getKod_street());
pst.setString(2, dol.getName_street());



pst.setBigDecimal(3, key);
// Выполнение оператора
pst.executeUpdate();
// Завершение транзакции – сохранение изменений
con.commit();
} catch (SQLException ex) {
// В случае ошибки – отмена транзакции 
RollBack();
// и вывод сообщения об ошибке
JOptionPane.showMessageDialog(null, ex.getMessage(),
    "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
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


//ДОБАВЛЕНИЕ ГОРОД
// Выполнение оператора добавления строки
public  boolean addStreet(Street dol) {
       PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
       //  Строка с текстом оператора (? – параметры)
String stm ="INSERT INTO Street(kod_Street,"+
"name_street)"
     + "  VALUES(?,?)";
try {  
//  Создание объекта «Оператор с параметрами»
           pst = con.prepareStatement(stm);
// Задаем значения параметров оператора
         
        pst.setBigDecimal(1, dol.getKod_street());
        pst.setString(2, dol.getName_street());

   
// Выполнение оператора
 pst.executeUpdate();
// Завершение транзакции – сохранение изменений
 con.commit();
 return true;
} catch (SQLException ex) {
// В случае ошибки – отмена транзакции 
 RollBack();
 JOptionPane.showMessageDialog(null, ex.getMessage(), 
              "Ошибка добавления данных", 
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

//УДАЛЕНИЕ ГОРОД
// Выполнение оператора удаления строки
public boolean deleteStreet(BigDecimal kod) {
      PreparedStatement pst = null;
//  Получение объекта соединения
Connection con=this.getConnection();
      //  Строка с текстом оператора (? – параметры)
String stm ="DELETE FROM Street"
+ " WHERE kod_Street=?";
try {  
//  Создание объекта «Оператор с параметрами»
       pst = con.prepareStatement(stm);
// Задаем значения параметров оператора
pst.setBigDecimal(1, kod);
// Выполнение оператора
pst.executeUpdate();
// Завершение транзакции – сохранение изменений
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
 	// Формирование запроса к БД
 	Statement stmt = conn.createStatement();
 	// Выполнение запроса и получение набора данных
 	ResultSet res = stmt.executeQuery(
  	"SELECT opl_stoim FROM book_uchyot_v"+
 	" where id_Book_uchyot="+jyrnal_prod.getId_book_uchyot().toString());
 	// В цикле просмотра набора данных формируем список
 	while (res.next()) {
 jyrnal_prod.setOpl_stoim(res.getBigDecimal(1));
 	}
 	} catch (SQLException ex) {
	JOptionPane.showMessageDialog(null, ex.getMessage(),
  	"Ошибка получения данных",
	    	JOptionPane.ERROR_MESSAGE);
	    	}
	}

		   
}