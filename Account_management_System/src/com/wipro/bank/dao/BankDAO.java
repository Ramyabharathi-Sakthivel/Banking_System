package com.wipro.bank.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.util.DBUtil;

public class BankDAO {

	public int generatesequence() {
		Connection connection = DBUtil.getConnection();
		String query = "SELECT transactionID_seq1.NEXTVAL FROM dual";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			return 0;
		}
	}

	public boolean validateAccount(String accountNumber) {
		Connection connection = DBUtil.getConnection();
		String query = "SELECT 1 FROM ACCOUNT_TABLE1 WHERE Account_Number = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, accountNumber);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
	}

	public float findBalance(String accountNumber) {
		if (!validateAccount(accountNumber))
			return -1;

		Connection connection = DBUtil.getConnection();
		String query = "SELECT Balance FROM ACCOUNT_TABLE1 WHERE Account_Number = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, accountNumber);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getFloat(1);
			return -1;
		} catch (SQLException e) {
			return -1;
		}
	}

	public boolean transferMoney(TransferBean transferBean) {
		Connection connection = DBUtil.getConnection();
		String query = "INSERT INTO TRANSFER_TABLE1 VALUES (?,?,?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setInt(1, transferBean.getTransactionID());
			ps.setString(2, transferBean.getFromAccountNumber());
			ps.setString(3, transferBean.getToAccountNumber());
			Date d = new Date(transferBean.getDateOfTransaction().getTime());
			ps.setDate(4, d);
			ps.setFloat(5, transferBean.getAmount());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}

	public boolean updateBalance(String accountNumber, float newBalance) {
		Connection connection = DBUtil.getConnection();
		String query = "UPDATE ACCOUNT_TABLE1 SET Balance = ? WHERE Account_Number = ?";
		try {
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setFloat(1, newBalance);
			ps.setString(2, accountNumber);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			return false;
		}
	}
}
