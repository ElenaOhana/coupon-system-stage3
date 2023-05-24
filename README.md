
Second step of the project - SpringJPA & SpringMVC. (end-points for client requests, filters - authentication & authorization, Spring AOP for maintain exceptions).

For running the project should be created the <coupon_system_stage3> schema in MySQL Database.

Tomcat will run on port=80.
Servlet context-path is: /my-coupon-app
Each run will create a data from the scratch in DB by DummyData class that implements CommandLineRunner.
After that CLR will perform tests in this order: AdminServiceTest, CompanyServiceTest, CustomerServiceTest and DailyJobTest.

For the REST testing the Postman requests collection is here:
https://www.getpostman.com/collections/7eef70695f2d5b71c70e


In my project I don't delete coupons/companies/customers, instead of it I change their enum Status.
The only 3 methods that really deletes a table row - deletes CustomerPurchase:
Integer removeCoupon(@NotNull Long id) in interface CompanyService, 
Integer deleteCustomerAsChangeStatus(Long customerID) in interface AdminService,
Integer deleteCompanyAsChangeStatus(Long id) in interface AdminService.


