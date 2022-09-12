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

public class EdBook_oplatDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// Поля класса
	  private DBManager manager;
	  private BigDecimal old_key, fk_key; // прежнее значение ключа
	  // Два варианта заголовка окна
	  private final static String title_add = 
	    "Добавление новой записи";
	  private final static String title_ed = 
	     "Редактирование записи";
	   //  Объектная переменная для класса Книга оплат
		private Book_oplat type = null;
	    // Флаг режима «добавление новой строки»
		private boolean isNewRow = false;
	    // Форматер для полей дат
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // Элементы (поля редактирования) для полей записи
		private JTextField edCod;
		private JTextField edId_book_uchyot;
		private JTextField edDate_zaver;
		private JTextField edSum_opl;
		//  кнопки
		private JButton btnOk;
		private JButton btnCancel;
	    //  Конструктор класса
		public EdBook_oplatDialog(Window parent,Book_oplat type,
	                    DBManager manager, BigDecimal fk_key) {
	      this.manager = manager;
	  // Установка флага для режима добавления новой строки
		  isNewRow = type == null ? true : false;
		  // Определение заголовка для операций добавл./редакт.
		  setTitle(isNewRow ? title_add : title_ed);
		     this.fk_key=fk_key;
		  // Определение объекта редактируемой строки  
		  if (!isNewRow) {
		     this.type = type; // Существующий объект
		
		     // Сохранение прежнего значения ключа
		     old_key=type.getId_book_oplat();
		     }
		  else
		     this.type = new Book_oplat();	// Новый объект
		  // Создание графического интерфейса окна
		  createGui();
		  // Назначение обработчиков для основных событий
		  bindListeners();
		  //  Получение данных
		  loadData();
		  pack();
		  // Задание режима неизменяемых размеров окна
		  setResizable(false);
		  setLocationRelativeTo(parent);
		}
		//Метод назначения обработчиков основных событий
		  private void bindListeners() {
		   //  Обработчик нажатия клавиш 
		   setKeyListener(this, new KeyAdapter(){
		    @Override
	        // Обработка нажатия клавиши ESC – закрытие окна 
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
	      // Обработка нажатия кнопки закрытия окна  
		  addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e)
			{
				close();
			}
		  });
	      //  Обработка  кнопки «Отмена»
		  btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
			  // Возврат Cancel и закрытие окна
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		  });
	      //  Обработка  кнопки «Сохранить»
		  btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
			  // Проверка данных, выход 
	  //   при неправильном заполнении полей
				if (!constructBook_oplat())
					return;
				if (isNewRow) {
	// вызов метода менеджера Добавить новую оплату
					if (manager.addBook_oplat(type)) {
					// при успехе возврат Ok и закрытие окна
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// вызов метода менеджера Изменить строку
					if (manager.updateBook_oplat(type, old_key)) {
					setDialogResult(JDialogResult.OK);
					close();
				}
				}
		  });
		}
		  // Метод создания графического интерфейса
		private void createGui() {
			// Создание панели
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// Создание полей для редактирования данных
//edCod = new JTextField(10);
	edId_book_uchyot = new JTextField(10);
					edSum_opl = new JTextField(25);
			edDate_zaver = new JFormattedTextField(
	                createFormatter("##-##-####"));
			edDate_zaver.setColumns(10);
			//  Создание кнопок
			btnOk = new JButton("Сохранить");
			btnCancel = new JButton("Отмена");
			// Добавление элементов на панель
		//	pnl.add(new JLabel("ИД оплаты"));
			//pnl.add(edCod,"span");
		
		
			pnl.add(new JLabel("Сумма оплаты"));
			pnl.add(edSum_opl,"span");
			pnl.add(new JLabel("Дата оплаты"));
			pnl.add(edDate_zaver,"span");
			pnl.add(btnOk, "span, split 2, center, sg ");
			pnl.add(btnCancel, "sg 1");
			//  Добавление панели в окно фрейма
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(pnl, BorderLayout.CENTER);
		}
		// Метод формирования маски ввода даты
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
		//  Метод добавление слушателя клавиатуры 
	//к компонентам окна
		private void setKeyListener(Component c, KeyListener kl)
		{
		  c.addKeyListener(kl);
		  if (c instanceof Container)
		    for (Component comp:((Container)c).getComponents())
			 setKeyListener(comp, kl);
		}
	    // Метод инициализации полей формы (при редактировании)
		private void loadData() {
		  if (!isNewRow){
				
				edCod.setText(type.getId_book_oplat().toString());
			
			
			edDate_zaver.setText(type.getDate_opl()==null? 
					   "":frmt.format(type.getDate_opl()));
			edSum_opl.setText(type.getSum_opl().toString());
			}
		}
		//Формирование объекта Кн. оплат перед сохранением
		private boolean constructBook_oplat()	{
		  try {
	//			type.setId_book_oplat(edCod.getText().equals("") ? 
//	null : new BigDecimal(edCod.getText()));
				type.setId_book_uchyot(fk_key);
				
			type.setSum_opl(edSum_opl.getText().equals("") ? 
						null : new BigDecimal(edSum_opl.getText()));
				type.setDate_opl(edDate_zaver.getText().substring(0,
					      1).trim().equals("") ? null : 
					      frmt.parse(edDate_zaver.getText()));
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "Ошибка данных",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// Возврат объекта Книга оплат
		public Book_oplat getBook_oplat()
		{
			return type;
		}
}

