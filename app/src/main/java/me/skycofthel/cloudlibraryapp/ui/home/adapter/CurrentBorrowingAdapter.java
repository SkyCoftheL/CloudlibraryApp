package me.skycofthel.cloudlibraryapp.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.skycofthel.cloudlibraryapp.R;
import me.skycofthel.cloudlibraryapp.domain.SelectNewBooks;

public class CurrentBorrowingAdapter extends ArrayAdapter<SelectNewBooks.DataDTO.RowsDTO> {
    private Context context;
    private int resource;
    private List<SelectNewBooks.DataDTO.RowsDTO> data;
    private List<Integer> booksId;
    private List<String> bookList;

//    private Map<String,String> data;

    public CurrentBorrowingAdapter(Context context, int resource, List<SelectNewBooks.DataDTO.RowsDTO> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        booksId=new ArrayList<>();
        bookList=new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView bookName = convertView.findViewById(R.id.book_name_tx);
        TextView bookBorrower=convertView.findViewById(R.id.book_borrower_tx);
        TextView bookLoanTime = convertView.findViewById(R.id.book_author_tx);
        TextView bookReturnTime = convertView.findViewById(R.id.book_status_tx);

        SelectNewBooks.DataDTO.RowsDTO datum=data.get(position);

        booksId.add(datum.getId());
        bookList.add(datum.getName());

        bookName.setText(datum.getName());
        bookBorrower.setText(datum.getBorrower());
        bookLoanTime.setText(datum.getBorrowTime());
        bookReturnTime.setText(datum.getReturnTime());




//        for (SelectNewBooks.DataDTO.RowsDTO datum : data) {
//            bookName.setText(datum.getName());
//            bookAuthor.setText(datum.getAuthor());
//            bookStatus.setText(datum.getStatus());
//        }

        return convertView;
    }

    public int getBooksId(int position){

        return booksId.get(position);
    }

    public String getBookName(int position){
        return bookList.get(position);
    }
}
