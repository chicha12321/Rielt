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
	// Поля класса
	  private DBManager manager;
	  private BigDecimal old_key; // прежнее значение ключа
	  // Два варианта заголовка окна
	  private final static String title_add = 
	    "Добавление нового объекта недвижимости";
	  private final static String title_ed = 
	     "Редактирование объекта недвижимости";
	   //  Объектная переменная для класса Объект недв.
		private Obj_nedvij type = null;
	    // Флаг режима «добавление новой строки»
		private boolean isNewRow = false;
	    // Форматер для полей дат
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // Элементы (поля редактирования) для полей записи
		private JTextField edCod;
		private JTextField edKod_zdan;
		private JTextField edPrice;
		private JTextField edArea;

		private JTextField edFloor;
		private JComboBox edVid_ned;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		//  кнопки
		private JButton btnOk;
		private JButton btnCancel;
	    //  Конструктор класса
		public EdObj_nedvijDialog(Window parent,Obj_nedvij type,
	                    DBManager manager) {
	      this.manager = manager;
	  // Установка флага для режима добавления новой строки
		  isNewRow = type == null ? true : false;
		  // Определение заголовка для операций добавл./редакт.
		  setTitle(isNewRow ? title_add : title_ed);
		  // Определение объекта редактируемой строки  
		  if (!isNewRow) {
		     this.type = type; // Существующий объект
		     // Сохранение прежнего значения ключа
		     old_key=type.getKod_obj();
		     }
		  else
		     this.type = new Obj_nedvij();	// Новый объект
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
				if (!constructObj_nedvij())
					return;
				if (isNewRow) {
	// вызов метода менеджера Добавить новый объект недв.
					if (manager.addObj_nedvij(type)) {
					// при успехе возврат Ok и закрытие окна
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// вызов метода менеджера Изменить строку
					if (manager.updateObj_nedvij(type, old_key)) {
					setDialogResult(JDialogResult.OK);
					close();
				}
				}
		  });
			// Событие выбора элемента списка
			cmbgor.addItemListener(new ItemListener() {
			 @Override
			 public void itemStateChanged(ItemEvent e) {
			 // установка значения поля внешнего ключа
			 if (e.getStateChange() == ItemEvent.SELECTED) {
			 if (e.getItem() != null) {
			 Zdan gr = (Zdan) e.getItem();
			 edKod_zdan.setText(gr.getKod_zdan().toString());
			 }
			 }
			 }
			});
			// Событие потери фокуса полем ввода
			edKod_zdan.addFocusListener(new FocusListener() {
			 @Override
			 public void focusLost(FocusEvent e) {
			 // При потере фокуса изменяем элемент списка
				//Проверка на пустоту-добавлена Романом
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
		  // Метод создания графического интерфейса
		private void createGui() {
			// Создание панели
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// Создание полей для редактирования данных
	edCod = new JTextField(10);
	edKod_zdan = new JTextField(10);
			edPrice = new JTextField(20);
			edArea = new JTextField(20);
			edFloor = new JTextField(20);
			cmbgor = new JComboBox <> ();
			edVid_ned = new JComboBox <> (new String[]
			        {"","квартира","апартаменты","частный дом","помещение"}); 
			//  Создание кнопок
			btnOk = new JButton("Сохранить");
			btnCancel = new JButton("Отмена");
			// Добавление элементов на панель
			pnl.add(new JLabel("Код объекта"));
			pnl.add(edCod,"span");
			
			pnl.add(new JLabel("Здание"));
			pnl.add(edKod_zdan, "split 2");
			pnl.add(cmbgor, "growx, wrap");
			pnl.add(new JLabel("Цена"));
			pnl.add(edPrice,"span");
			pnl.add(new JLabel("Площадь"));
			pnl.add(edArea,"span");
			pnl.add(new JLabel("Этаж"));
			pnl.add(edFloor,"span");
			pnl.add(new JLabel("Вид недвижимости"));
			pnl.add(edVid_ned,"span");
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
				edArea.setText(type.getArea().toString());
				edCod.setText(type.getKod_obj().toString());
				edKod_zdan.setText(type.getZdan().getKod_zdan().toString());
			edPrice.setText(type.getPrice().toString());
			edVid_ned.setSelectedItem(type.getVid_ned());

			edFloor.setText(type.getFloor().toString());
			}
		// Создание списка
					// Загружаем данные в список
				
					ArrayList<Zdan> lst = manager.loadZdan();
					if (lst != null) {
					 // Создание модели данных на базе списка
					 @SuppressWarnings({ "rawtypes", "unchecked" })
					DefaultComboBoxModel model =
					new DefaultComboBoxModel(lst.toArray());
					 // Установка модели для JComboBox
					 cmbgor.setModel(model);
					 // Определение поля внешнего ключа
					 BigDecimal grKod = (isNewRow? null :
					type.getZdan().getKod_zdan());
					 // Вызов метода установки элемента списка
					 // соответствующего значению внешнего ключа
					 setCmbItem(model, grKod);
					}
					} 
					//Создадим метод установки элемента поля списка
					// Установка элемента списка
					private void setCmbItem(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
					 BigDecimal grKod) {
					cmbgor.setSelectedItem(null);
					if (grKod != null)
					 // Просмотр элементов списка для нахождения элемента
					 // с заданным кодом
					 for (int i = 0, c = model.getSize(); i < c; i++)
					 if (((Zdan) model.getElementAt(i)).
					getKod_zdan().equals(grKod)) {
					 cmbgor.setSelectedIndex(i);
					 break;
					 }
					} 
		//Формирование объекта Объект недвиж. перед сохранением
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
	            ex.getMessage(), "Ошибка данных",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// Возврат объекта Объект недв.
		public Obj_nedvij getObj_nedvij()
		{
			return type;
		}
}
