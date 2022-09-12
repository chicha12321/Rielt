package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.MaskFormatter;
import com.data.*;

import net.miginfocom.swing.MigLayout;

public class EdBook_uchyotDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// Поля класса
	  private DBManager manager;
	  private BigDecimal fk_key;
	  private BigDecimal old_key; // прежнее значение ключа
	  // Два варианта заголовка окна
	  private final static String title_add = 
	    "Добавление новой услуги";
	  private final static String title_ed = 
	     "Редактирование услуги";
	   //  Объектная переменная для класса Книга учёта
		private Book_uchyot type = null;
	    // Флаг режима «добавление новой строки»
		private boolean isNewRow = false;
	    // Форматер для полей дат
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // Элементы (поля редактирования) для полей записи
		private JTextField edCod;
		
		private JTextField edCod_sotrud;
		private JTextField edOpl_stoim;
		private JTextField edDate_zaver;
		private JTextField edStoim, edKod_obj, edKod_usl;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbgor;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbSotr, cmbUsl;
		//  кнопки
		private JButton btnOk;
		private JButton btnCancel;
		//ДЛЯ ПОДЧ. ТАБЛИЦЫ
		 private JTable tblBook_oplat;
		 
			private JButton btnClose;
			// кнопка редактирования 
				private JButton btnEdit;
				// кнопка добавления 
				private JButton btnNew;
				// кнопка удаления 
				private JButton btnDelete;
				//табл модель
			private TKTableModel tblModel;
			 private ArrayList<Book_oplat> kls;
	    //  Конструктор класса
		public EdBook_uchyotDialog(Window parent,Book_uchyot type,
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
		     old_key=type.getId_book_uchyot();
		
		     loadDataGruz();
		     }
		  else {
		     this.type = new Book_uchyot();	// Новый объект
		     kls=new ArrayList <Book_oplat>();
		  }
		  // Создание графического интерфейса окна
		  createGui();
		  // Назначение обработчиков для основных событий
		  bindListeners();
		  //  Получение данных
		  loadData();
		  pack();
		  // Задание режима неизменяемых размеров окна
		  setResizable(false);
		  setButton(); // Настройка отображения кнопок формы
		  setLocationRelativeTo(parent);
		}
		// Метод настройки кнопок формы
		private void setButton() {
		if (btnOk.getText().equals("Сохранить")) {
		btnCancel.setText("Отмена");
		btnNew.setEnabled(false);
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);
		} else {
		btnCancel.setText("Выход");
		btnNew.setEnabled(true);
		btnEdit.setEnabled(true);
		btnDelete.setEnabled(true);
		}
		}
		 // Получение данных в таблицу Груз
		private void loadDataGruz() {
		 // Получение данных через менеджер
		 kls=manager.loadBook_oplat(type.getId_book_uchyot());
	//		sost_recipes=manager.loadSost_recipe();
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
		  btnOk.addActionListener(new ActionListener() {
			  @Override
			  public void actionPerformed(ActionEvent e) {
			  if (((AbstractButton) e.getSource()).getText().equals("Сохранить")) {
			  // Проверка данных, возврат Ok и закрытие окна
			  if (!constructBook_uchyot()) return;
			  //новая версия построения 31.03.2021
			  else {if (isNewRow)
			  			{
				  		if (manager.addBook_uchyot(type)==true)
				  			{
				  					setDialogResult(JDialogResult.OK);
				  					((AbstractButton) e.getSource()).setText("Редактировать");
				  					setButton();
				  			}
			  			}
			  else 
				  if (manager.updateBook_uchyot(type, old_key)==true)
				  {
					  setDialogResult(JDialogResult.OK);
				  ((AbstractButton) e.getSource()).setText("Редактировать");
				  setButton();
				  }	} 
			  }
			  else {
			  ((AbstractButton) e.getSource()).setText("Сохранить");
			  setButton();
			  }
			  }
			  });
		  // Для кнопки Закрыть
			 btnClose.addActionListener(new ActionListener() {
			 @Override
			 public void actionPerformed(ActionEvent e) {
			 // Закрываем окно
			 dispose();
			  }
			  });
			//  Для кнопки добавления
				btnNew.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						addBook_oplat();
					}
				});
				//  Для кнопки редактирования
				btnEdit.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						editBook_oplat();
					}
				});
				//  Для кнопки удаления
				btnDelete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
					deleteBook_oplat();
					}
				});
				// Событие выбора элемента списка
				cmbgor.addItemListener(new ItemListener() {
				 @Override
				 public void itemStateChanged(ItemEvent e) {
				 // установка значения поля внешнего ключа
				 if (e.getStateChange() == ItemEvent.SELECTED) {
				 if (e.getItem() != null) {
				 Obj_nedvij gr = (Obj_nedvij) e.getItem();
				 edKod_obj.setText(gr.getKod_obj().toString());
				 }
				 }
				 }
				});
				// Событие потери фокуса полем ввода
				edKod_obj.addFocusListener(new FocusListener() {
				 @Override
				 public void focusLost(FocusEvent e) {
				 // При потере фокуса изменяем элемент списка
					//Проверка на пустоту-добавлена Романом
					 if(!(edKod_obj.getText().isEmpty())) {
				 @SuppressWarnings("rawtypes")
				DefaultComboBoxModel model =
				(DefaultComboBoxModel) cmbgor.getModel();
				 BigDecimal grKod =
				new BigDecimal(edKod_obj.getText());
				 setCmbItem(model, grKod);
				 }
				 }
				 @Override
				 public void focusGained(FocusEvent e) {
				 }
				});
				// Событие выбора элемента списка
				  cmbSotr.addItemListener(new ItemListener() {
				  @Override
				  public void itemStateChanged(ItemEvent e) {
				  // установка значения поля внешнего ключа
				  if (e.getStateChange() == ItemEvent.SELECTED) {
				  if (e.getItem() != null) {
				  Sotrud c4 = (Sotrud) e.getItem();
				  edCod_sotrud.setText(c4.getKod_sotrud().toString());
				  }
				  }
				  }
				  });
				  // Событие потери фокуса полем ввода
				  edCod_sotrud.addFocusListener(new FocusListener() {
				  @Override
				  public void focusLost(FocusEvent e) {
				  // При потере фокуса изменяем элемент списка
						//Проверка на пустоту-добавлена Романом
						 if(!(edCod_sotrud.getText().isEmpty())) {
				  @SuppressWarnings("rawtypes")
				DefaultComboBoxModel model5 =
				  (DefaultComboBoxModel) cmbSotr.getModel();
				  BigDecimal grKod =
				  new BigDecimal(edCod_sotrud.getText());
				  setCmbItem6(model5, grKod);
				  }
				  }
				  
				  
				  @Override
				  public void focusGained(FocusEvent e) {
				  }
				  });
				// Событие выбора элемента списка
				  cmbUsl.addItemListener(new ItemListener() {
				  @Override
				  public void itemStateChanged(ItemEvent e) {
				  // установка значения поля внешнего ключа
				  if (e.getStateChange() == ItemEvent.SELECTED) {
				  if (e.getItem() != null) {
				  Usluga c2 = (Usluga) e.getItem();
				  edKod_usl.setText(c2.getKod_usl().toString());
				  }
				  }
				  }
				  });
				  // Событие потери фокуса полем ввода
				  edKod_usl.addFocusListener(new FocusListener() {
				  @Override
				  public void focusLost(FocusEvent e) {
				  // При потере фокуса изменяем элемент списка
						//Проверка на пустоту-добавлена Романом
						 if(!(edKod_usl.getText().isEmpty())) {
				  @SuppressWarnings("rawtypes")
				DefaultComboBoxModel model5 =
				  (DefaultComboBoxModel) cmbUsl.getModel();
				  BigDecimal grKod =
				  new BigDecimal(edKod_usl.getText());
				  setCmbItem2(model5, grKod);
				  }
				  }
				  
				  
				  @Override
				  public void focusGained(FocusEvent e) {
				  }
				  });
				} // конец 
		
		  // Метод создания графического интерфейса
		private void createGui() {
			// Создание панели
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// Создание полей для редактирования данных
	edCod = new JTextField(10);
	
	cmbgor =new JComboBox();
	cmbSotr = new JComboBox();
	cmbUsl = new JComboBox();
			edCod_sotrud = new JTextField(10);
			edKod_obj = new JTextField(10);
			
			edKod_usl = new JTextField(10);
			edOpl_stoim = new JTextField(50);
			edStoim = new JTextField(40);
			edDate_zaver = new JFormattedTextField(
	                createFormatter("##-##-####"));
			edDate_zaver.setColumns(10);
			//  Создание кнопок
			btnOk = new JButton("Сохранить");
			btnCancel = new JButton("Отмена");
			// Добавление элементов на панель
			//pnl.add(new JLabel("ИД книги учёта"));
		//	pnl.add(edCod,"span");
			//pnl.add(new JLabel("ИД заявки"));
		//	pnl.add(edId_zayav,"span");
			pnl.add(new JLabel("Объект"));
			pnl.add(edKod_obj, "split 2");
			pnl.add(cmbgor, "growx, wrap");
			pnl.add(new JLabel("Услуга"));
		
			pnl.add(edKod_usl, "split 2");
			pnl.add(cmbUsl, "growx, wrap");
			pnl.add(new JLabel("Дата услуги"));
			pnl.add(edDate_zaver,"span");
		
			pnl.add(new JLabel("Сотрудник"));
			pnl.add(edCod_sotrud, "split 2");
			pnl.add(cmbSotr, "growx, wrap");
			pnl.add(new JLabel("Стоимость"));
			pnl.add(edStoim,"span");
			pnl.add(new JLabel("Оплаченная стоимость"));
			pnl.add(edOpl_stoim,"span");
			pnl.add(btnOk, "span, split 2, center, sg ");
			pnl.add(btnCancel, "sg 1");
			//  Добавление панели в окно фрейма
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(pnl, BorderLayout.NORTH);
			
			 // Создание панели
			 JPanel pnfl = new JPanel(new MigLayout("insets 3, gapy 4",
			//	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
			 "[grow, fill]", "[]5[grow, fill]10[]"));
			 // Создание объекта таблицы
			 tblBook_oplat = new JTable();
			 // Создание объекта табличной модели на базе
			 // сформированного списка
			 tblBook_oplat.setModel(tblModel = new TKTableModel(kls));
			 // Создание объекта сортировки для табличной модели
			 RowSorter<TKTableModel> sorter = new
			 TableRowSorter<TKTableModel>(tblModel);
			 // Назначение объекта сортировки таблице
			 tblBook_oplat.setRowSorter(sorter);
			 // Задаем параметры внешнего вида таблицы
			 // Выделение полосой всей текущей строки
			 tblBook_oplat.setRowSelectionAllowed(true);
			 // Задаем промежутки между ячейками
			 tblBook_oplat.setIntercellSpacing(new Dimension(0, 1)); 
			 // Задаем цвет сетки
			 tblBook_oplat.setGridColor(new Color(170, 170, 255).darker());
			 // Автоматическое определение ширины последней колонки
			 tblBook_oplat.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			 // Возможность выделения только 1 строки
			 tblBook_oplat.getSelectionModel().setSelectionMode(
			 ListSelectionModel.SINGLE_SELECTION);
			 // Создание области прокрутки и вставка в нее таблицы
			 JScrollPane scrlPane = new JScrollPane(tblBook_oplat);
			 scrlPane.getViewport().setBackground(Color.white);
			 scrlPane.setBorder(BorderFactory.createCompoundBorder
			(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
			 tblBook_oplat.getColumnModel().getColumn(0).setMaxWidth(100);
			 tblBook_oplat.getColumnModel().getColumn(0).setMinWidth(100);
			 // Создание кнопки для закрытия формы
			 btnClose = new JButton("Закрыть");
			 //Добавление на панель: метки, области с таблицей и кнопки
			 pnfl.add(scrlPane, "grow, span");
			 pnfl.add(btnClose, "growx 0, right");
			 // Добавление панели в окно
			 
			 pnfl.add(getToolBar(),"growx,wrap");//Создание  панели
//			 pnl.add(new JLabel("Справочник видов техники:"), "growx,span");
			 	pnfl.add(scrlPane, "grow, span");
			 	pnfl.add(btnClose, "growx 0, right");
			 	 getContentPane().add(pnfl, BorderLayout.CENTER);
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
				edOpl_stoim.setText(type.getOpl_stoim().toString());
				edCod.setText(type.getId_book_uchyot().toString());
		//		edId_zayav.setText(type.getId_zayav().toString());
			edCod_sotrud.setText(type.getSotrud().getKod_sotrud().toString());
			edKod_obj.setText(type.getObj_nedvij().getKod_obj().toString());
			edKod_usl.setText(type.getUsluga().getKod_usl().toString());
			edDate_zaver.setText(type.getUch_date()==null? 
					   "":frmt.format(type.getUch_date()));
			edStoim.setText(type.getStoim().toString());
			}
		  else 
		  {
			  edOpl_stoim.setText("0");
			 
		  }
		  ArrayList<Sotrud> lst6 = manager.loadManagForCmb();
			if (lst6 != null) {
			// Создание модели данных на базе списка
			@SuppressWarnings({ "rawtypes" })
			DefaultComboBoxModel model5 =
			new DefaultComboBoxModel(lst6.toArray());
			// Установка модели для JComboBox
			cmbSotr.setModel(model5);
			BigDecimal grKod;
			// Определение поля внешнего ключа
			 //НОВАЯ ВЕРСИЯ ОПРЕДЕЛЕНИЯ ПОЛЯ ВНЕШНЕГО КЛЮЧА 10.02.2021
			 grKod = (isNewRow? null :
				 type.getSotrud()==null? null :
					 type.getSotrud().getKod_sotrud());
			// Вызов метода установки элемента списка
			// соответствующего значению внешнего ключа
			setCmbItem6(model5, grKod);
			}
		// Создание списка
			// Загружаем данные в список
		
			ArrayList<Obj_nedvij> lst = manager.loadObj_nedvij();
			if (lst != null) {
			 // Создание модели данных на базе списка
			 @SuppressWarnings({ "rawtypes", "unchecked" })
			DefaultComboBoxModel model =
			new DefaultComboBoxModel(lst.toArray());
			 // Установка модели для JComboBox
			 cmbgor.setModel(model);
			 // Определение поля внешнего ключа
			 BigDecimal grKod = (isNewRow? null :
			type.getObj_nedvij().getKod_obj());
			 // Вызов метода установки элемента списка
			 // соответствующего значению внешнего ключа
			 setCmbItem(model, grKod);
			}
			ArrayList<Usluga> lst2 = manager.loadUslugaForCmb();
			if (lst2 != null) {
			 // Создание модели данных на базе списка
			 @SuppressWarnings({ "rawtypes", "unchecked" })
			DefaultComboBoxModel model =
			new DefaultComboBoxModel(lst2.toArray());
			 // Установка модели для JComboBox
			 cmbUsl.setModel(model);
			 // Определение поля внешнего ключа
			 BigDecimal grKod = (isNewRow? null :
			type.getUsluga().getKod_usl());
			 // Вызов метода установки элемента списка
			 // соответствующего значению внешнего ключа
			 setCmbItem2(model, grKod);
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
			 if (((Obj_nedvij) model.getElementAt(i)).
			getKod_obj().equals(grKod)) {
			 cmbgor.setSelectedIndex(i);
			 break;
			 }
			} 
			// Установка элемента списка
			private void setCmbItem6(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
			BigDecimal grKod) {
			cmbSotr.setSelectedItem(null);
			if (grKod != null)
			// Просмотр элементов списка для нахождения элемента
			// с заданным кодом
			for (int i = 0, c = model.getSize(); i < c; i++)
			if (((Sotrud) model.getElementAt(i)).
			getKod_sotrud().equals(grKod)) {
				cmbSotr.setSelectedIndex(i);
				break;
				}
			}
			// Установка элемента списка
						private void setCmbItem2(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
						BigDecimal grKod) {
						cmbUsl.setSelectedItem(null);
						if (grKod != null)
						// Просмотр элементов списка для нахождения элемента
						// с заданным кодом
						for (int i = 0, c = model.getSize(); i < c; i++)
						if (((Usluga) model.getElementAt(i)).
						getKod_usl().equals(grKod)) {
							cmbUsl.setSelectedIndex(i);
							break;
							}
						}
		//Формирование объекта Книга учёта перед сохранением
		private boolean constructBook_uchyot()	{
		  try {
			//System.out.print(fk_key);
				type.setId_zayav(fk_key);
				type.setOpl_stoim(edOpl_stoim.getText().equals("") ? 
						null : new BigDecimal(edOpl_stoim.getText()));
				
				type.setKod_usl(edKod_usl.getText().equals("") ? 
						null : new BigDecimal(edKod_usl.getText()));
				type.setKod_obj(edKod_obj.getText().equals("") ? 
						null : new BigDecimal(edKod_obj.getText()));
			type.setStoim(edStoim.getText().equals("") ? 
						null : new BigDecimal(edStoim.getText()));
				type.setUch_date(edDate_zaver.getText().substring(0,
					      1).trim().equals("") ? null : 
					      frmt.parse(edDate_zaver.getText()));
				Object obj = cmbgor.getSelectedItem();
				 Obj_nedvij gr = (Obj_nedvij) obj;
				 type.setKod_obj(gr.getKod_obj());
					Object obj2 = cmbSotr.getSelectedItem();
					Sotrud gr2 = (Sotrud) obj2;
					type.setSotrud(gr2);
					Object obj3 = cmbUsl.getSelectedItem();
					Usluga gr3 = (Usluga) obj3;
					type.setUsluga(gr3);
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "Ошибка данных",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// Возврат объекта Книга учёта
		public Book_uchyot getBook_uchyot()
		{
			return type;
		}
	
		 
		
		 private JToolBar getToolBar() {
		     // Создание панели
			JToolBar res = new JToolBar();
			// Неизменяемое положение панели
			res.setFloatable(false);
			// Добавление кнопки «Добавить»
			// Определение местоположения изображения для кнопки
			URL url = FrmBook_uchyot.class.getResource("/images/add.png");
			// Создание кнопки с изображением
			btnNew = new JButton(new ImageIcon(url));
			// На кнопку не устанавливается фокус
			btnNew.setFocusable(false);
			//  Добавление всплывающей подсказки для кнопки
			btnNew.setToolTipText("Добавить новую оплату");
			// Добавление кнопки «Удалить»
			url = FrmBook_uchyot.class.getResource("/images/delete.png");
			btnDelete = new JButton(new ImageIcon(url));
			btnDelete.setFocusable(false);
			btnDelete.setToolTipText("Удалить оплату");
			// Добавление кнопки «Редактировать»
			url = FrmBook_uchyot.class.getResource("/images/edit.png");
			btnEdit = new JButton(new ImageIcon(url));
			btnEdit.setFocusable(false);
			btnEdit.setToolTipText("Изменить данные оплаты");
			//  Добавление кнопок на панель
			res.add(btnNew);
			res.add(btnEdit);
			res.add(btnDelete);
			// Возврат панели в качестве результата
			return res;
		   }
		 
		//Редактирование текущего клиента
			private void editBook_oplat() {			
				int index = tblBook_oplat.getSelectedRow();
				if (index == -1)
					return;
				// Преобразование индекса таблицы в индекс модели
				int modelRow = tblBook_oplat.convertRowIndexToModel(index);
				// Получаем объект из модели по индексу
				Book_oplat prod = kls.get(modelRow);
				@SuppressWarnings("unused")
				BigDecimal key = prod.getId_book_oplat();
				
				// Создание объекта окна редактирования
				EdBook_oplatDialog dlg = new EdBook_oplatDialog(this,
		    prod,manager,type.getId_book_uchyot());
				// Вызов окна и проверка кода возврата
				if (dlg.showDialog() == JDialogResult.OK) {
		               // Вызов метода обновления строки данных табличной модели
					manager.refreshVR(type);
					isNewRow = false;
					loadData();
					tblModel.updateRow(modelRow);
					System.out.println("Обновление OK");
					}
				
		
			}
			// Создание новой оплаты
				private void addBook_oplat() {

					// Создание объекта окна редактирования
					EdBook_oplatDialog dlg = new EdBook_oplatDialog(this, 
						null,manager,type.getId_book_uchyot());
					// Вызов окна и проверка кода возврата
					if (dlg.showDialog() == JDialogResult.OK) {
						// Создание нового объекта по введенным данным
						Book_oplat prod = dlg.getBook_oplat();
						manager.refreshVR(type);
						loadData();
						//  Добавление его в табличную модель
						tblModel.addRow(prod);}
			
				}
				// Удаление текущей оплаты
				private void deleteBook_oplat() {
					// Определяем индекс текущей строки.
					int index = tblBook_oplat.getSelectedRow();
					// Если нет выделенной строки, то выход
					if (index == -1)
						return;
					// Вывод запроса на удаления. При отказе - выход
					if (JOptionPane.showConfirmDialog(this, 
			  "Удалить оплату?", "Подтверждение", 
			  JOptionPane.YES_NO_OPTION,
					  JOptionPane.QUESTION_MESSAGE) != 
					  JOptionPane.YES_OPTION)
					  return;
					// Преобразование индекса представления в индекс модели
					int modelRow = tblBook_oplat.convertRowIndexToModel(index);
					// Создание объекта для выделенной строки
					Book_oplat prod = kls.get(modelRow);
					try {
				// Определение кода (первичного ключа) выделенной строки
					  BigDecimal kod = prod.getId_book_oplat();
					  // Вызов метода менеджера для удаления строки
					  if (manager.deleteBook_oplat(kod)) {
					  // Вызов метода удаления строки из табличной модели
						  manager.refreshVR(type);
							isNewRow = false;
							loadData();
					    tblModel.deleteRow(modelRow);
					    System.out.println("Удаление OK");
					  } else
						JOptionPane.showMessageDialog(this, 
			"Ошибка удаления строки", "Ошибка", 
			JOptionPane.ERROR_MESSAGE);
			
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(this, 
			ex.getMessage(), "Ошибка удаления", 
			JOptionPane.ERROR_MESSAGE);
					}
			
				}
		
		 // Модель данных для таблицы
		private class TKTableModel extends AbstractTableModel {

			private static final long serialVersionUID = 1L;
		private ArrayList<Book_oplat> prods;
		 // Конструктор объекта класса
		 public TKTableModel(ArrayList<Book_oplat> prods) {
		 this.prods = prods;
		 }
		 // Количество колонок в таблице
		 @Override
		 public int getColumnCount() {
		 return 3;
		 }
		 // Количество строк в таблице = размеру списка
		 @Override
		 public int getRowCount() {
		 return (prods==null?0:prods.size());
		 }
		 // Определение содержимого ячеек
		 @Override 
		 public Object getValueAt(int rowIndex, int columnIndex) {
		 // Выделяем объект из списка по текущему индексу
			 Book_oplat pr = prods.get(rowIndex);
		 // Каждой колонке сопоставляем поле объекта
		 switch (columnIndex) {
		 case 0:
		 return pr.getId_book_oplat();
		
		 case 1:
			 return pr.getDate_opl();
		 case 2:
			 return pr.getSum_opl();
		
		 default:
		 return null;
		 }
		 }
		 // Определение названия колонок
		 @Override
		 public String getColumnName(int column) {
		 switch (column) {
		 case 0:
		 return "ИД";
		
		 case 1:
			 return "Дата оплаты";
		 case 2:
			 return "Сумма оплаты";
		 
		 default:
		 return null;
		 }
		 }
		 // Этот метод используется для определения отрисовщика
		 // ячеек колонок в зависимости от типа данных
		 @SuppressWarnings({ "unchecked", "rawtypes" })
		public Class getColumnClass(int c) {
		 if (c==0) // защита от null в колонке 4
		 return java.lang.Number.class;
		 else if (c == 1) // защита от null в колонке 5
	//return Date.class;
		return java.lang.String.class;
		 else
		 return getValueAt(0, c).getClass();
		 }


	//Событие добавления строки
				public void addRow(Book_oplat prod) {
					//  Определяем положение добавляемой строки
					int len = prods.size();
					// Добавление в новой строки в список модели
					prods.add(prod);
					// Обновление отображения строки с новыми данными
					fireTableRowsInserted(len, len);
				}
				// Событие редактирования
				public void updateRow(int index) {
					// Обновление отображения измененной строки
					fireTableRowsUpdated(index, index);
				}
				// Событие удаления
				public void deleteRow(int index) {
					//  Если удаленная строка не конце таблицы
					if (index != prods.size() - 1)
						fireTableRowsUpdated(index + 1, prods.size() - 1);
						// Удаление строки из списка модели
						prods.remove(index);
						// Обновление отображения после удаления
						fireTableRowsDeleted(index, index);
				}

			
				
		 }
		}

