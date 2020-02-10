package employee;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import aws.util.*;

@Path("/employee")
public class EmployeeResource {

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/add")
	public Response addEmployee(@FormParam("forename") String forename, @FormParam("surname") String surname,
			@FormParam("dateOfBirth") String dateOfBirth,@FormParam("jobTitle") String jobTitle) {
		try {
			//@FormParam("qualificationTitle") String qualificationTitle,
			//@FormParam("dateCompleted") String dateCompleted, @FormParam("expiryDate") String expiryDate
			dateOfBirth = dateOfBirth.replaceAll("/", "-");
			//dateCompleted = dateCompleted.replaceAll("/", "-");
			//expiryDate = expiryDate.replaceAll("/", "-");
			Employee employee = new Employee(forename, surname, dateOfBirth, jobTitle); //, qualificationTitle, dateCompleted, expiryDate
			DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION, Config.LOCAL_ENDPOINT);
			mapper.save(employee);
			return Response.status(201).entity("New employee details saved").build();
		} catch (Exception e) {
			return Response.status(400).entity("Error in saving Employee").build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/get={id}")
	public Employee getOneEmployee(@PathParam("id") UUID id) {
		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION, Config.LOCAL_ENDPOINT);		
		System.out.println("In get one employee");
		Employee employee = mapper.load(Employee.class, id);
		System.out.println(employee.getForename());
		if (employee == null)
			throw new WebApplicationException(404);

		return employee;
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update/{id}/forename={forename}/surname={surname}/dateOfBirth={dateOfBirth}/jobTitle={jobTitle}")
	public Response updateEmployee(@PathParam("id") UUID id, @PathParam("forename") String forename,
			@PathParam("surname") String surname, @PathParam("dateOfBirth") String dateOfBirth,
			@PathParam("jobTitle") String jobTitle) {
		//+ "/qualificationTitle={qualificationTitle}/dateCompleted={dateCompleted}/expiryDate={expiryDate}
		//, @PathParam("qualificationTitle") String qualificationTitle,
		//@PathParam("dateCompleted") String dateCompleted, @PathParam("expiryDate") String expiryDate
		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION, Config.LOCAL_ENDPOINT);
		Employee Employee = mapper.load(Employee.class, id);
		
		if (Employee == null) throw new WebApplicationException(404);
		
		System.out.println("updateEmployee");
		Employee newEmployee = new Employee(id, forename, surname, dateOfBirth, jobTitle); //, qualificationTitle, dateCompleted, expiryDate
		mapper.delete(Employee);
		mapper.save(newEmployee);
		return Response.status(201).entity("Employee updated").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/search")
	public Collection<Employee> getAllEmployees() {
		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION, Config.LOCAL_ENDPOINT);
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<Employee> result = mapper.scan(Employee.class, scanExpression);
		List<Employee> finalResult = new ArrayList<>();
		//using different list to pass on employees, otherwise an error is thrown;
		for (int i = 0; i < result.size(); i++) { 
			finalResult.add(result.get(i));
		}
		return finalResult;
	}

	@Path("/delete/{id}")
	@DELETE
	public Response deleteOneEmployee(@PathParam("id") UUID id) {
		DynamoDBMapper mapper = DynamoDBUtil.getDBMapper(Config.REGION, Config.LOCAL_ENDPOINT);
		Employee Employee = mapper.load(Employee.class, id);
		if (Employee == null)
			throw new WebApplicationException(404);

		mapper.delete(Employee);
		return Response.status(200).entity("deleted").build();
	}

}
