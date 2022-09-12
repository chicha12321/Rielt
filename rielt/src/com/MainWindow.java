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
// Поля класса
  private DBManager manager;
  JMenuBar mainMenu; // Контейнер меню
  JMenuItem miExit; // Пункт меню Выход
  JMenu mSprav; // Подменю Справочники
  JMenuItem miGorod; // Пункт меню Город
  JMenuItem miZdan; // Пункт меню Здание
  JMenuItem miStreet; // Пункт меню Улица
  JMenuItem miObj_nedvij; // Пункт меню Объект недвижимости
  JMenuItem miUsluga; // Пункт меню Услуга
  JMenuItem miManag; // Пункт меню 
  JMenuItem miBook_oplat; // Пункт меню и
  JMenuItem miBook_uchyot; // Пункт меню 
  JMenuItem miZayav; // Пункт меню Заявка
  JMenuItem miKlient; // Пункт меню 

 
  
  
  JMenu mKn; // Подменю Книги
  //  Конструктор класса
  public MainWindow(DBManager manager) {
    this.manager = manager;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 // Установка текущей схемы
    manager.setPath(); 
    // Тест соединения
    testConn();
    //  Создание графического интерфейса окна
    createGUI();
    //  Назначение окну слушателей
    bindListeners();
    //  Пакуем окно
    pack();
    //  Заголовок окна
  
    setTitle("Автоматизация учета риелторских услуг в агентстве недвижимости.");
    // Вывод окна по центру экрана 
    setLocationRelativeTo(null);
  }
  
  private JMenuBar createMenu(){
	  // Создаем строку основного меню
	  mainMenu = new JMenuBar();
	  // Создаем подменю и пункты меню
	  mSprav = new JMenu("Справочники");
	  miExit = new JMenuItem("Выход");
	  miGorod= new JMenuItem("Город");
	  miZdan= new JMenuItem("Здание");
	  miStreet= new JMenuItem("Улица");
	  miBook_oplat= new JMenuItem("Книга оплат");
	  miManag= new JMenuItem("Менеджер");
	  miBook_uchyot= new JMenuItem("Книга учёта");
	  miZayav= new JMenuItem("Заявка");
	  miObj_nedvij= new JMenuItem("Объект недвижимости");
	  miUsluga= new JMenuItem("Услуга");
	  miKlient = new JMenuItem("Клиент");
	  
	  
	  mKn = new JMenu("Книги"); 
	  
	  // Формируем меню
	  mSprav.add(miGorod);
	  mSprav.add(miStreet);
	  mSprav.add(miZdan);
	  mKn.add(miZayav);
	  mKn.add(miBook_uchyot);
		 
	//  mKn.add(miBook_oplat);

	  mSprav.add(miObj_nedvij);
	  mSprav.add(miUsluga);
	  
	 
	  mSprav.addSeparator(); // линия-разделитель
	  mSprav.add(miManag);
	  
	  mSprav.add(miKlient);
	 
	  mainMenu.add(mSprav);
	  mainMenu.add(mKn);

	  mainMenu.add(miExit);
	  return mainMenu;
	  }
  //  Назначение элементам окна слушателей
  private void bindListeners() {
   // Сюда будут добавлены методы для активных элементов окна
	// Выход из приложения
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
  
  //показывать Клиентов
  protected void ShowKlient() {
	 FrmKlient frm = new FrmKlient(manager);
	  frm.setVisible(true);
	  } 
 
 //показывать заявку
  protected void ShowZayav() {
	  FrmZayav frm = new FrmZayav(manager);
	  frm.setVisible(true);
	  } 
  
 
  //показывать улицы
  protected void ShowStreet() {
	  FrmStreet frm = new FrmStreet(manager);
	  frm.setVisible(true);
	  } 
//показывать менеджеров
  protected void ShowManag() {
	  FrmManag frm = new FrmManag(manager);
	  frm.setVisible(true);
	  } 
  
 //показывать услуги
  protected void ShowUsluga() {
	 FrmUsluga frm = new FrmUsluga(manager);
	  frm.setVisible(true);
	  } 
  //показывать книгу учёта
  protected void ShowBook_uchyot() {
	  FrmBook_uchyot frm = new FrmBook_uchyot(manager);
	  frm.setVisible(true);
	  } 
  //показывать объект недвижимости
  protected void ShowObj_nedvij() {
	  FrmObj_nedvij frm = new FrmObj_nedvij(manager);
	  frm.setVisible(true);
	  } 
//показывать город
  protected void ShowGorod() {
	  FrmGorod frm = new FrmGorod(manager);
  frm.setVisible(true);
	  } 
//показывать здание
  protected void ShowZdan() {
	  FrmZdan frm = new FrmZdan(manager);
	  frm.setVisible(true);
	  } 

  
  // Метод тестирования соединения
  private void testConn() {
     // Вызов метода менеджера для получения версии PG
     String ver = manager.getVersion();
     // Вывод результата в консоль
     System.out.println(ver);
  }
    //  Создание графического интерфейса окна
  private void createGUI() {
 // Создаем панель
 JPanel pnlImg = new JPanel(new FlowLayout());
 // Размещаем на панели изображение
 // Изображение находится в папке src/images
 //    и в папке  bin\images
    URL url = this.getClass().getResource("/images/112.jpg");
    BufferedImage wPic=null;
    try {
       wPic = ImageIO.read(url);
    } catch (IOException e) {
       e.printStackTrace();
    }
    // Изображение размещаем на метке
    JLabel wIcon = new JLabel(new ImageIcon(wPic));
    // Метку добавляем на панель
    pnlImg.add(wIcon);
    // Задаем менеджер расположения основного окна
    // Метка заполняет все пространство окна
    getContentPane().setLayout(new MigLayout("insets 0 2 0 2, gapy 0",
		"[grow, fill]", "[grow, fill]"));
   // Панель размещаем на фрейме
getContentPane().add(pnlImg, "grow");
  //  getContentPane().add(pnlImg);
setJMenuBar(createMenu()); 
 }

	public void initialize() {
		Locale.setDefault(new Locale("ru"));
	}
}
