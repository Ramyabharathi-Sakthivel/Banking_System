package com.wipro.bank.service;

import com.wipro.bank.bean.TransferBean;
import com.wipro.bank.dao.BankDAO;
import com.wipro.bank.util.InsufficientFundsException;

public class BankService {

	BankDAO d = new BankDAO();

	public String checkBalance(String accountNumber) {
		boolean flag = d.validateAccount(accountNumber);
		if (flag) {
			float balance = d.findBalance(accountNumber);
			return "Balance: " + balance;
		} else {
			return "INVALID BALANCE";
		}
	}

	public String transfer(TransferBean transferBean) {

		String fromAcc = transferBean.getFromAccountNumber();
		String toAcc = transferBean.getToAccountNumber();
		float amount = transferBean.getAmount();

		if (!d.validateAccount(fromAcc) || !d.validateAccount(toAcc)) {
			return "INVALID ACCOUNT";
		}

		float fromBalance = d.findBalance(fromAcc);

		try {
			if (fromBalance < amount) {
				throw new InsufficientFundsException();
			}

			d.updateBalance(fromAcc, fromBalance - amount);

			float toBalance = d.findBalance(toAcc);
			d.updateBalance(toAcc, toBalance + amount);

			transferBean.setTransactionID(d.generatesequence());
			d.transferMoney(transferBean);

			return "TRANSFER SUCCESSFUL";

		} catch (InsufficientFundsException e) {
			return "INSUFFICIENT FUNDS";
		}
	}
}
