package Controller;

import DTO.*;
import Extension.TransactionType;
import Models.Customer;
import Models.Employee;
import Services.NetBankingService;
import Services.PaymentService;
import Services.UserServices;
import views.Sales.BillingPage;
import views.User.AdminPage;
import views.User.CustomerPage;
import views.User.SignInPage;
import views.User.SignupPage;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class UserController {
    UserServices userService = new UserServices();

    public int signUp() throws  SQLException {
        try{
            Customer customer = new Customer();
            SignupPage.SignupUser(customer);
            return userService.createCustomer(customer);
        }
        catch (Exception e){
            System.out.println("Error inside sign up"+ e.getMessage());
            return 0;
        }
    }

    public int addNewEmployee(int currentUserId) throws SQLException{
        try{
            Employee employee;
            employee=AdminPage.addEmployee();
            return userService.createEmployee(employee,currentUserId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public void processEmployeeSalary(int currentUserId){
        LocalDate currentDate = LocalDate.now();
        String paymonth=currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)+" "+currentDate.getYear();
        if(userService.isSalaryProcessedForCurrentMonth()){
            System.out.println("Already Salary Process for this month "+paymonth);
        }else{
            List<EmpSalaryDto> salaryList=new ArrayList<>();

            Map<Integer, Double> customerPayments=userService.getEmployeeSalaries();

            NetBankingService _paymentService=new NetBankingService();

            for (Map.Entry<Integer, Double> entry : customerPayments.entrySet()) {
                EmpSalaryDto salary=new EmpSalaryDto();
                salary.setCustomerId(entry.getKey());
                salary.setAmount(entry.getValue());

                String reference=_paymentService.processPayment(salary.getAmount(),String.valueOf(salary.getCustomerId()));
//                System.out.println("Customer ID: " + salary.getCustomerId() + ", Amount: " + salary.getAmount());

                salary.setReferenceKey(reference);

                salary.setPfdeductions((salary.getAmount()-3000)*12/100);

                salary.setPayMonth(paymonth);

                salaryList.add(salary);
            }
            userService.processEmpSalaries(salaryList,currentUserId);

            AdminPage.displaySalaryProcessed(salaryList);
        }
    }

    public int updateEmployee(int currentUserId) throws SQLException{
        try{
            List<Employee> employees=this.getEmployeeList();
            Employee employee;
            employee=AdminPage.getEmployee(employees);
            if(employee!=null){
                AdminPage.updateEmployee(employee);
                return userService.updateEmployee(employee,currentUserId);
            }else{
                System.out.println("Invalid employeeId");
                return 0;
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<Employee> getEmployeeList(){
        List<Employee> employees=new ArrayList<>();
        try{
            employees=userService.getEmployeeList();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public void showCustomerInfo(int currentuserId){
        try{
            Customer user=userService.getCustomer(currentuserId);
            int choice=CustomerPage.displyaCustomerProfile(user);
            if(choice==0){
                return;
            }else{
                handleCustomerProfile(choice,currentuserId);
            }

        }catch (Exception e){
            System.out.println("User - Error while processing your request.");
        }
    }

    public void handleCustomerProfile(int choice,int currentUserId){
        try{
            switch (choice){
                case 0:
                    return;
                case 1:
                    WalletDto wallet=userService.getWallet(currentUserId);
                    int walletchoice=CustomerPage.displayWalletInfo(wallet);
                    if(walletchoice==0){
                        return;
                    }else if(walletchoice==1){
                        showWalletHistory(wallet.getId(),currentUserId);
                    }else if(walletchoice==2){
                        handleWalletTopup(wallet,currentUserId);
                    }
                    break;
                case 2:
                    handleProfileUpdate(currentUserId);
                    break;
                default:
                    System.out.println("Invalid Input");
            }
        }catch (Exception e){
            System.out.println("User - Error while processing your request");
        }
    }

    public void handleProfileUpdate(int currentUserId){
        try{
            Customer customer=userService.getCustomer(currentUserId);
            customer=CustomerPage.updateCustomerPage(customer);
            int result=userService.updateCustomer(customer);
            if(result>0){
                System.out.println("Customer Updated Successfully");
            }else{
                System.out.println("Error in Customer Update. Please try again later.");
            }
        }catch (Exception e){
            System.out.println("User - Error while processing your request");
        }
    }

    public void handleWalletTopup(WalletDto wallet,int customerId) throws InputMismatchException, InterruptedException {
        double topupamount=CustomerPage.getwalletTopup(wallet.getAmount());
        if(topupamount<10){
            System.out.println("Invalid Amount. Amount should not be less than 10.");
            return;
        }
        WalletTopUpDto topupinput=new WalletTopUpDto();
        topupinput.setWalletamount(topupamount);
        topupinput.setUserId(customerId);
        topupinput.setWalletId(wallet.getId());
        topupinput.setTransactionType(TransactionType.Credit.getValue());
        System.out.println("Processing your Request");
        userService.addMoneyInWallet(topupinput);
        Thread.sleep(3000);
        System.out.println("Amount Successfully Added");
    }

    public void showWalletHistory(int walletId,int customerId){
        try{
            int limit=10,offset=0;
            while (true){
                List<WalletHistoryDto> myorders=userService.getWalletHistory(walletId,customerId,limit,offset);

                if(myorders.size()>0){
                    int totalpages=(int)Math.ceil((double) myorders.get(0).getTotalCount()/limit);

                    CustomerPage.displayWalletDetails(myorders,totalpages,offset);

                    int choice=CustomerPage.displayWalletHistoryMenu(totalpages,offset);

                    if(choice==0){
                        break;
                    }
                    switch (choice){
                        case 1:

                            if(totalpages!=offset+1 && totalpages!=1){
                                offset++;
                            }else{
                                System.out.println("Invalid Input");
                            }
                            break;
                        case 2:
                            if((offset+1)!=1 && totalpages!=1){
                                offset--;
                            }else{
                                System.out.println("Invalid Input");
                            }
                            break;
                        default:
                            System.out.println("Invalid Input");
                            break;
                    }
                }else{
                    System.out.println("No Record Found");
                    break;
                }
            };
        }catch (Exception e){
            System.out.println("Sales - Error while Processing your request");
        }
    }

    public void showEmployeeList(){
        List<Employee> employees=new ArrayList<>();
        try{
            employees=userService.getEmployeeList();
            AdminPage.displayEmployeeList(employees);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public UserDto signIn() throws SQLException {
        try{
            return SignInPage.signIn();
        }
        catch (InputMismatchException e){
            System.out.println("Invalid Input. Please enter correct input.");
        }
        catch (Exception e){
            System.out.println("Error inside sign in "+ e.getMessage());
            throw e;
        }
        return null;
    }

    public int changeUserPassword(PasswordChangeDto currentUser){
        try{
            currentUser=AdminPage.changePassword(currentUser);
            return userService.changeUserPassword(currentUser);
        }catch (Exception e){
            System.out.println("Failed to Change Password.");
        }
        return 0;
    }

    public int changeCustomerPassword(PasswordChangeDto currentUser){
        try{
            currentUser=AdminPage.changePassword(currentUser);
            return userService.changeCustomerPassword(currentUser);
        }catch (Exception e){
            System.out.println("Failed to Change Password.");
        }
        return 0;
    }
}
