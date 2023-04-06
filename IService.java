package com.service;

import java.util.List;

import com.model.BookEntry;
import com.model.BookTransaction;

public interface IService {

	public String save(BookTransaction bookTransaction);

	public BookTransaction getById(long id);

	public String updateById(BookTransaction bookTransaction);
	
	public BookEntry valueOfbookEntryUiByIndex(int j,List<BookEntry> bookEntryAllFromUi);

}
