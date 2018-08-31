package app.springbootdemo.service;


import app.springbootdemo.database.dbmodel.*;
import app.springbootdemo.database.mapper.EmployeeMapper;
import app.springbootdemo.database.mapper.TelephoneMapper;
import app.springbootdemo.database.repository.*;
import app.springbootdemo.exceptions.StartTimeAlreadyRecordedException;
import app.springbootdemo.service.mapper.EmployeeBOMapper;
import app.springbootdemo.service.mapper.TelephoneBOMapper;
import app.springbootdemo.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeService {


    final EmployeeRepository employeeRepository;

    final TimeTableRepository timeTableRepository;

    final HoliDayRepository holiDayRepository;

    final IllRepository illRepository;

    final TelephoneRepository telephoneRepository;

    final AddressRepository addressRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, TimeTableRepository timeTableRepository, HoliDayRepository holiDayRepository,
                           IllRepository illRepository, TelephoneRepository telephoneRepository, AddressRepository addressRepository) {
        this.employeeRepository = employeeRepository;
        this.timeTableRepository = timeTableRepository;
        this.holiDayRepository = holiDayRepository;
        this.illRepository = illRepository;
        this.telephoneRepository = telephoneRepository;
        this.addressRepository = addressRepository;
    }

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        Iterable<Employee> employees = employeeRepository.findAll();
        employees.forEach(list::add);
        return list;
    }

    public EmployeeBO postEmployee(EmployeeBO employeeBO) {
        EmployeeBO employeeBO1 = EmployeeBOMapper.from(employeeRepository.save(EmployeeMapper.from(employeeBO)));
        return employeeBO1;
    }

    public TelephoneBO phone(TelephoneBO telephoneBO){
        Employee emp = employeeRepository.findById((telephoneBO.getEmpId())).get();
        //emp.getId();
        TelephoneBO telephoneBO1 = TelephoneBOMapper.from(telephoneRepository.save(TelephoneMapper.from(telephoneBO)));
        return telephoneBO1;

    }


    public void ill(IllBO illBO) {
        Date startDate = illBO.getIllFromDate();// + "8:00";
        Date endDate = illBO.getIllToDate();// + "16:00";
        Employee emp = employeeRepository.findById((illBO.getEmpId())).get();
        Ill ill = new Ill();
        ill.setEmployee(emp);
        ill.setBegin(startDate);
        ill.setEnd(endDate);
        ill.setBegin_break(null);
        ill.setEnd_break(null);
        emp.getTimeTable().add(ill);
        illRepository.save(ill);


        //System.out.println(emp.getFirstName());
        //System.out.println(emp.getId());
        //emp.getTimeTable().add(IllMapper.from(startTime, endTime, begin_Break, end_Break));
        //System.out.println("/////////////////////////////////////////   " + timeTable.getEmployee().getId());
    }


    public void holiDay(HoliDayBO holiDayBO) {
        Date startDate = holiDayBO.getFromDate();
        Date endDate = holiDayBO.getToDate();// + "16:00";
        Employee emp = employeeRepository.findById((holiDayBO.getId())).get();
        HoliDay holiDay = new HoliDay();
        holiDay.setEmployee(emp);
        holiDay.setBegin(startDate);
        holiDay.setEnd(endDate);
        holiDay.setBegin_break(null);
        holiDay.setEnd_break(null);
        emp.getTimeTable().add(holiDay);
        holiDayRepository.save(holiDay);

    }


    public List<Employee> findByLastName(String lastName) {
        List<Employee> employee = employeeRepository.findByLastName(lastName);
        return employee;
    }


    public void deleteEmployee(long id){
        employeeRepository.deleteById(id);
    }


    public void startTime(long pEmployeeId){

        Employee emp = employeeRepository.findById(pEmployeeId).get();
        Set<TimeTable> lastTimeTable = timeTableRepository.findStartTimeforEmpId(pEmployeeId);
        if(lastTimeTable.size()>=1) {
            throw new StartTimeAlreadyRecordedException("Start time already logged");
        }
        TimeTable lcWorkingDay = new TimeTable();
        // lcWorkingDay.setId(emp.getId()); //new
        lcWorkingDay.setEmployee(emp);   //new
        lcWorkingDay.setBegin(new Date());
        emp.getTimeTable().add(lcWorkingDay);
        timeTableRepository.save(lcWorkingDay);   //timetablerepository    save 1cworkingday


    }

    public void endTime(long pEmployeeId) {
        Employee emp = employeeRepository.findById(pEmployeeId).get();
        Optional<TimeTable> currentTimeTableOptional = timeTableRepository.findForCurrentTimeTableForEmployee(emp.getId()).stream().findFirst();
        if (currentTimeTableOptional.isPresent()) {
            TimeTable currentTimeTable = currentTimeTableOptional.get();
            currentTimeTable.setEnd(new Date());
            timeTableRepository.save(currentTimeTable);
        }

        }


    public void startBreakTime(long pEmployeeId) {
        Employee emp = employeeRepository.findById(pEmployeeId).get();
        Optional<TimeTable> currentTimeTableOptional = timeTableRepository.currentTimeTableForEmployee1(emp.getId()).stream().findFirst();
        if (currentTimeTableOptional.isPresent()) {
            TimeTable currentTimeTable = currentTimeTableOptional.get();
            currentTimeTable.setBegin_break(new Date());
            timeTableRepository.save(currentTimeTable);
        }
    }


    public void stopBreakTime(long pEmployeeId){
        Employee emp = employeeRepository.findById(pEmployeeId).get();
        Optional<TimeTable> currentTimeTableOptional = timeTableRepository.currentTimeTableForEmployee2(emp.getId()).stream().findFirst();
        if (currentTimeTableOptional.isPresent()) {
            TimeTable currentTimeTable = currentTimeTableOptional.get();
            currentTimeTable.setEnd_break(new Date());
            timeTableRepository.save(currentTimeTable);



   /* public void endTime(long pEmployeeId){
        Employee emp = employeeRepository.findById(pEmployeeId).get();
        TimeTable lcWorkingDay = (TimeTable) (emp.getTimeTable().toArray())[0];
        lcWorkingDay.setEnd(new Date());

        Date newdate = new Date();
        newdate.setYear(2019);
        newdate.setMonth(11);

        timeTableRepository.save(lcWorkingDay);
    }*/

   /* public Employee findEmployeewithId(long id) {

        Employee employee = employeeRepository.findEmployeewithId(id);

        return employee;
    }*/

        }
    }

    public void addAddress(AddressBO addressBO) {

        Address address = new Address();
        Employee emp = employeeRepository.findById((addressBO.getEmpId())).get();
        address.setEmployee(emp);
        address.setStreet(addressBO.getStreet());
        address.setPostcode(addressBO.getPostcode());
        address.setType(addressBO.getType());
        addressRepository.save(address);

    }

    public void addContactDetails(TelephoneBO telephoneBO) {

        Telephone telephone = new Telephone();
        Employee emp = employeeRepository.findById((telephoneBO.getEmpId())).get();
        telephone.setEmployee(emp);
        telephone.setPhone(telephoneBO.getPhone());
        telephone.setType(telephoneBO.getType());
        telephoneRepository.save(telephone);
    }
}