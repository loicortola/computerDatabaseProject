package com.formation.project.computerDatabase.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.formation.project.computerDatabase.base.Company;
import com.formation.project.computerDatabase.base.Computer;
import com.formation.project.computerDatabase.base.ComputerBuilder;

public class JdbcDbComputerDao implements IComputerDao {
	
	private DaoFactory daoFactory = null;
	
	public JdbcDbComputerDao() {
	}
	
	public JdbcDbComputerDao(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Integer addComputer(Connection conn, Computer computer) {
		StringBuilder query	 = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			query.append("INSERT INTO computer(name, introduced, discontinued, id_company) ");
			query.append("VALUES(?,?,?,?);");
			ps = conn.prepareStatement(query.toString(),Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, computer.getName());
			ps.setTimestamp(2, new Timestamp(computer.getIntroduced().getTime()));
			if(computer.getDiscontinued() == null)
				ps.setTimestamp(3, null);
			else
				ps.setTimestamp(3, new Timestamp(computer.getDiscontinued().getTime()));
			ps.setInt(4, computer.getCompany().getId());
			ps.execute();
			ResultSet rs = ps.getGeneratedKeys();
			while(rs.next()) {
				return rs.getInt(1);
			}
				
		} catch (SQLException e) {
			System.out.println("Error in addComputer:" +e.getMessage());
		}
		return 0;
	}

	@Override
	public void updateComputer(Connection conn, Computer computer) {
		StringBuilder query	 = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			query.append("UPDATE computer ");
			query.append("SET name = ?, introduced = ?, discontinued = ?, id_company = ? ");
			query.append("WHERE id_computer = ?;");
			ps = conn.prepareStatement(query.toString());
			ps.setString(1, computer.getName());
			ps.setTimestamp(2, new Timestamp(computer.getIntroduced().getTime()));
			if(computer.getDiscontinued() == null)
				ps.setTimestamp(3, null);
			else
				ps.setTimestamp(3, new Timestamp(computer.getDiscontinued().getTime()));
			ps.setInt(4, computer.getCompany().getId());
			ps.setInt(5, computer.getId());
			ps.executeUpdate();
				
		} catch (SQLException e) {
			System.out.println("Error in updateComputer:" +e.getMessage());
		}
	}

	@Override
	public void deleteComputer(Connection conn, Integer computerId) {
		StringBuilder query	 = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			query.append("UPDATE computer ");
			query.append("SET is_visible = 0 ");
			query.append("WHERE id_computer = ?;");
			ps = conn.prepareStatement(query.toString());
			ps.setInt(1, computerId);
			ps.executeUpdate();				
		} catch (SQLException e) {
			System.out.println("Error in deleteComputer:" +e.getMessage());
		}
	}

	@Override
	public Computer getComputer(Integer computerId) {
		Connection conn 					= daoFactory.getConn();
		PreparedStatement ps 				= null;
		ResultSet rs 						= null;
		Computer computer			 		= null;
		ICompanyDao companyDao				= daoFactory.getCompanyDao();		
		HashMap<Integer,Company> companies	= companyDao.getCompanies("");
		
		try {
				ps = conn.prepareStatement("SELECT * FROM computer WHERE id_computer = ?;");
				ps.setInt(1, computerId);
				rs = ps.executeQuery();				
				while(rs.next())
				{
					computer = new ComputerBuilder()
									.id(rs.getInt("id_computer"))
									.name(rs.getString("name"))
									.introduced(rs.getTimestamp("introduced"))
									.discontinued(rs.getTimestamp("discontinued"))
									.company(companies.get(rs.getInt("id_company")))
									.build();
				}
			} catch (SQLException e) {
				System.out.println("Error in getComputer:" +e.getMessage());
			} finally {
				DaoFactory.closeConn(conn);	
			}

		return computer;
	}

	@Override
	public ArrayList<Computer> getComputers(Integer currentPage, Integer resultsPerPage, String sortBy, String name) {
		StringBuilder query					= new StringBuilder();
		Connection conn 					= daoFactory.getConn();
		PreparedStatement ps 				= null;
		ResultSet rs 						= null;
		ArrayList<Computer> computers 		= null;
		ICompanyDao companyDao				= daoFactory.getCompanyDao();		
		HashMap<Integer,Company> companies	= companyDao.getCompanies("");
		
		if(sortBy.equals("name1"))
			sortBy = "computer.name";
		else if(sortBy.equals("name0"))
			sortBy = "computer.name DESC";
		else if(sortBy.equals("introduced1"))
			sortBy = "introduced";
		else if(sortBy.equals("introduced0"))
			sortBy = "introduced DESC";
		else if(sortBy.equals("discontinued1"))
			sortBy = "discontinued";
		else if(sortBy.equals("discontinued0"))
			sortBy = "discontinued DESC";
		else if(sortBy.equals("company1"))
			sortBy = "company.name";
		else if(sortBy.equals("company0"))
			sortBy = "company.name DESC";
		
		query.append("SELECT computer.* FROM computer ");
		query.append("INNER JOIN company ON computer.id_company = company.id_company ");
		query.append("WHERE (LOWER(computer.name) LIKE CONCAT('%',?,'%') OR ? = '') ");
		query.append("AND is_visible = 1 ");		
		query.append(" ORDER BY ");
		query.append(sortBy);
		query.append(" LIMIT ?,?;");
		try {
				ps = conn.prepareStatement(query.toString());
				ps.setString(1, name);
				ps.setString(2, name);
				ps.setInt(3,(currentPage-1)*resultsPerPage);
				ps.setInt(4,resultsPerPage);
				
				
				rs = ps.executeQuery();
				computers = new ArrayList<Computer>();

				while(rs.next())
				{
					computers.add(new Computer(rs.getInt("id_computer"),rs.getString("name"),rs.getTimestamp("introduced"),rs.getTimestamp("discontinued"),companies.get(rs.getInt("id_company"))));
				}
			} catch (SQLException e) {
				System.out.println("Error in getComputers:" +e.getMessage());
			} finally {
				DaoFactory.closeConn(conn);	
			}

		return computers;
	}

	@Override
	public Integer getComputerCount(String name) {
		StringBuilder query					= new StringBuilder();
		Connection conn 					= daoFactory.getConn();
		PreparedStatement ps 				= null;
		ResultSet rs 						= null;
		Integer count						= null;
		try {
				query.append("SELECT COUNT(*) AS count FROM computer ");
				query.append("WHERE (LOWER(name) LIKE LOWER(CONCAT('%',?,'%')) OR ? = '') ");
				query.append("AND is_visible = 1;");
				ps = conn.prepareStatement(query.toString());
				ps.setString(1, name);
				ps.setString(2, name);
				rs = ps.executeQuery();
				while(rs.next())
				{
					count = rs.getInt("count");
				}
			} catch (SQLException e) {
				System.out.println("Error in getComputerCount:" +e.getMessage());
			} finally {
				DaoFactory.closeConn(conn);	
			}

		return count;
	}

	@Override
	public Integer getLastInsertId() {
		
		Connection conn 					= daoFactory.getConn();
		PreparedStatement ps 				= null;
		ResultSet rs 						= null;
		Integer lastInsertId				= null;
		try {
				ps = conn.prepareStatement("SELECT LAST_INSERT_ID() AS id;");
				rs = ps.executeQuery();
				while(rs.next())
				{
					lastInsertId = rs.getInt("id");
				}
			} catch (SQLException e) {
				System.out.println("Error in getLastInsertId:" +e.getMessage());
			} finally {
				DaoFactory.closeConn(conn);	
			}

		return lastInsertId;
	}

}
