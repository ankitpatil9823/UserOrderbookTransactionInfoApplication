package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.BookEntry;
import com.model.BookTransaction;
import com.repository.BT_Repo;

@Service
public class Service_Impl implements IService {

	@Autowired
	private BT_Repo bT_Repo;

// New Book_Transaction_Entry save into DB	

	public String save(BookTransaction bookTransaction) {
		java.util.List<BookEntry> bookEntry1 = bookTransaction.getBookEntry();

		int debit = 0;
		int credit = 0;

		for (int i = 0; i < bookEntry1.size(); i++) {

			debit = (int) (debit + bookEntry1.get(i).getDebit_amt());

			credit = (int) (credit + bookEntry1.get(i).getCredit_amt());
		}

		if (debit == credit) {

			BookTransaction bookTransaction1 = bT_Repo.save(bookTransaction);
			System.out.println("Ankit" + bookTransaction1);

			return "saved";
		} else {

			return "The ammount of debit or credit are not equal";
		}

	}

// Get Book_Transaction_Entry data  from DB by using book_transaction_Id 

	@Override
	public BookTransaction getById(long id) {
		// TODO Auto-generated method stub
		java.util.Optional<BookTransaction> optional = bT_Repo.findById(id);

		BookTransaction bookTransaction = optional.get();

		List<BookEntry> b1 = bookTransaction.getBookEntry();

		List<BookEntry> bookEntryNew = new ArrayList<BookEntry>();

		for (int i = 0; i < b1.size(); i++) {
			BookEntry singleBookEntryObject = b1.get(i);

			String statusOfSingleBookEntryObject = singleBookEntryObject.getAccess_Status();

			if (statusOfSingleBookEntryObject.equals("Active")) {

				bookEntryNew.add(singleBookEntryObject);
			}

		}

		bookTransaction.setBookEntry(bookEntryNew);

		return bookTransaction;
	}

// Updated Object

	@SuppressWarnings("unused")
	@Override
	public String updateById(BookTransaction bookTransaction_uiList) {

		Long id = bookTransaction_uiList.getBook_transaction_id();
		java.util.Optional<BookTransaction> existing_Db = bT_Repo.findById(id);
		BookTransaction existing_Database = existing_Db.get();

		existing_Database.setBook_transaction_id(bookTransaction_uiList.getBook_transaction_id());
		existing_Database.setBook_entry_name(bookTransaction_uiList.getBook_entry_name());
		existing_Database.setBuilding_id(bookTransaction_uiList.getBuilding_id());
		existing_Database.setDate(bookTransaction_uiList.getDate());
		existing_Database.setDescription(bookTransaction_uiList.getDescription());
		existing_Database.setLandlord_id(bookTransaction_uiList.getLandlord_id());
		existing_Database.setUnit_id(bookTransaction_uiList.getUnit_id());

		List<BookEntry> bookEntryAllFromDb = existing_Database.getBookEntry();
		List<BookEntry> bookEntryAllFromUi = bookTransaction_uiList.getBookEntry();
		List<BookEntry> bookEntryNewDb = new ArrayList<>();


		String str = "Not Active";

		int j = 0;

		for (int i = 0; i < bookEntryAllFromDb.size()+bookEntryAllFromUi.size()-j; i++) {

			//BookEntry bookEntryDbByIndex = bookEntryAllFromDb.get(i);
			
			BookEntry bookEntryDbByIndex = valueOfbookEntryDbByIndex(i,bookEntryAllFromDb);
			

			BookEntry bookEntryUiByIndex = valueOfbookEntryUiByIndex(j, bookEntryAllFromUi);

			System.out.println("=============" + bookEntryUiByIndex);
			
			if(i>bookEntryAllFromDb.size())
			{
				BookEntry bookEntry = bookEntryAllFromUi.get(i);
				
				BookEntry bookEntryNew = new BookEntry();
				
				bookEntryNew.setBook_entry_id(bookEntry.getBook_entry_id());
				bookEntryNew.setCredit_amt(bookEntry.getCredit_amt());
				bookEntryNew.setDebit_amt(bookEntry.getDebit_amt());
				bookEntryNew.setGlAccount(bookEntry.getGlAccount());
				bookEntryNew.setNature(bookEntry.getNature());
				
				bookEntryNewDb.add(bookEntryNew);
			}

			if (bookEntryUiByIndex == null) {
				bookEntryDbByIndex.setAccess_Status(str);
				bookEntryDbByIndex.setGlAccount("assett");
				bookEntryNewDb.add(bookEntryDbByIndex);

			} else if ((bookEntryDbByIndex.getBook_entry_id() != bookEntryUiByIndex.getBook_entry_id())) {
				bookEntryDbByIndex.setAccess_Status(str);
				bookEntryDbByIndex.setGlAccount("assett");
				bookEntryNewDb.add(bookEntryDbByIndex);
			} else if (bookEntryDbByIndex.getBook_entry_id() == bookEntryUiByIndex.getBook_entry_id()) {

				bookEntryDbByIndex.setBook_entry_id(bookEntryUiByIndex.getBook_entry_id());
				bookEntryDbByIndex.setCredit_amt(bookEntryUiByIndex.getCredit_amt());
				bookEntryDbByIndex.setDebit_amt(bookEntryUiByIndex.getDebit_amt());
				bookEntryDbByIndex.setGlAccount(bookEntryUiByIndex.getGlAccount());
				bookEntryDbByIndex.setNature(bookEntryUiByIndex.getNature());
				

				bookEntryNewDb.add(bookEntryDbByIndex);

				j++;

			}
			
			
			
		/*	if(i==bookEntryAllFromDb.size()-1)
			{
				for(int z=0;z<bookEntryAllFromUi.size()-j;z++)
				{
					BookEntry bookEntry = bookEntryAllFromUi.get(z+j);
					
					BookEntry bookEntryNew = new BookEntry();
					
					bookEntryNew.setBook_entry_id(bookEntry.getBook_entry_id());
					bookEntryNew.setCredit_amt(bookEntry.getCredit_amt());
					bookEntryNew.setDebit_amt(bookEntry.getDebit_amt());
					bookEntryNew.setGlAccount(bookEntry.getGlAccount());
					bookEntryNew.setNature(bookEntry.getNature());
					
					bookEntryNewDb.add(bookEntryNew);
				}
			}*/

		}
		existing_Database.setBookEntry(bookEntryNewDb);

		bT_Repo.save(existing_Database);

		return "Updated";

	}

	public BookEntry valueOfbookEntryUiByIndex(int j, List<BookEntry> bookEntryAllFromUi) {

		int length = bookEntryAllFromUi.size();

		if (j < length) {
			BookEntry bookEntry = bookEntryAllFromUi.get(j);

			return bookEntry;
		} 
			
		return null;

		

	}
	
	public BookEntry valueOfbookEntryDbByIndex(int i,List<BookEntry> bookEntryAllFromDb)
	{
		int lengthOfBookEntryAllFromDb = bookEntryAllFromDb.size();
		
		if(i<lengthOfBookEntryAllFromDb)
		{
			BookEntry bookEntry = bookEntryAllFromDb.get(i);
			
			return bookEntry;
		}
		
		return null;
		
	}
			
}