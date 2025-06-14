package me.skycofthel.cloudlibraryapp.ui.workbench;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.skycofthel.cloudlibraryapp.R;

public class WorkbenchFragment extends Fragment {

    private View root;
    private Button addNewBooks,confirmReturnBooks,editBooksInfor;
    private FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (root==null) root=inflater.inflate(R.layout.fragment_workbench,container,false);

        addNewBooks = root.findViewById(R.id.add_new_books_btn);
        editBooksInfor=root.findViewById(R.id.edit_books_btn);
        confirmReturnBooks=root.findViewById(R.id.confirm_return_books_btn);


        floatingActionButton=root.findViewById(R.id.fab);

        initLister();

        return root;
        // TODO: 2025/6/11 新增图书 编辑图书 还书确认
    }

    private void initLister() {
        addNewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddNewBooksActivity.class));
            }
        });

        editBooksInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),EditBooksInformationActivity.class));
            }
        });

        confirmReturnBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),ConfirmReturnBooksActivity.class));
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(new Intent(getContext(),AddNewBooksActivity.class)));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}