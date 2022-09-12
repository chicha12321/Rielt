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
	// Поля класса
	  private DBManager manager;
	  private BigDecimal old_key; // прежнее значение ключа
	  // Два варианта заголовка окна
	  private final static String title_add = 
	    "Добавление новой улицы";
	  private final static String title_ed = 
	     "Редактирование улицы";
	   //  Объектная переменная для класса Подтип сырья
		private Street type = null;
	    // Флаг режима «добавление новой строки»
		private boolean isNewRow = false;
	    // Форматер для полей дат
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // Элементы (поля редактирования) для полей записи
		private JTextField edKod;
		private JTextField edName;
		private JTextField edKod_gorod;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		//  кнопки
		private JButton btnOk;
		private JButton btnCancel;
	    //  Конструктор класса
		public EdStreetDialog(Window parent, Street type,
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
		     old_key=type.getKod_street();
		     }
		  else
		     this.type = new Street();	// Новый объект
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
				if (!constructStreet())
					return;
				if (isNewRow) {
	// вызов метода менеджера Добавить новый тип. прод
					if (manager.addStreet(type)) {
					// при успехе возврат Ok и закрытие окна
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
	// вызов метода менеджера Изменить тип прод.
					if (manager.updateStreet(type, old_key)) {
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
		 Gorod gr = (Gorod) e.getItem();
		 edKod_gorod.setText(gr.getKod_gorod().toString());
		 }
		 }
		 }
		});
		// Событие потери фокуса полем ввода
		edKod_gorod.addFocusListener(new FocusListener() {
		 @Override
		 public void focusLost(FocusEvent e) {
		 // При потере фокуса изменяем элемент списка
			//Проверка на пустоту-добавлена Романом
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
		} // конец bindListeners
		  
		  
		  // Метод создания графического интерфейса
		@SuppressWarnings("rawtypes")
		private void createGui() {
			// Создание панели
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// Создание полей для редактирования данных
	edKod = new JTextField(10);
			edName = new JTextField(40);
			edKod_gorod = new JTextField(10);
			cmbgor = new JComboBox ();
			//  Создание кнопок
			btnOk = new JButton("Сохранить");
			btnCancel = new JButton("Отмена");
			// Добавление элементов на панель
			pnl.add(new JLabel("Код"));
			pnl.add(edKod,"span");
			pnl.add(new JLabel("Наименование"));
			pnl.add(edName,"span");
			pnl.add(new JLabel("Город"));
			pnl.add(edKod_gorod, "split 2");
			pnl.add(cmbgor, "growx, wrap");
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
		@SuppressWarnings("unchecked")
		private void loadData() {
		  if (!isNewRow){
				edKod.setText(type.getKod_street().toString());
			edName.setText(type.getName_street());
			edKod_gorod.setText(type.getGorod().getKod_gorod() == null ?
					"" : type.getGorod().getKod_gorod().toString());
	
		  }
		// Создание списка
		// Загружаем данные в список
	
		ArrayList<Gorod> lst = manager.loadGorodForCmb();
		if (lst != null) {
		 // Создание модели данных на базе списка
		 @SuppressWarnings({ "rawtypes" })
		DefaultComboBoxModel model =
		new DefaultComboBoxModel(lst.toArray());
		 // Установка модели для JComboBox
		 cmbgor.setModel(model);
		 // Определение поля внешнего ключа
		 BigDecimal grKod = (isNewRow? null :
		type.getGorod().getKod_gorod());
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
		 if (((Gorod) model.getElementAt(i)).
		getKod_gorod().equals(grKod)) {
		 cmbgor.setSelectedIndex(i);
		 break;
		 }
		} 
		//Формирование объекта Тип прод. перед сохранением
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
	            ex.getMessage(), "Ошибка данных",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// Возврат объекта Тип прод.
		public Street getStreet()
		{
			return type;
		}
} 
