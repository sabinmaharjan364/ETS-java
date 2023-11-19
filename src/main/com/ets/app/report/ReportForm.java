/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.com.ets.app.report;
import com.orsoncharts.plot.PiePlot3D;
import java.awt.BorderLayout;
import main.com.ets.app.category.*;
import main.com.ets.app.DBConn;
import main.com.ets.app.DashboardForm;
import main.com.ets.app.QueryInterfce;
import java.util.Date;
import javax.swing.Timer;
import main.com.ets.app.expenses.ExpenseForm;
import main.com.ets.app.income.IncomeForm;
import main.com.ets.app.incomecategories.IncomeCategoryForm;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import main.com.ets.app.LoginForm;
import main.com.ets.app.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
/**
 *
 * @author sabinmaharjan
 */
public class ReportForm extends javax.swing.JFrame implements QueryInterfce{
    
    Statement stmt = null;    
    Statement stmtIncome = null;
    Statement stmtIncomeMonthly = null;
    Statement stmtExpenseMonthly = null;
    Statement stmtIncomeYearly = null;
    Statement stmtExpenseYearly = null;

    ResultSet rs = null;
    ResultSet rsIncome = null;
    ResultSet rsIncomeMonthly = null;
    ResultSet rsExpenseMonthly = null;
    ResultSet rsExpenseYearly = null;
    ResultSet rsIncomeYearly = null;

    DBConn conn=new DBConn();
    Connection con=conn.getConnection();
    /**
     * Creates new form CategoryForm
     */
    public ReportForm() {
        initComponents();
        showData();
//        jFreeChartDisplay();
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        showDate();
        showTime();
    }
    //   display date in report page
    void showDate(){
      Date d= new Date();
      SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
      date.setText(dateFormat.format(d));
    }
    
//   display time in report page
    void showTime(){
        new Timer(0, new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e){
        Date date = new Date();
        SimpleDateFormat s= new SimpleDateFormat("hh:mm:ss a");
        time.setText(s.format(date));
        }
           }).start();
       }
    
   
//    calculate profit and loss and show total expense and total income
    public void showData(){
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat current_date=new SimpleDateFormat("Y-MM-dd");
        System.out.println("now"+current_date.format(calendar.getTime()));
        String queryExpense="select sum(cost) from expenses where date='"+current_date.format(calendar.getTime())+"' and user_id='"+User.getUser_session_id()+"'"; 
        String queryIncome="select sum(cost) from incomes where date='"+current_date.format(calendar.getTime())+"' and user_id='"+User.getUser_session_id()+"'"; 
        
        String queryIncomeMonthly="select sum(cost) from incomes where month(date)='"+(calendar.get(Calendar.MONTH)+1)+"' and user_id='"+User.getUser_session_id()+"'"; 
        String queryExpenseMonthly="select sum(cost) from expenses where month(date)='"+(calendar.get(Calendar.MONTH)+1)+"' and user_id='"+User.getUser_session_id()+"'"; 
        
        String queryIncomeYearly="select sum(cost) from incomes where year(date)='"+(calendar.get(Calendar.YEAR))+"' and user_id='"+User.getUser_session_id()+"'"; 
        String queryExpenseYearly="select sum(cost) from expenses where year(date)='"+(calendar.get(Calendar.YEAR))+"' and user_id='"+User.getUser_session_id()+"'"; 
   
        System.out.println("current monthly "+calendar.get(Calendar.MONTH)+1);     
        System.out.println("current yearly "+calendar.get(Calendar.YEAR));

        System.out.println(queryIncomeMonthly);
        try{
            stmt=con.createStatement();
            rs=stmt.executeQuery(queryExpense);
            
            stmtIncome=con.createStatement();
            rsIncome=stmtIncome.executeQuery(queryIncome);
            
            stmtIncomeMonthly=con.createStatement();
            rsIncomeMonthly=stmtIncomeMonthly.executeQuery(queryIncomeMonthly);
            
            stmtExpenseMonthly=con.createStatement();
            rsExpenseMonthly=stmtExpenseMonthly.executeQuery(queryExpenseMonthly);
            
            stmtExpenseYearly=con.createStatement();
            rsExpenseYearly=stmtExpenseYearly.executeQuery(queryExpenseYearly);
            
            stmtIncomeYearly=con.createStatement();
            rsIncomeYearly=stmtIncomeYearly.executeQuery(queryIncomeYearly);
            
            rs.next();
            Float sum=rs.getFloat(1);
            rsIncome.next();
            Float sumIncome=rsIncome.getFloat(1);
            System.out.println(sum);
            if(sum > sumIncome){
                Float loss=sum-sumIncome;
                label_loss.setText(loss.toString());
            }else{
                 Float profit=sumIncome-sum;
                label_profit.setText(profit.toString());
            }
            
            label_total_income.setText(sumIncome.toString());           
            label_total_expense.setText(sum.toString());
           
            
            rsIncomeMonthly.next();
            rsExpenseMonthly.next();
            Float sumMonthlyIncome=rsIncomeMonthly.getFloat(1);
            
            Float sumMonthlyExpense=rsExpenseMonthly.getFloat(1);
            System.out.println(sum);
            if(sumMonthlyExpense > sumMonthlyIncome){
                Float loss=sumMonthlyExpense-sumMonthlyIncome;
                label_loss1.setText(loss.toString());
            }else{
                 Float profit=sumMonthlyIncome-sumMonthlyExpense;
                label_profit1.setText(profit.toString());
            }
            System.out.println("monthllllly income"+sumMonthlyIncome);
            label_total_income1.setText(sumMonthlyIncome.toString());           
            label_total_expense1.setText(sumMonthlyExpense.toString());
            
            
            rsIncomeYearly.next();
            rsExpenseYearly.next();
            Float sumYearlyIncome=rsIncomeYearly.getFloat(1);
            Float sumYearlyExpense=rsExpenseYearly.getFloat(1);
            System.out.println(sum);
            if(sumYearlyExpense > sumYearlyIncome){
                Float loss=sumYearlyExpense-sumYearlyIncome;
                label_loss2.setText(loss.toString());
            }else{
                 Float profit=sumYearlyIncome-sumYearlyExpense;
                label_profit2.setText(profit.toString());
            }
            System.out.println("yearly income"+sumYearlyIncome);
            label_total_income2.setText(sumYearlyIncome.toString());           
            label_total_expense2.setText(sumYearlyExpense.toString());
            
            DefaultPieDataset pie=new DefaultPieDataset();
            pie.setValue("income :"+ new Double(sumIncome), new Double(sumIncome));
            pie.setValue("expense: "+  new Double(sum), new Double(sum));
            JFreeChart chart=ChartFactory.createPieChart("Pie Chart", pie, true, true, false);
            chartDisplay.setLayout(new java.awt.BorderLayout());
            ChartPanel CP = new ChartPanel(chart);
            chartDisplay.add(CP,BorderLayout.CENTER);
            chartDisplay.validate();
            
            
            
            DefaultPieDataset pieMonthly=new DefaultPieDataset();
            pieMonthly.setValue(sumMonthlyIncome, new Double(sumMonthlyIncome));
            pieMonthly.setValue(sumMonthlyExpense, new Double(sumMonthlyExpense));
            JFreeChart chartMonthly=ChartFactory.createPieChart("Pie Chart", pieMonthly, true, true, false);
            chartDisplay1.setLayout(new java.awt.BorderLayout());
            ChartPanel monthly = new ChartPanel(chartMonthly);
            chartDisplay1.add(monthly,BorderLayout.CENTER);
            chartDisplay1.validate();
            
            
            
            DefaultPieDataset pieYearly=new DefaultPieDataset();
            pieYearly.setValue(sumYearlyIncome, new Double(sumYearlyIncome));
            pieYearly.setValue(sumYearlyExpense, new Double(sumYearlyExpense));
            JFreeChart chartYearly=ChartFactory.createPieChart("Pie Chart Yearly", pieYearly, true, true, false);
            chartDisplay2.setLayout(new java.awt.BorderLayout());
            ChartPanel yearly = new ChartPanel(chartYearly);
            chartDisplay2.add(yearly,BorderLayout.CENTER);
            chartDisplay2.validate();
            
            
            
        }catch(HeadlessException | SQLException error){
            error.printStackTrace();
            
        }
        
//        DefaultTableModel model=(DefaultTableModel) jTable_categories.getModel();
//        Object[] row=new Object[3];
//        for(int i=0;i<list.size();i++){
//            row[0]=list.get(i).getId();             
//            row[1]=list.get(i).getName(); 
//
//            row[2]=list.get(i).getDescription();
//            model.addRow(row);
//
//        }
        
    }
    
//    chart code
    public void jFreeChartDisplay(){
        DefaultPieDataset pie=new DefaultPieDataset();
        pie.setValue("one", new Integer(10));
        pie.setValue("two", new Integer(40));
        pie.setValue("three", new Integer(10));
        pie.setValue("four", new Integer(15));
        JFreeChart chart=ChartFactory.createPieChart("Pie Chart", pie, true, true, false);
//        
//        ChartPanel(chart);
//        //ChartFrame frame =new ChartFrame("Pie Chart", chart);
//        JPanel jPanel1 = new JPanel();
        chartDisplay.setLayout(new java.awt.BorderLayout());
//
        ChartPanel CP = new ChartPanel(chart);
//
        chartDisplay.add(CP,BorderLayout.CENTER);
        chartDisplay.validate();

        
    }
    
    /**
     * 
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        label_total_income = new javax.swing.JLabel();
        label_total_expense = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        label_loss = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        label_profit = new javax.swing.JLabel();
        chartDisplay = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lbl_income_monthly = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        label_total_income1 = new javax.swing.JLabel();
        label_total_expense1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        label_loss1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        label_profit1 = new javax.swing.JLabel();
        chartDisplay1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        lbl_income_monthly1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        label_total_income2 = new javax.swing.JLabel();
        label_total_expense2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        label_loss2 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        label_profit2 = new javax.swing.JLabel();
        chartDisplay2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        time = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        dashboardMenu = new javax.swing.JMenu();
        expenseMenu = new javax.swing.JMenu();
        incomeMenu = new javax.swing.JMenu();
        categoryMenu = new javax.swing.JMenu();
        incomeCategoryMenu = new javax.swing.JMenu();
        Report = new javax.swing.JMenu();
        Exit = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel7.setFont(new java.awt.Font("Bungee Inline", 0, 36)); // NOI18N
        jLabel7.setText("Expense Tracking System");

        jLabel5.setText("Dashboard  >  Report");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel5))
                .addGap(91, 1003, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(371, 371, 371)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setText("Total Income");

        jLabel2.setText("Total Expense");

        jLabel6.setText("Profit");

        label_loss.setText("0");

        jLabel9.setText("Loss");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_total_income, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_profit, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_loss, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_total_expense, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(169, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_total_expense, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(label_total_income)))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel9)
                    .addComponent(label_loss)
                    .addComponent(label_profit))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout chartDisplayLayout = new javax.swing.GroupLayout(chartDisplay);
        chartDisplay.setLayout(chartDisplayLayout);
        chartDisplayLayout.setHorizontalGroup(
            chartDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartDisplayLayout.setVerticalGroup(
            chartDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chartDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(chartDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 100, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Daily", jPanel2);

        jLabel3.setText("Total Income");

        jLabel4.setText("Total Expense");

        jLabel8.setText("Profit");

        label_loss1.setText("0");

        jLabel10.setText("Loss");

        javax.swing.GroupLayout chartDisplay1Layout = new javax.swing.GroupLayout(chartDisplay1);
        chartDisplay1.setLayout(chartDisplay1Layout);
        chartDisplay1Layout.setHorizontalGroup(
            chartDisplay1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartDisplay1Layout.setVerticalGroup(
            chartDisplay1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout lbl_income_monthlyLayout = new javax.swing.GroupLayout(lbl_income_monthly);
        lbl_income_monthly.setLayout(lbl_income_monthlyLayout);
        lbl_income_monthlyLayout.setHorizontalGroup(
            lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lbl_income_monthlyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chartDisplay1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(lbl_income_monthlyLayout.createSequentialGroup()
                        .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_total_income1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_profit1, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_loss1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label_total_expense1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 154, Short.MAX_VALUE)))
                .addContainerGap())
        );
        lbl_income_monthlyLayout.setVerticalGroup(
            lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lbl_income_monthlyLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_total_expense1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(label_total_income1)))
                .addGap(28, 28, 28)
                .addGroup(lbl_income_monthlyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(label_loss1)
                    .addComponent(label_profit1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 109, Short.MAX_VALUE)
                .addComponent(chartDisplay1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_income_monthly, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_income_monthly, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(29, 29, 29))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Monthly", jPanel6);

        jLabel11.setText("Total Income");

        jLabel12.setText("Total Expense");

        jLabel13.setText("Profit");

        label_loss2.setText("0");

        jLabel14.setText("Loss");

        javax.swing.GroupLayout chartDisplay2Layout = new javax.swing.GroupLayout(chartDisplay2);
        chartDisplay2.setLayout(chartDisplay2Layout);
        chartDisplay2Layout.setHorizontalGroup(
            chartDisplay2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        chartDisplay2Layout.setVerticalGroup(
            chartDisplay2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout lbl_income_monthly1Layout = new javax.swing.GroupLayout(lbl_income_monthly1);
        lbl_income_monthly1.setLayout(lbl_income_monthly1Layout);
        lbl_income_monthly1Layout.setHorizontalGroup(
            lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lbl_income_monthly1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lbl_income_monthly1Layout.createSequentialGroup()
                        .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_total_income2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_profit2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                        .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(label_total_expense2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(label_loss2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)))
                    .addComponent(chartDisplay2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        lbl_income_monthly1Layout.setVerticalGroup(
            lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lbl_income_monthly1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(label_total_income2)
                    .addComponent(label_total_expense2))
                .addGap(28, 28, 28)
                .addGroup(lbl_income_monthly1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(label_loss2)
                    .addComponent(label_profit2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 115, Short.MAX_VALUE)
                .addComponent(chartDisplay2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_income_monthly1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_income_monthly1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Yearly", jPanel9);

        time.setFont(new java.awt.Font("Wide Latin", 0, 18)); // NOI18N
        time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        date.setFont(new java.awt.Font("Wide Latin", 0, 18)); // NOI18N
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("  \tAbout This Software\n\n Expense Tracking System is a Java final\n project. This Expense Tracking System can\n be used for individual purpose and \n business purpose too. This software main \n motive is to track income and expenses and\n increase saving. ");
        jScrollPane1.setViewportView(jTextArea1);

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 3)); // NOI18N
        jLabel15.setIcon(new javax.swing.ImageIcon("C:\\Users\\Sabin Maharjan\\OneDrive - USQ\\project\\java\\ExpenseTrackerSystem\\img\\quotes-money-is-in_6398-0.png")); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(date, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(time, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel13Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(264, 264, 264))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton2.setText("Income");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton3.setText("Expense");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton4.setText("Category");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton5.setText("Income Category");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton6.setText("Signout");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dashboardMenu.setText("Dashboard");
        dashboardMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(dashboardMenu);

        expenseMenu.setText("Expense");
        expenseMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                expenseMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(expenseMenu);

        incomeMenu.setText("Income");
        incomeMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                incomeMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(incomeMenu);

        categoryMenu.setText("Category");
        categoryMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                categoryMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(categoryMenu);

        incomeCategoryMenu.setText("Income Category");
        incomeCategoryMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                incomeCategoryMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(incomeCategoryMenu);

        Report.setText("Report");
        Report.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReportMouseClicked(evt);
            }
        });
        jMenuBar1.add(Report);

        Exit.setText("Signout");
        Exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitActionPerformed(evt);
            }
        });
        jMenuBar1.add(Exit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 1125, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dashboardMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardMenuMouseClicked
        // TODO add your handling code here:
         // TODO add your handling code here:
        DashboardForm jframe = new DashboardForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_dashboardMenuMouseClicked

    private void expenseMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_expenseMenuMouseClicked
        // TODO add your handling code here:
        ExpenseForm jframe = new ExpenseForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);

    }//GEN-LAST:event_expenseMenuMouseClicked

    private void incomeMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_incomeMenuMouseClicked
        // TODO add your handling code here:
        IncomeForm jframe = new IncomeForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_incomeMenuMouseClicked

    private void categoryMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_categoryMenuMouseClicked
        // TODO add your handling code here:
        

        CategoryForm jframe = new CategoryForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);

    }//GEN-LAST:event_categoryMenuMouseClicked

    private void incomeCategoryMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_incomeCategoryMenuMouseClicked
        // TODO add your handling code here:
         IncomeCategoryForm jframe = new IncomeCategoryForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_incomeCategoryMenuMouseClicked

    private void ReportMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReportMouseClicked
        // TODO add your handling code here:r
        ReportForm jframe = new ReportForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_ReportMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        ExpenseForm jframe = new ExpenseForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        CategoryForm jframe = new CategoryForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(CategoryForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ReportForm frame = new ReportForm();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
          IncomeCategoryForm jframe = new IncomeCategoryForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        IncomeForm jframe = new IncomeForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        setVisible(false);
        LoginForm jframe = new LoginForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
        User.setUser_session_id(0);
        
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        // TODO add your handling code here:
        setVisible(false);
        LoginForm jframe = new LoginForm();
        jframe.setLocationRelativeTo(null);
        jframe.setDefaultCloseOperation(ReportForm.DISPOSE_ON_CLOSE);
        jframe.setVisible(true);
        User.setUser_session_id(0);

    }//GEN-LAST:event_ExitActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReportForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReportForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Exit;
    private javax.swing.JMenu Report;
    private javax.swing.JMenu categoryMenu;
    private javax.swing.JPanel chartDisplay;
    private javax.swing.JPanel chartDisplay1;
    private javax.swing.JPanel chartDisplay2;
    private javax.swing.JMenu dashboardMenu;
    private javax.swing.JLabel date;
    private javax.swing.JMenu expenseMenu;
    private javax.swing.JMenu incomeCategoryMenu;
    private javax.swing.JMenu incomeMenu;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel label_loss;
    private javax.swing.JLabel label_loss1;
    private javax.swing.JLabel label_loss2;
    private javax.swing.JLabel label_profit;
    private javax.swing.JLabel label_profit1;
    private javax.swing.JLabel label_profit2;
    private javax.swing.JLabel label_total_expense;
    private javax.swing.JLabel label_total_expense1;
    private javax.swing.JLabel label_total_expense2;
    private javax.swing.JLabel label_total_income;
    private javax.swing.JLabel label_total_income1;
    private javax.swing.JLabel label_total_income2;
    private javax.swing.JPanel lbl_income_monthly;
    private javax.swing.JPanel lbl_income_monthly1;
    private javax.swing.JLabel time;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onQueryExec(String message) {
//        DefaultTableModel model=(DefaultTableModel)jTable_categories.getModel();
//        model.setRowCount(0);
//        showData();
//        JOptionPane.showMessageDialog(null, message);
    }

    @Override
    public void onError(String message) {
        JOptionPane.showMessageDialog(null, message);

    }

    private void ChartPanel(JFreeChart chart) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
