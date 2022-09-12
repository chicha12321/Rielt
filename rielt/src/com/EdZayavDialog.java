package com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.*;
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

public class EdZayavDialog extends JRDialog {

	private static final long serialVersionUID = 1L;
	// Поля класса
	  private DBManager manager;
	  private BigDecimal old_key; // прежнее значение ключа
	  // Два варианта заголовка окна
	  private final static String title_add = 
	    "Добавление новой заявки";
	  private final static String title_ed = 
	     "Редактирование заявки";
	   //  Объектная переменная для класса Заявка
		private Zayav type = null;
	    // Флаг режима «добавление новой строки»
		private boolean isNewRow = false;
	    // Форматер для полей дат
		SimpleDateFormat frmt = 
	          new SimpleDateFormat("dd-MM-yyyy");
	    // Элементы (поля редактирования) для полей записи
		private JTextField edCod_sotrud;
		private JTextField edNum_zayav;
		private JTextField edVid;
		private JTextField edPrefer;
		private JTextField edDate_zaver;
		private JTextField edKod_klient;
		@SuppressWarnings("rawtypes")
		private JComboBox cmbSotr, cmbKlient;
		
		//  кнопки
		private JButton btnOk;
		private JButton btnCancel;
		//ДЛЯ ПОДЧ. ТАБЛИЦЫ
		 private JTable tblBook_uchyot;
		 
			private JButton btnClose;
			// кнопка редактирования 
				private JButton btnEdit;
				// кнопка добавления 
				private JButton btnNew;
				// кнопка удаления 
				private JButton btnDelete;
				//табл модель
			private TKTableModel tblModel;
			 private ArrayList<Book_uchyot> kls;
			 
	    //  Конструктор класса
		public EdZayavDialog(Window parent,Zayav type,
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
		     old_key=type.getId_zayav();
		     loadDataGruz(); //Загрузка в таблицу 2
		     }
		  else {
		     this.type = new Zayav();	// Новый объект
		     kls=new ArrayList <Book_uchyot>();
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
		 kls=manager.loadBook_uchyot(type.getId_zayav());
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
			  if (!constructZayav()) return;
			  //новая версия построения 31.03.2021
			  else {if (isNewRow)
			  			{
				  		if (manager.addZayav(type)==true)
				  			{
				  					setDialogResult(JDialogResult.OK);
				  					((AbstractButton) e.getSource()).setText("Редактировать");
				  					setButton();
				  			}
			  			}
			  else 
				  if (manager.updateZayav(type, old_key)==true)
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
									addBook_uchyot();
								}
							});
							//  Для кнопки редактирования
							btnEdit.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									editBook_uchyot();
								}
							});
							//  Для кнопки удаления
							btnDelete.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
								deleteBook_uchyot();
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
							//Событие выбора элемента списка
								cmbKlient.addItemListener(new ItemListener() {
									 @Override
									 public void itemStateChanged(ItemEvent e) {
									 // установка значения поля внешнего ключа
									 if (e.getStateChange() == ItemEvent.SELECTED) {
									 if (e.getItem() != null) {
									 Klient gr = (Klient) e.getItem();
									 edKod_klient.setText(gr.getKod_klient().toString());
									 }
									 }
									 }
									});
									// Событие потери фокуса полем ввода
								edKod_klient.addFocusListener(new FocusListener() {
									 @Override
									 public void focusLost(FocusEvent e) {
									 // При потере фокуса изменяем элемент списка
										//Проверка на пустоту-добавлена Романом
										 if(!(edKod_klient.getText().isEmpty())) {
									 @SuppressWarnings("rawtypes")
									DefaultComboBoxModel model1 =
									(DefaultComboBoxModel) cmbKlient.getModel();
									 BigDecimal grKod =
									new BigDecimal(edKod_klient.getText());
									 setCmbItem2(model1, grKod);
									 }
									 }
									 @Override
									 public void focusGained(FocusEvent e) {
									 }
									});
			 }
			 
		  // Метод создания графического интерфейса
		@SuppressWarnings("rawtypes")
		private void createGui() {
			// Создание панели
			JPanel pnl = new JPanel(new MigLayout(
	"insets 5", "[][]","[]5[]10[]"));
			// Создание полей для редактирования данных
	edCod_sotrud = new JTextField(10);
	edNum_zayav = new JTextField(10);
	cmbSotr = new JComboBox();
	cmbKlient = new JComboBox();
			edVid = new JTextField(20);
			edPrefer = new JTextField(50);
			edKod_klient = new JTextField(10);
			edDate_zaver = new JFormattedTextField(
	                createFormatter("##-##-####"));
			edDate_zaver.setColumns(10);
			//  Создание кнопок
			btnOk = new JButton("Сохранить");
			btnCancel = new JButton("Отмена");
			// Добавление элементов на панель
			
			pnl.add(new JLabel("Номер заявки"));
			pnl.add(edNum_zayav,"span");
			pnl.add(new JLabel("Вид"));
			pnl.add(edVid,"span");
			pnl.add(new JLabel("Предпочтения"));
			pnl.add(edPrefer,"span");
			pnl.add(new JLabel("Клиент"));
			pnl.add(edKod_klient,"split 2");
			pnl.add(cmbKlient, "growx, wrap");
			
			pnl.add(new JLabel("Дата заявки"));
			pnl.add(edDate_zaver,"span");
			pnl.add(new JLabel("Сотрудник"));
			pnl.add(edCod_sotrud, "split 2");
			pnl.add(cmbSotr, "growx, wrap");
			
			pnl.add(btnOk, "span, split 2, center, sg ");
			pnl.add(btnCancel, "sg 1");
			//  Добавление панели в окно фрейма
			getContentPane().setLayout(new BorderLayout());
			getContentPane().add(pnl, BorderLayout.NORTH);
			 JPanel pnfl = new JPanel(new MigLayout("insets 3, gapy 4",
						//	JPanel pnl = new JPanel(new MigLayout("insets 3, gap 90! 90!",
						 "[grow, fill]", "[]5[grow, fill]10[]"));
						 // Создание объекта таблицы
						 tblBook_uchyot = new JTable();
						 // Создание объекта табличной модели на базе
						 // сформированного списка
						 tblBook_uchyot.setModel(tblModel = new TKTableModel(kls));
						 // Создание объекта сортировки для табличной модели
						 RowSorter<TKTableModel> sorter = new
						 TableRowSorter<TKTableModel>(tblModel);
						 // Назначение объекта сортировки таблице
						 tblBook_uchyot.setRowSorter(sorter);
						 // Задаем параметры внешнего вида таблицы
						 // Выделение полосой всей текущей строки
						 tblBook_uchyot.setRowSelectionAllowed(true);
						 // Задаем промежутки между ячейками
						 tblBook_uchyot.setIntercellSpacing(new Dimension(0, 1)); 
						 // Задаем цвет сетки
						 tblBook_uchyot.setGridColor(new Color(170, 170, 255).darker());
						 // Автоматическое определение ширины последней колонки
						 tblBook_uchyot.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
						 // Возможность выделения только 1 строки
						 tblBook_uchyot.getSelectionModel().setSelectionMode(
						 ListSelectionModel.SINGLE_SELECTION);
						 // Создание области прокрутки и вставка в нее таблицы
						 JScrollPane scrlPane = new JScrollPane(tblBook_uchyot);
						 scrlPane.getViewport().setBackground(Color.white);
						 scrlPane.setBorder(BorderFactory.createCompoundBorder
						(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
						 tblBook_uchyot.getColumnModel().getColumn(0).setMaxWidth(100);
						 tblBook_uchyot.getColumnModel().getColumn(0).setMinWidth(100);
						 // Создание кнопки для закрытия формы
						 btnClose = new JButton("Закрыть");
						 pnfl.add(getToolBar(),"growx,wrap");//Создание  панели
						 //Добавление на панель: метки, области с таблицей и кнопки
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
				edPrefer.setText(type.getPrefer().toString());
		//		edCod_sotrud.setText(type.getId_zayav().toString());
				edNum_zayav.setText(type.getNum_zayav().toString());
			edVid.setText(type.getVid());
			edDate_zaver.setText(type.getDate_zayav()==null? 
					   "":frmt.format(type.getDate_zayav()));
			edKod_klient.setText(type.getKlient().getKod_klient().toString());
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
			ArrayList<Klient> lst2 = manager.loadKlient();
			if (lst2 != null) {
			 // Создание модели данных на базе списка
			 @SuppressWarnings({ "rawtypes" })
			DefaultComboBoxModel model2 =
			new DefaultComboBoxModel(lst2.toArray());
			 // Установка модели для JComboBox
			 cmbKlient.setModel(model2);
			 // Определение поля внешнего ключа
			 BigDecimal grKod = (isNewRow? null :
			type.getKlient().getKod_klient());
			 // Вызов метода установки элемента списка
			 // соответствующего значению внешнего ключа
			 setCmbItem2(model2, grKod);
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
		private void setCmbItem2(@SuppressWarnings("rawtypes") DefaultComboBoxModel model,
				 BigDecimal grKod) {
				cmbKlient.setSelectedItem(null);
				if (grKod != null)
				 // Просмотр элементов списка для нахождения элемента
				 // с заданным кодом
				 for (int i = 0, c = model.getSize(); i < c; i++)
				 if (((Klient) model.getElementAt(i)).
				getKod_klient().equals(grKod)) {
				 cmbKlient.setSelectedIndex(i);
				 break;
				 }
				} 
		
		
		//Формирование объекта Заявка перед сохранением
		private boolean constructZayav()	{
		  try {
		//		type.setId_zayav(edCod_sotrud.getText().equals("") ? 
//	null : new BigDecimal(edCod_sotrud.getText()));
				type.setNum_zayav(edNum_zayav.getText().equals("") ? 
						null : new BigDecimal(edNum_zayav.getText()));
				
				if(!(edPrefer.getText().isEmpty()))
					type.setPrefer(edPrefer.getText());
				if(!(edVid.getText().isEmpty()))
					type.setVid(edVid.getText());
				
				type.setKod_klient(edKod_klient.getText().equals("") ? 
						null : new BigDecimal(edKod_klient.getText()));
				type.setDate_zayav(edDate_zaver.getText().substring(0,
					      1).trim().equals("") ? null : 
					      frmt.parse(edDate_zaver.getText()));
				Object obj = cmbSotr.getSelectedItem();
				Sotrud gr = (Sotrud) obj;
				type.setSotrud(gr);
				
				 Object obj2 = cmbKlient.getSelectedItem();
				 Klient gr2 = (Klient) obj2;
				 type.setKlient(gr2);
return true;
		  }
		  catch (Exception ex){
			JOptionPane.showMessageDialog(this, 
	            ex.getMessage(), "Ошибка данных",
			   JOptionPane.ERROR_MESSAGE);
			 return false;
		  }
		}
		// Возврат объекта Заявка
		public Zayav getZayav()
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
			btnNew.setToolTipText("Добавить новую запись");
			// Добавление кнопки «Удалить»
			url = FrmBook_uchyot.class.getResource("/images/delete.png");
			btnDelete = new JButton(new ImageIcon(url));
			btnDelete.setFocusable(false);
			btnDelete.setToolTipText("Удалить запись");
			// Добавление кнопки «Редактировать»
			url = FrmBook_uchyot.class.getResource("/images/edit.png");
			btnEdit = new JButton(new ImageIcon(url));
			btnEdit.setFocusable(false);
			btnEdit.setToolTipText("Изменить данные записи");
			//  Добавление кнопок на панель
			res.add(btnNew);
			res.add(btnEdit);
			res.add(btnDelete);
			// Возврат панели в качестве результата
			return res;
		   }
		 
		//Редактирование текущей записи
			private void editBook_uchyot() {			
				int index = tblBook_uchyot.getSelectedRow();
				if (index == -1)
					return;
				// Преобразование индекса таблицы в индекс модели
				int modelRow = tblBook_uchyot.convertRowIndexToModel(index);
				// Получаем объект из модели по индексу
				Book_uchyot prod = kls.get(modelRow);
				@SuppressWarnings("unused")
				BigDecimal key = prod.getId_book_uchyot();
				
				// Создание объекта окна редактирования
				EdBook_uchyotDialog dlg = new EdBook_uchyotDialog(this,
		    prod,manager, type.getId_zayav());
				// Вызов окна и проверка кода возврата
				if (dlg.showDialog() == JDialogResult.OK) {
		               // Вызов метода обновления строки данных табличной модели
					tblModel.updateRow(modelRow);
					System.out.println("Обновление OK");
					}
				
		
			}
			// Создание новой записи
				private void addBook_uchyot() {

					// Создание объекта окна редактирования
					EdBook_uchyotDialog dlg = new EdBook_uchyotDialog(this, 
						null,manager,type.getId_zayav());
					dlg.showDialog();
					Book_uchyot prod = dlg.getBook_uchyot();
					if (prod!=null&&prod.getId_book_uchyot()!=null) {
						// Добавление его в табличную модель
						tblModel.addRow(prod);
					}
			
				}
				// Удаление текущей записи
				private void deleteBook_uchyot() {
					// Определяем индекс текущей строки.
					int index = tblBook_uchyot.getSelectedRow();
					// Если нет выделенной строки, то выход
					if (index == -1)
						return;
					// Вывод запроса на удаления. При отказе - выход
					if (JOptionPane.showConfirmDialog(this, 
			  "Удалить услугу?", "Подтверждение", 
			  JOptionPane.YES_NO_OPTION,
					  JOptionPane.QUESTION_MESSAGE) != 
					  JOptionPane.YES_OPTION)
					  return;
					// Преобразование индекса представления в индекс модели
					int modelRow = tblBook_uchyot.convertRowIndexToModel(index);
					// Создание объекта для выделенной строки
					Book_uchyot prod = kls.get(modelRow);
					try {
				// Определение кода (первичного ключа) выделенной строки
					  BigDecimal kod = prod.getId_book_uchyot();
					  // Вызов метода менеджера для удаления строки
					  if (manager.deleteBook_uchyot(kod)) {
					  // Вызов метода удаления строки из табличной модели
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
		private ArrayList<Book_uchyot> prods;
		 // Конструктор объекта класса
		 public TKTableModel(ArrayList<Book_uchyot> prods) {
		 this.prods = prods;
		 }
		 // Количество колонок в таблице
		 @Override
		 public int getColumnCount() {
		 return 7;
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
			 Book_uchyot pr = prods.get(rowIndex);
		 // Каждой колонке сопоставляем поле объекта
		 switch (columnIndex) {
		 case 0:
		 return pr.getId_book_uchyot();
		
		 case 1:
			 return pr.getObj_nedvij().getKod_obj();
		 case 2:
			 return pr.getUsluga().getName_usl();
		 case 3:
			 return pr.getUch_date();
		 case 4:
			 return pr.getSotrud().getFio();
		 case 5:
			 return pr.getStoim();
		 case 6:
			 return pr.getOpl_stoim();
		
		 default:
		 return null;
		 }
		 }
		 // Определение названия колонок
		 @Override
		 public String getColumnName(int column) {
		 switch (column) {
		 case 0:
		 return "ИД книги";
		
		 case 1:
			 return "Код объекта";
		 case 2:
			 return "Услуга";
		 case 3:
			 return "Дата услуги";
		 case 4:
			 return "Сотрудник";
		 case 5:
			 return "Стоимость";
		 case 6:
			 return "Оплачено";
		 
		 
		 
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
		return java.lang.Number.class;
		 else
		 return java.lang.String.class;
		 }


	//Событие добавления строки
				public void addRow(Book_uchyot prod) {
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


