package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity<onItemEditClick> extends AppCompatActivity implements UserAdapter.UserCallback{
    RecyclerView rvListCode;
    ArrayList<User> lstUser;
    UserAdapter userAdapter;
    FloatingActionButton fbAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvListCode = findViewById(R.id.rvList);
        fbAdd = findViewById(R.id.fbAdd);
        fbAdd.setOnClickListener(view -> addUserDialog());

        lstUser = UserDataQuery.getAll(this);
        userAdapter = new UserAdapter(lstUser);
        userAdapter.setCallback(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvListCode.setAdapter(userAdapter);
        rvListCode.setLayoutManager(linearLayoutManager);
    }

    void addUserDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Thêm mới");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        alertDialog.setView(dialogView);
        EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        EditText edAvatar = (EditText) dialogView.findViewById(R.id.edAvatar);
        alertDialog.setPositiveButton("Đồng ý", (dialog, which) -> {
            String name = edName.getText().toString();
            String avatar = edAvatar.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Nhập dữ liệu không hợp lệ", Toast.LENGTH_LONG).show();
            } else {
                User us = new User(0, name, avatar);
                long id = UserDataQuery.insert(MainActivity.this, us);
                if (id > 0) {
                    Toast.makeText(MainActivity.this, "Thêm người dùng thành công", Toast.LENGTH_LONG).show();
                    resetData();
                    dialog.dismiss();
                }
            }
        });
        alertDialog.setNegativeButton("Hủy", (dialog, which) -> {
           dialog.dismiss();
        });
        alertDialog.create();
        alertDialog.show();
    }

    void updateUserDialog(User us) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Cập nhật");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        alertDialog.setView(dialogView);
        EditText edName = (EditText) dialogView.findViewById(R.id.edName);
        EditText edAvatar = (EditText) dialogView.findViewById(R.id.edAvatar);

        edName.setText(us.getName());
        edAvatar.setText(us.getAvatar());

        alertDialog.setPositiveButton("Đồng ý", (dialog, which) -> {
            us.setName(edName.getText().toString());
            us.setAvatar(edAvatar.getText().toString());

            if (us.name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Nhập dữ liệu không hợp lệ", Toast.LENGTH_LONG).show();
            } else {
                int id = UserDataQuery.update(MainActivity.this,us);
                if (id > 0) {
                    Toast.makeText(MainActivity.this, "Cập nhật người dùng thành công", Toast.LENGTH_LONG).show();
                    resetData();
                    dialog.dismiss();
                }
            }
        });
        alertDialog.setNegativeButton("Hủy", (dialog, which) -> {
            dialog.dismiss();
        });
        alertDialog.create();
        alertDialog.show();
    }
    void resetData() {
        lstUser.clear();
        lstUser.addAll(UserDataQuery.getAll(MainActivity.this));
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDeleteClicked(User us, int posidion) {
        boolean rs = UserDataQuery.delete(MainActivity.this, us.id);
        if (rs) {
            Toast.makeText(this, "Xóa thành công", Toast.LENGTH_LONG).show();
            resetData();
        } else {
            Toast.makeText(this, "Xóa thật bại", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemEditClicked (User us, int position) {
        updateUserDialog(us);
    }
}