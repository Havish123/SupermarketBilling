package Services;

import DTO.*;
import Extension.*;
import Models.Category;
import Models.Customer;
import Models.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UserServices {
    static Connection conn = DatabaseUtil.getConnection();

    public Customer getCustomer(int id) throws SQLException {
        try {
            Customer customer = null;
            String query = "select id,name,emailid,mobilenumber,password,address,issubscribeemails from Customers where id= ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                customer = new Customer();
                customer.setName(rs.getString("name"));
                customer.setId(rs.getInt("id"));
                customer.setEmailId(rs.getString("emailid"));
                customer.setMobileNumber(rs.getString("mobilenumber"));
                customer.setPassword(SecurityConfig.DecryptData(rs.getString("password")));
                customer.setAddress(rs.getString("address"));
                customer.setSubscribeEmails(rs.getBoolean("issubscribeemails"));

            }
            return customer;
        }
        catch (SQLException e){
            System.out.println("Error in getUser " +e.getMessage());
            throw e;
        }
    }

    public void addMoneyInWallet(WalletTopUpDto input){
        try {
            String query = "UPDATE Wallets SET Amount = Amount + ?, ModifiedDate = NOW(),modifiedby=? WHERE customerId = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, input.getWalletamount());
                stmt.setInt(2, input.getUserId());
                stmt.setInt(3,input.getUserId());
                stmt.executeUpdate();
                addWalletHistory(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMoneyInWallet(WalletTopUpDto input){
        try {
            String query = "UPDATE Wallets SET Amount = ?, ModifiedDate = NOW(),modifiedby=? WHERE Id = ?;";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setDouble(1, input.getBalanceAmount());
                stmt.setInt(2, input.getUserId());
                stmt.setInt(3,input.getWalletId());
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Wallet updated successfully for customer ID: " + input.getUserId());
                    addWalletHistory(input);
                } else {
                    System.out.println("No wallet found for customer ID: " + input.getUserId());
                }
                SQLWarning warning = stmt.getWarnings();
                if (warning != null) {
                    System.out.println("SQL Warning: " + warning.getMessage());
                }
                addWalletHistory(input);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addWalletHistory(WalletTopUpDto topup){
        try {
            String query = "INSERT INTO wallethistory (walletid, transactiontype, amount,createddate,createdby) VALUES (?, ?, ?,NOW(),?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, topup.getWalletId());
                stmt.setInt(2, topup.getTransactionType());
                stmt.setDouble(3, topup.getWalletamount());
                stmt.setInt(4,topup.getUserId());

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public List<WalletHistoryDto> getWalletHistory(int walletid,int currentUserId, int limit, int offset){
        List<WalletHistoryDto> walletlist=new ArrayList<>();
        try{
            String query = "select id as walletId,transactiontype,amount,createddate,COUNT(1) OVER() as totalcount from wallethistory where walletid= ? order by createddate desc limit ? offset ? ;";
            try (PreparedStatement stmt = conn.prepareStatement(query);) {
                stmt.setInt(1,walletid);
                stmt.setInt(2,limit);
                stmt.setInt(3,limit*offset);

                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    WalletHistoryDto wallet=new WalletHistoryDto();
                    wallet.setId(rs.getInt("walletId"));
                    wallet.setAmount(rs.getDouble("amount"));
                    wallet.setTransactionType(TransactionType.getNameByValue(rs.getInt("transactiontype")));
                    wallet.setTransactionId(rs.getInt("transactiontype"));
                    wallet.setTransactionDate(rs.getTimestamp("createddate").toLocalDateTime());
                    wallet.setTotalCount(rs.getInt("totalcount"));
                    walletlist.add(wallet);
                }
            }
        }catch (SQLException e){
            System.out.println("Sales - Error in Connection.");
        }
        return walletlist;
    }

    public WalletDto getWallet(int id) throws SQLException {
        try {
            WalletDto wallet = null;

            String checkQuery = "SELECT COUNT(1) FROM wallets WHERE customerid = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkQuery);
            checkPs.setInt(1, id);
            ResultSet checkRs = checkPs.executeQuery();

            if(!checkRs.next() && checkRs.getInt(1) <= 0){
                createWalletAccount(id);
            }

            String query = "select w.id,w.customerid,w.amount,w.modifieddate,c.name as customername from wallets w join customers c on w.customerid=c.id where w.customerid= ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                wallet = new WalletDto();
                wallet.setCustomerName(rs.getString("customername"));
                wallet.setId(rs.getInt("id"));
                wallet.setCustomerId(rs.getInt("customerid"));
                wallet.setAmount(rs.getDouble("amount"));
                wallet.setLastmodifiedDate(rs.getTimestamp("modifieddate").toLocalDateTime());

            }
            return wallet;
        }
        catch (SQLException e){
            System.out.println("Error in getUser " +e.getMessage());
            throw e;
        }
    }

    public int createCustomer(Customer customer) throws SQLException {
        try{
            if(customer.getPassword()==null){
                customer.setPassword("1234");
            }
            String query = "INSERT INTO Customers (Name, EmailId, MobileNumber, Password, IsActive, Address, CreatedDate) VALUES (?, ?, ?, ?, ?, ?, NOW())";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmailId());
            stmt.setString(3, customer.getMobileNumber());
            stmt.setString(4, SecurityConfig.EncryptData(customer.getPassword()));
            stmt.setBoolean(5, true);
            stmt.setString(6, customer.getAddress());


            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        createWalletAccount(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("Signup completed successfully.");
            return rowsInserted;
        }
        catch (Exception e){
            System.out.println("Error in create user "+ e.getMessage());
            throw e;
        }
    }

    public int updateCustomer(Customer customer) throws SQLException{
        String query = "update customers set name=?,mobilenumber =?,emailid =?,address= ?,modifieddate =NOW() where id =?";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getMobileNumber());
            stmt.setString(3, customer.getEmailId());
            stmt.setString(4, customer.getAddress());
            stmt.setInt(5,customer.getId());

            int rowsInserted = stmt.executeUpdate();

            return rowsInserted;
        }
    }

    public int createEmployee(Employee employee,int currentUserId) throws SQLException {
        try{

            String query = "INSERT INTO Employees (Name,mobilenumber,emailId,password,roleId,isactive,createdby,createddate) VALUES (?, ?, ?,?,?,true,?,NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, employee.getName());
                stmt.setString(2, employee.getMobileNumber());
                stmt.setString(3, employee.getEmailId());
                stmt.setString(4, SecurityConfig.EncryptData(employee.getPassword()));
                stmt.setInt(5, Roles.Cashier.getValue());
                stmt.setInt(6, currentUserId);

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Employee added successfully.");
                }
                return rowsInserted;
            }

        }
        catch (Exception e){
            System.out.println("Error in create user "+ e.getMessage());
            throw e;
        }
    }

    public int updateEmployee(Employee employee ,int currentUserId) throws SQLException{
        try{

            String query = "update employees set name=?,mobilenumber =?,emailid =?,password=?,modifiedby =?,modifieddate =NOW() where id =?";
            try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, employee.getName());
                stmt.setString(2, employee.getMobileNumber());
                stmt.setString(3, employee.getEmailId());
                stmt.setString(4, SecurityConfig.EncryptData(employee.getPassword()));
                stmt.setInt(5,currentUserId);
                stmt.setInt(6, employee.getId());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Employee updated successfully.");
                }
                return rowsInserted;
            }

        }
        catch (Exception e){
            System.out.println("Error in create user "+ e.getMessage());
            throw e;
        }

    }

    public int changeUserPassword(PasswordChangeDto inputDto) throws SQLException{
        try{

            String query = "update employees set password=?,modifiedby =?,modifieddate =NOW() where id =?";
            try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, SecurityConfig.EncryptData(inputDto.getNewPassword()));
                stmt.setInt(2, inputDto.getUserId());
                stmt.setInt(3, inputDto.getUserId());

                int rowsInserted = stmt.executeUpdate();

                return rowsInserted;
            }

        }
        catch (Exception e){
            System.out.println("Error in create user "+ e.getMessage());
            throw e;
        }
    }

    public int changeCustomerPassword(PasswordChangeDto inputDto) throws SQLException{
        try{

            String query = "update customers set password=?,modifieddate =NOW() where id =?";
            try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, SecurityConfig.EncryptData(inputDto.getNewPassword()));
                stmt.setInt(2, inputDto.getUserId());

                int rowsInserted = stmt.executeUpdate();

                return rowsInserted;
            }

        }
        catch (Exception e){
            System.out.println("Error in create user "+ e.getMessage());
            throw e;
        }
    }

    public Set<Integer> getEmployeeIds(){
        Set<Integer> employeeList=new HashSet<>();
        try {
            String query = "SELECT id FROM Employees;";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                   int empId=rs.getInt("id");

                    employeeList.add(empId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }
    public boolean isSalaryProcessedForCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String currentMonthYear = currentDate.format(formatter);

        String query = "SELECT COUNT(*) FROM EmployeeSalaryHistory WHERE PayMonth = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, currentMonthYear);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Salary not processed for the current month
    }

    public void processEmpSalaries(List<EmpSalaryDto> salaries,int currentUserId){
        try  {
            String query = "INSERT INTO employeesalaryHistory (amount, deductions, pfdeductions,PayMonth,createdby,createddate,reference,employeeId) VALUES (?,?,?,?,?,Now(),?,?)";
            for(EmpSalaryDto emp:salaries){
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setDouble(1, emp.getAmount());
                    stmt.setDouble(2, emp.getDeductions());
                    stmt.setDouble(3, emp.getPfdeductions());
                    stmt.setString(4,emp.getPayMonth());
                    stmt.setInt(5,currentUserId);
                    stmt.setString(6,emp.getReferenceKey());
                    stmt.setInt(7,emp.getCustomerId());
                    stmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public Map<Integer,Double> getEmployeeSalaries(){
        Map<Integer,Double> employeeList=new HashMap<>();
        try {
            String query = "SELECT id,salary FROM Employees;";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int empId=rs.getInt("id");
                    double empSalary=rs.getDouble("salary");

                    employeeList.put(empId,empSalary);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public List<Employee> getEmployeeList(){
        List<Employee> employeeList=new ArrayList<>();
        try {
            String query = "SELECT id,name,mobilenumber,emailid,password,roleid,isactive FROM Employees;";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Employee employee=new Employee();
                    employee.setId(rs.getInt("id"));
                    employee.setName(rs.getString("name"));
                    employee.setMobileNumber(rs.getString("mobilenumber"));
                    employee.setEmailId(rs.getString("emailid"));
                    employee.setPassword(SecurityConfig.DecryptData(rs.getString("password")));
                    employee.getRole().setId(rs.getInt("roleid"));
                    employee.setActive(rs.getBoolean("isactive"));
                    employeeList.add(employee);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public void createWalletAccount(int customerId){
        try  {
            String query = "INSERT INTO wallets (customerId, amount, modifiedBy,modifiedDate) VALUES (?, ?, ?,NOW())";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, customerId);
                stmt.setInt(2, 0);
                stmt.setInt(3, customerId);

                stmt.executeUpdate();
                System.out.println("Wallet Account Created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public UserDto getUserByEmail(String email,String signInType) throws SQLException {
        try {

            UserDto user = null;
            if(signInType=="customer"){
                String query = "select Id as Userid,Name,EmailId,mobilenumber,address,Password,isactive from Customers where emailId = ? and isactive=true";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    user = new UserDto(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            Roles.Customer.getValue(),
                            SecurityConfig.DecryptData(rs.getString(6))
                    );
                }
            }else{
                String query = "select Id as Userid,Name,EmailId,mobilenumber,'' as address,RoleId,Password from Employees where emailId = ?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    user = new UserDto(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getInt(6),
                            SecurityConfig.DecryptData(rs.getString(7))
                    );
                }
            }

            return user;
        }
        catch (SQLException e){
            System.out.println("Error in getUser " +e.getMessage());
            throw e;
        }
    }

}
