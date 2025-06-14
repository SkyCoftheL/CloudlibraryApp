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

public class NewArrivesAdapter extends ArrayAdapter<SelectNewBooks.DataDTO.RowsDTO> {
    private Context context;
    private int resource;
    private List<SelectNewBooks.DataDTO.RowsDTO> data;
    private List<Integer> booksId;
    private List<String> bookList;

//    private Map<String,String> data;

    public NewArrivesAdapter(Context context, int resource, List<SelectNewBooks.DataDTO.RowsDTO> data) {
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
        TextView bookAuthor = convertView.findViewById(R.id.book_author_tx);
        TextView bookStatus = convertView.findViewById(R.id.book_status_tx);

        SelectNewBooks.DataDTO.RowsDTO datum=data.get(position);

        booksId.add(datum.getId());
        bookList.add(datum.getName());

        bookName.setText(datum.getName());
        bookAuthor.setText(datum.getAuthor());
        String status = datum.getStatus();
        if (status.equals("0")) {
            bookStatus.setText(R.string.title_available_for_borrowing);
        } else if (status.equals("2")) {
            bookStatus.setText(R.string.title_return_in_progress);
        } else {
            bookStatus.setText(R.string.title_borrowing_in_progress);
        }



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
