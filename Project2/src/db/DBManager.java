/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Date;

/**
 *
 * @author tonitan
 */
public class DBManager {
	Connection connection; //Kuhail had this in his code but I'm not 100% we need it
	
	//fairly certain this function can be exactly the same as Kuhails so I just copied it in, let me know if anything needs to be changed
    public void connect(String userName, String password, String serverName, String portNumber, String dbName) throws SQLException, InstantiationException, IllegalAccessException {
        System.out.println("Loading driver...");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Driver loaded!");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find the driver in the classpath!", e);
        }

        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);

        conn = DriverManager.getConnection(
                "jdbc:mysql://"
                + serverName
                + ":" + portNumber + "/" + dbName,
                connectionProps);

        System.out.println("Connected to database");
        this.connection=conn;
    }

    public LinkedList<Record> getAccountTypes() {
         LinkedList<Record> records=new LinkedList();
         PreparedStatement stmt = null;
        
        String query = "select * FROM Account_Types";

        try {
            stmt=connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            String account_type_id = rs.getString("Account_Type_ID");
            String account_type_name = rs.getString("Account_Type_Name");
            Record record=new Record(account_type_id,account_type_name);
            records.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return records;
        }
        return records;
    }

    public boolean checkUsername(String user_name) {
        
        PreparedStatement stmt = null;
        //done
        String query = "select * FROM users WHERE User_ID'=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,user_name); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            int count = getResultSetSize(rs);
            if (count == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }
    
    public boolean checkModUsername(String user_name) {
        
        PreparedStatement stmt = null;
        
        String query = "select * FROM Moderators WHERE User_ID'=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,user_name); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            int count = getResultSetSize(rs);
            if (count == 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    public Object[][] getUserAccounts(String user_id) {
        PreparedStatement stmt = null;
        Object[][] result=new Object[][]{};
        
		// this was how the SQL stuff looked in Kuhail's code "SELECT Account_ID,Employee_ID, Customers.Customer_ID,CONCAT(CustFirst_Name,' ',CustLast_Name) CustName,AcctOpen_Date,Account_Type_ID,AcctStatus FROM Accounts INNER JOIN Customers ON Accounts.Customer_ID=Customers.Customer_ID WHERE Employee_ID=?"
        String query = "select Advtitle as Title, AdvDetails as Description,Price,AdvDateTime as Date FROM advertisements WHERE user_id =?s";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,user_id); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            int count = getResultSetSize(rs);
            result=getAccountData(count,rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }
        return result;
    }
    
    public Object[][] getUserAccounts1(String user_id) {
        PreparedStatement stmt = null;
        Object[][] result=new Object[][]{};
        //SQL DONE
       String query = "select a.`AdvTitle` as Title, a.`AdvDetails` as descrption, price, s.`Name`, a.`AdvDateTime` as Date from advertisements a inner join statuses s on a.`Status_ID`=s.`Status_ID` where user_id=?";
        
      try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,user_id); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            int count = getResultSetSize(rs);
            result=getAccountData(count,rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }
        return result;//To change body of generated methods, choose Tools | Templates.
    }

    public boolean changeAdvStatus(String adv_name, String status) {
        PreparedStatement stmt = null;
        
        String query = "UPDATE Advertisements SET Status_ID=? WHERE AdvTitle=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,status); //binding the parameter with the given string
            stmt.setString(2,adv_name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean editAdv(String adv_name, String adv_desc) {
        PreparedStatement stmt = null;
        
        String query = "UPDATE Advertisements SET AdvDetails=?  WHERE AdvTitle=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,adv_desc); //binding the parameter with the given string
            stmt.setString(2,adv_name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public boolean moderatorClaim(String mod_id, String adv_name) {
        PreparedStatement stmt = null;
        
        String query = "UPDATE Advertisements SET Moderator_ID=? WHERE AdvTitle =?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,mod_id); //binding the parameter with the given string
            stmt.setString(2,adv_name);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Object[][] getModeratorAccounts(String moderator_id) {
        PreparedStatement stmt = null;
        Object[][] result=new Object[][]{};
        //SQL DONE
        String query = "select Advtitle as Title, AdvDetails as Description,Price,AdvDateTime as Date, User_ID as UserName FROM advertisements WHERE moderator_id=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,moderator_id); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            int count = getResultSetSize(rs);
            result=getAccountData(count,rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }
        return result;
    }
    
    public Object[][] getModeratorAccounts1(String moderator_id) {
        PreparedStatement stmt = null;
        Object[][] result=new Object[][]{};
        //SQL DONE
        String query = "select Advtitle as Title, AdvDetails as Description,Price,AdvDateTime as Date, User_ID as UserName FROM advertisements WHERE moderator_id=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,moderator_id); //binding the parameter with the given string
            ResultSet rs = stmt.executeQuery();
            int count = getResultSetSize(rs);
            result=getAccountData(count,rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }
        return result;
    }

    public LinkedList<Record> getCategoryTypes() {
        LinkedList<Record> records=new LinkedList();
         PreparedStatement stmt = null;
        
        String query = "select * FROM 'whatever table category types is stored in'";

        try {
            stmt=connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            String category_type_id = rs.getString("Category_Type_ID");
            String category_type_name = rs.getString("Category_Type_Name");
            Record record=new Record(category_type_id,category_type_name);
            records.add(record);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return records;
        }
        return records;
    }

    public boolean addAdv(String  adv_title, String adv_description, String adv_price, String cat_id, String user_id) {
        PreparedStatement stmt = null;
        
        String query = "Insert into Advertisment(AdvDateTime, AdvTitle, AdvDetails, Price, Category_ID, User_ID, Status_ID,Moderator_ID) Values (CURRENT_DATE(),?,?,?,?,?,?,?)";

        try {
            //Date date = new Date();
            //String currDate = String.format("%tF", date);
            stmt=connection.prepareStatement(query);
            stmt.setString(1,adv_title); //binding the parameter with the given string
            stmt.setString(2,adv_description);
            stmt.setString(3,adv_price);
            stmt.setString(4,cat_id);
            stmt.setString(5,user_id);
            //stmt.setString(6, currDate);
            stmt.setString(6, "PN");
            stmt.setString(7, "NA");
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
  
    
    public boolean deleteAdv(String adv_id, String user_id) {
        PreparedStatement stmt = null;
        
        String query = "DELETE FROM 'appropriate table'  WHERE 'whatever adv_id is called'=? AND 'whatever user_id is called'=?";

        try {
            stmt=connection.prepareStatement(query);
            stmt.setString(1,adv_id);
            stmt.setString(2,user_id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private int getResultSetSize(ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
    private Object[][] getAccountData(int count, ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
    

    public static class Record {

        public String ID;
		public String Name;

        public Record(String ID, String Name) 
		{
			this.ID = ID;
			this.Name=Name;
        }
		
		public String toString()
		{
			return Name;
		}
    }
    

}